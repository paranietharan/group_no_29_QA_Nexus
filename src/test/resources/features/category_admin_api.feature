@category @api @admin
Feature: Category API Management - Admin
  As an admin
  I want to manage categories via API
  So that I can organize flower inventory

  Background:
    Given the API service is available
    And admin is authenticated with valid credentials

  @TC_API_CAT_ADMIN_01 @positive
  Scenario: Admin creates main category
    When admin sends POST request to "/api/categories" with main category "Dahlia" and no parent
    Then the response status code is 201
    And the response contains category with name "Dahlia"
    And the response contains a non-null category id

  @TC_API_CAT_ADMIN_02 @positive
  Scenario: Admin creates sub-category
    #And a main category "Dahlia" exist
    When admin sends POST request to "/api/categories" with sub-category "RedRo" under parent
    Then the response status code is 201
    And the response contains category with name "RedRo"
    And the response contains a non-null category id

  @TC_API_CAT_ADMIN_03 @positive
  Scenario: Admin updates category name
    And a category "Dahlia" exists
    When admin sends PUT request to update category name to "DahliaNew"
    Then the response status code is 200
    And the response contains updated name "DahliaNew"

  @TC_API_CAT_ADMIN_04 @positive
  Scenario: Admin deletes category
    And a category "Dahlia76" exists with no sub-categories
    When admin sends DELETE request to remove the category
    Then the response status code is 204

  @TC_API_CAT_ADMIN_05 @positive
  Scenario: Admin fetches category by ID
    And a category "Sunflow" exists
    When admin sends GET request to fetch category by id
    Then the response status code is 200
    And the response contains category details

  @TC_API_CAT_ADMIN_06 @negative
  Scenario: Admin creates duplicate category
    #And a main category "Orchid" exists
    When admin attempts to create duplicate category "Orchid" with same parent
    Then the response status code is 400
    And the response contains error message about duplicate

  @TC_API_CAT_ADMIN_07 @negative
  Scenario: Admin creates category with short name
    When admin attempts to create category with name "AB"
    Then the response status code is 400
    And the category response contains validation error for name length

  @TC_API_CAT_ADMIN_08 @negative
  Scenario: Admin creates category with long name
    When admin attempts to create category with name "VeryLongFlowerNameTooLong"
    Then the response status code is 400
    And the category response contains validation error for name length

  @TC_API_CAT_ADMIN_09 @negative
  Scenario: Admin deletes category with sub-categories
    And a main category "Child1" exists with sub-categories
    When admin attempts to delete the main category
    Then the response status code is 204

  @TC_API_CAT_ADMIN_10 @negative
  Scenario: Admin updates category with invalid parent
    And a category "Peony" exists
    When admin attempts to update category with invalid parent id 999999
    Then the response status code is 404
    And the response contains error message about invalid parent

  @TC_API_CAT_ADMIN_11 @negative
  Scenario: Admin fetches non-existent category
    When admin sends GET request to fetch category with invalid id 999999
    Then the response status code is 404
    And the response contains error message

  @TC_API_CAT_ADMIN_12 @positive
  Scenario: Admin searches categories by name
    #And categories "Rose", "Roses", "Red" exist
    When admin sends GET request with search parameter "Rose"
    Then the response status code is 200
    And the response contains categories matching "Rose"

  @TC_API_CAT_ADMIN_13 @positive
  Scenario: Admin filters categories by parent
    And a main category with sub-categories exists
    When admin sends GET request with parent filter
    Then the response status code is 200
    And the response contains only sub-categories of that parent

  @TC_API_CAT_ADMIN_14 @positive
  Scenario: Admin paginates categories with sorting
    And multiple categories exist
    When admin sends GET request with page=0, size=5, sortBy="name"
    Then the response status code is 200
    And the response contains paginated categories sorted by name
