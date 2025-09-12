package com.corhuila.sgie.User.DTO;

import com.corhuila.sgie.User.Entity.Usuario;

import java.util.List;

public class ApiResponseDto<T> {
    private Boolean status;
    private T data;
    private String message;
    public ApiResponseDto(boolean b, List<Usuario> usuarios, String consultaExitosa) {
    }
    public ApiResponseDto(String message, T data, Boolean status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
