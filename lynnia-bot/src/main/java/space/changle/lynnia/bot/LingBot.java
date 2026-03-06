package space.changle.lynnia.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScope;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllGroupChats;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllPrivateChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import space.changle.lynnia.common.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/2/28 21:53
 * @description
 */
@Slf4j
@Component
public class LingBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Value("${telegram.token}")
    private String botToken;

    private final TelegramClient telegramClient;


    public LingBot(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        log.info("update: {}", JsonUtils.toJson(update));
    }

    @EventListener(ApplicationReadyEvent.class)
    private void setBotCommons()  {
        List<BotCommand> privateCommands = new ArrayList<>();
        List<BotCommand> groupCommands = new ArrayList<>();
        for (Command command : Command.values()) {
            if (command.getScope() == Command.Scope.PRIVATE) {
                privateCommands.add(command.toBotCommand());
            } else {
                groupCommands.add(command.toBotCommand());
            }
        }
        if (!privateCommands.isEmpty()) {
            registerCommands(BotCommandScopeAllPrivateChats.builder().build(), privateCommands);
        }
        if (!groupCommands.isEmpty()) {
            registerCommands(BotCommandScopeAllGroupChats.builder().build(), groupCommands);
        }

    }

    private void registerCommands(BotCommandScope scope, List<BotCommand> commands)  {
        try {
            telegramClient.execute(SetMyCommands.builder().scope(scope).commands(commands).build());
        } catch (TelegramApiException e) {
            log.error("注册命令失败", e);
        }
    }

}
