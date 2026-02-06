@api @sales @user
Feature: Sales API - User Role

Background:
  Given the user is authenticated

@TC_API_SALES_USER_01
Scenario: Fetch all sales
  When the user sends a GET request to fetch all sales
  Then the sales API response status code is 200
  And the response contains a list of sales

@TC_API_SALES_USER_02
Scenario: Fetch sale by ID
  Given a valid sale exists
  When the user sends a GET request to fetch sale by ID
  Then the sales API response status code is 200
  And the response contains sale details


@TC_API_SALES_USER_03
Scenario: Paginate sales
  When the user sends a GET request to paginate sales with page 0 and size 5
  Then the sales API response status code is 200
  And the response contains paginated sales data

@TC_API_SALES_USER_04
Scenario: Sort sales
  When the user sends a GET request to sort sales by "soldAt,desc"
  Then the sales API response status code is 200

@TC_API_SALES_USER_05
Scenario: Block sale creation
  When the user sends a POST request to create a sale
  Then the sales API response status code is 403
  And the sale is not created

@TC_API_SALES_USER_06
Scenario: Block sale deletion
  When the user sends a DELETE request to delete sale ID 1
  Then the sales API response status code is 403
  And the sale is not deleted

@TC_API_SALES_USER_07
Scenario: Fetch non-existent sale
  When the user sends a GET request with invalid sale ID 9999
  Then the sales API response status code is 404
  And the response contains not found error

@TC_API_SALES_USER_08
Scenario: Filter sales by plant name
  When the user sends a GET request to filter sales by plant name "Rose"
  Then the sales API response status code is 200
  And the response contains only filtered plant sales

