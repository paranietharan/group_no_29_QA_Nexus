@mayuravel @api
Feature: Mayuravel API Test Cases - Categories, Plants, and Sales
  As a QA engineer
  I want to test the API endpoints for categories, plants, and sales
  So that I can verify the functionality for different user roles

  Background:
    Given the API service is available

  @TC-API-ADMIN-01 @positive @admin @pagination
  Scenario: TC-API-ADMIN-01 Admin fetches paginated categories successfully
    Given admin is authenticated
    When admin sends GET request to categories endpoint with page 0 and size 10
    Then the response status code is 200
    And the response contains paginated category list
    And the response contains pagination metadata

  @TC-API-ADMIN-02 @positive @admin @pagination
  Scenario: TC-API-ADMIN-02 Admin fetches paginated plants successfully
    Given admin is authenticated
    When admin sends GET request to plants endpoint with page 0 and size 10
    Then the response status code is 200
    And the response contains paginated plant list
    And the response contains pagination metadata


  @TC-API-ADMIN-04 @positive @admin @summary
  Scenario: TC-API-ADMIN-04 Admin fetches plant summary
    Given admin is authenticated
    When admin sends GET request to plant summary endpoint
    Then the response status code is 200
    And the response contains plant summary data
    And plant summary contains total count

  @TC-API-ADMIN-05 @positive @admin @summary
  Scenario: TC-API-ADMIN-05 Admin fetches category summary
    Given admin is authenticated
    When admin sends GET request to category summary endpoint
    Then the response status code is 200
    And the response contains category summary data
    And category summary contains total count

  @TC-API-ADMIN-06 @positive @admin @search
  Scenario: TC-API-ADMIN-06 Admin searches across modules with filters
    Given admin is authenticated
    When admin sends GET request with search param "rose" to categories endpoint
    Then the response status code is 200
    And the response contains filtered category results
    When admin sends GET request with search param "rose" to plants endpoint
    Then the response status code is 200
    And the response contains filtered plant results

  @TC-API-ADMIN-08 @negative @admin @sorting
  Scenario: TC-API-ADMIN-08 Admin sorts by invalid field
    Given admin is authenticated
    When admin sends GET request to plants endpoint with sort "invalidField"
    Then the response status code should be 400 or 200
    And if status is 200 then default sorting is applied

  @TC-API-ADMIN-11 @positive @admin @categories
  Scenario: TC-API-ADMIN-11 Admin fetches main categories
    Given admin is authenticated
    When admin sends GET request to main categories endpoint
    Then the response status code is 200
    And the response contains list of main categories

  @TC-API-ADMIN-12 @positive @admin @filter
  Scenario: TC-API-ADMIN-12 Admin filters plants by low stock
    Given admin is authenticated
    When admin sends GET request to plants endpoint with lowStock filter true
    Then the response status code is 200
    And the response contains only low stock plants

  @TC-API-ADMIN-15 @positive @admin @consistency
  Scenario: TC-API-ADMIN-15 Admin cross-module consistency
    Given admin is authenticated
    And a category with plants exists
    When admin deletes the category via API
    And admin fetches plants under that category
    Then the plants show error or null category
    And data consistency is maintained

  @TC-API-USER-01 @positive @user @pagination
  Scenario: TC-API-USER-01 User fetches paginated categories successfully
    Given user is authenticated
    When user sends GET request to categories endpoint with page 0 and size 10
    Then the response status code is 200
    And the response contains paginated category list
    And the response contains pagination metadata

  @TC-API-USER-02 @positive @user @pagination
  Scenario: TC-API-USER-02 User fetches paginated plants successfully
    Given user is authenticated
    When user sends GET request to plants endpoint with page 0 and size 10
    Then the response status code is 200
    And the response contains paginated plant list
    And the response contains pagination metadata

  @TC-API-USER-03 @positive @user @pagination
  Scenario: TC-API-USER-03 User fetches paginated sales successfully
    Given user is authenticated
    When user sends GET request to sales endpoint with page 0 and size 10
    Then the response status code is 200
    And the response contains paginated sales list
    And the response contains pagination metadata

  @TC-API-USER-04 @positive @user @summary
  Scenario: TC-API-USER-04 User fetches plant summary
    Given user is authenticated
    When user sends GET request to plant summary endpoint
    Then the response status code is 200
    And the response contains plant summary data

  @TC-API-USER-05 @positive @user @summary
  Scenario: TC-API-USER-05 User fetches category summary
    Given user is authenticated
    When user sends GET request to category summary endpoint
    Then the response status code is 200
    And the response contains category summary data

  @TC-API-USER-06 @positive @user @search
  Scenario: TC-API-USER-06 User searches across modules
    Given user is authenticated
    When user sends GET request with search param "rose" to categories endpoint
    Then the response status code is 200
    And the response contains filtered results
    When user sends GET request with search param "rose" to plants endpoint
    Then the response status code is 200
    And the response contains filtered results

  @TC-API-USER-07 @positive @user @pagination
  Scenario: TC-API-USER-07 User uses pagination
    Given user is authenticated
    When user sends GET request to categories endpoint with page 1 and size 5
    Then the response status code is 200
    And the correct page is returned with 5 or fewer items

  @TC-API-USER-08 @positive @user @sorting
  Scenario: TC-API-USER-08 User sorts data
    Given user is authenticated
    When user sends GET request to plants endpoint with sort "name,asc"
    Then the response status code is 200
    And the results are sorted correctly by name ascending