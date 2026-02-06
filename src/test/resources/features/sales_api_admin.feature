@api @sales @admin
Feature: Sales API - Admin Role

  TC_API_SALES_ADMIN_01 Delete sale
  Scenario: Admin deletes an existing sale
    Given the admin is authenticated
    And an existing sale record is available
    When the admin sends a DELETE request to delete the sale
    Then the sales API response status code should be 204
    And the sale should be deleted successfully

  TC_API_SALES_ADMIN_02 Fetch all sales
  Scenario: Admin fetches all sales
    Given the admin is authenticated
    When the admin sends a GET request to fetch all sales
    Then the sales API response status code should be 200
    And a sales list should be returned

  TC_API_SALES_ADMIN_03 Fetch sale by ID
  Scenario: Admin fetches a sale by ID
    Given the admin is authenticated
    And an existing sale record is available
    When the admin sends a GET request to fetch sale by ID
    Then the sales API response status code should be 200
    And sale details should be returned

  TC_API_SALES_ADMIN_04 Delete non-existent sale
  Scenario: Admin deletes a non-existent sale
    Given the admin is authenticated
    When the admin sends a DELETE request with non-existent sale ID 999999
    Then the sales API response status code should be 404

  TC_API_SALES_ADMIN_05 Paginate sales
  Scenario: Admin fetches paginated sales
    Given the admin is authenticated
    When the admin requests paginated sales sorted by soldAt desc
    Then the sales API response status code should be 200
    And paginated sales data should be returned

  TC_API_SALES_ADMIN_06 Sort sales
  Scenario: Admin sorts sales by soldAt
    Given the admin is authenticated
    When the admin requests sales sorted by soldAt desc
    Then the sales API response status code should be 200
    And sales should be sorted by soldAt in descending order
