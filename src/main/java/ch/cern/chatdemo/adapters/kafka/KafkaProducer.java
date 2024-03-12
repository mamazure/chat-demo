package ch.cern.chatdemo.adapters.kafka;

import ch.cern.chatdemo.adapters.websocket.model.WebSocketMessage;
import ch.cern.chatdemo.application.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, WebSocketMessage> kafkaTemplate;

    public void sendToMessageTopic(WebSocketMessage message) {
        kafkaTemplate.send(KafkaConfig.MESSAGE_TOPIC, message.getToWhom(), message);
    }

}
