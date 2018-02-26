package com.sam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableCircuitBreaker
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ReservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationClientApplication.class, args);
	}
}

@RestController
@RequestMapping(value = "/reservations")
class ReservationApiGatewayRestController {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@LoadBalanced
	@Autowired
	RestTemplate restTemplate;
	
	/*@Autowired
	private Source source;
	
	@RequestMapping(method = RequestMethod.POST)
	@InboundChannelAdapter(value = Source.OUTPUT)
	public void writeReservation(@RequestBody Reservation r) {
		Message<String> message = MessageBuilder.withPayload(r.getReservationName()).build();
		this.source.output().send(message);
	}*/

	public Collection<String> getReservationNamesFallback() {
		return new ArrayList<>();
	}

	@HystrixCommand(fallbackMethod = "getReservationNamesFallback")
	@RequestMapping(value = "/names", method = RequestMethod.GET)
	public Collection<String> getReservationNames() {
		ParameterizedTypeReference<Resources<Reservation>> ptr = new ParameterizedTypeReference<Resources<Reservation>>() {
		};

		ResponseEntity<Resources<Reservation>> entity = this.restTemplate
				.exchange("http://reservation-service/reservations", HttpMethod.GET, null, ptr);
		return entity.getBody().getContent().stream().map(Reservation::getReservationName).collect(Collectors.toList());
	}

}

class Reservation {
	String reservationName;

	public String getReservationName() {
		return reservationName;
	}

}
