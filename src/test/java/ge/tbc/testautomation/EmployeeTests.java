package ge.tbc.testautomation;

import ge.tbc.testautomation.steps.EmployeeSteps;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static ge.tbc.testautomation.data.ConstantsSoap.*;

@Epic("Employee Management API")
public class EmployeeTests {
    EmployeeSteps employeeSteps = new EmployeeSteps();

    @Test(priority = 1)
    @Feature("Employee adding operation")
    @Description("Test to add a new employee and validate the response.")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an admin, I want to add a new employee to the system.")
    public void addEmployeeTest() {
        employeeSteps
                .prepareEmployeeInfo()
                .prepareAddEmployeeRequestBody()
                .submitAddEmployeeRequest()
                .validateStatusCode()
                .validateEmployeeAdditionMessage();
    }

    @Test(priority = 2)
    @Feature("Employee getting by id operation")
    @Description("Test to retrieve an employee by ID and validate the returned information.")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an admin, I want to retrieve employee details by their ID.")
    public void getEmployeeByIdTest() {
        employeeSteps
                .prepareGetEmployeeByIdRequestBody(EMPLOYEE_ID)
                .sendGetEmployeeByIdRequest()
                .validateReturnedEmployeeName(NAME)
                .validateReturnedEmployeeEmail(EMAIL)
                .validateReturnedEmployeeDepartment(DEPARTMENT)
                .validateReturnedEmployeeAddress(ADDRESS)
                .validateReturnedEmployeePhoneNumber(PHONE_NUMBER);
    }

    @Test(priority = 3)
    @Feature("Employee updating operation")
    @Description("Test to update an existing employee's information and validate the update.")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an admin, I want to update an employee's details.")
    public void updateEmployeeTest() {
        employeeSteps
                .prepareEmployeeInfoForUpdate()
                .prepareUpdateRequestBody()
                .sendRequestForUpdate()
                .validateEmployeeUpdateMessage()
                .prepareGetEmployeeByIdRequestBody(EMPLOYEE_ID)
                .sendGetEmployeeByIdRequest()
                .validateReturnedEmployeeName(UPDATED_NAME)
                .validateReturnedEmployeeEmail(UPDATED_EMAIL)
                .validateReturnedEmployeeDepartment(UPDATED_DEPARTMENT)
                .validateReturnedEmployeeAddress(UPDATED_ADDRESS)
                .validateReturnedEmployeePhoneNumber(UPDATED_PHONE_NUMBER);
    }

    @Test(priority = 4)
    @Feature("Employee deleting operation")
    @Description("Test to delete an employee and validate the deletion response.")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an admin, I want to delete an employee from the system.")
    public void deleteEmployeeTest() {
        employeeSteps
                .prepareDeleteRequest(EMPLOYEE_ID)
                .sendRequestForDelete()
                .validateStatusCode()
                .validateDeleteMessage()
                .prepareGetEmployeeByIdRequestBody(EMPLOYEE_ID)
                .sendGetEmployeeByIdRequest()
                .validateStatusCode500();
    }
}
