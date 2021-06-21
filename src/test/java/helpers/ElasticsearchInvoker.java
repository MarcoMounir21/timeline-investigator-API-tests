package helpers;

import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.equalTo;

public class ElasticsearchInvoker {
  private static final String baseUri = "http://localhost:9200/";
  private static final String DOC = "/_doc/";

  public static void createDocument(String indexUri, JsonObject requestBody, String id) {
    RestAssured.given()
        .header("Content-Type", "application/json")
        .body(requestBody)
        .post(baseUri + indexUri + DOC + id)
        .then()
        .body("result", equalTo("created"));
  }

  public static void deleteIndex(String indexUri) {
    RestAssured.given()
        .header("Content-Type", "application/json")
        .delete(baseUri + indexUri)
        .then()
        .body("acknowledged", equalTo(true));
  }

  public static void createIndex(String indexUri) {
    RestAssured.given()
        .header("Content-Type", "application/json")
        .put(baseUri + indexUri)
        .then()
        .assertThat()
        .body("acknowledged", equalTo(true));
  }
}
