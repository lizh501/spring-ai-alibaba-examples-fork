package com.alibaba.cloud.ai.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;

import java.util.Map;

/**
 * @author lizh501
 * @since 2025/7/24
 */
public class NameAssignerNode implements NodeAction {
    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String input = (String) state.value("user_input").orElse("");
        // 简单正则提取中文名，真实业务可用LLM+Tool/Regex/NER
        String name = null;
        if (input.startsWith("我叫")) {
            name = input.replaceAll("^我叫", "").trim();
        }
        if (name != null && !name.isEmpty()) {
            return Map.of("user_name", name);
        }
        return Map.of();
    }
}
