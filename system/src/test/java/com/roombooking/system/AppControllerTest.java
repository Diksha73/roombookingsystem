package com.roombooking.system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roombooking.system.model.Customer;
import com.roombooking.system.repository.CustomerRepository;

import junit.framework.Assert;

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	private MockMvc mockMvc;
	
	private static final ObjectMapper om = new ObjectMapper();
	 @Before
	public void setup() {
		customerRepo.deleteAll();
		om.setDateFormat(simpleDateFormat);
	}
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testAddCustomer() throws Exception {
		Customer actualRecord = getTestData().get("customer1");
		Customer expectedRecord = om.readValue(mockMvc
				.perform(post("/api/customer").contentType("application/json")
						.content(om.writeValueAsString(getTestData().get("customer1"))))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
				Customer.class);
		Assert.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
	}

	@Test
	public void testAddCustomerWithInvalidPassword() throws Exception {
		mockMvc.perform(post("/api/customer").contentType("application/json")
				.content(om.writeValueAsString(getTestData().get("customer2")))).andExpect(status().isBadRequest());
	}
	@Test
	public void testAddCustomerWithNullInput() throws Exception {
		mockMvc.perform(post("/api/customer").contentType("application/json")
				.content("")).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testAddCustomerWithExistingEmail() throws Exception {
		mockMvc.perform(post("/api/customer").contentType("application/json")
				.content(om.writeValueAsString(getTestData().get("customer3")))).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testGetCustomerEndpoint() throws Exception {
		Customer expectedRecord = om.readValue(mockMvc
				.perform(post("/api/customer").contentType("application/json")
						.content(om.writeValueAsString(getTestData().get("customer4"))))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(),
				Customer.class);
		
		Customer actualRecord = om.readValue(
				mockMvc.perform(get("/api/customer/" + expectedRecord.getUserId()).contentType("application/json"))
						.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
				Customer.class);
	}
	
	@Test
	public void testGetCustomerDoesnotExist() throws Exception {
		mockMvc.perform(get("/api/customer" + Integer.MAX_VALUE)).andExpect(status().isNotFound());
	}
	
	private Map<String, Customer> getTestData() {
		Map<String, Customer> data = new LinkedHashMap<>();
		String dateString;
		Customer customer1 = new Customer();
		customer1.setEmail("testemail1@test.com");
		dateString = "1995-01-01";
		try {
			customer1.setDob(simpleDateFormat.parse(dateString));
		} catch (ParseException e) {
		}
		customer1.setUserId(1);
		customer1.setFirstName("Test User");
		customer1.setLastName("Last Name");
		customer1.setPassword("test12345");
		data.put("customer1", customer1);
		
		Customer customer2 = new Customer();
		customer2.setEmail("testemail2@test.com");
		try {
			customer2.setDob(simpleDateFormat.parse(dateString));
		} catch (ParseException e) {
		}
		customer2.setFirstName("Test User");
		customer2.setLastName("Last Name");
		customer2.setPassword("test123"); // password not between 8 to 10
		data.put("customer2", customer2);
		
		Customer customer3 = new Customer();
		customer3.setEmail("testemail1@test.com"); // duplicate email
		try {
			customer3.setDob(simpleDateFormat.parse(dateString));
		} catch (ParseException e) {
		}
		customer3.setFirstName("Test User");
		customer3.setLastName("Last Name");
		customer3.setPassword("12345test");
		data.put("customer3", customer3);
		
		Customer customer4 = new Customer();
		customer4.setEmail("testemail4@test.com");
		dateString = "1998-07-07";
		try {
			customer4.setDob(simpleDateFormat.parse(dateString));
		} catch (ParseException e) {
		}
		customer4.setUserId(1);
		customer4.setFirstName("Test User4");
		customer4.setLastName("Last Name");
		customer4.setPassword("password1");
		data.put("customer4", customer4);
		return data;
	}

}
