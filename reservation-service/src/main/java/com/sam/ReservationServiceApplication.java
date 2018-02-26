package com.sam;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ReservationRepository rr) {
		return args -> {

			Arrays.asList("Heath,Jeff,Josh,Rod,Juergen,Charlie".split(",")).forEach(n -> rr.save(new Reservation(n)));

			rr.findAll().forEach(System.out::println);

		};
	}

}

/*@MessageEndpoint
class ReservationProcesor {

	@Autowired
	private ReservationRepository reservationRepository;

	@ServiceActivator(inputChannel = Sink.INPUT)
	public void acceptReservations(String rn) {
		this.reservationRepository.save(new Reservation(rn));
	}

}*/

@RefreshScope
@RestController
class MessageREstController {

	@Value("${message}")
	private String message;

	@RequestMapping("/message")
	public String message() {
		return message;
	}

}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Collection<Reservation> findByReservationName(String rn);
}

@Entity
class Reservation {

	@Id
	@GeneratedValue
	private Long id;

	private String reservationName;

	public Reservation() {
	}

	@Override
	public String toString() {
		return "Reservation{" + "id=" + id + ", reservationName='" + reservationName + '\'' + '}';
	}

	public Long getId() {
		return id;
	}

	public String getReservationName() {
		return reservationName;
	}

	public Reservation(String reservationName) {
		this.reservationName = reservationName;
	}
}
