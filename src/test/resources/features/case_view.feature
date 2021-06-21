Feature: View Investigation Case

  Background:
    Given insert the following investigation cases:
      | Id | Name       | CreatedAt  | Description            | From       | To         |
      | 1  | First Case | 01-06-2021 | This is the first case | 01-06-2021 | 01-07-2021 |

  Scenario: View Investigation Case As a Super User
    Given user is logged in as a super user
    When user views an investigation case with id 1
    Then an investigation case is returned to the user with the following data:
      | Name       | CreatedAt  | Description            | From       | To         |
      | First Case | 01-06-2021 | This is the first case | 01-06-2021 | 01-07-2021 |

  Scenario: View Missing Investigation Case As a Super User
    Given user is logged in as a super user
    When User views an investigation case with id 2
    Then a "Cannot find Case: not found" response is returned