package ge.tbc.testautomation;

import ge.tbc.testautomation.steps.CountryInfoSteps;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static ge.tbc.testautomation.data.ConstantsSoap.*;

@Epic("Country Information API")
public class CountryInfoTest {

    private CountryInfoSteps countryInfoSteps = new CountryInfoSteps();

    @Test
    @Feature("Retrieve Country Information")
    @Description("This test retrieves country information and validates various aspects of the response.")
    @Severity(SeverityLevel.NORMAL)
    @Story("As a user, I want to retrieve and validate country information from the API.")
    public void testCountryInfoRetrievalAndValidation() {
        countryInfoSteps
                .getListOfCountries()
                .validateStatusCode()
                .validateCountOfAllSNameNode(EXPECTED_SIZE_OF_SNAME_NODES)
                .validateListOfAllSNameNodesValue()
                .validateSNameNodeWithValueOfSCode(AN, ANTARCTICA)
                .validateLastTContinentValue(THE_AMERICAS)
                .validateEachSNameIsUnique()
                .validatePresenceOfSNameForEachContinent()
                .validatePresenceOfSCodeForEachContinent()
                .validateEachContinentCodeNamePair()
                .validateSCodeValuesFollowASpecificPattern()
                .validateTContinentsNamesAreAlphabeticallySorted()
                .validatePresenceOfAllContinents()
                .validateSNamesDoNotContainNumericCharacters()
                .validateOnlyOcenaniaStartsWithO()
                .validateNamesThatStartsWithAEndsWithCa();
    }
}
