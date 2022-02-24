package org.simbirsoft.tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.simbirsoft.pojo.UserPojo;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.filter.Filters.filter;
import static org.assertj.core.api.filter.InFilter.in;

public class UserEmail {

    @BeforeMethod
    public void configureRestAssured() {
        RestAssured.baseURI = "https://reqres.in/api/users";
    }

    @Test
    public void CheckingTheFirstUser() {
        List<UserPojo> users = given()
                .contentType(ContentType.JSON)
                .when().get("?page=1")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("data", UserPojo.class);
        assertThat(filter(users)
                .and("firstName")
                .equalsTo("George")
                .and("lastName")
                .equalsTo("Bluth").get())
                .extracting("email")
                .contains("george.bluth@reqres.in");
    }

    @Test
    public void CheckingTheSecondUser() {
        List<UserPojo> users = given()
                .contentType(ContentType.JSON)
                .when().get("?page=2")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("data", UserPojo.class);
        assertThat(users)
                .filteredOn("firstName", in("Michael"))
                .filteredOn("lastName", in("Lawson"))
                .extracting("email")
                .contains("michael.lawson@reqres");
    }
}
