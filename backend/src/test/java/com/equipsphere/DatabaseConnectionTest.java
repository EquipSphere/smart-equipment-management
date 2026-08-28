package com.equipsphere;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        System.out.println("--- TESTING DATABASE CONNECTION ---");
        long start = System.currentTimeMillis();
        List<Map<String, Object>> result = jdbcTemplate.queryForList("SELECT 1 as num, NOW() as current_time");
        long end = System.currentTimeMillis();
        System.out.println("Query executed in: " + (end - start) + " ms");
        System.out.println("Result: " + result);
        assertNotNull(result);
    }
}
