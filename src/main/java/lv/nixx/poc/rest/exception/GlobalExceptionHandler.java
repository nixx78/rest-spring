package lv.nixx.poc.rest.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handle(MethodArgumentNotValidException e, HttpServletRequest request) {

        BindingResult bindingResult = e.getBindingResult();

        List<String> globalErrors = bindingResult.getGlobalErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Collection<ObjectValidationResponse> fieldValidationErrors = bindingResult.getFieldErrors()
                .stream()
                .map(fieldError ->
                        {
                            String value = escapeValue(fieldError.getRejectedValue());
                            return ObjectValidationResponse.builder()
                                    .field(fieldError.getField())
                                    .value(value)
                                    .message(fieldError.getDefaultMessage())
                                    .build();
                        }
                ).collect(Collectors.toMap(ObjectValidationResponse::getField, Function.identity(), (t1, t2) ->
                        ObjectValidationResponse.builder()
                                .field(t1.field)
                                .value(t1.value)
                                .message(t1.message + ", " + t2.message)
                                .build()
                )).values();

        return ResponseEntity.badRequest().body(MethodValidationResponse.builder()
                .path(request.getRequestURI())
                .httpMethod(request.getMethod())
                .globalValidationErrors(globalErrors)
                .fieldValidationErrors(fieldValidationErrors)
                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handle(ConstraintViolationException e, HttpServletRequest request) {

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        Collection<ObjectValidationResponse> fieldValidations = constraintViolations.stream()
                .map(t -> {
                    PathImpl p = (PathImpl) t.getPropertyPath();
                    NodeImpl leafNode = p.getLeafNode();
                    Integer index = leafNode.getIndex();

                    String value = escapeValue(t.getInvalidValue());

                    return ObjectValidationResponse.builder()
                            .index(index == null ? 0 : index)
                            .field(leafNode.getName())
                            .value(value)
                            .message(t.getMessage())
                            .build();
                }).collect(Collectors.toMap(ObjectValidationResponse::getField, Function.identity(), (t1, t2) ->
                        ObjectValidationResponse.builder()
                                .field(t1.field)
                                .value(t1.value)
                                .message(t1.message + ", " + t2.message)
                                .build()
                )).values();

        return ResponseEntity.badRequest().body(new CollectionValidationResponse(request.getRequestURI(), request.getMethod(), fieldValidations));
    }

    private String escapeValue(Object v) {
        if (v == null) {
            return null;
        }

        if (v instanceof String) {
            return StringEscapeUtils.escapeHtml4((String) v);
        }
        return v.toString();
    }


    @Getter
    static class CollectionValidationResponse {
        private final String path;
        private final String httpMethod;

        private final Map<Integer, List<ObjectValidationResponse>> fieldsResponse;

        CollectionValidationResponse(String path, String httpMethod, Collection<ObjectValidationResponse> fieldsResponse) {
            this.path = path;
            this.httpMethod = httpMethod;
            this.fieldsResponse = fieldsResponse.stream()
                    .collect(groupingBy(ObjectValidationResponse::getIndex, TreeMap::new, toList()));
        }
    }

    @ExceptionHandler(UnsupportedApiVersionException.class)
    public ProblemDetail handle(UnsupportedApiVersionException ex, HttpServletRequest request) {

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid API version");
        pd.setDetail(ex.getMessage());
        pd.setInstance(URI.create(request.getRequestURI()));
        pd.setProperty("supportedVersions", List.of("1.0", "2.0"));
        pd.setProperty("wrongVersion", ex.getVersion());

        return pd;
    }

    @Getter
    @Builder
    static class MethodValidationResponse {
        private final String path;
        private final String httpMethod;

        private final Collection<String> globalValidationErrors;
        private final Collection<ObjectValidationResponse> fieldValidationErrors;
    }

    @Getter
    @Builder
    static class ObjectValidationResponse {
        @JsonIgnore
        private final int index;
        private final String field;
        private final String value;
        private final String message;
    }

}
