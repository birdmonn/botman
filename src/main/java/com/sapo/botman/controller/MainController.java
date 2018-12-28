package com.sapo.botman.controller;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class MainController {

    private LineMessagingClient lineMessagingClient;
    private QuestPokemonGoService questPokemonGoService;

    @Autowired
    public MainController(LineMessagingClient lineMessagingClient,
                          QuestPokemonGoService questPokemonGoService) {
        this.lineMessagingClient = lineMessagingClient;
        this.questPokemonGoService = questPokemonGoService;
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

//
//    @EventMapping
//    public void handleStickerMessage(MessageEvent<StickerMessageContent> event) {
//        StickerMessageContent message = event.getMessage();
//        if (!event.getSource().getSenderId().equals(ConfigGroup.GROUPID)) {
//            new ReplayController(lineMessagingClient).reply(event.getReplyToken(), new StickerMessage(
//                    message.getPackageId(), message.getStickerId()
//            ));
//        }
//    }

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
        try {
            QuestPokemonGo questPokemonGo = questPokemonGoService.findAll().get(0);
            QuestPokemonGo jpg = saveContent(questPokemonGo);
            QuestPokemonGo previewImage = createTempFile(questPokemonGo);
            system("convert", "-resize", "240x",
                    jpg.getPath(),
                    previewImage.getPath());
            System.out.println("url : " + questPokemonGo.getUrl());
            System.out.println("Path : " + questPokemonGo.getPath());
            new ReplayController(lineMessagingClient).reply(replyToken, new ImageMessage(questPokemonGo.getUrl(), questPokemonGo.getUrl()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    //
    private static QuestPokemonGo saveContent(QuestPokemonGo questPokemonGo) throws FileNotFoundException {
        QuestPokemonGo tempFile = createTempFile(questPokemonGo);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(questPokemonGo.getPath()))) {
            ByteStreams.copy(new BufferedInputStream(new FileInputStream(questPokemonGo.getPath())), outputStream);
//            log.info("Save {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //
    private static QuestPokemonGo createTempFile(QuestPokemonGo questPokemonGo) {
        Path tempFile = Paths.get(questPokemonGo.getPath());
        tempFile.toFile().deleteOnExit();
        return new QuestPokemonGo(tempFile.toString(), createUri(questPokemonGo.getPath()));

    }

    //
    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toUriString();
    }
//    @EventMapping
//    public void handleImageMessage(MessageEvent<ImageMessageContent> event) {
////        log.info(event.toString());
//        ImageMessageContent content = event.getMessage();
//        String replyToken = event.getReplyToken();
//
//        try {
//            MessageContentResponse response = lineMessagingClient.getMessageContent(
//                    content.getId()).get();
//            QuestPokemonGo jpg = saveContent("jpg", response);
//            QuestPokemonGo previewImage = createTempFile("jpg");
//            saveImageToDb(jpg);
//            system("convert", "-resize", "240x",
//                    jpg.getPath(),
//                    previewImage.getPath());
//
//            reply(replyToken, new ImageMessage(jpg.getUrl(), previewImage.getUrl()));
//
//        } catch (InterruptedException | ExecutionException e) {
//            reply(replyToken, new TextMessage("Cannot get image: " + content));
//            throw new RuntimeException(e);
//        }
//
//    }
}

