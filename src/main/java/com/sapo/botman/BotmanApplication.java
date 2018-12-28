package com.sapo.botman;

import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BotmanApplication {
	@Autowired
	private QuestPokemonGoService questPokemonGoService;

	public static void main(String[] args) {
		SpringApplication.run(BotmanApplication.class, args);
	}

	@Bean
	InitializingBean initUserAdmin() {
		if (questPokemonGoService.findAll().isEmpty()) {
			questPokemonGoService.create(new QuestPokemonGo());
		}
		return null;
	}
}

