package com.sapo.botman;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotmanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotmanApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessage(MessageEvent<TextMessageContent> e) {
		System.out.println("event: " + e);
		TextMessageContent message = e.getMessage();
		return new TextMessage(message.getText());
	}
}

