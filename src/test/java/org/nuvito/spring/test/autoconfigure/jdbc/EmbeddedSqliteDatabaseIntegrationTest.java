package org.nuvito.spring.test.autoconfigure.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests, if the embedded sqlite database is used with a SingleConnectionDataSource.
 */
@DataJdbcTest
class EmbeddedSqliteDatabaseIntegrationTest {
    @Configuration
    static class TestConfiguration {}

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext context;

    @Test
    void givenUnconfiguredDataTest_dataSourceForEmbeddedSqliteShouldBeCreated() {
        assertThat(dataSource).isNotNull().isInstanceOf(SingleConnectionDataSource.class);
        assertThat(ReflectionTestUtils.invokeGetterMethod(dataSource, "isSuppressClose")).isEqualTo(Boolean.TRUE);
    }

    @Test
    void givenContextLoadedWithDataSource_NamedParameterJdbcTemplateShouldExist() {
        assertThat(context.getBean(NamedParameterJdbcOperations.class)).isInstanceOf(NamedParameterJdbcTemplate.class);
    }
}
