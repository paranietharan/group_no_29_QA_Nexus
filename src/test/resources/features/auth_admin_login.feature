@auth @smoke @critical
Feature: Admin Authentication
  As an admin user
  I want to authenticate using my credentials
  So that I can access the admin dashboard

  Background:
    Given the authentication service is available

  @AUTH-API-ADMIN-01 @positive
  Scenario: AUTH-API-ADMIN-01 Valid admin login returns token
    Given valid admin credentials from "testdata/auth_admin_login_valid.json"
    When the admin login request is sent
    Then the response status code is 200
    And the response contains a non-empty token
    And the token should be a valid JWT format

  @AUTH-API-ADMIN-02 @negative
  Scenario: AUTH-API-ADMIN-02 Invalid password returns 401
    Given valid username with invalid password
    When the admin login request is sent
    Then the response status code is 401
    And the response contains an error message

  @AUTH-API-ADMIN-03 @negative
  Scenario: AUTH-API-ADMIN-03 Non-existent user returns 401
    Given non-existent user credentials
    When the admin login request is sent
    Then the response status code is 401

  @AUTH-API-ADMIN-04 @negative
  Scenario: AUTH-API-ADMIN-04 Empty credentials returns 400
    Given empty username and password
    When the admin login request is sent
    Then the response status code is 400

  @AUTH-API-ADMIN-05 @edge-case
  Scenario: AUTH-API-ADMIN-05 SQL injection attempt is rejected
    Given SQL injection payload in credentials
    When the admin login request is sent
    Then the response status code is 400
    And the response does not contain error details

