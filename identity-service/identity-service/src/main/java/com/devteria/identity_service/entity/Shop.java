package com.devteria.identity_service.entity;

import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String shopName;
    String description;
    String userId;
    String address;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    List<Product> products;
}
