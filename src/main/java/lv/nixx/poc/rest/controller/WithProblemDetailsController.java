package lv.nixx.poc.rest.controller;

import lv.nixx.poc.rest.exception.UnsupportedApiVersionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// RFC 7807 (Problem Details for HTTP APIs)
@RestController
public class WithProblemDetailsController {

    @GetMapping("/problemDetails/wrongVersion")
    public List<String> users(@RequestParam("X-API-Version") String version) {

        if (!List.of("1.0", "2.0").contains(version)) {
            throw new UnsupportedApiVersionException(version);
        }

        return List.of("Alice", "Bob");
    }

    @GetMapping("/problemDetails/ErrorResponseException")
    public void throwErrorResponseException(@RequestParam String param) {
        throw new ErrorResponseException(
                HttpStatus.BAD_REQUEST,
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Incorrect parameter: " + param
                ),
                null
        );
    }
}
