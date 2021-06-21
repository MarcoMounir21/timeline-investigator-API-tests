package utils;

import com.google.gson.JsonObject;
import helpers.BackendInvoker;
import helpers.ElasticsearchInvoker;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.Map;

public class CaseUtils {
  private static final String URI = "cases";
  private static final String BACK_URI = "case";
  private static Response response;

  public static void createCase(DataTable dataTable) {
    Map<String, String> caseData = dataTable.asMaps().get(0);
    JsonObject caseRequestBody = CaseUtils.createPostCaseElasticsearchRequestBody(caseData);
    ElasticsearchInvoker.deleteIndex(URI);
    ElasticsearchInvoker.createIndex(URI);
    ElasticsearchInvoker.createDocument(URI, caseRequestBody, caseData.get("Id"));
  }

  public static JsonObject createPostCaseElasticsearchRequestBody(Map<String, String> caseData) {
    JsonObject investigationCase = new JsonObject();
    investigationCase.addProperty("Name", caseData.get("Name"));
    investigationCase.addProperty("Created_at", caseData.get("CreatedAt"));
    investigationCase.addProperty("Description", caseData.get("Description"));
    investigationCase.addProperty("From", caseData.get("From"));
    investigationCase.addProperty("To", caseData.get("To"));
    return investigationCase;
  }

  public static void getCaseBackend(Integer id) throws InterruptedException {
    CaseUtils.response = BackendInvoker.sendGetRequest(BACK_URI, id.toString());
  }

  public static void assertViewCase(DataTable responseTable) {
    Map<String, String> expectedCaseMap = responseTable.asMaps().get(0);
    JsonObject jsonResponse = CaseUtils.response.then().extract().as(JsonObject.class);
    JsonObject caseResponse = jsonResponse.get("case").getAsJsonObject();
    Assert.assertEquals(expectedCaseMap.get("Name"), caseResponse.get("name").getAsString());
    Assert.assertEquals(
        expectedCaseMap.get("Description"), caseResponse.get("description").getAsString());
  }

  public static void assertResponseErrorMessage(String message) {
    JsonObject jsonResponse = CaseUtils.response.then().extract().as(JsonObject.class);
    String errorResponse = jsonResponse.get("error").getAsString();
    Assert.assertEquals(message, errorResponse);
  }
}
