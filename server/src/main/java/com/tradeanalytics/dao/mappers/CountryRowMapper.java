package com.tradeanalytics.dao.mappers;

import com.tradeanalytics.dao.model.Country;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
        Country country = new Country();

        country.setId(rs.getLong("id"));
        country.setName(rs.getString("name"));
        country.setIso2(rs.getString("iso2"));
        country.setIso3(rs.getString("iso3"));

        return country;
    }
}
