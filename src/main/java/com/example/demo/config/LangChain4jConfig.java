package com.example.demo.config;

import com.example.demo.model.Assistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

	// 1. Define your LLM Provider
	@Bean
	public ChatModel chatModel() {
		String apiKey = System.getenv("OPENAI_API_KEY");
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalStateException("OPENAI_API_KEY environment variable is not set. Please set it in launch.json or system environment.");
		}
		System.out.println("[DEBUG] OpenAI API Key loaded: " + (apiKey.length() > 10 ? apiKey.substring(0, 10) + "..." : "***"));
		return OpenAiChatModel.builder()
				.apiKey(apiKey)
				.modelName("gpt-4o-mini")
				.build();
	}

	// 2. Build the Assistant Service Bean
	@Bean
	public Assistant assistant(ChatModel chatModel) {
		return AiServices.builder(Assistant.class)
				.chatModel(chatModel)
				.build();
	}
}