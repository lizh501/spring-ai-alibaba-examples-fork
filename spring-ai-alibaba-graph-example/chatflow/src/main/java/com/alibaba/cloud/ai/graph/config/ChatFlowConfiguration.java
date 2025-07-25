package com.alibaba.cloud.ai.graph.config;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.node.CustomLLMNode;
import com.alibaba.cloud.ai.graph.node.NameAssignerNode;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * @author lizh501
 * @since 2025/7/24
 */
@Configuration
public class ChatFlowConfiguration {
    @Bean
    public StateGraph chatflowGraph(ChatClient.Builder chatClientBuilder) throws GraphStateException {

        ChatClient chatClient = chatClientBuilder.build();

        KeyStrategyFactory keyStrategyFactory = () -> {
            HashMap<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();

            keyStrategyHashMap.put("user_input", new ReplaceStrategy());
            keyStrategyHashMap.put("user_name", new ReplaceStrategy());
            return keyStrategyHashMap;
        };
        StateGraph graph = new StateGraph("chatFlow-demo", keyStrategyFactory)
                .addNode("assign", node_async(new NameAssignerNode()))
                .addNode("llm", node_async(new CustomLLMNode(chatClient)))
                .addEdge(START, "assign")
                .addEdge("assign", "llm")
                .addEdge("llm", END);

        return graph;
    }
}
