package com.devteria.identity_service.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String shopName;
    private String description;
    private String userId;

    @OneToMany(mappedBy = "shop")
    @JsonIgnore
    private List<Product> products;
}
