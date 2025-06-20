package ge.tbc.testautomation.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import pet.store.v3.invoker.ApiClient;
import pet.store.v3.model.Category;
import pet.store.v3.model.Order;
import pet.store.v3.model.Pet;
import pet.store.v3.model.Tag;

import java.time.OffsetDateTime;
import java.util.Collections;

import static ge.tbc.testautomation.data.Constants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static pet.store.v3.invoker.ResponseSpecBuilders.shouldBeCode;
import static pet.store.v3.invoker.ResponseSpecBuilders.validatedWith;


public class PetStoreAPISteps {
    Pet pet;
    Response response;
    Order newOrderRequest;
    Order createdOrderResponse;

    @Step("Generate order request with ID: {ID}, pet ID: {PET_ID}, and ship date: {SHIP_DATE}")
    public PetStoreAPISteps generateOrderBody() {
        newOrderRequest = new Order()
                .id(ID)
                .petId(PET_ID)
                .quantity(QUANTITY)
                .shipDate(OffsetDateTime.parse(SHIP_DATE))
                .status(Order.StatusEnum.PLACED)
                .complete(COMPLETE);
        return this;

    }

    @Step("Submit new order for pet ID: {requestOrder.petId} and validate successful response")
    public PetStoreAPISteps submitPetStoreOrder(ApiClient apiClient) {
        createdOrderResponse = apiClient
                .store()
                .placeOrder()
                .body(newOrderRequest)
                .executeAs(response -> {
                    this.response = response;
                    validatedWith(shouldBeCode(200)).andThen(ResponseBody::print).apply(response);
                    return response;

                });
        return this;
    }

    @Step("Check order data consistency")
    public PetStoreAPISteps checkOrderDataConsistency() {
        assertThat(createdOrderResponse, allOf(
                hasProperty(PROPERTY_NAME_ID, equalTo(newOrderRequest.getId())),
                hasProperty(PROPERTY_NAME_PET_ID, equalTo(newOrderRequest.getPetId())),
                hasProperty(PROPERTY_NAME_QUANTITY, equalTo(newOrderRequest.getQuantity())),
                hasProperty(PROPERTY_NAME_SHIP_DATE, equalTo(newOrderRequest.getShipDate())),
                hasProperty(PROPERTY_NAME_STATUS, equalTo(newOrderRequest.getStatus())),
                hasProperty(PROPERTY_NAME_COMPLETE, equalTo(newOrderRequest.getComplete()))
        ));
        return this;
    }

    @Step("Generate pet request body")
    public PetStoreAPISteps generatePetRequest(String status) {
        pet = new Pet()
                .id(ID)
                .name(PET_NAME)
                .category(new Category()
                        .id(ID)
                        .name(PET_NAME))
                .photoUrls(Collections.singletonList(PHOTO_URL))
                .tags(Collections.singletonList(
                        new Tag()
                                .id(ID)
                                .name(PET_NAME)))
                .status(Pet.StatusEnum.fromValue(status));

        return this;
    }

    @Step("Post new pet and get response")
    public PetStoreAPISteps postNewPet(ApiClient api) {
        response = api
                .pet()
                .addPet()
                .body(pet)
                .execute(resp -> resp);
        return this;
        
    }

    @Step("Validate the created pet using Rest-Assured functional assertions")
    public PetStoreAPISteps validateNewPet() {
        response.then()
                .statusCode(200)
                .body(BODY_ID, equalTo(pet.getId().intValue()))
                .body(NAME, not(emptyString()))
                .body("photoUrls.size()", greaterThan(0))
                .body(PHOTO_URLS, hasItem(pet.getPhotoUrls().get(0)))
                .body("tags[0].id", equalTo(pet.getTags().get(0).getId().intValue()))
                .body(STATUS, anyOf(equalTo(AVAILABLE), equalTo(PENDING), equalTo(SOLD)));
        return this;

    }


}

