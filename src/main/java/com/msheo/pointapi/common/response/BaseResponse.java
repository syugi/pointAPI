package com.msheo.pointapi.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public BaseResponse(final int statusCode, final String message){
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> BaseResponse<T> res(final int statusCode, final String message){
        return BaseResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .build();
    }

    public static<T> BaseResponse<T> res(final int statusCode, final String message, final T t){
        return BaseResponse.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .message(message)
                .build();
    }

}
