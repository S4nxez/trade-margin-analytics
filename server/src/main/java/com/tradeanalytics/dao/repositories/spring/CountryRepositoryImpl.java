package com.tradeanalytics.dao.repositories.spring;

import com.tradeanalytics.dao.mappers.CountryRowMapper;
import com.tradeanalytics.dao.model.Country;
import com.tradeanalytics.dao.repositories.CountryRepository;
import com.tradeanalytics.dao.repositories.common.QuerysSQL;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final  JdbcClient jdbcClient;

    private final CountryRowMapper countryRowMapperSpring;

    @Override
    public List<Country> getAll() {
        return jdbcClient.sql(QuerysSQL.SELECT_ALL_COUNTRIES)
                .query(countryRowMapperSpring)
                .list();
    }
}
