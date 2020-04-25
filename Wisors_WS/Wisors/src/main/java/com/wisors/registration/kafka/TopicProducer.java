package com.wisors.registration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.wisors.registration.domain.WsrUserAccount;

/**
 * 
 * @author Ranjit Sharma ,Wisors INC, USA
 * @since @11-04-2020
 * @version 1.0
 */

@Service
public class TopicProducer {

	private static final Logger log = LoggerFactory.getLogger(TopicProducer.class);

	@Value("${kafka.create.response.topic.name}")
	private String TOPIC_CREATE_RESPONSE;

	@Value("${kafka.update.response.topic.name}")
	private String TOPIC_UPDATE_RESPONSE;

	@Value("${kafka.retrive.response.topic.name}")
	private String TOPIC_RETRIVE_RESPONSE;

	@Value("${kafka.delete.response.topic.name}")
	private String TOPIC_DELETE_RESPONSE;

	@Autowired
	private KafkaTemplate<String, ResponseEntity<WsrUserAccount>> kafkaTemplate;

	public void sendCreateUserMessage(ResponseEntity<WsrUserAccount> registeredUser) {
		// log.info("Generated message: " + userAccount.toString());

		log.info("Generated create message: " + registeredUser.toString());
		this.kafkaTemplate.send(TOPIC_CREATE_RESPONSE, registeredUser);
	}

	public void sendUpdatUeserMessage(ResponseEntity<WsrUserAccount> userAccount) {
		// log.info("Generated message: " + userAccount.toString());

		log.info("Generated update message: " + userAccount.toString());
		this.kafkaTemplate.send(TOPIC_UPDATE_RESPONSE, userAccount);
	}

	public void sendRetriveUeserMessage(ResponseEntity<WsrUserAccount> registeredUser) {
		// log.info("Generated message: " + userAccount.toString());

		log.info("Generated retrive message: " + registeredUser.toString());
		this.kafkaTemplate.send(TOPIC_RETRIVE_RESPONSE, registeredUser);
	}

	public void sendDeleteUeserMessage(ResponseEntity<WsrUserAccount> userAccount) {
		// log.info("Generated message: " + userAccount.toString());

		log.info("Generated delete message: " + userAccount.toString());
		this.kafkaTemplate.send(TOPIC_DELETE_RESPONSE, userAccount);
	}

}
