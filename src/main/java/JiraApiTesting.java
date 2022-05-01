import Helpers.JsonHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import pojo.GetUserSessionDetails;
import pojo.PostUserLoginDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class JiraApiTesting {

    public static void main(String[] args) throws IOException {

        var properties = new Properties();
        properties.load(new FileInputStream("C:\\Users\\faisa\\IdeaProjects\\DemoProject\\src\\main\\resources\\global.properties"));

        var baseUrl = properties.getProperty("baseUrl");

        var requestSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .build();

        var responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        //RestAssured.baseURI = "http://localhost:8080";

        var sessionFilter = new SessionFilter();
        var userLoginDetails = new PostUserLoginDetails();

        userLoginDetails.setUsername("faisal");
        userLoginDetails.setPassword("admin123");

        //Get Session
        var userLoginResponse = given().spec(requestSpec)
                .body(userLoginDetails)
                .filter(sessionFilter)
                .when().post("/rest/auth/1/session")
                .then().spec(responseSpec).extract().response().as(GetUserSessionDetails.class);

        System.out.println(userLoginResponse.getSession().getName() + " " + userLoginResponse.getSession().getValue());
        System.out.println(userLoginResponse.getLoginInfo().getLoginCount() + " " + userLoginResponse.getLoginInfo().getPreviousLoginTime());

        //Add Comment
        /*given().pathParam("id", "10002")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"Testing Comment via code.\"\n" +
                        "}")
                .filter(sessionFilter)
                .when().post("/rest/api/2/issue/{id}/comment")
                .then().assertThat().statusCode(201);*/

        //Add Attachment
        /*given().pathParam("id", "10002")
                .header("X-Atlassian-Token", "no-check")
                .header("Content-Type", "multipart/form-data")
                .filter(sessionFilter)
                .multiPart("file", new File("C:\\Users\\faisa\\IdeaProjects\\DemoProject\\src\\main\\resources\\jira.txt"))
                .when().post("/rest/api/2/issue/{id}/attachments")
                .then().assertThat().statusCode(200);

        //Get Issue
        var getResponse = given().pathParam("id", "10002")
                .queryParam("fields", "comment")
                .header("Content-Type", "application/json")
                .filter(sessionFilter)
                .when().get("/rest/api/2/issue/{id}")
                .then().assertThat().statusCode(200).extract().response().asString();

        //commit comment
        System.out.println(getResponse);*/
    }

}
