package com.b3al.med.medi_cal.config;

import com.b3al.med.medi_cal.util.MongoOffsetDateTimeReader;
import com.b3al.med.medi_cal.util.MongoOffsetDateTimeWriter;
import io.mongock.runner.springboot.EnableMongock;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
@EnableMongoRepositories("com.b3al.med.medi_cal")
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
@EnableMongock
public class MongoConfig {

    @Bean
    public MongoTransactionManager transactionManager(final MongoDatabaseFactory databaseFactory) {
        return new MongoTransactionManager(databaseFactory);
    }

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(
            final LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new MongoOffsetDateTimeWriter(),
                new MongoOffsetDateTimeReader()
                ));
    }

}
