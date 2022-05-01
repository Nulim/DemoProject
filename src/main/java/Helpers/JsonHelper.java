package Helpers;

import io.restassured.path.json.JsonPath;

public class JsonHelper {

    public static String getDataFromJsonResponse(String response, String dataJsonPath){
        return new JsonPath(response).getString(dataJsonPath);
    }
}
