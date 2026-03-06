package space.changle.lynnia.bot;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 13:24
 * @description
 */
@Getter
public enum Command {


    START("/start", "🚀 开始使用", Scope.PRIVATE),

    CHINESE("/chinese", "🌐 中文", Scope.PRIVATE),

   VOTE_LOCK("/vote", "🔓 投票开锁", Scope.GROUP),

    JIAOLANG("/jiaolang", "⚡ 郊狼", Scope.GROUP);

    private final String command;

    private final String desc;

    private final Scope scope;

    Command(String command, String desc, Scope scope) {
        this.command = command;
        this.desc = desc;
        this.scope = scope;
    }


    public BotCommand toBotCommand() {
        return BotCommand.builder().command(command).description(desc).build();
    }
    public enum Scope {
        PRIVATE,
        GROUP
    }
}
