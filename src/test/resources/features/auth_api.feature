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
  Scenario: AUTH-API-ADMIN-03 Non-existent admin returns 401
    Given non-existent admin credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-04 @negative @admin @api
  Scenario: AUTH-API-ADMIN-04 Empty username with valid password returns 401
    Given empty username with valid admin password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-05 @negative @admin @api
  Scenario: AUTH-API-ADMIN-05 Valid username with empty password returns 401
    Given valid admin username with empty password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-06 @negative @admin @api
  Scenario: AUTH-API-ADMIN-06 Empty username and password returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-07 @edge-case @admin
  Scenario: AUTH-API-ADMIN-07 SQL injection in username is rejected
    Given SQL injection payload in username
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-ADMIN-08 @edge-case @admin
  Scenario: AUTH-API-ADMIN-08 SQL injection in password is rejected
    Given SQL injection payload in password
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-ADMIN-09 @edge-case @admin
  Scenario: AUTH-API-ADMIN-09 XSS injection attempt is rejected
    Given XSS payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain script tags

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
  Scenario: AUTH-API-USER-03 Non-existent user returns 401
    Given non-existent user credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-04 @negative @user @api
  Scenario: AUTH-API-USER-04 Empty username with valid password returns 401
    Given empty username with valid user password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-05 @negative @user @api
  Scenario: AUTH-API-USER-05 Valid username with empty password returns 401
    Given valid user username with empty password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-06 @negative @user @api
  Scenario: AUTH-API-USER-06 Empty username and password returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-07 @edge-case @user @api
  Scenario: AUTH-API-USER-07 SQL injection in username is rejected
    Given SQL injection payload in username
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-USER-08 @edge-case @user @api
  Scenario: AUTH-API-USER-08 SQL injection in password is rejected
    Given SQL injection payload in password
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  @AUTH-API-USER-09 @edge-case @user @api
  Scenario: AUTH-API-USER-09 XSS injection attempt is rejected
    Given XSS payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain script tags

