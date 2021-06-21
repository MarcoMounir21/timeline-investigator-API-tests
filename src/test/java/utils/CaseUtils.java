package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.BackendInvoker;
import helpers.ElasticsearchInvoker;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import org.junit.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CaseUtils {
  private static final String URI = "cases";
  private static final String BACK_URI = "case";
  private static Response response;

  public static void createCase(DataTable dataTable) throws ParseException {
    Map<String, String> caseData = dataTable.asMaps().get(0);
    JsonObject caseRequestBody = CaseUtils.createPostCaseElasticsearchRequestBody(caseData);
    ElasticsearchInvoker.deleteIndex(URI);
    ElasticsearchInvoker.createIndex(URI);
    ElasticsearchInvoker.createDocument(URI, caseRequestBody, caseData.get("Base-Id"));
  }

  public static JsonObject createPostCaseElasticsearchRequestBody(Map<String, String> caseData)
      throws ParseException {
    JsonObject investigationCase = new JsonObject();
    JsonObject baseObject = new JsonObject();

    JsonArray jsonInvestigators = new JsonArray();
    String[] investigators = caseData.get("Investigators").split("-");

    for (String investigator : investigators) {
      jsonInvestigators.add(investigator);
    }

    baseObject.addProperty("id", caseData.get("Base-Id"));

    baseObject.addProperty("createdAt", getUnixDate(caseData.get("Base-CreatedAt")));
    baseObject.addProperty("updatedAt", getUnixDate(caseData.get("Base-UpdatedAt")));
    baseObject.addProperty("deletedAt", getUnixDate(caseData.get("Base-DeletedAt")));
    investigationCase.add("base", baseObject);
    investigationCase.addProperty("creatorID", caseData.get("CreatorID"));
    investigationCase.addProperty("name", caseData.get("Name"));
    investigationCase.addProperty("description", caseData.get("Description"));
    investigationCase.addProperty("fromDate", getUnixDate(caseData.get("FromDate")));
    investigationCase.addProperty("toDate", getUnixDate(caseData.get("ToDate")));
    investigationCase.add("investigators", jsonInvestigators);

    return investigationCase;
  }

  private static long getUnixDate(String date) throws ParseException {

    if (date.equals("0")) {
      return 0;
    }
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    Date unixDate = format.parse(date);
    return unixDate.getTime();
  }

  public static void getCaseBackend(Integer id) throws InterruptedException {
    CaseUtils.response = BackendInvoker.sendGetRequest(BACK_URI, id.toString());
  }

  public static void assertViewCase(DataTable responseTable) throws ParseException {
    Map<String, String> expectedCaseMap = responseTable.asMaps().get(0);

    JsonObject jsonResponse = CaseUtils.response.then().extract().as(JsonObject.class);
    JsonObject caseResponse = jsonResponse.get("case").getAsJsonObject();
    JsonObject baseObject = caseResponse.get("base").getAsJsonObject();

    // Assert Base Object fields
    Assert.assertEquals(expectedCaseMap.get("Base-Id"), baseObject.get("id").getAsString());
    Assert.assertEquals(
        getUnixDate(expectedCaseMap.get("Base-CreatedAt")),
        baseObject.get("createdAt").getAsLong());
    Assert.assertEquals(
        getUnixDate(expectedCaseMap.get("Base-UpdatedAt")),
        baseObject.get("updatedAt").getAsLong());
    Assert.assertEquals(
        getUnixDate(expectedCaseMap.get("Base-DeletedAt")),
        baseObject.get("deletedAt").getAsLong());

    // Assert Case Details
    Assert.assertEquals(
        expectedCaseMap.get("CreatorID"), caseResponse.get("creatorID").getAsString());
    Assert.assertEquals(expectedCaseMap.get("Name"), caseResponse.get("name").getAsString());
    Assert.assertEquals(
        expectedCaseMap.get("Description"), caseResponse.get("description").getAsString());
    Assert.assertEquals(
        getUnixDate(expectedCaseMap.get("FromDate")), caseResponse.get("fromDate").getAsLong());
    Assert.assertEquals(
        getUnixDate(expectedCaseMap.get("ToDate")), caseResponse.get("toDate").getAsLong());

    // Assert on Investigators Array
    JsonArray jsonInvestigators = caseResponse.get("investigators").getAsJsonArray();
    String[] expectedInvestigators = expectedCaseMap.get("Investigators").split("-");

    for (int i = 0; i < jsonInvestigators.size(); i++) {
      Assert.assertEquals(expectedInvestigators[i], jsonInvestigators.get(i).getAsString());
    }
  }

  public static void assertResponseErrorMessage(String message) {
    JsonObject jsonResponse = CaseUtils.response.then().extract().as(JsonObject.class);
    String errorResponse = jsonResponse.get("error").getAsString();
    Assert.assertEquals(message, errorResponse);
  }
}
