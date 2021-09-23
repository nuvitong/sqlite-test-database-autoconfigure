package org.nuvito.spring.test.autoconfigure.jdbc;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ConfigurationCondition;

/**
 * Condition matches, if property {@code spring.test.database.replace} is either <em>ANY</em> or <em>AUTO_CONFIGURED</em>
 */
public class OnTestDatabaseReplacementCondition extends AnyNestedCondition {

    public OnTestDatabaseReplacementCondition() {
        super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
    }

    @ConditionalOnProperty(prefix = "spring.test.database", name = {"replace"}, havingValue = "ANY", matchIfMissing = true)
    static class ReplaceAnyDataSourceCondition {

    }

    @ConditionalOnProperty(prefix = "spring.test.database", name = {"replace"}, havingValue = "AUTO_CONFIGURED")
    static class ReplaceAutoConfiguredDataSourceCondition {

    }

}
