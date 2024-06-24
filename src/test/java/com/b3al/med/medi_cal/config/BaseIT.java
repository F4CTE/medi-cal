package com.b3al.med.medi_cal.config;

import com.b3al.med.medi_cal.MediCalApplication;
import com.b3al.med.medi_cal.appointment.AppointmentRepository;
import com.b3al.med.medi_cal.patient.PatientRepository;
import com.b3al.med.medi_cal.prescription.PrescriptionRepository;
import com.b3al.med.medi_cal.user.UserRepository;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import org.testcontainers.containers.MongoDBContainer;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context with a
 * Datasource connected to the Testcontainers Docker instance. The instance is reused for all tests,
 * with all data wiped out before each test.
 */
@SpringBootTest(
        classes = MediCalApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("it")
public abstract class BaseIT {

    @ServiceConnection
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.9");

    static {
        mongoDBContainer.withReuse(true)
                .start();
    }

    @LocalServerPort
    public int serverPort;

    @Autowired
    public TestData testData;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public PrescriptionRepository prescriptionRepository;

    @PostConstruct
    public void initRestAssured() {
        RestAssured.port = serverPort;
        RestAssured.urlEncodingEnabled = false;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    public void beforeEach() {
        testData.clearAll();
        testData.user();
    }

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), StandardCharsets.UTF_8);
    }

    public String adminJwtToken() {
        // user admin, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImlzcyI6ImJvb3RpZnkiLCJpYXQiOjE3MTgxOTQ5ODYsImV4cCI6MjIwODk4ODgwMH0." +
                "SVv2r_4PwCHO-zLh7yy3ac1ef_ExiXinjONfo3l_GZsrHr4BFNJ4U_U7geci4_30PlvYnlU9gK4QhA-iDc16vw";
    }

    public String doctorJwtToken() {
        // user doctor, expires 2040-01-01
        return "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiJkb2N0b3IiLCJyb2xlcyI6WyJET0NUT1IiXSwiaXNzIjoiYm9vdGlmeSIsImlhdCI6MTcxODE5NDk4NiwiZXhwIjoyMjA4OTg4ODAwfQ." +
                "kyhZj2Kb_j_eqwGyrtaY8aS0pxKfSl3WPVUlSaaemGAf6I66wQ-XBZC_0J-6Y_GV2imI-Zuga9I0O6BmZQ4jsw";
    }

}
