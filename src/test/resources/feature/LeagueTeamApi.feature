Feature: The league team API provides the functionality to store and retrieve league teams data obtained from the
  premier league fantasy API.

  Rule: The Get endpoint must provide functionality to query and fetch league team records from the data store.
    Scenario Outline: The GET API must fetch the league team record for the provided teamCode or return status as not found if no record is found.
      When The record for <teamCode> is <isAdded> in DB
      And A league team record is queried using a <teamCode> and API returns <status>
      And The record with <teamCode> if <isAdded> is deleted

      Examples:
        | teamCode | isAdded | status |
        | 1        | false     | 404    |
        | 2        | true      | 200    |