import Files.payload;
import Helpers.JsonHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Basics {
    public static void main(String[] args) {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        var response = given()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(payload.AddPlace())
                .when().post("maps/api/place/add/json")
                .then()
                .assertThat()
                    .statusCode(200)
                    .body("scope", equalTo("APP"))
                .extract().response().asString();

        System.out.println(response);
        var placeId = new JsonPath(response).getString("place_id");

        //Update Place
        given().queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\"70 Summer walk, USA\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

        //Get Place
        var getResponse = given().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).body("address", equalTo("70 Summer walk, USA")).extract().response().asString();

        Assert.assertEquals(JsonHelper.getDataFromJsonResponse(getResponse, "address"), "70 Summer walk, USA");
    }
}
