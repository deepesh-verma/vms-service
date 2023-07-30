package com.strata.vms.vmsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strata.vms.vmsservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestVmsServiceApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VmsServiceApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    private void logAsPrettyJson(ResponseEntity<String> input) {

        try {
            String formattedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
            log.info(formattedJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            ApiResponseModel<?> apiResponseModel = objectMapper.readValue(input.getBody(), ApiResponseModel.class);
            log.info("Response body: \n {}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiResponseModel));
        } catch (Exception e) {
            // do nothing
        }
    }

    @Test
    @Order(1)
    @DisplayName("The application health check should be green")
    void healthCheckIsGreen() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    @DisplayName("A new company with invalid email should fail to create")
    void testAddCompany_whenCalledWithInvalidEmail_shouldFail() {

        CompanyModel appleCompany = this.getValidCompanyModel("Apple");
        appleCompany.setEmail("invalid-email");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/companies", appleCompany, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("A new company with valid data should get created")
    void testAddCompany_success() {

        CompanyModel appleCompany = this.getValidCompanyModel("Apple");
        ResponseEntity<String> response = testRestTemplate.postForEntity("/companies", appleCompany, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(4)
    @DisplayName("A new worker with invalid firstName should fail to create")
    void testAddWorker_whenCalledWithInvalidFirstName_shouldFail() {

        WorkerModel bobTheBuilder = this.getValidWorkerModel(1L);
        bobTheBuilder.setFirstName(null);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/workers", bobTheBuilder, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("A new worker with valid data should get created")
    void testAddWorker_success() {

        WorkerModel bobTheBuilder = this.getValidWorkerModel(1L);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/workers", bobTheBuilder, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(6)
    @DisplayName("A new contract with invalid name should fail to create")
    void testAddContract_whenCalledWithInvalidName_shouldFail() {

        ServiceContractModel serviceContract = this.getValidServiceContractModel(1L, 1L);
        serviceContract.setName(null);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/contracts", serviceContract, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(7)
    @DisplayName("A new contract with valid data should get created")
    void testAddContract_success() {

        ServiceContractModel serviceContract = this.getValidServiceContractModel(1L, 1L);
        ResponseEntity<String> response = testRestTemplate.postForEntity("/contracts", serviceContract, String.class);

        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(8)
    @DisplayName("Assign owner to contract should fail, if called with worker from different company than company that owns the contract")
    void testAssignOwner_whenCalledWithWorkerFromDifferentCompany_shouldFail() {

        // ------- arrange ----------- //

        // Create two companies - Google and Facebook
        CompanyModel googleCompany = this.getValidCompanyModel("Google");
        CompanyModel facebookCompany = this.getValidCompanyModel("Facebook");
        Long googleCompanyId = testRestTemplate.postForObject("/companies", googleCompany, Long.class);
        Long facebookCompanyId = testRestTemplate.postForObject("/companies", facebookCompany, Long.class);

        // Create two workers, one for each company
        WorkerModel googleWorker = this.getValidWorkerModel(googleCompanyId);
        WorkerModel facebookWorker = this.getValidWorkerModel(facebookCompanyId);
        Long googleWorkerId = testRestTemplate.postForObject("/workers", googleWorker, Long.class);
        Long facebookWorkerId = testRestTemplate.postForObject("/workers", facebookWorker, Long.class);

        // Create a contract for Google
        ServiceContractModel googleServiceContract = this.getValidServiceContractModel(googleCompanyId, googleWorkerId);
        Long googleServiceContractId = testRestTemplate.postForObject("/contracts", googleServiceContract, Long.class);

        // ---------- act -----------//
        String url = MessageFormat.format("/contracts/{0}/owner/{1}", googleServiceContractId, facebookWorkerId);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.PATCH, null, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(9)
    @DisplayName("Assign owner to contract should succeed, if called with worker from the same company that owns the contract")
    void testAssignOwner_success() {

        // ------- arrange ----------- //

        // Create two companies - Tesla and Spacex
        CompanyModel teslaCompany = this.getValidCompanyModel("Tesla");
        Long teslaCompanyId = testRestTemplate.postForObject("/companies", teslaCompany, Long.class);

        // Create two workers for Tesla
        WorkerModel teslaFirstWorker = this.getValidWorkerModel(teslaCompanyId);
        WorkerModel teslaSecondWorker = this.getValidWorkerModel(teslaCompanyId);
        Long teslaFirstWorkerId = testRestTemplate.postForObject("/workers", teslaFirstWorker, Long.class);
        Long teslaSecondWorkerId = testRestTemplate.postForObject("/workers", teslaSecondWorker, Long.class);

        // Create a contract for Tesla
        ServiceContractModel teslaServiceContract = this.getValidServiceContractModel(teslaCompanyId, teslaFirstWorkerId);
        Long teslaServiceContractId = testRestTemplate.postForObject("/contracts", teslaServiceContract, Long.class);

        // ---------- act -----------//
        String url = MessageFormat.format("/contracts/{0}/owner/{1}", teslaServiceContractId, teslaSecondWorkerId);
        ResponseEntity<String> response = testRestTemplate.exchange(url, HttpMethod.PATCH, null, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(10)
    @DisplayName("Assign contract to workers should fail, if called with contract from a different company than where the worker belongs")
    void testAssignContractToWorkers_whenCalledWithContractFromDifferentCompany_shouldFail() {

        // ------- arrange ----------- //

        // Create two companies - Infosys and Wipro
        CompanyModel infosysCompany = this.getValidCompanyModel("Infosys");
        CompanyModel wiproCompany = this.getValidCompanyModel("Wipro");
        Long infosysCompanyId = testRestTemplate.postForObject("/companies", infosysCompany, Long.class);
        Long wiproCompanyId = testRestTemplate.postForObject("/companies", wiproCompany, Long.class);

        // Create one worker for each company
        WorkerModel infosysWorker = this.getValidWorkerModel(infosysCompanyId);
        WorkerModel wiproWorker = this.getValidWorkerModel(wiproCompanyId);
        Long infosysWorkerId = testRestTemplate.postForObject("/workers", infosysWorker, Long.class);
        Long wiproWorkerId = testRestTemplate.postForObject("/workers", wiproWorker, Long.class);

        // Create contracts
        ServiceContractModel infosysContract = this.getValidServiceContractModel(infosysCompanyId, infosysWorkerId);
        Long infosysContractId = testRestTemplate.postForObject("/contracts", infosysContract, Long.class);

        // ---------- act -----------//
        HttpEntity<?> requestEntity = new HttpEntity<>(List.of(wiproWorkerId));
        ResponseEntity<String> response = testRestTemplate.exchange("/workers/assign/" + infosysContractId, HttpMethod.PATCH, requestEntity, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(11)
    @DisplayName("Assign contract to workers should fail, if called with contract with non-active status")
    void testAssignContractToWorkers_whenCalledWithContractWithNonActiveStatus_shouldFail() {

        // ------- arrange ----------- //

        // Create two companies - Infosys and Wipro
        CompanyModel infosysCompany = this.getValidCompanyModel("Infosys");
        Long infosysCompanyId = testRestTemplate.postForObject("/companies", infosysCompany, Long.class);

        // Create one worker for each company
        WorkerModel infosysContractOwner = this.getValidWorkerModel(infosysCompanyId);
        WorkerModel infosysWorker = this.getValidWorkerModel(infosysCompanyId);
        Long infosysContractOwnerId = testRestTemplate.postForObject("/workers", infosysContractOwner, Long.class);
        Long infosysWorkerId = testRestTemplate.postForObject("/workers", infosysWorker, Long.class);

        // Create contracts
        ServiceContractModel infosysContract = this.getValidServiceContractModel(infosysCompanyId, infosysContractOwnerId);
        infosysContract.getStatus().setId(2L);
        Long infosysContractId = testRestTemplate.postForObject("/contracts", infosysContract, Long.class);

        // ---------- act -----------//
        HttpEntity<?> requestEntity = new HttpEntity<>(List.of(infosysWorkerId));
        ResponseEntity<String> response = testRestTemplate.exchange("/workers/assign/" + infosysContractId, HttpMethod.PATCH, requestEntity, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Order(12)
    @DisplayName("Assign contract to workers should succeed, if called with contract from the same company where the worker belongs")
    void testAssignContractToWorkers_success() {

        // ------- arrange ----------- //

        // Create two companies - Tesla and Spacex
        CompanyModel teslaCompany = this.getValidCompanyModel("Tesla");
        Long teslaCompanyId = testRestTemplate.postForObject("/companies", teslaCompany, Long.class);

        // Create two workers for Tesla
        WorkerModel teslaFirstWorker = this.getValidWorkerModel(teslaCompanyId);
        WorkerModel teslaSecondWorker = this.getValidWorkerModel(teslaCompanyId);
        Long teslaFirstWorkerId = testRestTemplate.postForObject("/workers", teslaFirstWorker, Long.class);
        Long teslaSecondWorkerId = testRestTemplate.postForObject("/workers", teslaSecondWorker, Long.class);

        // Create a contract for Tesla
        ServiceContractModel teslaServiceContract = this.getValidServiceContractModel(teslaCompanyId, teslaFirstWorkerId);
        Long teslaServiceContractId = testRestTemplate.postForObject("/contracts", teslaServiceContract, Long.class);

        // ---------- act -----------//
        HttpEntity<?> requestEntity = new HttpEntity<>(List.of(teslaSecondWorkerId));
        ResponseEntity<String> response = testRestTemplate.exchange("/workers/assign/" + teslaServiceContractId, HttpMethod.PATCH, requestEntity, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(13)
    @DisplayName("Off-boarding workers from contract should succeed")
    void testOffBoardWorkers_success() {

        // ------- arrange ----------- //

        // Create two companies - Tesla and Spacex
        CompanyModel teslaCompany = this.getValidCompanyModel("Tesla");
        Long teslaCompanyId = testRestTemplate.postForObject("/companies", teslaCompany, Long.class);

        // Create two workers for Tesla
        WorkerModel teslaFirstWorker = this.getValidWorkerModel(teslaCompanyId);
        WorkerModel teslaSecondWorker = this.getValidWorkerModel(teslaCompanyId);
        Long teslaFirstWorkerId = testRestTemplate.postForObject("/workers", teslaFirstWorker, Long.class);
        Long teslaSecondWorkerId = testRestTemplate.postForObject("/workers", teslaSecondWorker, Long.class);

        // Create a contract for Tesla
        ServiceContractModel teslaServiceContract = this.getValidServiceContractModel(teslaCompanyId, teslaFirstWorkerId);
        Long teslaServiceContractId = testRestTemplate.postForObject("/contracts", teslaServiceContract, Long.class);

        // Assign a worker
        HttpEntity<?> requestEntity = new HttpEntity<>(List.of(teslaSecondWorkerId));
        testRestTemplate.exchange("/workers/assign/" + teslaServiceContractId, HttpMethod.PATCH, requestEntity, String.class);

        // ---------- act -----------//
        requestEntity = new HttpEntity<>(List.of(teslaSecondWorkerId));
        ResponseEntity<String> response = testRestTemplate.exchange("/workers/offboard", HttpMethod.PATCH, requestEntity, String.class);

        // assert
        this.logAsPrettyJson(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(14)
    @DisplayName("Last test")
    void testLast() {
        assertTrue(true, "Always passes");
    }

    private WorkerModel getValidWorkerModel(long companyId) {
        return WorkerModel.builder()
                .firstName("Bob").lastName("Johnson").startDate(LocalDate.now())
                .company(new CompanyModel(companyId)).workerRole(new WorkerRoleModel(1L)).build();
    }


    private CompanyModel getValidCompanyModel(String companyName) {
        return CompanyModel.builder()
                .name(companyName).description(companyName + " Company").email("company@"+companyName+".com")
                .phone(companyName+"2374892").address(companyName + " INC").logo("https://tinyurl.com/47e54736")
                .build();
    }

    private ServiceContractModel getValidServiceContractModel(Long companyId, Long ownerId) {
        return ServiceContractModel.builder()
                .company(new CompanyModel(companyId)).status(new ContractStatusModel(1L)).name("ServiceContract__C__" + companyId)
                .owner(new WorkerModel(ownerId)).description("Service contract for company " + companyId)
                .build();
    }
}
