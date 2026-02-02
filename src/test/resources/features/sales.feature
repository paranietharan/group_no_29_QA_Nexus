Feature: Sales - User UI

  @ui @sales @user
  Scenario: TC_UI_SALES_USER_01 Sales menu navigation
    Given I login to the application as "USER"
    When I click the Sales menu from sidebar
    Then Sales page should load and URL should contain "/ui/sales"


