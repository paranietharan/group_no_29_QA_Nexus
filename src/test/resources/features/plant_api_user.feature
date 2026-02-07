@api @plant @user
Feature: Plant API - User Role
  As a user with read-only access
  I want to view plants through the API
  But I should not be able to create, update or delete plants

  Background:
    Given the user is authenticated

  # TC_API_USER_001
  @TC_API_USER_001 @positive
  Scenario: User fetches all plants returns 200
    When the user sends a GET request to fetch all plants
    Then the plant API response status code is 200
    And the response contains a list of plants

  # TC_API_USER_002
  @TC_API_USER_002 @positive
  Scenario: User fetches plant by ID returns 200
    Given a plant with ID 1 exists
    When the user sends a GET request to fetch plant with ID 1
    Then the plant API response status code is 200
    And the response contains plant details

  # TC_API_USER_003
  @TC_API_USER_003 @positive
  Scenario: User fetches plants by category returns 200
    Given a category with ID 2 exists with plants
    When the user sends a GET request to fetch plants by category ID 2
    Then the plant API response status code is 200
    And the response contains plants from the specified category

  # TC_API_USER_004
  @TC_API_USER_004 @positive
  Scenario: User searches plants by name returns 200
    Given a plant named "Rose" exists via API
    When the user sends a GET request to search plants with name "Rose"
    Then the plant API response status code is 200
    And the response contains plants matching the search term

  # TC_API_USER_005
  @TC_API_USER_005 @positive
  Scenario: User paginates plants returns 200
    Given more than 15 plants exist
    When the user sends a GET request to fetch plants with page 0 and size 5
    Then the plant API response status code is 200
    And the response contains at most 5 plants

  # TC_API_USER_006
  @TC_API_USER_006 @negative @authorization
  Scenario: User tries to create plant returns 403
    Given valid plant data for creation
    When the user sends a POST request to create a plant
    Then the plant API response status code is 403
    And the response contains an authorization error

  # TC_API_USER_007
  @TC_API_USER_007 @negative @authorization
  Scenario: User tries to update plant returns 403
    Given a plant with ID 1 exists
    And valid plant data for update
    When the user sends a PUT request to update plant with ID 1
    Then the plant API response status code is 403
    And the response contains an authorization error

  # TC_API_USER_008
  @TC_API_USER_008 @negative @authorization
  Scenario: User tries to delete plant returns 403
    Given a plant with ID 1 exists
    When the user sends a DELETE request to delete plant with ID 1
    Then the plant API response status code is 403
    And the response contains an authorization error
