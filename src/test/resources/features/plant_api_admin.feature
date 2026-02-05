@api @plant @admin
Feature: Plant API - Admin Role
  As an admin with full access
  I want to manage plants through the API
  So that I can create, read, update and delete plant records

  Background:
    Given the admin is authenticated

  # TC_API_ADMIN_001
  @TC_API_ADMIN_001 @positive @create
  Scenario: Admin creates plant under valid sub-category returns 201
    Given a valid sub-category exists
    And valid plant data with name "Test Plant" price 150.00 and quantity 25
    When the admin sends a POST request to create a plant
    Then the plant API response status code is 201
    And the response contains the created plant with ID

  # TC_API_ADMIN_002
  @TC_API_ADMIN_002 @positive @update
  Scenario: Admin updates plant details returns 200
    Given an existing plant to update
    And updated plant data with price 200.00
    When the admin sends a PUT request to update the plant
    Then the plant API response status code is 200
    And the response shows the updated plant data

  # TC_API_ADMIN_003
  @TC_API_ADMIN_003 @positive @delete
  Scenario: Admin deletes plant returns 204
    Given an existing plant to delete
    When the admin sends a DELETE request to delete the plant
    Then the plant API response status code is 204
    And the plant no longer exists

  # TC_API_ADMIN_004
  @TC_API_ADMIN_004 @positive @read
  Scenario: Admin fetches plant by ID returns 200
    Given a plant with ID exists
    When the admin sends a GET request to fetch the plant by ID
    Then the plant API response status code is 200
    And the response contains plant details with all fields

  # TC_API_ADMIN_005
  @TC_API_ADMIN_005 @negative @validation
  Scenario: Admin creates plant with negative quantity returns 400
    Given a valid sub-category exists
    And plant data with negative quantity -5
    When the admin sends a POST request to create a plant
    Then the plant API response status code is 400
    And the response contains validation error for quantity

  # TC_API_ADMIN_006
  @TC_API_ADMIN_006 @negative @validation
  Scenario: Admin creates plant with name less than 3 characters returns 400
    Given a valid sub-category exists
    And plant data with name "AB" which is too short
    When the admin sends a POST request to create a plant
    Then the plant API response status code is 400
    And the response contains validation error for name length

  # TC_API_ADMIN_007
  @TC_API_ADMIN_007 @negative @validation
  Scenario: Admin creates plant with price zero or negative returns 400
    Given a valid sub-category exists
    And plant data with price 0
    When the admin sends a POST request to create a plant
    Then the plant API response status code is 400
    And the response contains validation error for price

  # TC_API_ADMIN_008
  @TC_API_ADMIN_008 @negative @validation
  Scenario: Admin updates plant with invalid categoryId - category unchanged
    Given an existing plant to update
    And update data with non-existent category ID 999
    When the admin sends a PUT request to update the plant with invalid category
    Then the plant API response status code is 200
    And the response shows the original category is unchanged

  # TC_API_ADMIN_009
  @TC_API_ADMIN_009 @negative @notfound
  Scenario: Admin fetches non-existent plant returns 404
    When the admin sends a GET request to fetch plant with ID 99999
    Then the plant API response status code is 404
    And the response contains plant not found error

  # TC_API_ADMIN_010
  @TC_API_ADMIN_010 @positive @filter
  Scenario: Admin fetches plants by category returns 200
    Given a category with plants exists
    When the admin sends a GET request to fetch plants by the category
    Then the plant API response status code is 200
    And the response contains only plants from that category

  # TC_API_ADMIN_011
  @TC_API_ADMIN_011 @positive @search
  Scenario: Admin searches plants by name returns 200
    Given a plant named "Tulip" exists in the system
    When the admin sends a GET request to search plants with name "Tulip"
    Then the plant API response status code is 200
    And the response contains plants with Tulip in name

  # TC_API_ADMIN_012
  @TC_API_ADMIN_012 @positive @pagination
  Scenario: Admin paginates plants with filters returns 200
    Given multiple plants exist in a category
    When the admin sends a GET request with page 0 size 10 and category filter
    Then the plant API response status code is 200
    And the response contains paginated plant data

  # TC_API_ADMIN_013
  @TC_API_ADMIN_013 @positive @summary
  Scenario: Admin fetches plant summary returns 200
    Given plants exist in the system for API testing
    When the admin sends a GET request to fetch plant summary
    Then the plant API response status code is 200
    And the response contains total plants count
    And the response contains low stock plants count
