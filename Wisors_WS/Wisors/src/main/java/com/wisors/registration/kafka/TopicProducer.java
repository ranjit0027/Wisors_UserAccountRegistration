package com.wisors.registration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.wisors.registration.domain.UserInfo;
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

	@Value("${kafka.create.topic.name}")
	private String TOPIC_CREATE_RESPONSE;

	@Value("${kafka.update.topic.name}")
	private String TOPIC_UPDATE_RESPONSE;

	@Value("${kafka.retrive.topic.name}")
	private String TOPIC_RETRIVE_RESPONSE;

	@Value("${kafka.delete.topic.name}")
	private String TOPIC_DELETE_RESPONSE;

	@Autowired
	private KafkaTemplate<String, UserInfo> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate2;

	public void sendCreateUserMessage(UserInfo userInfo) {
		log.info("");
		log.info("Recived create message ===>: " + userInfo.toString());
		log.info("");
		
		
		this.kafkaTemplate.send(TOPIC_CREATE_RESPONSE, userInfo);
	}

	public void sendUpdatUeserMessage(UserInfo userinfo, String phoneNo) {

		log.info("Recived update message: " + userinfo.toString() + " , " + phoneNo);
		this.kafkaTemplate.send(TOPIC_UPDATE_RESPONSE, userinfo);
	}

	public void sendRetriveUeserMessage(String phoneno) {

		log.info("");
		log.info("Recived search message ????  : " + phoneno);
		log.info("");
		this.kafkaTemplate2.send(TOPIC_RETRIVE_RESPONSE, phoneno);
	}

	public void sendDeleteUeserMessage(String phoneno) {

		log.info("Received delete message: " + phoneno);
		this.kafkaTemplate2.send(TOPIC_DELETE_RESPONSE, String.valueOf(phoneno));
	}

}
