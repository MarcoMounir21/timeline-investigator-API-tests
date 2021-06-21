package definitions;

import io.cucumber.java8.En;
import utils.CaseUtils;

public class ViewCaseStepDefinitions implements En {
  public ViewCaseStepDefinitions() {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    Given("insert the following investigation cases:", CaseUtils::createCase);
    Given(
        "user is logged in as a super user",
        () -> {
          // Write code here that turns the phrase above into concrete actions
          // TODO: implement when login is ready
        });
    // Write code here that turns the phrase above into concrete actions
    When("user views an investigation case with id {int}", CaseUtils::getCaseBackend);
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    Then(
        "an investigation case is returned to the user with the following data:",
        CaseUtils::assertViewCase);

    When(
        "User views an investigation case with id {int}",
        // Write code here that turns the phrase above into concrete actions
        CaseUtils::getCaseBackend);

    Then("^a \"([^\"]*)\" message is returned to the user$", CaseUtils::assertResponseErrorMessage);
  }
}
