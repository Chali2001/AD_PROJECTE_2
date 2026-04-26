package com.ra34.projecte2.dto;

public class UserCreateRequestDTO {

    private String email;
    private String password;
    private CustomerCreateRequestDTO customer;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CustomerCreateRequestDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerCreateRequestDTO customer) {
        this.customer = customer;
    }
}
