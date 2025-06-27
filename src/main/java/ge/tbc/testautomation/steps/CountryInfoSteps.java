package ge.tbc.testautomation.steps;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ge.tbc.testautomation.data.ConstantsSoap.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryInfoSteps {
    private Response response;
    private List<String> sNames;
    private List<String> sCodes;

    @Step("Retrieve the list of countries")
    public CountryInfoSteps getListOfCountries() {
        response = RestAssured
                .given()
                .filter(new AllureRestAssured())
                .when()
                .get(COUNTRY_INFO_URL)
                .then()
                .extract()
                .response();
        return this;
    }

    @Step("Validate the status code is 200")
    public CountryInfoSteps validateStatusCode() {
        response.then().statusCode(200);
        return this;
    }

    @Step("Validate the count of all sName nodes is {0}")
    public CountryInfoSteps validateCountOfAllSNameNode(int expectedSize) {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        assertThat(sNames, hasSize(expectedSize));
        return this;
    }

    @Step("Validate the list of all sName nodes contains expected values")
    public CountryInfoSteps validateListOfAllSNameNodesValue() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        assertThat(sNames, hasItems(SNAMES));
        return this;
    }

    @Step("Validate sName for sCode {0} is {1}")
    public CountryInfoSteps validateSNameNodeWithValueOfSCode(String sCode, String expectedName) {
        String sName = response.xmlPath().getString("ArrayOfContinent.tContinent.find { it.sCode == '" + sCode + "' }.sName");
        assertThat(sName, equalTo(expectedName));
        return this;
    }

    @Step("Validate the last tContinent value is {0}")
    public CountryInfoSteps validateLastTContinentValue(String expectedName) {
        String sName = response.xmlPath().getString("ArrayOfContinent.tContinent[-1].sName");
        assertThat(sName, equalTo(expectedName));
        return this;
    }

    @Step("Validate each sName is unique")
    public CountryInfoSteps validateEachSNameIsUnique() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        assertThat(new HashSet<>(sNames), hasSize(sNames.size()));
        return this;
    }

    @Step("Validate presence of sName for each continent")
    public CountryInfoSteps validatePresenceOfSNameForEachContinent() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        assertThat(sNames, hasItems(SNAMES));
        return this;
    }

    @Step("Validate presence of sCode for each continent")
    public CountryInfoSteps validatePresenceOfSCodeForEachContinent() {
        sCodes = response.xmlPath().getList("ArrayOfContinent.tContinent.sCode");
        assertThat(sCodes, hasItems(SCODES));
        return this;
    }

    @Step("Validate each continent code and name pair")
    public CountryInfoSteps validateEachContinentCodeNamePair() {
        sCodes = response.xmlPath().getList("ArrayOfContinent.tContinent.sCode");
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        for (int i = 0; i < sCodes.size(); i++) {
            String sCode = sCodes.get(i);
            String sName = sNames.get(i);
            assertThat(sName, equalTo(CONTINENT_MAP.get(sCode)));
        }
        return this;
    }

    @Step("Validate sCode values follow the pattern [A-Z]{{2}}")
    public CountryInfoSteps validateSCodeValuesFollowASpecificPattern() {
        sCodes = response.xmlPath().getList("ArrayOfContinent.tContinent.sCode");
        sCodes.forEach(sCode -> assertThat(sCode, matchesPattern("[A-Z]{2}")));
        return this;
    }

    @Step("Validate tContinents names are alphabetically sorted")
    public CountryInfoSteps validateTContinentsNamesAreAlphabeticallySorted() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        List<String> sorted = sNames.stream()
                .sorted()
                .collect(Collectors.toList());
        assertThat(sNames, equalTo(sorted));
        return this;
    }

    @Step("Validate presence of all continents")
    public CountryInfoSteps validatePresenceOfAllContinents() {
        response.then().assertThat().body("ListOfContinentsByNameResult.tContinent.sName", hasItems(SNAMES));
        return this;
    }

    @Step("Validate sNames do not contain numeric characters")
    public CountryInfoSteps validateSNamesDoNotContainNumericCharacters() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        sNames.forEach(name -> assertThat(name, not(matchesPattern(".*\\d.*"))));
        return this;
    }

    @Step("Validate only Oceania starts with 'O'")
    public CountryInfoSteps validateOnlyOcenaniaStartsWithO() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        assertThat(
                sNames.stream().filter(name -> name.startsWith("O")).collect(Collectors.toList()),
                equalTo(Collections.singletonList(OCENANIA))
        );
        return this;
    }

    @Step("Validate names starting with 'A' and ending with 'ca'")
    public CountryInfoSteps validateNamesThatStartsWithAEndsWithCa() {
        sNames = response.xmlPath().getList("ArrayOfContinent.tContinent.sName");
        List<String> filtered = sNames.stream()
                .filter(name -> name.startsWith("A") && name.endsWith("ca"))
                .collect(Collectors.toList());

        assertThat(filtered, hasItems(ANTARCTICA, AFRICA));
        assertThat(filtered, everyItem(startsWith("A")));
        assertThat(filtered, everyItem(endsWith("ca")));
        return this;
    }
}
