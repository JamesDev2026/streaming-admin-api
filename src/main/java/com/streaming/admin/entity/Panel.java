package com.streaming.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Panel", schema = "dbo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Panel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPanel;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(length = 2000)
    private String url;

    @Column(length = 990)
    private String description;
}