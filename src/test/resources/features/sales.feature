Feature: Sales - User UI
 # ==================== UI USER LOGIN TEST CASES ====================

  @ui @sales @user
  Scenario: TC_UI_SALES_USER_01 Sales menu navigation
    Given I login to the application as "USER"
    When I click the Sales menu from sidebar
    Then Sales page should load and URL should contain "/ui/sales"



      @ui @sales @user
  Scenario: TC_UI_SALES_USER_02 Direct URL access to Sales page
    Given I login to the application as "USER"
    When I access the Sales page via direct URL
    Then Sales page should load and URL should contain "/ui/sales"


  @ui @sales @user
  Scenario: TC_UI_SALES_USER_03 Active menu highlight
    Given I login to the application as "USER"
    When I click the Sales menu from sidebar
    Then Sales menu item should be highlighted as active

  @ui @sales @user
  Scenario: TC_UI_SALES_USER_04 No sales message
    Given I login to the application as "USER"
    When I access the Sales page via direct URL
    Then No sales found message should be displayed and sales table headers should be visible


  @ui @sales @user
  Scenario: TC_UI_SALES_USER_05 Default sorting by Sold Date (descending)
    Given I login to the application as "USER"
    When I access the Sales page via direct URL
    Then Sales records should be sorted by Sold Date in descending order

      @ui @sales @user
  Scenario: TC_UI_SALES_USER_06 Sell Plant button hidden
    Given I login to the application as "USER"
    When I click the Sales menu from sidebar
    Then Sell Plant button should not be visible to USER

  @ui @sales @user
  Scenario: TC_UI_SALES_USER_07 Delete action hidden
    Given I login to the application as "USER"
    When I access the Sales page via direct URL
    Then Delete action should not be visible for USER in sales table


@ui @sales @user
Scenario: TC_UI_SALES_USER_08 Block access to Sell Plant page by URL
  Given I login to the application as "USER"
  When I try to access Sell Plant page via direct URL
  Then User should be redirected to Access Denied page

@ui @sales @user
Scenario: TC_UI_SALES_USER_09 Pagination is present
  Given I login to the application as "USER"
  When I access the Sales page via direct URL
  Then Pagination controls should be visible and USER can navigate pages


 # ==================== UI ADMIN LOGIN TEST CASES ====================

@ui @sales @admin
Scenario: TC_UI_SALES_ADMIN_01 Admin sees Sell Plant button
  Given I login to the application as "ADMIN"
  When I access the Sales page via direct URL
  Then Sell Plant button should be visible and enabled for ADMIN

@ui @sales @admin
Scenario: TC_UI_SALES_ADMIN_02 Admin can open Sell Plant form
  Given I login to the application as "ADMIN"
  When I access the Sales page via direct URL
  And I click the Sell Plant button
  Then Sell Plant form should open with plant selection and quantity fields


@ui @sales @admin
Scenario: TC_UI_SALES_ADMIN_03 Quantity validation against stock
  Given I login to the application as "ADMIN"
  When I access the Sales page via direct URL
  And I click the Sell Plant button
  And I select a plant with available stock
  And I enter quantity greater than available stock
  Then Validation error should be displayed for quantity greater than stock


