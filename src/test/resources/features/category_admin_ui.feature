@category @ui @admin
Feature: Category UI Management - Admin
  As an admin
  I want to manage categories via UI
  So that I can organize flower inventory visually

  Background:
    Given admin is logged in via UI

  @TC_UI_CAT_ADMIN_01 @positive
  Scenario: Admin sees Category List with pagination
    When admin navigates to "/ui/categories"
    Then the category list page is displayed
    And pagination controls are visible if multiple pages exist

  @TC_UI_CAT_ADMIN_02 @positive
  Scenario: Admin sees Add Category button
    When admin navigates to "/ui/categories"
    Then the "Add Category" button is visible

  @TC_UI_CAT_ADMIN_03 @positive
  Scenario: Admin can add main category
    When admin navigates to "/ui/categories"
    And admin clicks the "Add Category" button
    And admin enters category name "Carnations"
    And admin leaves parent category empty
    And admin submits the category form
    Then the new main category "Carnations" appears in the list
    And the category has no parent assigned

  @TC_UI_CAT_ADMIN_04 @positive
  Scenario: Admin can add sub-category
    Given a category "Carnations" exists without sub-categories
    When admin navigates to "/ui/categories"
    And admin clicks the "Add Category" button
    And admin enters category name "Spring01"
    And admin selects parent category "Carnations"
    And admin submits the category form
    Then the new sub-category "Spring01" appears under "Carnations"

  @TC_UI_CAT_ADMIN_05 @positive
  Scenario: Admin can edit existing category
    Given a category "CarnatNew" exists without sub-categories
    When admin navigates to "/ui/categories"
    And admin clicks edit icon next to "CarnatNew"
    And admin changes the name to "Carnations"
    And admin submits the category form
    Then the category name is updated to "Carnations" in the list

  @TC_UI_CAT_ADMIN_06 @positive
  Scenario: Admin can delete category with confirmation
    Given a category "Carnatio48" exists without sub-categories
    When admin navigates to "/ui/categories"
    And admin clicks delete icon next to "Carnatio48"
    And admin confirms the deletion in modal
    Then the category "Carnatio48" is removed from the list
    And the confirmation modal disappears
  # SKIP: Application does not display validation errors on UI (DOM confirmed missing error block)

  @TC_UI_CAT_ADMIN_07 @negative
  Scenario: Admin sees validation error for short name
    When admin navigates to "/ui/categories"
    And admin clicks the "Add Category" button
    And admin enters category name "AB"
    And admin submits the category form
    Then a red error message is displayed
    And the form is not submitted
  # SKIP: Application does not display validation errors on UI (DOM confirmed missing error block)

  @TC_UI_CAT_ADMIN_08 @negative
  Scenario: Admin sees validation error for long name
    When admin navigates to "/ui/categories"
    And admin clicks the "Add Category" button
    And admin enters category name "VeryLongFlowerNameExceedingLimit"
    And admin submits the category form
    Then a red error message is displayed
    And the form is not submitted

  @TC_UI_CAT_ADMIN_09 @positive
  Scenario: Admin can search categories by name
    Given a category "Spring01" exists without sub-categories
    When admin navigates to "/ui/categories"
    And admin enters "Spring01" in the search box
    And admin presses Enter
    Then the category list filters to show only "Spring01" matches

  @TC_UI_CAT_ADMIN_10 @positive
  Scenario: Admin can filter by parent category
    Given a category "Carnations" exists without sub-categories
    When admin navigates to "/ui/categories"
    And admin selects parent filter "Carnations"
    And admin applies the filter
    Then the list shows only sub-categories of "Carnations"

  @TC_UI_CAT_ADMIN_11 @positive
  Scenario: Admin can sort categories
    And multiple categories exist
    When admin navigates to "/ui/categories"
    And admin clicks the "Name" column header to sort
    Then the category list is sorted by name
    And clicking again reverses the sort order

  @TC_UI_CAT_ADMIN_12 @positive
  Scenario: Admin sees No category found message
    When admin navigates to "/ui/categories"
    And admin searches for "NonExistentFlower123"
    Then the message "No category found" is displayed

  @TC_UI_CAT_ADMIN_13 @positive
  Scenario: Admin category form cancel button works
    When admin navigates to "/ui/categories"
    And admin clicks the "Add Category" button
    And admin enters category name "TestCategory"
    And admin clicks the Cancel button
    Then the URL is "/ui/categories"
    And the form is not submitted
