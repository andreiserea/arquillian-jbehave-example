package org.jboss.arquillian.jbehave.example.steps;

import junit.framework.Assert;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Test;

public class ExampleTestSteps {

	private ClientResponse<?> response;

	@Test
	@Given("an user of the test application")
	public void given() {
		//nothing to do actually
	}
	
	@Test
	@When("the user accesses the home page of the application")
	public void when() throws Exception {
		final ClientRequest request = new ClientRequest("http://localhost:8080/rhea_test/o/countries");
		response = request.get();
	}
	
	@Test
	@Then("the user receives a \"Hello world!\" message")
	public void then() {
//		Assert.assertEquals("Hello world!", response.getEntity().toString());
	}
	
}
