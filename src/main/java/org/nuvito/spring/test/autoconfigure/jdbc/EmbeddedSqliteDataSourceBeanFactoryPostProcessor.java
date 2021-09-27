package org.nuvito.spring.test.autoconfigure.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;

/**
 * {@code BeanFactoryPostProcessor} which replaces either the single or primary data source bean definition
 * by a bean definition for an embedded SQLite data source, or creates a new one.
 */
@Order(2147483646)
class EmbeddedSqliteDataSourceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddedSqliteDataSourceBeanFactoryPostProcessor.class);
    private static final String SQLITE_EMBEDDED_URL = "jdbc:sqlite::memory:";
    private static final String SQLITE_DRIVER_CLASS = "org.sqlite.JDBC";

    /**
     * Replaces the single or primary data source bean definition by an embedded data source or creates a new one.
     */
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        Assert.isInstanceOf(BeanDefinitionRegistry.class, beanFactory, "Test Database Auto-configuration can only be used with a BeanDefinitionRegistry");
        this.process((BeanDefinitionRegistry) beanFactory, beanFactory);
    }

    private void process(BeanDefinitionRegistry registry, ConfigurableListableBeanFactory beanFactory) {
        BeanDefinitionHolder holder = this.getDataSourceBeanDefinition(beanFactory);
        if (holder != null) {
            replaceByEmbeddedDataSourceBean(registry, holder);
        } else {
            registerNewEmbeddedDataSourceBean(registry);
        }
    }

    /**
     * @return {@code BeanDefinitionHolder}, if a single data source bean definition is found or a primary bean
     *  within several bean definition, else {@code null}
     */
    private BeanDefinitionHolder getDataSourceBeanDefinition(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanNamesForType(DataSource.class);
        if (ObjectUtils.isEmpty(beanNames)) {
            return null;
        } else if (beanNames.length == 1) {
            return wrapSingleBeanDefinition(beanFactory, beanNames[0]);
        } else {
            return wrapPrimaryBeanDefinition(beanFactory, beanNames);
        }
    }

    private BeanDefinitionHolder wrapSingleBeanDefinition(ConfigurableListableBeanFactory beanFactory, String beanName) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        return new BeanDefinitionHolder(beanDefinition, beanName);
    }

    private BeanDefinitionHolder wrapPrimaryBeanDefinition(ConfigurableListableBeanFactory beanFactory, String[] beanNames) {
        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            if (beanDefinition.isPrimary()) {
                return new BeanDefinitionHolder(beanDefinition, beanName);
            }
        }

        return null;
    }

    private void replaceByEmbeddedDataSourceBean(BeanDefinitionRegistry registry, BeanDefinitionHolder holder) {
        String dataSourceBeanName = holder.getBeanName();
        boolean isPrimary = holder.getBeanDefinition().isPrimary();
        logger.info("Replacing '" + dataSourceBeanName + "' DataSource bean with " + (isPrimary ? "primary " : "") + "embedded sqlite version");
        registry.removeBeanDefinition(dataSourceBeanName);
        registry.registerBeanDefinition(dataSourceBeanName, this.createEmbeddedBeanDefinition(isPrimary));
    }

    private void registerNewEmbeddedDataSourceBean(BeanDefinitionRegistry registry) {
        logger.info("Creating 'testDataSource' DataSource bean with embedded sqlite version.");
        logger.trace("da bin ich trace");
        registry.registerBeanDefinition("testDataSource", this.createEmbeddedBeanDefinition(false));
    }

    private BeanDefinition createEmbeddedBeanDefinition(boolean primary) {
        return BeanDefinitionBuilder.rootBeanDefinition(SingleConnectionDataSource.class)
                .addConstructorArgValue(SQLITE_EMBEDDED_URL)
                .addConstructorArgValue(Boolean.TRUE)
                .addPropertyValue("driverClassName", SQLITE_DRIVER_CLASS)
                .setPrimary(primary)
                .getBeanDefinition();
    }
}
