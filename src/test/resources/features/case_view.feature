Feature: View Investigation Case

  Background:
    Given insert the following investigation cases:
      | Base-Id | Base-CreatedAt | Base-UpdatedAt | Base-DeletedAt | CreatorID | Name       | Description            | FromDate   | ToDate     | Investigators     |
      | 1       | 01-06-2021     | 0              | 0              | Avian     | First Case | This is the first case | 01-06-2021 | 01-07-2021 | John Doe-Jane Doe |

  Scenario: View Investigation Case As a Super User
    Given user is logged in as a super user
    When user views an investigation case with id 1
    Then an investigation case is returned to the user with the following data:
      | Base-Id | Base-CreatedAt | Base-UpdatedAt | Base-DeletedAt | CreatorID | Name       | Description            | FromDate   | ToDate     | Investigators     |
      | 1       | 01-06-2021     | 0              | 0              | Avian     | First Case | This is the first case | 01-06-2021 | 01-07-2021 | John Doe-Jane Doe |

  Scenario: View Missing Investigation Case As a Super User
    Given user is logged in as a super user
    When User views an investigation case with id 2
    Then a "Cannot find Case: not found" message is returned to the user