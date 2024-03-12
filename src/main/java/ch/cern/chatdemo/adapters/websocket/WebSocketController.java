package ch.cern.chatdemo.adapters.websocket;

import ch.cern.chatdemo.adapters.kafka.KafkaMessageConsumer;
import ch.cern.chatdemo.adapters.kafka.KafkaProducer;
import ch.cern.chatdemo.adapters.websocket.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final KafkaProducer kafkaProducer;
    private final WebSocketPublisher webSocketPublisher;

    @MessageMapping("/register")
    public void registerUser(Principal principal) {
        String webChatUsername = principal.getName();
        webSocketPublisher.notifyEveryoneTheNewUserJoined(webChatUsername);
        webSocketPublisher.sendToUserListOfLoggedInUsers(webChatUsername);
        if (KafkaMessageConsumer.messageStorage.containsKey(webChatUsername)) {
            for (var message : KafkaMessageConsumer.messageStorage.get(webChatUsername)) {
                webSocketPublisher.publishMessage(message);
            }
            KafkaMessageConsumer.messageStorage.remove(webChatUsername);
        }
    }

    @MessageMapping("/unregister")
    public void unregisterUser(Principal principal) {
        webSocketPublisher.notifyEveryoneTheUserLeftApp(principal.getName());
    }

    //    Incoming message from user's websocket
    @MessageMapping("/message")
    public void greeting(WebSocketMessage message, Principal principal) {
        message.setFromWho(principal.getName());
        kafkaProducer.sendToMessageTopic(message);
    }
}
