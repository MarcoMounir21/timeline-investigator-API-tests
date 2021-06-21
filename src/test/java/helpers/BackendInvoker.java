package helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BackendInvoker {

  public static final String baseUri = "http://localhost:8080/";

  public static Response sendGetRequest(String apiUri, String caseID) throws InterruptedException {
    Thread.sleep(1000);
    return RestAssured.given()
        .header("Content-Type", "application/json")
        .get(baseUri + apiUri + "/" + caseID);
  }
}
