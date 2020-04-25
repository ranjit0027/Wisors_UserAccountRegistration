package com.wisors.registration.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.wisors.registration.domain.UserAccount;

import com.wisors.registration.domain.WsrUserAccount;

/**
 * 
 * @author Ranjit Sharma ,Wisors INC, USA
 * @since @11-04-2020
 * @version 1.0
 */

@RestController
@RequestMapping("/api/kafka/users")
public class KafkaController {

	@Autowired
	private TopicProducer topicProducer;

	/*
	 * @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE) public void createUserAccount(@RequestBody
	 * ResponseEntity<WsrUserAccount> userAccount) {
	 * topicProducer.sendCreateUserMessage(userAccount); }
	 */

}
