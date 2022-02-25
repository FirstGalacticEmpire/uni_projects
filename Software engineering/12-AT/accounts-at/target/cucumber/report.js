$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/resources/put/bdd/login.feature");
formatter.feature({
  "name": "Login.",
  "description": "",
  "keyword": "Feature"
});
formatter.background({
  "name": "",
  "description": "",
  "keyword": "Background"
});
formatter.step({
  "name": "I have cleared the database",
  "keyword": "Given "
});
formatter.match({
  "location": "LoginStepsDefinitions.I_have_cleared_the_database()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I have registered as user \"john\" with password \"john\", name \"john\" and with address \"Colourful St\"",
  "keyword": "Given "
});
formatter.match({
  "location": "LoginStepsDefinitions.I_have_registered_as_user_with_password_name_and_with_address(String,String,String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.scenario({
  "name": "Login",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "I\u0027ve moved to the login page",
  "keyword": "Given "
});
formatter.match({
  "location": "LoginStepsDefinitions.I_am_on()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I fill the login form with username \"john\" and password \"john\", and I submit it",
  "keyword": "When "
});
formatter.match({
  "location": "LoginStepsDefinitions.I_fill_in_with(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I should see the accounts-list page",
  "keyword": "Then "
});
formatter.match({
  "location": "LoginStepsDefinitions.I_should_see_accunts_list_page()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});