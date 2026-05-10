package com.tradeanalytics.dao.repositories;

import com.tradeanalytics.dao.model.Location;
import com.tradeanalytics.dao.model.RouteEdge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteEdgeRepository extends JpaRepository<RouteEdge, Long> {
    List<RouteEdge> findByOriginLocationAndDestinationLocationAndActiveTrue(Location origin, Location destination);
}
