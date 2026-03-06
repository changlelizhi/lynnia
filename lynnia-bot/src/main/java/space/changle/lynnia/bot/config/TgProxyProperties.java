package space.changle.lynnia.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 00:14
 * @description
 */
@Data
@ConfigurationProperties(prefix = "proxy")
public class TgProxyProperties {

    private String host;

    private int port;
}
