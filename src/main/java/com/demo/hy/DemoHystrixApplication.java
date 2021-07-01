package com.demo.hy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@RestController
@EnableHystrix
public class DemoHystrixApplication {
	@Autowired
	private RestTemplate restTemplate;

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	//http://localhost:9090/apphystrix
	@HystrixCommand(commandKey = "demohystrix",groupKey = "demohystrix", fallbackMethod = "fallbackMethod")
	@RequestMapping("/apphystrix")
	public String methodwithHystrix() {
		String fromServiceOne = restTemplate.getForObject("http://localhost:9091/demo/serviceone", String.class);
		String fromServiceTwo = restTemplate.getForObject("http://localhost:9092/demo/servicetwo", String.class);

		return fromServiceOne+"\n"+fromServiceTwo;
	}
	
	//http://localhost:9090/appwithouthystrix
	@RequestMapping("/appwithouthystrix")
	public String methodwithoutHystrix() {
		String fromServiceOne = restTemplate.getForObject("http://localhost:9091/demo/serviceone", String.class);
		String fromServiceTwo = restTemplate.getForObject("http://localhost:9092/demo/servicetwo", String.class);
		return fromServiceOne+"\n"+fromServiceTwo;	
		
	}
	public String fallbackMethod() {
		return "service gatway failure:failure of microservice";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoHystrixApplication.class, args);
	}

}
