package org.jboss.arquillian.jbehave.example;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("/")
@LocalBean
public class TestService {

	@GET
	public String test() {
		return "Hello world!";
	}
}
