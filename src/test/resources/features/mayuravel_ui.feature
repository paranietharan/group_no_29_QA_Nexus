@mayuravel @ui
Feature: Mayuravel UI Test Cases - Dashboard and Navigation
  As a QA engineer
  I want to test the UI functionality for admin and user roles
  So that I can verify the dashboard, navigation, and user interface

  @TC-UI-ADMIN-01 @positive @admin @ui @dashboard
  Scenario: TC-UI-ADMIN-01 Admin dashboard loads after login
    Given admin navigates to login page
    When admin logs in with valid credentials
    Then the dashboard loads within 3 seconds
    And admin sees the dashboard page

  @TC-UI-ADMIN-06 @positive @admin @ui @dashboard
  Scenario: TC-UI-ADMIN-06 Admin sees real-time updates in summaries
    Given admin is logged in and on dashboard
    And admin notes the current plant count in summary
    When admin navigates to plants page
    And admin adds a new plant
    And admin returns to dashboard
    Then the plant summary card shows updated count

  @TC-UI-ADMIN-07 @positive @admin @ui @pagination
  Scenario: TC-UI-ADMIN-07 Admin pagination works in all lists
    Given admin is logged in
    When admin navigates to categories list page
    And admin clicks next page button
    Then the next page loads correctly
    When admin navigates to plants list page
    And admin clicks next page button
    Then the next page loads correctly

  @TC-UI-ADMIN-08 @positive @admin @ui @responsive
  Scenario: TC-UI-ADMIN-08 Admin can resize browser without UI break
    Given admin is logged in and on dashboard
    When admin resizes browser window to 1024x768
    Then the UI adapts correctly
    And there are no overlapping elements
    When admin resizes browser window to 1920x1080
    Then the UI adapts correctly
    And there are no overlapping elements

  @TC-UI-ADMIN-09 @positive @admin @ui @loading
  Scenario: TC-UI-ADMIN-09 Admin sees loading indicators for API calls
    Given admin is logged in
    When admin navigates to plants page
    Then a loading spinner or indicator appears during data fetch
    And the loading indicator disappears when data loads

  @TC-UI-ADMIN-10 @positive @admin @ui @keyboard
  Scenario: TC-UI-ADMIN-10 Admin can use keyboard shortcuts (if any)
    Given admin is logged in on a page with keyboard shortcuts
    When admin uses a keyboard shortcut
    Then the expected action is performed

  @TC-UI-ADMIN-11 @positive @admin @ui @theme
  Scenario: TC-UI-ADMIN-11 Admin sees consistent theme/colors
    Given admin is logged in
    When admin navigates to categories page
    Then the theme colors and fonts are consistent
    When admin navigates to plants page
    Then the theme colors and fonts are consistent
    When admin navigates to sales page
    Then the theme colors and fonts are consistent

  @TC-UI-ADMIN-13 @positive @admin @ui @breadcrumbs
  Scenario: TC-UI-ADMIN-13 Admin sees breadcrumbs for navigation
    Given admin is logged in
    When admin navigates deep into a module
    Then breadcrumbs show the navigation path
    And breadcrumbs are clickable

  @TC-UI-ADMIN-14 @positive @admin @ui @error-handling
  Scenario: TC-UI-ADMIN-14 Admin UI handles API errors gracefully
    Given admin is logged in
    When an API error occurs
    Then a user-friendly error message is shown
    And the error message is clear and helpful

  @TC-UI-ADMIN-15 @positive @admin @ui @responsive
  Scenario: TC-UI-ADMIN-15 Admin dashboard is responsive
    Given admin is logged in
    When admin views dashboard on mobile device
    Then the UI adjusts correctly for mobile
    When admin views dashboard on tablet device
    Then the UI adjusts correctly for tablet

  @TC-UI-USER-01 @positive @user @ui @dashboard
  Scenario: TC-UI-USER-01 User dashboard loads after login
    Given user navigates to login page
    When user logs in with valid credentials
    Then the dashboard loads within 3 seconds
    And user sees the dashboard page

  @TC-UI-USER-02 @positive @user @ui @dashboard
  Scenario: TC-UI-USER-02 User sees summary cards (read-only)
    Given user is logged in and on dashboard
    Then user sees Categories summary card
    And user sees Plants summary card
    And user sees Sales summary card
    And all cards are displayed but not clickable for editing

  @TC-UI-USER-03 @negative @user @ui @authorization
  Scenario: TC-UI-USER-03 User cannot click to edit from dashboard
    Given user is logged in and on dashboard
    When user tries to interact with summary cards
    Then there are no clickable edit or delete buttons
    And user has read-only access

  @TC-UI-USER-04 @positive @user @ui @navigation
  Scenario: TC-UI-USER-04 User navigation menu is limited
    Given user is logged in
    When user views the navigation menu
    Then only allowed modules are visible
    And admin-only menu items are not displayed

  @TC-UI-USER-05 @positive @user @ui @navigation
  Scenario: TC-UI-USER-05 User can navigate to all modules (read-only)
    Given user is logged in
    When user navigates to categories page
    Then the page loads but shows no edit options
    When user navigates to plants page
    Then the page loads but shows no edit options
    When user navigates to sales page
    Then the page loads but shows no edit options

  @TC-UI-USER-06 @positive @user @ui @pagination
  Scenario: TC-UI-USER-06 User pagination works
    Given user is logged in
    When user navigates to categories list page
    And user clicks pagination controls
    Then the page loads correctly with paginated data

  @TC-UI-USER-07 @positive @user @ui @loading
  Scenario: TC-UI-USER-07 User sees loading states
    Given user is logged in
    When user performs a data fetch operation
    Then a loading spinner or indicator appears
    And the indicator disappears when data loads

  @TC-UI-USER-08 @positive @user @ui @responsive
  Scenario: TC-UI-USER-08 User UI is responsive
    Given user is logged in
    When user views the page on mobile device
    Then the UI adapts correctly for mobile viewport

  @TC-UI-USER-09 @negative @user @ui @authorization
  Scenario: TC-UI-USER-09 User cannot see admin-only UI elements
    Given user is logged in
    When user views any page
    Then no add, edit, or delete buttons are visible
    And admin-only features are hidden

  @TC-UI-USER-10 @positive @user @ui @theme
  Scenario: TC-UI-USER-10 User sees consistent styling
    Given user is logged in
    When user navigates across different pages
    Then the theme and layout are consistent
    And all pages follow the same design pattern
