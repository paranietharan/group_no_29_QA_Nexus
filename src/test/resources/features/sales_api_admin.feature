@api @sales @admin
Feature: Sales API - Admin Role

Background:
  Given the admin is authenticated

@TC_API_SALES_ADMIN_01
Scenario: Delete an existing sale
  Given a valid sale exists
  When the admin sends a DELETE request to delete the sale
  Then the sales API response status code is 204
  And the sale is deleted successfully

@TC_API_SALES_ADMIN_02
Scenario: Fetch all sales
  When the admin sends a GET request to fetch all sales
  Then the sales API response status code is 200
  And the response contains a list of sales

@TC_API_SALES_ADMIN_03
Scenario: Fetch sale by ID
  Given a valid sale exists
  When the admin sends a GET request to fetch sale by ID
  Then the sales API response status code is 200
  And the response contains sale details

@TC_API_SALES_ADMIN_04
Scenario: Delete non-existent sale
  When the admin sends a DELETE request with invalid sale ID 999999
  Then the sales API response status code is 404
  And the response contains not found error

@TC_API_SALES_ADMIN_05
Scenario: Paginate sales
  When the admin sends a GET request to paginate sales with page 0 and size 5
  Then the sales API response status code is 200
  And the response contains paginated sales data

@TC_API_SALES_ADMIN_06
Scenario: Sort sales
  When the admin sends a GET request to sort sales by "soldAt,desc"
  Then the sales API response status code is 200
