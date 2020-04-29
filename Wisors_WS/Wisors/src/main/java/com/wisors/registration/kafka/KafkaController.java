package com.wisors.registration.kafka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.wisors.registration.domain.UserAccount;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisors.registration.RegistrationApplication;
import com.wisors.registration.domain.UserInfo;
import com.wisors.registration.domain.WsrUserAccount;
import com.wisors.registration.domain.WsrUserAddress;
import com.wisors.registration.domain.WsrUserGroupType;
import com.wisors.registration.domain.WsrUserGroupXref;
import com.wisors.registration.domain.WsrUserInGroup;
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

	@Value("${kafka.broker.name}")
	private String broker;

	@Autowired
	private TopicProducer topicProducer;

	@Value("${user.registration.response.kafka.topic.name}")
	private String REGISTRATION_RESPONSE_TOPIC;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WsrUserAccount> createUserAccount(@RequestBody UserInfo userinfo) {

		log.info("KAFKA CREATE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO : " + userinfo);
		log.info("");

		ResponseEntity<WsrUserAccount> registeredUser = null;

		topicProducer.sendCreateUserMessage(userinfo);

		log.info("MESSAGE SEND to Producer");

		String response = comsumeResponseEntity(REGISTRATION_RESPONSE_TOPIC);

		JSONObject responseobj = new JSONObject(response);

		byte[] jsonData = responseobj.toString().getBytes();

		ObjectMapper mapper = new ObjectMapper();
		try {
			WsrUserAccount useraccount = mapper.readValue(jsonData, WsrUserAccount.class);
			registeredUser = ResponseEntity.ok().body(useraccount);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return registeredUser;

	}

	@PutMapping(value = "/{phoneNo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WsrUserAccount> updateUserAccount(@RequestBody UserInfo userinfo,
			@PathVariable String phoneNo) {

		log.info("KAFKA UPDATE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO : " + userinfo);
		log.info("");

		ResponseEntity<WsrUserAccount> registeredUser = null;

		topicProducer.sendUpdatUeserMessage(userinfo, phoneNo);

		log.info("MESSAGE SEND to Producer");

		String response = comsumeResponseEntity(REGISTRATION_RESPONSE_TOPIC);

		JSONObject responseobj = new JSONObject(response);

		byte[] jsonData = responseobj.toString().getBytes();

		ObjectMapper mapper = new ObjectMapper();
		try {
			WsrUserAccount useraccount = mapper.readValue(jsonData, WsrUserAccount.class);
			log.info(" **** WsrUserAccount ***** : " + useraccount);
			registeredUser = ResponseEntity.ok().body(useraccount);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		log.info("");
		log.info(" **** registeredUser ***** : " + registeredUser);
		log.info("");

		return registeredUser;

	}

	@GetMapping(value = "/{phoneNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<WsrUserAccount> getUserAccount(@PathVariable String phoneNo) {

		log.info("KAFKA RETRIVE CONTROLLER END POINT");
		log.info("");
		log.info("USER_INFO PHONE NO : " + phoneNo);
		log.info("");

		ResponseEntity<WsrUserAccount> registeredUser = null;

		topicProducer.sendRetriveUeserMessage(phoneNo);

		String response = comsumeResponseEntity(REGISTRATION_RESPONSE_TOPIC);

		JSONObject responseobj = new JSONObject(response);

		byte[] jsonData = responseobj.toString().getBytes();

		ObjectMapper mapper = new ObjectMapper();
		try {
			WsrUserAccount useraccount = mapper.readValue(jsonData, WsrUserAccount.class);
			log.info(" **** WsrUserAccount ***** : " + useraccount);
			registeredUser = ResponseEntity.ok().body(useraccount);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		log.info("");
		log.info(" **** registeredUser ***** : " + registeredUser);
		log.info("");

		return registeredUser;

	}

	@DeleteMapping(value = "/{phoneNo}")
	public ResponseEntity<String> deleteUserAccount(@PathVariable String phoneNo) {

		topicProducer.sendDeleteUeserMessage(phoneNo);

		String response = comsumeResponseEntity(REGISTRATION_RESPONSE_TOPIC);

		log.info("DELEETD RESPOSNE : " + response);

		return new ResponseEntity<String>(String.valueOf(response), HttpStatus.OK);

	}

	private String comsumeResponseEntity(String topic) {

		log.info("**** comsumeResponseEntity : ****" + topic);
		Properties properties = new Properties();
		properties.put("bootstrap.servers", broker);

		// properties.put("bootstrap.servers", "localhost:9092");
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("group.id", "group_id");

		KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
		List topics = new ArrayList();
		topics.add(topic);

		kafkaConsumer.subscribe(topics);

		boolean flag = true;
		String message = "";

		try {
			while (flag) {

				ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
				for (ConsumerRecord<String, String> record : records) {
					log.info(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(),
							record.value()));

					message = record.value();
					log.info("message ***** : " + message);
					flag = false;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			kafkaConsumer.close();
		}

		log.info("");
		log.info("======== > CONSUME KAFKA MESSAGE =======> : " + message);
		log.info("");
		log.info(" RETRIVE JSON DATA FROM KAFKA CONSUME MESSAGE ");
		log.info("=================================================");

		return message;

	}

}
