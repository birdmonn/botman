package com.sapo.botman.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.sapo.botman.model.MemberJOB;
import com.sapo.botman.service.MemberJOBService;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.utils.SplitString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.util.Arrays;

@LineMessageHandler
public class MainController {

    private LineMessagingClient lineMessagingClient;
    private MemberJOBService memberJOBService;
    @Autowired
    public MainController(LineMessagingClient lineMessagingClient,
                          QuestPokemonGoService questPokemonGoService,
                          MemberJOBService memberJOBService) {
        this.lineMessagingClient = lineMessagingClient;
        this.memberJOBService = memberJOBService;
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
        String[] text = SplitString.getInstance().stringContent(content.getText());
        switch (text[0]) {
            case "Profile":
                showProfile(replyToken, event);
                break;
            case "#listmember":
                this.listMember(replyToken);
                break;
            case "#quest2":
                new ReplayController(lineMessagingClient).reply(replyToken, new ImageMessage("asd", "sds"));
                break;
            case "#regi":
                this.regiMember(text,event,replyToken);
                break;
            default:
//                new ReplayController(lineMessagingClient).replyText(replyToken, text);
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





    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).toUriString();
    }

    private void regiMember(String[] content,Event event,String replyToken){
        if(content.length >= 2) {
            String userId = event.getSource().getUserId();
            String msgReplay = memberJOBService.regiMember(new MemberJOB(userId, content[1]));
            new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
        } {
            new ReplayController(lineMessagingClient).replyText(replyToken, "command fail");
        }
    }

    private void listMember(String replyToken){
           String msgReplay = memberJOBService.getMemberList();
           new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
    }
}

