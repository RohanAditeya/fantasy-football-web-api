Feature: The League player API provides endpoints for performing CRUD operations to manage and store player details from fantasy leagues.

  Rule: The Post endpoint must provide functionality to save new player records to the data store
    @PostRequest
    Scenario: The user can save valid player records to the data store through POST API when the team already exists.
      Given The check for team with code 4 existing in league team table returns true
      When A user calls the POST '/fantasy/football/v1/league-player/create' API with values
      """json
        {
          "code": 184029,
          "firstName": "Martin",
          "secondName": "Ødegaard",
          "squadNumber": null,
          "status": "a",
          "teamCode": 4,
          "webName": "Ødegaard"
        }
      """
      Then the player record with code 184029 is successfully stored in DB
    @PostRequest
    Scenario: When a user tries to save a player whose team code does not exists in league team table an exception is thrown
      Given The check for team with code 4 existing in league team table returns false
      Then A user calls the POST '/fantasy/football/v1/league-player/create' API with values returns exception
      """json
        {
          "code": 184029,
          "firstName": "Martin",
          "secondName": "Ødegaard",
          "squadNumber": null,
          "status": "a",
          "teamCode": 5,
          "webName": "Ødegaard"
        }
      """