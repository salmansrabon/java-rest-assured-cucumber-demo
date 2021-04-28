import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class StepDefinitions {
    Properties prop=new Properties();
    FileInputStream file;
    {
        try
        {
            file = new FileInputStream("./src/test/resources/config.properties");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    RequestSpecification request;
    @Given("^Login API is provided$")
    public void login_API_is_provided() throws Exception {
        prop.load(file);
        RestAssured.baseURI  = prop.getProperty("baseUrl");
        request = RestAssured.given();

    }
    Response response;
    @When("^User call login API$")
    public void user_call_login_API() throws Exception {
        JSONObject requestParams = new JSONObject();
        requestParams.put("username", "salman");
        requestParams.put("password", "salman1234");
        request.header("Content-Type", "application/json");
        System.out.print(request.body(requestParams.toString()));
        response = request.post("/customer/api/v1/login");

    }

    @Then("^a token will be generated$")
    public void a_token_will_be_generated() throws Exception {
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        System.out.print(response.asString());
        String token= response.jsonPath().getString("token");
        System.out.println(token);
        Utils.setEnvVariable(token);

    }

    @Given("^Customer list API is provided$")
    public void customer_list_API_is_provided() throws Exception {
        prop.load(file);
        RestAssured.baseURI  = prop.getProperty("baseUrl");
        request = RestAssured.given();

    }

    @When("^User call customer list API$")
    public void user_call_customer_list_API() throws Exception {
        Header authorizationHeader = new Header("Authorization", prop.getProperty("token"));
        request.header(authorizationHeader);
        response = request.get("/customer/api/v1/list");
    }

    @Then("^Customer list will be shown$")
    public void customer_list_will_be_shown() throws Exception {
        System.out.println("Response Body is =>  " + response.asString());
        String customerId=response.jsonPath().getString("Customers[0].id");
        Assert.assertEquals(customerId,"101");

    }

    @Given("^Customer get API is provided$")
    public void customer_get_API_is_provided() throws Exception {
        prop.load(file);
        RestAssured.baseURI  = prop.getProperty("baseUrl");
        request = RestAssured.given();

    }

    @When("^User call customer get API$")
    public void user_call_customer_get_API() throws Exception {
        Header authorizationHeader = new Header("Authorization", prop.getProperty("token"));
        request.header(authorizationHeader);
        response = request.get("/customer/api/v1/get/101");

    }

    @Then("^Specific customer info will be shown$")
    public void specific_customer_info_will_be_shown() throws Exception {
        System.out.println("Response Body is =>  " + response.asString());
        String customerId=response.jsonPath().getString("id");
        Assert.assertEquals(customerId,"101");

    }
}
