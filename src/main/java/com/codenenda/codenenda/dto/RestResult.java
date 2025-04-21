package com.codenenda.codenenda.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResult {
    private String status;
    private String errorCode;
    private Object data;

    public RestResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public RestResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public RestResult setData(Object data) {
        this.data = data;
        return this;
    }
}
