package com.spartified.connectrateplan;

import com.spartified.connectrateplan.config.AsyncSyncConfiguration;
import com.spartified.connectrateplan.config.DatabaseTestcontainer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        ConnectRateplanApp.class,
        AsyncSyncConfiguration.class,
        com.spartified.connectrateplan.config.JacksonHibernateConfiguration.class,
        DatabaseTestcontainer.class,
    }
)
public @interface IntegrationTest {}
