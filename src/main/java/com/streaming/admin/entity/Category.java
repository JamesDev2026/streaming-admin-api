package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategory;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 45)
    private String description;
}