package com.alibaba.cloud.ai.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.Map;

/**
 * @author lizh501
 * @since 2025/7/24
 */
public class CustomLLMNode implements NodeAction {

    private final ChatClient chatClient;

    public CustomLLMNode(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String input = (String) state.value("user_input").orElse("");
        String userName = (String) state.value("user_name").orElse(null);

        // 只有在问“我叫什么名字”时特殊处理，否则走LLM
        if ((input.contains("我叫什么名字") || input.contains("我是谁")) && userName != null) {
            return Map.of("reply", "你的名字是：" + userName);
        }

        // 其它情况直接问 LLM
        ChatResponse reply = chatClient.prompt(input).call().chatResponse();
        return Map.of("reply", reply.getResult().getOutput());
    }
}
