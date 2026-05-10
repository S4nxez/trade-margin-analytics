package com.tradeanalytics.dao.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "route_edge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "origin_location_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_route_origin"))
    private Location originLocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destination_location_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_route_destination"))
    private Location destinationLocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transport_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_route_transport"))
    private TransportType transportType;

    @Column(name = "distance_km", nullable = false)
    private Double distanceKm;

    @Column(name = "base_cost", nullable = false, precision = 19, scale = 2)
    private BigDecimal baseCost;

    @Column(name = "estimated_hours", nullable = false)
    private Integer estimatedHours;

    @Column(name = "active", nullable = false)
    private Boolean active;
}