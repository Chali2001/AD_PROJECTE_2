package com.ra34.projecte2.dto;

//DTO per a gestionar els errors i retornar missatges d'error clars al client
public class ErrorDto {
    private int status;
    private String message;

    public ErrorDto() {
    }

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
