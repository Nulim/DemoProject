package Tests;

import Files.payload;
import Helpers.JsonHelper;
import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJsonTest {

    @Test(dataProvider = "BooksData")
    public void AddBookTest(String isbn, String aisle){
        RestAssured.baseURI = "http://216.10.245.166";

        var response = given().header("Content-Type", "application/json")
                .body(payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then().assertThat().statusCode(200).extract().response().asString();

        var dataResponse = JsonHelper.getDataFromJsonResponse(response, "ID");
        System.out.println(dataResponse);
    }

    @Test(dataProvider = "BooksData")
    public void DeleteBookTest(String isbn, String aisle){
        RestAssured.baseURI = "http://216.10.245.166";

        var response = given().header("Content-Type", "application/json")
                .body(payload.deleteBook(isbn+aisle))
                .when()
                .post("/Library/DeleteBook.php")
                .then().assertThat().statusCode(200).extract().response().asString();

        var dataResponse = JsonHelper.getDataFromJsonResponse(response, "msg");
        System.out.println(dataResponse);
    }

    @DataProvider(name = "BooksData")
    public Object[][] getBookData(){
        return new Object[][]{{"FazTest1", "123"}, {"FazTest2", "123"}, {"FazTest3", "123"}};
    }
}
