package ge.tbc.testautomation.steps;

import com.example.local.invoker.ApiClient;
import com.example.local.model.*;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.example.local.invoker.ResponseSpecBuilders.shouldBeCode;
import static com.example.local.invoker.ResponseSpecBuilders.validatedWith;
import static ge.tbc.testautomation.data.Constants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;


public class AuthSteps {
    RegisterRequest registerRequest;
    AuthenticationResponse authenticationResponse;
    Response response;
    RefreshTokenResponse refreshTokenResponse;

    @Step("Create a new user registration request with firstname: {firstname}, lastname: {lastname}, email: {email}")
    public AuthSteps createUser(String firstname, String lastname, String email, String password){
        registerRequest = new RegisterRequest()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .role(RegisterRequest.RoleEnum.ADMIN);
        return this;
    }

    @Step("Register a new user with the provided registration details")
    public AuthSteps registerNewUser(ApiClient api){
        authenticationResponse = api
                .authentication()
                .register()
                .body(registerRequest)
                .executeAs(response -> {
                    validatedWith(shouldBeCode(200));
                    return response;
                });
        return this;
    }

    @Step("Authenticate the registered user with email: {email}")
    public AuthSteps authenticateRegisteredUser(ApiClient api){
        authenticationResponse = api
                .authentication()
                .authenticate()
                .body(new AuthenticationRequest()
                        .email(registerRequest.email())
                        .password(registerRequest.password()))
                .executeAs(response -> response);
        return this;
    }

    @Step("Verify that the authenticated user has the following privileges: {privileges}")
    public AuthSteps verifyUserPrivileges(){
        assertThat(authenticationResponse.getRoles(), hasItems(
               UPDATE_PRIVILEGE,
                DELETE_PRIVILEGE,
                READ_PRIVILEGE,
                WRITE_PRIVILEGE,
                ADMIN_PRIVILEGE));
        return this;
    }

    @Step("Authorize using the provided token and retrieve the resource")
    public AuthSteps authorizeWithToken(ApiClient api){
        response = api
                .authorization()
                .sayHelloWithRoleAdminAndReadAuthority()
                .reqSpec(requestSpecBuilder -> requestSpecBuilder
                        .addHeader(AUTHORIZATION,
                                BEARER + authenticationResponse.accessToken()))
                .execute(response -> response);
        return this;
    }

    @Step("Verify the resource response contains expected message: {RESOURCE_MESSAGE}")
    public AuthSteps verifyResourceResponse(){
        response
                .then()
                .statusCode(200)
                .body(equalTo(RESOURCE_MESSAGE));
        return this;
    }

    @Step("Refresh authentication token using refresh token: {authenticationResponse.refreshToken()}")
    public AuthSteps refreshAuthenticationToken(ApiClient api){
        refreshTokenResponse = api
                .authentication()
                .refreshToken()
                .body(new RefreshTokenRequest()
                        .refreshToken(authenticationResponse.refreshToken()))
                .executeAs(response -> {
                    validatedWith(shouldBeCode(200));
                    return response;
                });
        return this;
    }

    @Step("Check if the old token is still valid")
    public AuthSteps checkOldTokenValidity(ApiClient api){
        Response oldTokenResponse = api
                .authorization()
                .sayHelloWithRoleAdminAndReadAuthority()
                .reqSpec(requestSpecBuilder -> requestSpecBuilder
                        .addHeader(AUTHORIZATION, BEARER + authenticationResponse.accessToken()))
                .execute(response -> response);
        oldTokenResponse.then().statusCode(200);
        return this;
    }

    @Step("Register user and expect 400 Bad Request due to invalid input")
    public AuthSteps registerUserExpecting400(ApiClient api) {
        api.authentication()
                .register()
                .body(registerRequest)
                .execute(response -> {
                    assertThat(response.getStatusCode(), equalTo(400));
                    return response;
                });
        return this;
    }


}