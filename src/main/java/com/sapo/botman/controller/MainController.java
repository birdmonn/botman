package com.sapo.botman.controller;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@LineMessageHandler
public class MainController {

    private static StorageProperties properties;
    private LineMessagingClient lineMessagingClient;
    private QuestPokemonGoService questPokemonGoService;

    @Autowired
    public MainController(LineMessagingClient lineMessagingClient,
                          StorageProperties properties,
                          QuestPokemonGoService questPokemonGoService) {
        this.lineMessagingClient = lineMessagingClient;
        this.questPokemonGoService = questPokemonGoService;
        this.properties = properties;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) {
//        log.info(event.toString());
        System.out.println("event Stick :" + event.toString());
        System.out.println("Source :" + event.getSource().toString());
        System.out.println("Sender :" + event.getSource().getSenderId());
        System.out.println("user :" + event.getSource().getUserId());
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();
        switch (text) {
            case "Profile":
                showProfile(replyToken, event);
                break;
            case "#quest":
                showQuestPokemon(replyToken);
                break;
            case "#quest2":
                new ReplayController(lineMessagingClient).reply(replyToken, new ImageMessage("asd", "sds"));
                break;
            default:
                new ReplayController(lineMessagingClient).replyText(replyToken, text);
        }
    }


    private void showProfile(String replyToken, Event event) {
        String userId = event.getSource().getUserId();
        if (userId != null) {
            lineMessagingClient.getProfile(userId)
                    .whenComplete((profile, throwable) -> {
                        if (throwable != null) {
                            new ReplayController(lineMessagingClient).replyText(replyToken, throwable.getMessage());
                            return;
                        }
                        new ReplayController(lineMessagingClient).reply(replyToken, Arrays.asList(
                                new TextMessage("Display name: " + profile.getDisplayName()),
                                new TextMessage("Status message: " + profile.getStatusMessage()),
                                new TextMessage("User ID: " + profile.getUserId())
                        ));
                    });
        }
    }

    private void showQuestPokemon(String replyToken) {
        QuestPokemonGo questPokemonGo = questPokemonGoService.findAll().get(0);
        QuestPokemonGo jpg = saveContent(questPokemonGo);
        QuestPokemonGo previewImage = createTempFile(questPokemonGo);
        system("convert", "-resize", "240x",
                jpg.getPath(),
                previewImage.getPath());
        System.out.println("url : " + questPokemonGo.getUrl());
        System.out.println("Path : " + questPokemonGo.getPath());
        new ReplayController(lineMessagingClient).reply(replyToken, new ImageMessage(questPokemonGo.getUrl(), questPokemonGo.getUrl()));
    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static QuestPokemonGo saveContent(QuestPokemonGo questPokemonGo){
        QuestPokemonGo tempFile = createTempFile(questPokemonGo);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(questPokemonGo.getPath()))) {
            ByteStreams.copy(new BufferedInputStream(new FileInputStream(questPokemonGo.getPath())), outputStream);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static QuestPokemonGo createTempFile(QuestPokemonGo questPokemonGo) {
        String fileName = "downloadsquest"
                + ".jpg";
        Path tempFile = Paths.get(properties.getLocation() + "/" + fileName);
        tempFile.toFile().deleteOnExit();
        return new QuestPokemonGo(tempFile.toString(), createUri(questPokemonGo.getPath()));

    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toUriString();
    }
}

