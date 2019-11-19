package org.highwire.alert.sync.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KafkaMessageService {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessageToKafka(final String topic, final String messageKey, final String message) {
    log.debug(String.format("Sending msg=%s key=%s to Kafka topic=%s", message, messageKey, topic));
    kafkaTemplate.send(new Message<String>() {
      @Override
      public String getPayload() {
        return message;
      }

      @Override
      public MessageHeaders getHeaders() {
        Map<String, Object> props = new HashMap<>();
        props.put(KafkaHeaders.MESSAGE_KEY, messageKey);
        props.put(KafkaHeaders.TOPIC, topic);
        return new MessageHeaders(props);
      }
    });
    log.info(String.format("Message sent to topic:%s key:%s msg:%s ", topic, messageKey,
        message));
  }
}
