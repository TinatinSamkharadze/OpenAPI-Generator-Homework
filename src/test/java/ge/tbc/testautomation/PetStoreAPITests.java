package ge.tbc.testautomation;

import ge.tbc.testautomation.steps.PetStoreAPISteps;
import ge.tbc.testautomation.utils.Retry;
import ge.tbc.testautomation.utils.RetryAnalyzer;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pet.store.v3.invoker.ApiClient;
import pet.store.v3.invoker.JacksonObjectMapper;

import static ge.tbc.testautomation.data.Constants.PENDING;
import static ge.tbc.testautomation.data.Constants.PET_STORE_URI;
import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

@Epic("Petstore API Tests")
public class PetStoreAPITests {
    private ApiClient apiClient;
    private PetStoreAPISteps petStoreAPISteps = new PetStoreAPISteps();
    @BeforeSuite
    public void initializeApiClient() {
        apiClient = ApiClient.api(ApiClient.Config.apiConfig()
                .reqSpecSupplier(() -> new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .log(LogDetail.ALL)
                        .setConfig(config()
                                .objectMapperConfig(objectMapperConfig()
                                        .defaultObjectMapper(JacksonObjectMapper.jackson())))
                        .addFilter(new ErrorLoggingFilter())
                        .addFilter(new AllureRestAssured())
                        .setBaseUri(PET_STORE_URI)));
    }


    @Severity(SeverityLevel.NORMAL)
    @Feature("Petstore Order Placement")
    @Story("Submitting a pet order to the Petstore")
    @Description("Create a pet order request body, send the request," +
            "receive the response, deserialize it, and validate the data.")
    @Test(description = "Generate and submit a pet order, then validate the response", priority = 1)
    public void storeOrderTest() {
        petStoreAPISteps
                .generateOrderBody()
                .submitPetStoreOrder(apiClient)
                .checkOrderDataConsistency();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Feature("Adding a New Pet to the Petstore")
    @Story("Creating and adding a new pet to the Petstore")
    @Description("Generate a new pet request body, send the request, and validate the response.")
    @Test(description = "Add a new pet to the Petstore and validate the response",
            retryAnalyzer = RetryAnalyzer.class, priority = 2)
    @Retry(maxRetries = 3)
    public void addNewPetTest() {
        petStoreAPISteps
                .generatePetRequest(PENDING)
                .postNewPet(apiClient)
                .validateNewPet();
    }

}
