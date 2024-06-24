package com.b3al.med.medi_cal.prescription;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.b3al.med.medi_cal.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;


public class PrescriptionResourceTest extends BaseIT {

    @Test
    void getAllPrescriptions_success() {
        testData.prescription();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/prescriptions")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo(1300));
    }

    @Test
    void getAllPrescriptions_filtered() {
        testData.prescription();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/prescriptions?filter=1301")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo(1301));
    }

    @Test
    void getPrescription_success() {
        testData.prescription();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/prescriptions/1300")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("medicine", Matchers.equalTo("No sea takimata."));
    }

    @Test
    void getPrescription_notFound() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/prescriptions/1966")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createPrescription_success() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/prescriptionDTORequest.json"))
                .when()
                    .post("/api/prescriptions")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, prescriptionRepository.count());
    }

    @Test
    void createPrescription_missingField() {
        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/prescriptionDTORequest_missingField.json"))
                .when()
                    .post("/api/prescriptions")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("medicine"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    void updatePrescription_success() {
        testData.prescription();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/prescriptionDTORequest.json"))
                .when()
                    .put("/api/prescriptions/1300")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Consetetur sadipscing.", prescriptionRepository.findById(((long)1300)).orElseThrow().getMedicine());
        assertEquals(2, prescriptionRepository.count());
    }

    @Test
    void deletePrescription_success() {
        testData.prescription();

        RestAssured
                .given()
                    .header(HttpHeaders.AUTHORIZATION, adminJwtToken())
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/prescriptions/1300")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, prescriptionRepository.count());
    }

}
