package ge.tbc.testautomation;

import com.example.local.invoker.ApiClient;
import com.example.local.invoker.JacksonObjectMapper;
import com.github.javafaker.Faker;
import ge.tbc.testautomation.data.DataSupplier;
import ge.tbc.testautomation.steps.AuthSteps;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static ge.tbc.testautomation.data.Constants.LOCAL_URI;
import static ge.tbc.testautomation.data.Constants.VALID_PASSWORD;
import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;

@Epic("Local API Tests")
public class AuthTests {
 Faker faker = new Faker();
    private ApiClient apiClient;
    AuthSteps authSteps = new AuthSteps();
    @BeforeSuite
    public void initializeApiClient() {
        apiClient = ApiClient.api(ApiClient.Config.apiConfig()
                .reqSpecSupplier(() -> new RequestSpecBuilder()
                        .setContentType(ContentType.JSON)
                        .setAccept(ContentType.JSON)
                        .setConfig(config()
                                .objectMapperConfig(objectMapperConfig()
                                        .defaultObjectMapper(JacksonObjectMapper.jackson())))
                        .addFilter(new ErrorLoggingFilter())
                        .addFilter(new AllureRestAssured())
                        .setBaseUri(LOCAL_URI)));
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = DataSupplier.class)
    @Severity(SeverityLevel.NORMAL)
    @Feature("User  Registration")
    @Story("Testing registration with invalid password formats")
    @Description("Test registration with various invalid password formats")
    public void testRegistrationWithInvalidPasswordFormats(String invalidPassword) {
        authSteps
                .createUser(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.internet().emailAddress(),
                        invalidPassword)
                .registerUserExpecting400(apiClient);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Feature("User  Registration and Authorization")
    @Story("Testing successful registration and authorization of a user")
    @Description("Test successful registration and authorization of a user with a valid password")
    public void testSuccessfulRegistrationAndAuthorization() {
        authSteps
                .createUser(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.internet().emailAddress(),
                        VALID_PASSWORD)
                .registerNewUser(apiClient)
                .authenticateRegisteredUser(apiClient)
                .verifyUserPrivileges()
                .authorizeWithToken(apiClient)
                .verifyResourceResponse();
    }

    @Test(dependsOnMethods = "testSuccessfulRegistrationAndAuthorization")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Token Management")
    @Story("Testing token refresh and validation")
    @Description("Test token refresh and validate the old token's validity")
    public void testTokenRefreshAndValidation() {
        authSteps
                .refreshAuthenticationToken(apiClient)
                .checkOldTokenValidity(apiClient);
    }
}

