package ch.cern.chatdemo.adapters.kafka;

import ch.cern.chatdemo.adapters.websocket.WebSocketPublisher;
import ch.cern.chatdemo.adapters.websocket.model.WebSocketMessage;
import ch.cern.chatdemo.application.KafkaConfig;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaMessageConsumer {
    public static final Map<String, List<WebSocketMessage>> messageStorage = new HashMap<>();
    private final WebSocketPublisher webSocketPublisher;
    private final SimpUserRegistry simpUserRegistry;

    @KafkaListener(id = "consumerGroup", topics = KafkaConfig.MESSAGE_TOPIC)
    public void listen(ConsumerRecord<String, WebSocketMessage> consumerRecord,
                       Acknowledgment acknowledgment) {
        var message = consumerRecord.value();

        if (simpUserRegistry.getUser(message.getToWhom()) == null) {
            messageStorage.computeIfAbsent(message.getToWhom(), k -> new ArrayList<>()).add(message);
            acknowledgment.acknowledge();
            return;
        }
        webSocketPublisher.publishMessage(message);
        acknowledgment.acknowledge();
    }
}
