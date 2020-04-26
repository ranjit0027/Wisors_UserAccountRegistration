package com.wisors.registration.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.wisors.registration.domain.UserInfo;
//import com.wisors.registration.util.Utility;
import com.wisors.registration.domain.WsrUserAccount;

import org.springframework.kafka.core.KafkaTemplate;

/**
 * 
 * @author Ranjit Sharma ,Wisors INC, USA
 * @since @11-04-2020
 * @version 1.0
 */

@Configuration
public class KafkaConfiguration {

	@Value("${kafka.broker.name}")
	private String broker;

	@Bean
	public ProducerFactory<String, UserInfo> producerFactory() {

		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);
		// config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory(config);
	}

	@Bean
	public ProducerFactory<String, String> producerFactory2() {

		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);

		// TODO: Have to use KafkaAvroSerializer

		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory(config);
	}

	@Bean
	public ProducerFactory<String, WsrUserAccount> responsedataProducerFactory() {

		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker);

		// TODO: Have to use KafkaAvroSerializer

		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory(config);
	}

	@Bean
	public KafkaTemplate<String, UserInfo> kafkaTemplate() {

		return new KafkaTemplate<String, UserInfo>(producerFactory());

	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate2() {

		return new KafkaTemplate<String, String>(producerFactory2());

	}

	@Bean
	public KafkaTemplate<String, WsrUserAccount> kafkaTemplate3() {

		return new KafkaTemplate<String, WsrUserAccount>(responsedataProducerFactory());

	}

}
