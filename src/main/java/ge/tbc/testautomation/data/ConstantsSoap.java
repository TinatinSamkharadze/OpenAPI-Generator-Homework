package ge.tbc.testautomation.data;

import java.util.Map;

public class ConstantsSoap {
    public static String[] SNAMES = new String[]{"Africa", "Antarctica", "Asia",
            "Europe", "Ocenania", "The Americas"},
            SCODES = new String[]{"AF", "AN", "AS", "EU", "OC", "AM"};
    public static final String COUNTRY_INFO_URL = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName",
            ANTARCTICA = "Antarctica",
            THE_AMERICAS = "The Americas",
            OCENANIA = "Ocenania",
            AFRICA = "Africa",
            NAME = "Saba",
            ADDRESS = "Far Far Away",
            EMAIL = "SorcererKing@AinzOoalGown.ovl",
            DEPARTMENT = "Happy Farm HR Division",
            PHONE_NUMBER = "37067890123456",
            LOCALHOST_BASE_URI = "http://localhost:8087/ws",
            UPDATED_NAME = "Sorcerer King",
            UPDATED_ADDRESS = "Sorcerer Kingdom",
            UPDATED_EMAIL = "PapaBones@Nazarick.skull",
            UPDATED_DEPARTMENT = "Department of SASUGA",
            UPDATED_PHONE_NUMBER = "380647901234567",
            CONTENT_DELETED_SUCCESSFULLY = "Content Deleted Successfully",
            CONTENT_UPDATED_SUCCESSFULLY = "Content Updated Successfully",
            PREFIX_NS2 = "ns2",
            PREFIX_SOAP_ENV = "SOAP_ENV",
            NAMESPACE_URI_SOAP_SPRINGBOOT = "http://interfaces.soap.springboot.example.com",
            ACTION_ADD_EMPLOYEE = "interfaces.soap.springboot.example.com/exampleSoapHttp/addEmployeeRequest",
            ACTION_GET_EMPLOYEE_BY_ID = "interfaces.soap.springboot.example.com/exampleSoapHttp/getEmployeeByIdRequest",
            ACTION_UPDATE_EMPLOYEE = "interfaces.soap.springboot.example.com/exampleSoapHttp/updateEmployeeRequest",
            ACTION_DELETE_EMPLOYEE = "interfaces.soap.springboot.example.com/exampleSoapHttp/deleteEmployeeRequest",
            NAMESPACE_URI_SOAP_ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/",
            CONTENT_ADDED_SUCCESSFULLY = "Content Added Successfully",
            AN = "AN";


    public static final int EXPECTED_SIZE_OF_SNAME_NODES = 6,
            EMPLOYEE_ID = 5,
            BIRTH_YEAR = 2004,
            BIRTH_MONTH = 1,
            BIRTH_DAY = 11,
            UPDATED_BIRTH_YEAR = 1867,
            UPDATED_BIRTH_MONTH = 11,
            UPDATED_BIRTH_DAY = 7,
            SALARY = 50000,
            UPDATED_SALARY = 60000;
    public static final Map<String, String> CONTINENT_MAP = Map.of(
            "AF", "Africa",
            "AN", "Antarctica",
            "AS", "Asia",
            "EU", "Europe",
            "OC", "Ocenania",
            "AM", "The Americas"
    );

}
