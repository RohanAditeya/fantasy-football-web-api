Feature: The league team API provides the functionality to store and retrieve league teams data obtained from the
  premier league fantasy API.

  Rule: The Post endpoint must provide functionality to save new league team records to the data store
    @PostRequest
    Scenario: The user can save league team records to the data store through POST API
      When A user calls the POST API with values
        """json
          {
              "code": 3,
              "draw": 0,
              "form": null,
              "id": 1,
              "loss": 0,
              "name": "Arsenal",
              "played": 0,
              "points": 0,
              "position": 0,
              "shortName": "ARS",
              "strength": 4,
              "teamDivision": null,
              "unavailable": false,
              "win": 0,
              "strengthOverallHome": 1230,
              "strengthOverallAway": 1285,
              "strengthAttackHome": 1250,
              "strengthAttackAway": 1250,
              "strengthDefenceHome": 1210,
              "strengthDefenceAway": 1320,
              "pulseId": 1
          }
        """
      Then The record with team code 3 is successfully stored in DB