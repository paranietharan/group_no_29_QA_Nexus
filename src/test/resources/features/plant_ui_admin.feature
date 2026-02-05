@ui @plant @admin
Feature: Plant UI - Admin Role
  As an admin with full access
  I want to manage plants through the UI
  So that I can add, view, edit and delete plant records

  Background:
    Given the admin is logged in via UI

  # TC_UI_ADMIN_001
  @TC_UI_ADMIN_001 @positive @list
  Scenario: Admin sees Plant List with pagination
    When the admin navigates to the admin plants page
    Then the plant table displays plants
    And pagination controls are visible

  # TC_UI_ADMIN_002
  @TC_UI_ADMIN_002 @positive
  Scenario: Admin sees Add Plant button
    When the admin navigates to the admin plants page
    Then the Add New Plant button is visible

  # TC_UI_ADMIN_003
  @TC_UI_ADMIN_003 @positive @create
  Scenario: Admin can add new plant with valid data
    Given sub-categories exist in the system
    When the admin navigates to the admin plants page
    And the admin clicks the Add Plant button
    And the admin fills in valid plant data
    And the admin clicks Save
    Then a success message is displayed
    And the new plant is visible in the list

  # TC_UI_ADMIN_004
  @TC_UI_ADMIN_004 @positive @update
  Scenario: Admin can edit existing plant
    Given a plant named "Rose" exists
    When the admin navigates to the admin plants page
    And the admin clicks edit on the plant "Rose"
    And the admin updates the price
    And the admin clicks Save
    Then a success message is displayed
    And the updated price is shown in the list

  # TC_UI_ADMIN_005
  @TC_UI_ADMIN_005 @positive @delete
  Scenario: Admin can delete plant with confirmation
    Given a plant named "Tulip" exists
    When the admin navigates to the admin plants page
    And the admin clicks delete on the plant "Tulip"
    And the admin confirms the deletion
    Then the plant "Tulip" is removed from the list

  # TC_UI_ADMIN_006
  @TC_UI_ADMIN_006 @negative @validation
  Scenario: Admin sees validation errors for invalid inputs
    When the admin navigates to the admin plants page
    And the admin clicks the Add Plant button
    And the admin enters name "AB"
    And the admin enters price "0"
    And the admin tries to save
    Then validation error messages are displayed under each field

  # TC_UI_ADMIN_007
  @TC_UI_ADMIN_007 @positive @search
  Scenario: Admin can search plants by name
    Given a plant named "Lily" exists
    When the admin navigates to the admin plants page
    And the admin searches for "Lily"
    Then only plants with Lily in the name are shown

  # TC_UI_ADMIN_008
  @TC_UI_ADMIN_008 @positive @filter
  Scenario: Admin can filter plants by category
    Given multiple categories with plants exist
    When the admin navigates to the admin plants page
    And the admin filters by category "Indoor Plants"
    Then only plants from the selected category are shown

 
  # TC_UI_ADMIN_010
  @TC_UI_ADMIN_010 @positive @empty
  Scenario: Admin sees No plants found message
    When the admin navigates to the admin plants page
    And the admin searches for "XYZ123NonExistent"
    Then a No plants found message is displayed

  # TC_UI_ADMIN_011
  @TC_UI_ADMIN_011 @positive @cancel
  Scenario: Admin plant form cancel button works
    When the admin navigates to the admin plants page
    And the admin clicks the Add Plant button
    And the admin enters some plant data
    And the admin clicks Cancel
    Then the admin is returned to the plant list
    And no unsaved data is persisted

  # TC_UI_ADMIN_012
  @TC_UI_ADMIN_012 @positive @realtime
  Scenario: Admin plant list updates after actions
    When the admin navigates to the admin plants page
    And the admin adds a new plant
    Then the plant list shows the new plant without page reload

  # TC_UI_ADMIN_013
  @TC_UI_ADMIN_013 @positive @dropdown
  Scenario: Admin can only select sub-categories in dropdown
    When the admin navigates to the admin plants page
    And the admin clicks the Add Plant button
    And the admin opens the category dropdown
    Then only sub-categories are selectable
    And main categories are disabled or not shown

  # TC_UI_ADMIN_014
  @TC_UI_ADMIN_014 @positive @realtime
  Scenario: Admin sees real-time stock updates
    Given a plant with quantity 10 exists
    When the admin opens two browser tabs with the plants page
    And the admin updates the quantity to 5 in one tab
    Then the other tab shows the updated quantity
