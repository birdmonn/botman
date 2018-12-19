package com.sapo.botman;

import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.storage.StorageProperties;
import com.sapo.botman.storage.StorageService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
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

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.init();
		};
	}
}

