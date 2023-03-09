package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CanvasController {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private Canvas canvas;

    private String[] players = new String[] {"X", "O"};
    private int currentPlayer = 0;

    @MessageMapping("/paint")
    public void paint(PaintMessage paintMessage, SimpMessageHeaderAccessor headerAccessor) {
        String color = paintMessage.getColor();
        if (color.equals(players[currentPlayer])) {
            canvas.paint(paintMessage);
            currentPlayer = (currentPlayer + 1) % 2;
            template.convertAndSend("/topic/canvas", canvas);
        } else {
            String sessionId = headerAccessor.getSessionId();
            template.convertAndSendToUser(sessionId, "/queue/error", "It's not your turn");
        }
    }

    @SubscribeMapping("/canvas")
    public void sendInitialCanvas(SimpMessageHeaderAccessor headerAccessor) {
        String symbol = players[currentPlayer];
        headerAccessor.getSessionAttributes().put("symbol", symbol);
        currentPlayer = (currentPlayer + 1) % 2;
        template.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/symbol", symbol);
        template.convertAndSend("/topic/canvas", canvas);
    }

    @MessageMapping("/reset")
    public void reset() {
        canvas.reset();
        currentPlayer = 0;
        template.convertAndSend("/topic/canvas", canvas);
    }
}
