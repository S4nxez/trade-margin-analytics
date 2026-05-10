package com.tradeanalytics.dao.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location_kind")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationKind {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="code", nullable = false, unique = true, length = 50)
    private String code;
}