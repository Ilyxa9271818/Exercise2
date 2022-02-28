package org.simbirsoft.tests;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import org.simbirsoft.pojo.UserPojo;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.filter.InFilter.in;

public class UserEmail {

    RequestSpecification requestSpec =
            new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/api/users")
                    .setContentType(ContentType.JSON)
                    .build();

    ResponseSpecification responseSpec =
            new ResponseSpecBuilder()
                    .expectStatusCode(200)
                    .build();

    @Test
    public void CheckingTheFirstUser() {
        List<UserPojo> users = given()
                .spec(requestSpec)
                .param("page", "1")
                .when()
                .get()
                .then()
                .spec(responseSpec)
                .extract().jsonPath().getList("data", UserPojo.class);
        assertThat(users)
                .filteredOn("firstName", in("George"))
                .filteredOn("lastName", in("Bluth"))
                .extracting("email")
                .contains("george.bluth@reqres.in");
    }

    @Test
    public void CheckingTheSecondUser() {
        List<UserPojo> users = given()
                .spec(requestSpec)
                .param("per_page", "100")
                .when()
                .get()
                .then()
                .spec(responseSpec)
                .extract().jsonPath().getList("data", UserPojo.class);
        assertThat(users)
                .filteredOn("firstName", in("Michael"))
                .filteredOn("lastName", in("Lawson"))
                .extracting("email")
                .contains("michael.lawson@reqres");
    }
}
