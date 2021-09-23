package org.nuvito.spring.test.autoconfigure.jdbc;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.sqlite.SQLiteConnection;

import javax.sql.DataSource;

/**
 * Auto-Configuration for a data source of an embedded sqlite database in tests, which are annotated with
 * {@code AutoConfigureTestDatabase}
 * <p>
 * This auto-configuration comes into action, if there is a {@link SQLiteConnection}
 * on the classpath.
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@AutoConfigureAfter(TestDatabaseAutoConfiguration.class)
@ConditionalOnClass(SQLiteConnection.class)
public class SqliteTestDatabaseAutoConfiguration {

    /**
     * If no data source bean is available create a <em>NullObject</em>, so that conditions for other beans,
     * which are dependent on a datasource bean (definition), are matching.
     * <p>
     * The replaceable data source will be replaced by the BeanFactoryPostProcessor.
     * @return a NullObject for a data source, which will be replaced by an embedded data source
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.test.database", name = {"replace"}, havingValue = "ANY", matchIfMissing = true)
    public DataSource replaceableDataSource() {
        return new DelegatingDataSource();
    }

    /**
     * The {@code BeanFactoryPostProcessor} replaces the existing data source or primary data source, if more than one
     * exist, respectively, by an embedded SQLite data source.
     */
    @Conditional(OnTestDatabaseReplacementCondition.class)
    @Bean
    public static BeanFactoryPostProcessor postProcessDataSourceBean() {
        return new EmbeddedSqliteDataSourceBeanFactoryPostProcessor();
    }

}
