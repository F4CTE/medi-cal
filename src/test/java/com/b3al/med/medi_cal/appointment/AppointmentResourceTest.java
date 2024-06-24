package com.b3al.med.medi_cal.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.b3al.med.medi_cal.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


public class AppointmentResourceTest extends BaseIT {

    @Test
    void getAllAppointments_success() {
        testData.appointment();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/appointments")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo(1200));
    }

    @Test
    void getAllAppointments_filtered() {
        testData.appointment();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/appointments?filter=1201")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo(1201));
    }

    @Test
    void getAppointment_success() {
        testData.appointment();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/appointments/1200")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("notes", Matchers.equalTo("Quis nostrud exerci."));
    }

    @Test
    void getAppointment_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/appointments/1866")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createAppointment_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/appointmentDTORequest.json"))
                .when()
                    .post("/api/appointments")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, appointmentRepository.count());
    }

    @Test
    void createAppointment_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/appointmentDTORequest_missingField.json"))
                .when()
                    .post("/api/appointments")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("visitDate"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    void updateAppointment_success() {
        testData.appointment();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/appointmentDTORequest.json"))
                .when()
                    .put("/api/appointments/1200")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("At vero eos.", appointmentRepository.findById(((long)1200)).orElseThrow().getNotes());
        assertEquals(2, appointmentRepository.count());
    }

    @Test
    void deleteAppointment_success() {
        testData.appointment();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/appointments/1200")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, appointmentRepository.count());
    }

}
