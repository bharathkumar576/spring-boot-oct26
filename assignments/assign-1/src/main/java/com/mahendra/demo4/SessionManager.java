package com.mahendra.demo4;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {
    
    private Map<String, ShoppingCart> sessions;
    public SessionManager() {
        this.sessions = new HashMap<>();
    }

    public ShoppingCart getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public void createSession(String sessionId, ShoppingCart cart) {
        sessions.put(sessionId, cart);
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean sessionExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public ShoppingCart createOrGetSession(String sessionId) {
        return sessions.computeIfAbsent(sessionId, k -> new ShoppingCart());
    }
}
