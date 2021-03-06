package io.pivotal.workshop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringBootTddApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void should_return_201_and_location_field_when_car_is_posted() throws Exception {
		ResponseEntity<Car> carResponseEntity = testRestTemplate.postForEntity("/cars", new Car("prius", "hybrid"), Car.class);
		assertThat(carResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(carResponseEntity.getHeaders().getLocation()).hasPath("/cars/prius");
	}

	@Test
	public void should_return_http_response_200_given_existing_car() throws Exception {

		// Add a test car before testing
		testRestTemplate.postForEntity("/cars", new Car("prius", "hybrid"), Car.class);

		ResponseEntity<Car> carResponseEntity = testRestTemplate.getForEntity("/cars/prius", Car.class);
		assertThat(carResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(carResponseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
		assertThat(carResponseEntity.getBody().getName()).isEqualTo("prius");
		assertThat(carResponseEntity.getBody().getType()).isEqualTo("hybrid");
	}

	@Test
	public void should_return_http_response_404_with_non_existent_car() throws Exception {

		ResponseEntity<Car> carResponseEntity = testRestTemplate.getForEntity("/cars/junk", Car.class);
		assertThat(carResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
