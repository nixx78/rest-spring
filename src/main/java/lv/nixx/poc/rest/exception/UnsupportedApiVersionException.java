package lv.nixx.poc.rest.exception;

import lombok.Getter;

@Getter
public class UnsupportedApiVersionException extends RuntimeException {

    private final String version;

    public UnsupportedApiVersionException(String version) {
        this.version = version;
    }

}
