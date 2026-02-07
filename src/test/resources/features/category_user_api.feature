@category @api @user
Feature: Category API Management - User
  As a user
  I want to access categories via API
  So that I can view flower inventory

  Background:
    Given the API service is available
    And user is authenticated with valid credentials

  @TC_API_CAT_USER_01 @positive
  Scenario: User fetches all categories
    When user sends GET request to "/api/categories"
    Then the response status code is 200
    And the response contains list of categories

  @TC_API_CAT_USER_02 @positive
  Scenario: User fetches category by ID
    When user sends GET request to "/api/categories"
    And user extracts first category ID
    And user sends GET request to fetch category by id
    Then the response status code is 200
    And the response contains category details

  @TC_API_CAT_USER_03 @positive
  Scenario: User searches categories by name
    When user sends GET request with search parameter "Rose"
    Then the response status code is 200
    And the response contains matching categories

  @TC_API_CAT_USER_04 @positive
  Scenario: User fetches sub-categories
    When user sends GET request for main categories only
    And user extracts first parent category ID
    And user sends GET request with parent filter
    Then the response status code is 200

  @TC_API_CAT_USER_05 @positive
  Scenario: User fetches main categories
    When user sends GET request for main categories only
    Then the response status code is 200
    And the response contains only main categories

  @TC_API_CAT_USER_06 @negative
  Scenario: User tries to create category
    When user sends POST request to create category "Roses"
    Then the response status code is 403
    And the response contains permission error message

  @TC_API_CAT_USER_07 @negative
  Scenario: User tries to update category
    When user sends GET request to "/api/categories"
    And user extracts first category ID
    And user sends PUT request to update category name
    Then the response status code is 403
    And the response contains permission error message

  @TC_API_CAT_USER_08 @negative
  Scenario: User tries to delete category
    When user sends GET request to "/api/categories"
    And user extracts first category ID
    And user sends DELETE request to remove the category
    Then the response status code is 403
    And the response contains permission error message

  @TC_API_CAT_USER_09 @negative
  Scenario: User fetches non-existent category
    When user sends GET request to fetch category with invalid id 999999
    Then the response status code is 404
    And the response contains error message

  @TC_API_CAT_USER_10 @positive
  Scenario: User paginates categories
    When user sends GET request with page=0, size=10
    Then the response status code is 200
    And the response contains paginated categories
