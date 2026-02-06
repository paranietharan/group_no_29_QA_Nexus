@auth @api
Feature: Authentication API
  As a user or admin
  I want to authenticate using my credentials
  So that I can access the system

  Background:
    Given the authentication service is available

  # ==================== ADMIN LOGIN TEST CASES ====================

  @AUTH-API-ADMIN-01 @positive @admin @api
  Scenario: AUTH-API-ADMIN-01 Valid admin login returns token
    Given valid admin credentials from "testdata/auth_admin_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    And the token should be a valid JWT format

  @AUTH-API-ADMIN-02 @negative @admin @api
  Scenario: AUTH-API-ADMIN-02 Invalid admin password returns 401
    Given admin username with invalid password
    When the login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-ADMIN-03 @negative @admin @api
  Scenario: AUTH-API-ADMIN-03 Admin login with missing username
    Given empty username with valid admin password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-04 @negative @admin @api
  Scenario: AUTH-API-ADMIN-04 Admin login with missing password
    Given valid admin username with empty password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-05 @negative @admin @api
  Scenario: AUTH-API-ADMIN-05 Admin login with empty JSON
    When the login request is sent with empty json
    Then the response status code is 401

  @AUTH-API-ADMIN-06 @negative @admin @api
  Scenario: AUTH-API-ADMIN-06 Access protected endpoint without admin login
    When I access the categories endpoint without token
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-ADMIN-07 @positive @admin @api
  Scenario: AUTH-API-ADMIN-07 Admin can access read-only endpoint
    Given valid admin credentials from "testdata/auth_admin_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    When I access the categories endpoint with admin token
    Then the response status code is 200

  @AUTH-API-ADMIN-08 @edge-case @admin @api
  Scenario: AUTH-API-ADMIN-08 Verify SQL injection attempts in login fields
    Given SQL injection payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-ADMIN-09 @negative @admin @api
  Scenario: AUTH-API-ADMIN-09 Non-JWT format in Authorization header with admin endpoint
    When I access the categories endpoint with non-jwt token
    Then the response status code is 401
    And the response contains an error message

  # ==================== USER LOGIN TEST CASES ====================

  @AUTH-API-USER-01 @positive @user @api
  Scenario: AUTH-API-USER-01 Valid user login returns token
    Given valid user credentials from "testdata/auth_user_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    And the token should be a valid JWT format

  @AUTH-API-USER-02 @negative @user @api
  Scenario: AUTH-API-USER-02 Invalid user password returns 401
    Given user username with invalid password
    When the login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-USER-03 @negative @user @api
  Scenario: AUTH-API-USER-03 User login with missing username
    Given empty username with valid user password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-04 @negative @user @api
  Scenario: AUTH-API-USER-04 User login with missing password
    Given valid user username with empty password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-05 @negative @user @api
  Scenario: AUTH-API-USER-05 User login with empty JSON
    When the login request is sent with empty json
    Then the response status code is 401

  @AUTH-API-USER-06 @negative @user @api
  Scenario: AUTH-API-USER-06 Access user protected endpoint without login
    When I access the categories endpoint without token
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-USER-07 @negative @user @api
  Scenario: AUTH-API-USER-07 Access protected admin endpoint with user credentials
    Given valid user credentials from "testdata/auth_user_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    When I access the admin categories endpoint with user token
    Then the response status code is 403

  @AUTH-API-USER-08 @positive @user @api
  Scenario: AUTH-API-USER-08 User accesses read-only endpoint
    Given valid user credentials from "testdata/auth_user_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    When I access the categories endpoint with user token
    Then the response status code is 200

  @AUTH-API-USER-09 @negative @user @api
  Scenario: AUTH-API-USER-09 Non-JWT format in Authorization header with user endpoint
    When I access the categories endpoint with non-jwt token
    Then the response status code is 401
    And the response contains an error message

  # ==================== ADDITIONAL API SCENARIOS ====================

  @AUTH-API-ADMIN-01 @negative @admin @api
  Scenario: AUTH-API-ADMIN-01 Non-existent admin returns 401
    Given non-existent admin credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-02 @negative @admin @api
  Scenario: AUTH-API-ADMIN-02 Empty username and password returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-03 @edge-case @admin @api
  Scenario: AUTH-API-ADMIN-03 SQL injection in username is rejected
    Given SQL injection payload in username
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-ADMIN-04 @edge-case @admin @api
  Scenario: AUTH-API-ADMIN-04 SQL injection in password is rejected
    Given SQL injection payload in password
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-ADMIN-05 @edge-case @admin @api
  Scenario: AUTH-API-ADMIN-05 XSS injection attempt is rejected
    Given XSS payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain script tags

  @AUTH-API-USER-01 @negative @user @api
  Scenario: AUTH-API-USER-01 Non-existent user returns 401
    Given non-existent user credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-02 @negative @user @api
  Scenario: AUTH-API-USER-02 Empty username and password returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-03 @edge-case @user @api
  Scenario: AUTH-API-USER-03 SQL injection in username is rejected
    Given SQL injection payload in username
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-USER-04 @edge-case @user @api
  Scenario: AUTH-API-USER-04 SQL injection in password is rejected
    Given SQL injection payload in password
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-USER-05 @edge-case @user @api
  Scenario: AUTH-API-USER-05 XSS injection attempt is rejected
    Given XSS payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain script tags

