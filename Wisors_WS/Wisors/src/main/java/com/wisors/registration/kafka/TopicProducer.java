package com.wisors.registration.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

	@Value("${user.registration.kafka.topic.name}")
	private String REGISTRATION_TOPIC;

	@Value("${user.registration.response.kafka.topic.name}")
	private String REGISTRATION_RESPONSE_TOPIC;

	@Autowired
	private KafkaTemplate<String, UserInfo> kafkaTemplate;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate2;

	@Autowired
	private KafkaTemplate<String, WsrUserAccount> kafkaTemplate3;

	public void sendCreateUserMessage(UserInfo userInfo) {
		log.info("");
		log.info("Recived create message ===>: " + userInfo.toString());
		log.info("");

		this.kafkaTemplate.send(REGISTRATION_TOPIC, userInfo);
	}

	public void sendUpdatUeserMessage(UserInfo userinfo, String phoneNo) {

		log.info("Recived update message: " + userinfo.toString() + " , " + phoneNo);
		this.kafkaTemplate.send(REGISTRATION_TOPIC, userinfo);
	}

	public void sendRetriveUeserMessage(String phoneno) {

		log.info("");
		log.info("Recived search message : " + phoneno);
		log.info("");
		this.kafkaTemplate2.send(REGISTRATION_TOPIC, phoneno);
	}

	public void sendDeleteUeserMessage(String phoneno) {

		log.info("Received delete message: " + phoneno);
		this.kafkaTemplate2.send(REGISTRATION_TOPIC, String.valueOf(phoneno));
	}

	public void sendCreateUserAccountResponseMessage(WsrUserAccount wsrUserAct, String errorMsg) {
		log.info("Recived create message  ==> : " + wsrUserAct);

		if (wsrUserAct != null && errorMsg == null) {
			this.kafkaTemplate3.send(REGISTRATION_RESPONSE_TOPIC, wsrUserAct);
		} else {
			this.kafkaTemplate2.send(REGISTRATION_RESPONSE_TOPIC, errorMsg);
		}

	}

	public void sendUpdateUserAccountResponseMessage(WsrUserAccount wsrUserAct, String errorMsg) {
		log.info("Recived update message ==> : " + wsrUserAct);

		if (wsrUserAct != null && errorMsg == null) {
			this.kafkaTemplate3.send(REGISTRATION_RESPONSE_TOPIC, wsrUserAct);
		} else {
			this.kafkaTemplate2.send(REGISTRATION_RESPONSE_TOPIC, errorMsg);
		}

	}

	public void sendRetriveUserAccountResponseMessage(WsrUserAccount wsrUserAct, String errorMsg) {
		log.info("Recived retrive message ==> : " + wsrUserAct);
		log.info("Recived retrive message 2==> : " + errorMsg);

		if (wsrUserAct != null && errorMsg == null) {
			this.kafkaTemplate3.send(REGISTRATION_RESPONSE_TOPIC, wsrUserAct);
		} else {
			this.kafkaTemplate2.send(REGISTRATION_RESPONSE_TOPIC, errorMsg);
		}

	}

	public void sendDeleteUserAccountResponseMessage(String str, HttpStatus ok) {
		log.info("Recived delete message ==> : " + HttpStatus.OK);
		this.kafkaTemplate2.send(REGISTRATION_RESPONSE_TOPIC, str);

	}

}
