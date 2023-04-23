package com.project.springboot.restapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.Base64;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {
	private static String All_SURVEYS_URL="/surveys";
	private static String SPECIFIC_SURVEY_URL="/surveys/Survey1";
	private static String SPECIFIC_SURVEYQUESTIONS_URL="/surveys/Survey1/questions";
	private static String SPECIFIC_QUESTION_URL="/surveys/Survey1/questions/Question1";
	
	@Autowired
	private TestRestTemplate template;
	
	@Test
	void retrieveAllSurveys_basicScenario() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(All_SURVEYS_URL, String.class);
		String expectedResponse = 
				"""
					[{"id":"Survey1","title":"My Favorite Survey","description":"Description of the Survey","questions":[{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},{"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},{"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]}]
				""";
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json",responseEntity.getHeaders().get("Content-Type").get(0));
		
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	
	@Test
	void retrieveSurveyById_basicScenario() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEY_URL, String.class);
		String expectedResponse = 
				"""
					{"id":"Survey1","title":"My Favorite Survey","description":"Description of the Survey","questions":[{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},{"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},{"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]}
				""";
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json",responseEntity.getHeaders().get("Content-Type").get(0));
		
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void retrieveAllSurveyQuestion_basicScenario() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEYQUESTIONS_URL, String.class);
		String expectedResponse = 
				"""
					[{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},{"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},{"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]	
				""";
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json",responseEntity.getHeaders().get("Content-Type").get(0));
		
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void retrieveSpecificSurveyQuestion_basicScenario() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
		String expectedResponse = 
				"""
					{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}	
				""";
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		assertEquals("application/json",responseEntity.getHeaders().get("Content-Type").get(0));
		
		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	// URL: http://localhost:8080/surveys/Survey1/questions
	// Method: POST
	// content-type: application/json
	// status: 201
	// location: http://localhost:8080/surveys/Survey1/questions/3718798071
	
	@Test
	void addSurveyQuestion_BasicScenario() {
		String requestBody=
				"""
					{
					  "description": "Your favorite programming language",
					  "options": [
					    "Java",
					    "JavaScript",
					    "C++",
					    "Qt"
					  ],
					  "correctAnswer": "Java"
					}
				""";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", "application/json");
		headers.add("Authorization", "Basic"+performBasicAuthEncoding("admin","admin"));
		
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody,headers);
		
		ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_SURVEYQUESTIONS_URL, HttpMethod.POST, httpEntity,String.class);
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		
		String locationHeader = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));
		
		template.delete(locationHeader);
	}
	
	public String performBasicAuthEncoding(String user, String pass) {
		String combined=user+":"+pass;
		
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		
		return new String(encodedBytes);
		
	}
	
}
