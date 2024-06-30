package com.b3al.med.medi_cal.changelogs;

import com.b3al.med.medi_cal.user.*;
import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@ChangeUnit(id = "init-collections", order = "001", author = "CSE")
public class InitCollectionsChangeLog {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @BeforeExecution
    public void beforeExecution(final MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("user", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                .required("username", "role", "status", "dateCreated", "lastUpdated")
                .properties(
                        JsonSchemaProperty.int64("id"),
                        JsonSchemaProperty.string("username"),
                        JsonSchemaProperty.string("password"),
                        JsonSchemaProperty.string("role")
                                .possibleValues("ADMIN", "DOCTOR"),
                        JsonSchemaProperty.string("status")
                                .possibleValues("ACTIVE", "DISABLED"),
                        JsonSchemaProperty.string("firstname"),
                        JsonSchemaProperty.string("lastname"),
                        JsonSchemaProperty.string("specialization"),
                        JsonSchemaProperty.object("dateCreated")
                                .required("dateTime", "offset"),
                        JsonSchemaProperty.object("lastUpdated")
                                .required("dateTime", "offset")).build())))
                .createIndex(new Document("username", 1), new IndexOptions().name("username").unique(true));
        mongoTemplate.createCollection("patient", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                .required("ssn", "firstName", "lastName", "dob", "gender", "dateCreated", "lastUpdated")
                .properties(
                        JsonSchemaProperty.int64("id"),
                        JsonSchemaProperty.string("ssn"),
                        JsonSchemaProperty.string("firstName"),
                        JsonSchemaProperty.string("lastName"),
                        JsonSchemaProperty.date("dob"),
                        JsonSchemaProperty.string("gender")
                                .possibleValues("MALE", "FEMALE", "OTHER"),
                        JsonSchemaProperty.object("dateCreated")
                                .required("dateTime", "offset"),
                        JsonSchemaProperty.object("lastUpdated")
                                .required("dateTime", "offset")).build())))
                .createIndex(new Document("ssn", 1), new IndexOptions().name("ssn").unique(true));
        mongoTemplate.createCollection("appointment", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                .required("visitDate", "dateCreated", "lastUpdated")
                .properties(
                        JsonSchemaProperty.int64("id"),
                        JsonSchemaProperty.string("notes"),
                        JsonSchemaProperty.date("visitDate"),
                        JsonSchemaProperty.object("dateCreated")
                                .required("dateTime", "offset"),
                        JsonSchemaProperty.object("lastUpdated")
                                .required("dateTime", "offset")).build())));
        mongoTemplate.createCollection("prescription", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                .required("medicine", "dosage", "duration", "dateCreated", "lastUpdated")
                .properties(
                        JsonSchemaProperty.int64("id"),
                        JsonSchemaProperty.string("medicine"),
                        JsonSchemaProperty.string("dosage"),
                        JsonSchemaProperty.string("duration"),
                        JsonSchemaProperty.object("dateCreated")
                                .required("dateTime", "offset"),
                        JsonSchemaProperty.object("lastUpdated")
                                .required("dateTime", "offset")).build())));
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(final MongoTemplate mongoTemplate) {
    }

    @Execution
    public void execution(final MongoTemplate mongoTemplate) {
        if (userRepository.findByUsernameIgnoreCase("admin") == null) {

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername("admin");
            userDTO.setPassword("admin");
            userDTO.setRole(UserRole.ADMIN);
            userDTO.setSpecialization("ADMIN SI");
            userDTO.setStatus(UserStatus.ACTIVE);
            userDTO.setFirstname("shems");
            userDTO.setLastname("chelgoui");

            final User user = new User();
            userMapper.updateUser(userDTO, user, passwordEncoder);
            userRepository.save(user);
        }
    }

    @RollbackExecution
    public void rollbackExecution(final MongoTemplate mongoTemplate) {
    }

}
