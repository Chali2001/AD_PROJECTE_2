package com.ra34.projecte2.dto;

public class ErrorDTO {
    private String status;
    private String description;

    public ErrorDTO(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}