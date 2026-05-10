package com.tradeanalytics.dao.repositories;

import com.tradeanalytics.dao.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCountry_NameIgnoreCase(String country);
}
