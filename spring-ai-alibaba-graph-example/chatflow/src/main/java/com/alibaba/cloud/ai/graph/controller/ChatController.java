package com.alibaba.cloud.ai.graph.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * @author lizh501
 * @since 2025/7/24
 */
@RestController
@RequestMapping("/demo")
public class ChatController {

    private final CompiledGraph compiledGraph;

    public ChatController(@Qualifier("chatflowGraph") StateGraph chatflowGraph) throws GraphStateException {
        this.compiledGraph = chatflowGraph.compile();
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest req) throws GraphRunnerException {
        String threadId = req.getSessionId();
        String userInput = req.getMessage();

        Map<String, Object> input = Map.of("user_input", userInput);
        Optional<OverAllState> result = compiledGraph.invoke(
                input,
                RunnableConfig.builder().threadId(threadId).build()
        );
        result.map(OverAllState::data).orElse(Map.of());
        OverAllState state = result.orElseThrow();
        AssistantMessage message = (AssistantMessage) state.value("reply").orElse("");
        String reply = message.getText();
        String name = (String) state.value("user_name").orElse(null);

        return ResponseEntity.ok(Map.of(
                "reply", reply,
                "user_name", name
        ));
    }

}
