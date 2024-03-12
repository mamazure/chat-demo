package ch.cern.chatdemo.adapters.websocket.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WebSocketMessage {
    private String toWhom;
    private String fromWho;
    private String message;

}
