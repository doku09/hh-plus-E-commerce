package kr.hhplus.be.server.config;

import kr.hhplus.be.server.domain.order.OrderCompletedMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

	/** DLQ 토픽 이름 (필요 없으면 지우세요) */
	private static final String DLQ_TOPIC = "order-completed-dlq";

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	/** 동시 처리 스레드 수 */
	@Value("${spring.kafka.listener.concurrency:3}")
	private int concurrency;


	@Bean
	public ConsumerFactory<String, OrderCompletedMessage> orderCompletedConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "data-platform-sync");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		JsonDeserializer<OrderCompletedMessage> valueDeserializer =
			new JsonDeserializer<>(OrderCompletedMessage.class)
				.ignoreTypeHeaders()
				.trustedPackages("kr.hhplus.be.server.infrastructure.order");

		return new DefaultKafkaConsumerFactory<>(props,
			new StringDeserializer(),
			valueDeserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderCompletedMessage>
	orderCompletedKafkaListenerContainerFactory(
		ConsumerFactory<String, OrderCompletedMessage> orderCompletedConsumerFactory
	) {
		var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderCompletedMessage>();
		factory.setConsumerFactory(orderCompletedConsumerFactory);

		/* 수동 커밋(MANUAL) */
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

		/* 동시성 */
		factory.setConcurrency(concurrency);

		return factory;
	}
}
