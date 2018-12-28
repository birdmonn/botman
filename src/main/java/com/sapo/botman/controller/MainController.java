package com.sapo.botman.controller;

import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.sapo.botman.config.ConfigGroup;
import com.sapo.botman.model.QuestPokemonGo;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.storage.StorageProperties;
import com.sapo.botman.storage.StorageService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class MainController {

    private LineMessagingClient lineMessagingClient;
    private StorageProperties properties;
    private QuestPokemonGoService questPokemonGoService;
    private StorageService storageService;

    @Autowired
    public MainController(StorageProperties properties,
                          LineMessagingClient lineMessagingClient,
                          QuestPokemonGoService questPokemonGoService,
                          StorageService storageService) {
        this.properties = properties;
        this.lineMessagingClient = lineMessagingClient;
        this.questPokemonGoService = questPokemonGoService;
        this.storageService = storageService;
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


    @EventMapping
    public void handleStickerMessage(MessageEvent<StickerMessageContent> event) {
        StickerMessageContent message = event.getMessage();
        if (!event.getSource().getSenderId().equals(ConfigGroup.GROUPID)) {
            new ReplayController(lineMessagingClient).reply(event.getReplyToken(), new StickerMessage(
                    message.getPackageId(), message.getStickerId()
            ));
        }
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) {
        String text = content.getText();
        switch (text) {
            case "Profile":
                showProfile(replyToken, event);
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

