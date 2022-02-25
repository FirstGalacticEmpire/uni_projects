Feature: Login.
 
Background:
  Given I have cleared the database
  Given I have registered as user "john" with password "john", name "john" and with address "Colourful St"
  
Scenario: Login
  Given I've moved to the login page
  When I fill the login form with username "john" and password "john", and I submit it
  Then I should see the accounts-list page