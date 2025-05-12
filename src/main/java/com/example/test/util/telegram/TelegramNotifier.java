package com.example.test.util.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelegramNotifier {

    @Value(value = "${telegram.bot.token}")
    private String telegramBotToken;

    // 发送消息方法，参数为 chat ID 和消息内容
    public void sendMessage(String chatId, String text) {
        log.info("消息发送机器人ID："+telegramBotToken);
        log.info("消息发送用户ID："+chatId);
        log.info("消息发送内容："+text);

        TelegramBot bot = new TelegramBot(telegramBotToken);
        SendMessage request = new SendMessage(chatId, text);
        SendResponse response = bot.execute(request);
        if (!response.isOk()) {
            log.error("消息发送失败: " + response.description());
        }
    }

    // 测试用的 main 方法
    public static void main(String[] args) {
        String botToken = "8148575408:AAGDms8LOyn_VFxExueSfM-cbfyC5a0uakw";  // 替换为您的 bot token
        String chatId = "7158942980";      // 替换为获取到的 chat ID
        TelegramNotifier notifier = new TelegramNotifier();
        notifier.sendMessage(chatId, "来自 Java 的消息推送！");
    }
}
