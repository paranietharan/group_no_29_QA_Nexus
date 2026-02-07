@ui @plant @user
Feature: Plant UI - User Role
  As a user with read-only access
  I want to view and browse plants through the UI
  But I should not be able to add, edit or delete plants

  Background:
    Given the user is logged in via UI

  # TC_UI_USER_001
  @TC_UI_USER_001 @positive
  Scenario: User sees Plant List
    When the user navigates to the plants page
    Then the plant table is visible
    And no edit or delete buttons are displayed

  # TC_UI_USER_002
  @TC_UI_USER_002 @positive
  Scenario: User does not see Add Plant button
    When the user navigates to the plants page
    Then no Add Plant button is visible anywhere on the page

  # TC_UI_USER_003
  @TC_UI_USER_003 @positive @search
  Scenario: User can search plants
    Given plants exist in the system for UI testing
    When the user navigates to the plants page
    And the user searches for "Orchid"
    Then matching plants are displayed in the results

  # TC_UI_USER_004
  @TC_UI_USER_004 @positive @filter
  Scenario: User can filter by category
    Given multiple categories with plants exist
    When the user navigates to the plants page
    And the user filters by category "Succulents"
    Then only plants from the selected category are displayed

  # TC_UI_USER_005
  @TC_UI_USER_005 @positive @sort
  Scenario: User can sort plants
    Given multiple plants exist
    When the user navigates to the plants page
    And the user clicks the Name column header
    Then the plants are sorted alphabetically
    And sort indicators are visible

  # TC_UI_USER_006
  @TC_UI_USER_006 @negative @readonly
  Scenario: User cannot edit or delete plants
    When the user navigates to the plants page
    And the user hovers over plant rows
    Then no edit icons are visible
    And no delete icons are visible

  # TC_UI_USER_007
  @TC_UI_USER_007 @positive @detail
  Scenario: User can view plant details
    Given a plant named "Cactus" exists
    When the user navigates to the plants page
    And the user clicks on the plant named "Cactus"
    Then the plant details page is displayed
    And all plant information is visible
    And no edit form is displayed

  # TC_UI_USER_008
  @TC_UI_USER_008 @positive @responsive
  Scenario: User UI is responsive
    When the user navigates to the plants page
    Then the layout adapts to the screen size
    And no horizontal scrolling is required
