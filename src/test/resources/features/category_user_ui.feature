@category @ui @user
Feature: Category UI Management - User
  As a user
  I want to view categories via UI
  So that I can browse flower inventory visually

  Background:
    Given user is logged in via UI

  @TC_UI_CAT_USER_01 @positive
  Scenario: User sees Category List (read-only)
    When user navigates to "/ui/categories"
    Then the category list page is displayed
    And edit/delete buttons are not visible

  @TC_UI_CAT_USER_02 @positive
  Scenario: User does not see "Add Category" button
    When user navigates to "/ui/categories"
    Then the "Add Category" button is not visible

  @TC_UI_CAT_USER_03 @positive
  Scenario: User can search categories
    When user navigates to "/ui/categories"
    And user enters "Rose" in the search box
    And user presses Enter
    Then the category list filters to show matching categories

  @TC_UI_CAT_USER_04 @positive
  Scenario: User can filter categories by parent
    And a main category with sub-categories exists
    When user navigates to "/ui/categories"
    And user selects parent filter
    Then the list shows only sub-categories of that parent

  @TC_UI_CAT_USER_05 @positive
  Scenario: User can sort categories
    When user navigates to "/ui/categories"
    And user clicks column header to sort
    Then the list sorts correctly
    And sort order toggles on second click

  @TC_UI_CAT_USER_06 @positive
  Scenario: User cannot click edit/delete buttons
    When user navigates to "/ui/categories"
    Then edit/delete icons are not visible

  @TC_UI_CAT_USER_07 @positive
  Scenario: User sees "No category found" for empty search
    When user navigates to "/ui/categories"
    And user searches for "NonExistentFlower123"
    Then the message "No category found" is displayed

  @TC_UI_CAT_USER_08 @positive
  Scenario: User can navigate to category details
    When user navigates to "/ui/categories"
    And user clicks on a category name
    Then the category details page loads
    And the URL contains the category ID

  @TC_UI_CAT_USER_09 @negative
  Scenario: User cannot access add category page directly
    When user navigates directly to "/ui/categories/add"
    Then the user is redirected to 403 page or denied access
    And an error message is displayed
