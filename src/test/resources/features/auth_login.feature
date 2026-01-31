@auth @smoke
Feature: Authentication API
  As a user or admin
  I want to authenticate using my credentials
  So that I can access the system

  Background:
    Given the authentication service is available

  # ==================== ADMIN LOGIN TEST CASES ====================

  @AUTH-API-ADMIN-01 @positive @admin
  Scenario: AUTH-API-ADMIN-01 Valid admin login returns token
    Given valid admin credentials from "testdata/auth_admin_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    And the token should be a valid JWT format

  @AUTH-API-ADMIN-02 @negative @admin
  Scenario: AUTH-API-ADMIN-02 Invalid admin password returns 401
    Given admin username with invalid password
    When the login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-ADMIN-03 @negative @admin
  Scenario: AUTH-API-ADMIN-03 Non-existent admin returns 401
    Given non-existent admin credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-04 @negative @admin
  Scenario: AUTH-API-ADMIN-04 Empty admin credentials returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-05 @edge-case @admin
  Scenario: AUTH-API-ADMIN-05 SQL injection attempt is rejected
    Given SQL injection payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details

  # ==================== USER LOGIN TEST CASES ====================

  @AUTH-API-USER-01 @positive @user
  Scenario: AUTH-API-USER-01 Valid user login returns token
    Given valid user credentials from "testdata/auth_user_login_valid.json"
    When the login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    And the token should be a valid JWT format

  @AUTH-API-USER-02 @negative @user
  Scenario: AUTH-API-USER-02 Invalid user password returns 401
    Given user username with invalid password
    When the login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-USER-03 @negative @user
  Scenario: AUTH-API-USER-03 Non-existent user returns 401
    Given non-existent user credentials
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-04 @negative @user
  Scenario: AUTH-API-USER-04 Empty user credentials returns 401
    Given empty username and password
    When the login request is sent
    Then the response status code is 401

  @AUTH-API-USER-05 @edge-case @user
  Scenario: AUTH-API-USER-05 User SQL injection attempt is rejected
    Given SQL injection payload in credentials
    When the login request is sent
    Then the response status code is 401
    And the response does not contain error details
