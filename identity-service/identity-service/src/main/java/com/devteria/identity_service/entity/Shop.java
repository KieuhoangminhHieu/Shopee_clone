package com.devteria.identity_service.entity;

import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String shopName;
    private String description;
    private String userId;
    private String address;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    private List<Product> products;
}
