package ge.tbc.testautomation.steps;

import com.example.springboot.soap.interfaces.*;
import ge.tbc.testautomation.data.ConstantsSoap;
import ge.tbc.testautomation.util.Marshall;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.LocalDate;

import static ge.tbc.testautomation.data.ConstantsSoap.*;
import static ge.tbc.testautomation.util.Marshall.marshallSoapRequest;
import static io.restassured.RestAssured.given;
import static io.restassured.config.XmlConfig.xmlConfig;
import static org.hamcrest.Matchers.hasXPath;

public class EmployeeSteps {
    ObjectFactory objectFactory = new ObjectFactory();
    Response response;
    String body;
    EmployeeInfo employeeInfo;

    @Step("Prepare employee info for addition")
    public EmployeeSteps prepareEmployeeInfo() {
        employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(EMPLOYEE_ID);
        employeeInfo.setName(NAME);
        employeeInfo.setAddress(ADDRESS);
        employeeInfo.setEmail(EMAIL);
        employeeInfo.setDepartment(DEPARTMENT);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(LocalDate.of(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY).toString());
            employeeInfo.setBirthDate(xmlDate);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        employeeInfo.setPhone(PHONE_NUMBER);
        employeeInfo.setSalary(BigDecimal.valueOf(SALARY));
        return this;
    }

    @Step("Prepare add employee request body")
    public EmployeeSteps prepareAddEmployeeRequestBody() {
        AddEmployeeRequest employeeRequest = objectFactory.createAddEmployeeRequest();
        employeeRequest.setEmployeeInfo(employeeInfo);
        body = marshallSoapRequest(employeeRequest);
        return this;
    }

    @Step("Send SOAP request to add employee")
    public EmployeeSteps submitAddEmployeeRequest() {
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace(PREFIX_NS2, NAMESPACE_URI_SOAP_SPRINGBOOT)
                        .declareNamespace(PREFIX_SOAP_ENV, NAMESPACE_URI_SOAP_ENVELOPE)))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_ADD_EMPLOYEE)
                .body(body)
                .post(LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate the status code of the response")
    public EmployeeSteps validateStatusCode() {
        response.then().statusCode(200);
        return this;
    }

    @Step("Validate employee addition message in response")
    public EmployeeSteps validateEmployeeAdditionMessage() {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and text()='" + CONTENT_ADDED_SUCCESSFULLY + "']"));
        return this;
    }

    @Step("Prepare request body to get employee by ID: {0}")
    public EmployeeSteps prepareGetEmployeeByIdRequestBody(long id) {
        GetEmployeeByIdRequest getEmployeeByIdRequest = objectFactory.createGetEmployeeByIdRequest();
        getEmployeeByIdRequest.setEmployeeId(id);
        this.body = Marshall.marshallSoapRequest(getEmployeeByIdRequest);
        return this;
    }

    @Step("Send request to get employee by ID")
    public EmployeeSteps sendGetEmployeeByIdRequest() {
        response = given()
                .contentType("text/xml")
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_GET_EMPLOYEE_BY_ID)
                .body(body)
                .when()
                .post(LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate returned employee name: {0}")
    public EmployeeSteps validateReturnedEmployeeName(String name) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='name' and text()='" + name + "']"));
        return this;
    }

    @Step("Validate returned employee email: {0}")
    public EmployeeSteps validateReturnedEmployeeEmail(String email) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='email' and text()='" + email + "']"));
        return this;
    }

    @Step("Validate returned employee department: {0}")
    public EmployeeSteps validateReturnedEmployeeDepartment(String department) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='department' and text()='" + department + "']"));
        return this;
    }

    @Step("Validate returned employee address: {0}")
    public EmployeeSteps validateReturnedEmployeeAddress(String address) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='address' and text()='" + address + "']"));
        return this;
    }

    @Step("Validate returned employee phone number: {0}")
    public EmployeeSteps validateReturnedEmployeePhoneNumber(String number) {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='phone' and text()='" + number + "']"));
        return this;
    }

    @Step("Prepare employee info for updating")
    public EmployeeSteps prepareEmployeeInfoForUpdate() {
        employeeInfo = objectFactory.createEmployeeInfo();
        employeeInfo.setEmployeeId(EMPLOYEE_ID);
        employeeInfo.setName(UPDATED_NAME);
        employeeInfo.setEmail(UPDATED_EMAIL);
        employeeInfo.setAddress(UPDATED_ADDRESS);
        employeeInfo.setDepartment(UPDATED_DEPARTMENT);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(LocalDate.of(UPDATED_BIRTH_YEAR, UPDATED_BIRTH_MONTH, UPDATED_BIRTH_DAY).toString());
            employeeInfo.setBirthDate(xmlDate);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
        employeeInfo.setPhone(UPDATED_PHONE_NUMBER);
        employeeInfo.setSalary(BigDecimal.valueOf(UPDATED_SALARY));
        return this;
    }

    @Step("Create update employee request body")
    public EmployeeSteps prepareUpdateRequestBody() {
        UpdateEmployeeRequest updateEmployeeRequest = objectFactory.createUpdateEmployeeRequest();
        updateEmployeeRequest.setEmployeeInfo(employeeInfo);
        body = marshallSoapRequest(updateEmployeeRequest);
        return this;
    }

    @Step("Send request to update employee")
    public EmployeeSteps sendRequestForUpdate() {
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace(PREFIX_NS2, NAMESPACE_URI_SOAP_SPRINGBOOT)
                        .declareNamespace(PREFIX_SOAP_ENV, NAMESPACE_URI_SOAP_ENVELOPE)))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_UPDATE_EMPLOYEE)
                .body(body)
                .post(ConstantsSoap.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate employee update message in response")
    public EmployeeSteps validateEmployeeUpdateMessage() {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and text()='" + CONTENT_UPDATED_SUCCESSFULLY + "']"));
        return this;
    }

    @Step("Prepare request body for deleting employee with ID: {0}")
    public EmployeeSteps prepareDeleteRequest(long id) {
        DeleteEmployeeRequest deleteEmployeeRequest = objectFactory.createDeleteEmployeeRequest();
        deleteEmployeeRequest.setEmployeeId(id);
        body = marshallSoapRequest(deleteEmployeeRequest);
        return this;
    }

    @Step("Send request to delete employee")
    public EmployeeSteps sendRequestForDelete() {
        response = given()
                .config(RestAssured.config().xmlConfig(xmlConfig()
                        .declareNamespace(PREFIX_NS2, NAMESPACE_URI_SOAP_SPRINGBOOT)
                        .declareNamespace(PREFIX_SOAP_ENV, NAMESPACE_URI_SOAP_ENVELOPE)))
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SOAPAction", ACTION_DELETE_EMPLOYEE)
                .body(body)
                .post(ConstantsSoap.LOCALHOST_BASE_URI);
        return this;
    }

    @Step("Validate the delete confirmation message in response")
    public EmployeeSteps validateDeleteMessage() {
        response.then()
                .assertThat()
                .body(hasXPath("//*[local-name()='message' and text()='" + CONTENT_DELETED_SUCCESSFULLY + "']"));
        return this;
    }

    @Step("Validate the status code of the response is 500")
    public EmployeeSteps validateStatusCode500() {
        response.then().statusCode(500);
        return this;
    }
}
