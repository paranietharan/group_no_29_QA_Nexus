@auth @ui
Feature: UI Login Functionality
  As a user
  I want to log in using the login page
  So that I can access the application

  Background:
    Given the login page is displayed

  # ==================== UI ADMIN LOGIN TEST CASES ====================

  @AUTH-UI-ADMIN-01 @positive @admin @ui
  Scenario: AUTH-UI-ADMIN-01 Admin successful login
    When I enter admin username "admin"
    And I enter admin password "admin123"
    And I click the login button
    Then I should be redirected to the dashboard
    And the dashboard should be displayed

  @AUTH-UI-ADMIN-02 @negative @admin @ui
  Scenario: AUTH-UI-ADMIN-02 Verify username validation works for Admin
    When I leave username field empty
    And I enter admin password "admin123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-03 @negative @admin @ui
  Scenario: AUTH-UI-ADMIN-03 Verify password validation works for Admin
    When I enter admin username "admin"
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-04 @negative @admin @ui
  Scenario: AUTH-UI-ADMIN-04 Login with incorrect admin credentials
    When I enter admin username "admin"
    And I enter admin password "wrongpassword"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-05 @positive @admin @ui
  Scenario: AUTH-UI-ADMIN-05 Admin logout works
    Given admin is logged in
    When I click the logout button
    Then I should be redirected to the login page

  @AUTH-UI-ADMIN-06 @positive @admin @ui
  Scenario: AUTH-UI-ADMIN-06 Admin logout correctly works
    Given admin is logged out
    When I navigate to the dashboard page
    Then I should be redirected to the login page

  # ==================== UI USER LOGIN TEST CASES ====================

  @AUTH-UI-USER-01 @positive @user @ui
  Scenario: AUTH-UI-USER-01 User successful login
    When I enter user username "testuser"
    And I enter user password "test123"
    And I click the login button
    Then I should be redirected to the dashboard
    And the dashboard should be displayed

  @AUTH-UI-USER-02 @negative @user @ui
  Scenario: AUTH-UI-USER-02 Verify username validation works for user
    When I leave username field empty
    And I enter user password "test123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-03 @negative @user @ui
  Scenario: AUTH-UI-USER-03 Verify password validation works for User
    When I enter user username "testuser"
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-04 @negative @user @ui
  Scenario: AUTH-UI-USER-04 Verify form validation works for user
    When I leave username field empty
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-05 @positive @user @ui
  Scenario: AUTH-UI-USER-05 User logout works
    Given user is logged in
    When I click the logout button
    Then I should be redirected to the login page

  @AUTH-UI-USER-06 @positive @user @ui
  Scenario: AUTH-UI-USER-06 User logout correctly works
    Given user is logged out
    When I navigate to the dashboard page
    Then I should be redirected to the login page

  @AUTH-UI-USER-07 @positive @user @ui
  Scenario: AUTH-UI-USER-07 Password field shows masked characters
    When I type the password "test123"
    Then the password field should be masked

  @AUTH-UI-USER-08 @negative @user @ui
  Scenario: AUTH-UI-USER-08 Access protected page without login
    When I navigate to the dashboard page
    Then I should be redirected to the login page

  # ==================== ADDITIONAL UI SCENARIOS ====================

  @AUTH-UI-ADMIN-09 @edge-case @admin @ui
  Scenario: AUTH-UI-ADMIN-09 SQL injection in username is handled
    When I enter admin username "admin' OR '1'='1"
    And I enter admin password "admin123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-10 @edge-case @admin @ui
  Scenario: AUTH-UI-ADMIN-10 SQL injection in password is handled
    When I enter admin username "admin"
    And I enter admin password "' OR '1'='1"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-11 @edge-case @admin @ui
  Scenario: AUTH-UI-ADMIN-11 XSS injection attempt is handled
    When I enter admin username "<script>alert('xss')</script>"
    And I enter admin password "<script>alert('xss')</script>"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-ADMIN-12 @negative @admin @ui
  Scenario: AUTH-UI-ADMIN-12 Username case sensitivity for admin
    When I enter admin username "ADMIN"
    And I enter admin password "admin123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-09 @edge-case @user @ui
  Scenario: AUTH-UI-USER-09 SQL injection in username is handled
    When I enter user username "user' OR '1'='1"
    And I enter user password "test123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-10 @edge-case @user @ui
  Scenario: AUTH-UI-USER-10 SQL injection in password is handled
    When I enter user username "testuser"
    And I enter user password "' OR '1'='1"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-11 @edge-case @user @ui
  Scenario: AUTH-UI-USER-11 XSS injection attempt is handled
    When I enter user username "<script>alert('xss')</script>"
    And I enter user password "<script>alert('xss')</script>"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @AUTH-UI-USER-12 @negative @user @ui
  Scenario: AUTH-UI-USER-12 Username case sensitivity for user
    When I enter user username "TESTUSER"
    And I enter user password "test123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

