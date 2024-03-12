package ch.cern.chatdemo.adapters.websocket;

import ch.cern.chatdemo.adapters.websocket.model.WebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WebSocketPublisher {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    public void publishMessage(WebSocketMessage message) {
        simpMessagingTemplate.convertAndSendToUser(message.getToWhom(), "/msg", message);
    }

    public void notifyEveryoneTheNewUserJoined(String userName) {
        simpMessagingTemplate.convertAndSend("/topic/newMember", userName);
    }

    public void notifyEveryoneTheUserLeftApp(String userName) {
        simpMessagingTemplate.convertAndSend("/topic/disconnectedUser", userName);
    }

    public void sendToUserListOfLoggedInUsers(String userName) {
        Set<String> connectedUsers = simpUserRegistry.getUsers().stream().map(SimpUser::getName).collect(Collectors.toSet());
        simpMessagingTemplate.convertAndSendToUser(userName, "/queue/newMember", connectedUsers
                .stream().filter(user -> !user.equals(userName))
                .collect(Collectors.toSet()));
    }
}
