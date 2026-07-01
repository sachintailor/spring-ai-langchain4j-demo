package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final Assistant assistant;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
    private static final int REQUESTS_PER_MINUTE = 5; // Allow 1 request per minute
    private static final long RESET_INTERVAL_MS = 60000; // 1 minute

    // Spring autowires the LangChain4j proxy bean automatically (optional)
    public ChatController(@Autowired(required = false) Assistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/{message}")
    public String askAssistant(@PathVariable String message) {
        // Rate limiting: check if request limit exceeded
        long currentTime = System.currentTimeMillis();
        long timeSinceReset = currentTime - lastResetTime.get();

        // Reset counter if time window has passed
        if (timeSinceReset >= RESET_INTERVAL_MS) {
            requestCount.set(0);
            lastResetTime.set(currentTime);
        }

        // Check if request limit is exceeded
        if (requestCount.getAndIncrement() >= REQUESTS_PER_MINUTE) {
            long remainingTime = RESET_INTERVAL_MS - timeSinceReset;
            return "Rate limit exceeded. Maximum " + REQUESTS_PER_MINUTE + " request per minute allowed. Try again in " + remainingTime + "ms";
        }

        if (assistant == null) {
            return "Assistant not configured. Message received: " + message;
        }
        
        try {
            return assistant.chat(message);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("401")) {
                return "Error: OpenAI API authentication failed. Check if OPENAI_API_KEY is valid. Details: " + errorMsg;
            }
            return "Error calling assistant: " + errorMsg;
        }
    }
}
