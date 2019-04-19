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
import com.sapo.botman.service.LeaveMemberService;
import com.sapo.botman.service.MemberJOBService;
import com.sapo.botman.service.QuestPokemonGoService;
import com.sapo.botman.utils.SplitString;
import com.sapo.botman.utils.StringToDate;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@LineMessageHandler
public class MainController {

    private LineMessagingClient lineMessagingClient;
    private MemberJOBService memberJOBService;
    private LeaveMemberService leaveMemberService;
    @Autowired
    public MainController(LineMessagingClient lineMessagingClient,
                          QuestPokemonGoService questPokemonGoService,
                          LeaveMemberService leaveMemberService,
                          MemberJOBService memberJOBService) {
        this.lineMessagingClient = lineMessagingClient;
        this.memberJOBService = memberJOBService;
        this.leaveMemberService = leaveMemberService;
    }

    @EventMapping
    public void handleTextMessage(MessageEvent<TextMessageContent> event) throws ParseException {
//        log.info(event.toString());
        System.out.println("event Stick :" + event.toString());
        System.out.println("Source :" + event.getSource().toString());
        System.out.println("Sender :" + event.getSource().getSenderId());
        System.out.println("user :" + event.getSource().getUserId());
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws ParseException {
        String[] text = SplitString.getInstance().stringContent(content.getText());
        switch (text[0].toLowerCase()) {
            case "profile":
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
            case "#leave":
                this.listLeaveMember(text,event,replyToken);
                break;
            case "#leavedate":
                this.memberLeaveDate(text,event,replyToken);
                break;
            case "#help":
                this.help(replyToken);
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

    private void regiMember(String[] content,Event event,String replyToken){
        if(content.length >= 2) {
            String userId = event.getSource().getUserId();
            String msgReplay = memberJOBService.regiMember(new MemberJOB(userId, content[1]));
            new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
        } else {
            new ReplayController(lineMessagingClient).replyText(replyToken, "command fail");
        }
    }

    private void listMember(String replyToken){
           String msgReplay = memberJOBService.getMemberList();
           new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
    }

    private void listLeaveMember(String[] content,Event event,String replyToken){
        if(content.length == 2){
            MemberJOB memberJOBFind = memberJOBService.findByName(content[1]);
            if (memberJOBFind != null){
                String msgReplay = leaveMemberService.getLeaveList(memberJOBFind.getId());
                new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
            } else {
                new ReplayController(lineMessagingClient).replyText(replyToken, "user not found");
            }
        } else {
            new ReplayController(lineMessagingClient).replyText(replyToken, "command fail");
        }
    }

    private void memberLeaveDate(String[] content,Event event,String replyToken) throws ParseException {
        if(content.length == 2 || content.length == 4){

            List<Date> dateLeave ;
            String msgReplay;
            if(content.length == 2){
                dateLeave = StringToDate.getInstance().dateA(content[1]);
                msgReplay = leaveMemberService.createLeaveDate(event.getSource().getUserId(),dateLeave);
                new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
            } else {
                dateLeave = StringToDate.getInstance().dateAToDateB(content[1],content[3]);
                msgReplay = leaveMemberService.createLeaveDate(event.getSource().getUserId(),dateLeave);
                new ReplayController(lineMessagingClient).replyText(replyToken, msgReplay);
            }

        } else {
            new ReplayController(lineMessagingClient).replyText(replyToken, "command fail");
        }
    }
    private void help(String replyToken){
        new ReplayController(lineMessagingClient).reply(replyToken, Arrays.asList(
                new TextMessage("ดูรายชื่อที่สมัคร : #listmember"),
                new TextMessage("สมัครใช้ระบบ : #regi (name)"),
                new TextMessage("บันทึกวันลา : #leavedate date"),
                new TextMessage("บันทึกวันลา : #leavedate datefrom - dateto"),
                new TextMessage("ดูวันลา : #leave (name)")
        ));
    }
}

