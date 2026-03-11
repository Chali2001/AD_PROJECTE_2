package com.ra34.projecte2.model;

import java.time.LocalDateTime;

public class Product {
    int id;
    String name;
    String description;
    int stock;
    double price;
    double rating;
    ProductCondition condition;
    boolean status;
    LocalDateTime dataCreated;
    LocalDateTime dataUpdated;
}
