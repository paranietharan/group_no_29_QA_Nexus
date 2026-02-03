Feature: Sales - User UI

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
