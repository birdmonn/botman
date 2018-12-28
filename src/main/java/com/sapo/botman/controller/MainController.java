package com.sapo.botman.controller;

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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

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
        new ReplayController(lineMessagingClient).reply(replyToken, new ImageMessage(questPokemonGo.getUrl(), questPokemonGo.getUrl()));
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

