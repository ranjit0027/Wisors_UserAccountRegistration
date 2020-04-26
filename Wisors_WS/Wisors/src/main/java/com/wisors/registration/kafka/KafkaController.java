package com.wisors.registration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.wisors.registration.domain.UserAccount;

import com.wisors.registration.RegistrationApplication;
import com.wisors.registration.domain.UserInfo;
import com.wisors.registration.domain.WsrUserAccount;
import com.wisors.registration.exception.UserAccountNotFoundException;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Ranjit Sharma ,Wisors INC, USA
 * @since @11-04-2020
 * @version 1.0
 */

@Configuration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@Api(value = "UserAccountController", description = "REST Apis related to Kafka")
@EnableSwagger2
@RestController
@RequestMapping("/api/kafka/users")
public class KafkaController {

	private static final Logger log = LoggerFactory.getLogger(KafkaController.class);

	@Autowired
	private TopicProducer topicProducer;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void createUserAccount(@RequestBody UserInfo userinfo) {

		log.info("KAFKA CREATE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO : " + userinfo);
		log.info("");

		topicProducer.sendCreateUserMessage(userinfo);

		log.info("MESSAGE SEND to Producer");

	}

	@PutMapping(value = "/{phoneNo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateUserAccount(@RequestBody UserInfo userinfo, @PathVariable String phoneNo) {

		log.info("KAFKA CREATE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO : " + userinfo);
		log.info("");

		topicProducer.sendUpdatUeserMessage(userinfo, phoneNo);

		log.info("MESSAGE SEND to Producer");

	}

	@GetMapping(value = "/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getUserAccount(@PathVariable String phoneNo) {

		log.info("KAFKA RETRIVE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO PHONE NO : " + phoneNo);
		log.info("");

		topicProducer.sendRetriveUeserMessage(phoneNo);
	}

	@DeleteMapping(value = "/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteUserAccount(@PathVariable String phoneNo) {

		topicProducer.sendDeleteUeserMessage(phoneNo);
	}

}
