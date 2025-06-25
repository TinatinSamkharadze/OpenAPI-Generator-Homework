package ge.tbc.testautomation.data;

import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;

public class DataSupplier {
    private static final Faker faker = new Faker();

    @DataProvider(name = "invalidPasswords")
    public Object[][] invalidPasswords() {
        return new Object[][]{
                {faker.internet().password(1, 7, false, false, false)},
                {faker.lorem().characters(8, 15).toUpperCase()},
                {faker.lorem().characters(8, 15).toLowerCase()},
                {faker.number().digits(8)},
                {"!@#$%^&*()"},
                {faker.internet().password(8, 15, true, false, true)},
                {faker.internet().password(8, 15, false, true, true)},
                {faker.internet().password(8, 15, true, true, false)},
        };
    }
}
