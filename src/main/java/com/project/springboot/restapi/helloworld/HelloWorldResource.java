package com.project.springboot.restapi.helloworld;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class HelloWorldResource {
	
	// /hello-world => "Hello World"
	
	@RequestMapping("/hello-world")
	public String helloWorld() {
		return "Hello World";
	}
	
	@RequestMapping("/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}
	
	// Path Variables and path Params
	// /user/Ranga/1
	
	@RequestMapping("/hello-world-path-params/{name}")
	public HelloWorldBean helloWorldPathParams(@PathVariable String name) {
		return new HelloWorldBean("Hello World, "+name);
	}
	
	@RequestMapping("/hello-world-path-params/{name}/todo/{id}")
	public HelloWorldBean helloWorldMultiplePathParams(@PathVariable String name,@PathVariable int id) {
		return new HelloWorldBean("Hello World, "+name+" Todo number "+id);
	}

}
