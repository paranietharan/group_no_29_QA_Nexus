@ui @login
Feature: UI Login Functionality
  As a user
  I want to log in using the login page
  So that I can access the application

  Background:
    Given the login page is displayed

  # ==================== UI ADMIN LOGIN TEST CASES ====================

  @UI-LOGIN-ADMIN-01 @positive @admin
  Scenario: UI-LOGIN-ADMIN-01 Valid admin login via UI
    When I enter admin username "admin"
    And I enter admin password "admin123"
    And I click the login button
    Then I should be redirected to the dashboard
    And the dashboard should be displayed

  @UI-LOGIN-ADMIN-02 @negative @admin
  Scenario: UI-LOGIN-ADMIN-02 Invalid admin password shows error
    When I enter admin username "admin"
    And I enter admin password "wrongpassword"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-03 @negative @admin
  Scenario: UI-LOGIN-ADMIN-03 Non-existent admin shows error
    When I enter admin username "fakeadmin"
    And I enter admin password "password123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-04 @negative @admin
  Scenario: UI-LOGIN-ADMIN-04 Empty username with valid password shows error
    When I leave username field empty
    And I enter admin password "admin123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-05 @negative @admin
  Scenario: UI-LOGIN-ADMIN-05 Valid username with empty password shows error
    When I enter admin username "admin"
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-06 @negative @admin
  Scenario: UI-LOGIN-ADMIN-06 Empty username and password shows error
    When I leave username field empty
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-07 @edge-case @admin
  Scenario: UI-LOGIN-ADMIN-07 SQL injection in username is handled
    When I enter admin username "admin' OR '1'='1"
    And I enter admin password "admin123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-08 @edge-case @admin
  Scenario: UI-LOGIN-ADMIN-08 SQL injection in password is handled
    When I enter admin username "admin"
    And I enter admin password "' OR '1'='1"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-ADMIN-09 @edge-case @admin
  Scenario: UI-LOGIN-ADMIN-09 XSS injection attempt is handled
    When I enter admin username "<script>alert('xss')</script>"
    And I enter admin password "<script>alert('xss')</script>"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  # ==================== UI USER LOGIN TEST CASES ====================

  @UI-LOGIN-USER-01 @positive @user
  Scenario: UI-LOGIN-USER-01 Valid user login via UI
    When I enter user username "testuser"
    And I enter user password "test123"
    And I click the login button
    Then I should be redirected to the dashboard
    And the dashboard should be displayed

  @UI-LOGIN-USER-02 @negative @user
  Scenario: UI-LOGIN-USER-02 Invalid user password shows error
    When I enter user username "testuser"
    And I enter user password "wrongpassword"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-03 @negative @user
  Scenario: UI-LOGIN-USER-03 Non-existent user shows error
    When I enter user username "fakeuser"
    And I enter user password "password123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-04 @negative @user
  Scenario: UI-LOGIN-USER-04 Empty username with valid password shows error
    When I leave username field empty
    And I enter user password "test123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-05 @negative @user
  Scenario: UI-LOGIN-USER-05 Valid username with empty password shows error
    When I enter user username "testuser"
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-06 @negative @user
  Scenario: UI-LOGIN-USER-06 Empty username and password shows error
    When I leave username field empty
    And I leave password field empty
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-07 @edge-case @user
  Scenario: UI-LOGIN-USER-07 SQL injection in username is handled
    When I enter user username "user' OR '1'='1"
    And I enter user password "test123"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-08 @edge-case @user
  Scenario: UI-LOGIN-USER-08 SQL injection in password is handled
    When I enter user username "testuser"
    And I enter user password "' OR '1'='1"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

  @UI-LOGIN-USER-09 @edge-case @user
  Scenario: UI-LOGIN-USER-09 XSS injection attempt is handled
    When I enter user username "<script>alert('xss')</script>"
    And I enter user password "<script>alert('xss')</script>"
    And I click the login button
    Then an error message should be displayed
    And I should remain on the login page

