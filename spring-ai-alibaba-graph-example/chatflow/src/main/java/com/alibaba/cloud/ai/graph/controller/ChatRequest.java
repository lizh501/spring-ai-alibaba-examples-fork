package com.alibaba.cloud.ai.graph.controller;

/**
 * @author lizh501
 * @since 2025/7/24
 */
public class ChatRequest {
    private String sessionId;
    private String message;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
