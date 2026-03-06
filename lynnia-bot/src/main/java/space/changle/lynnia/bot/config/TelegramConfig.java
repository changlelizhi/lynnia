package space.changle.lynnia.bot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 00:13
 * @description
 */
@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    private final TgProxyProperties tgProxyProperties;

    @Value("${telegram.token}")
    private  String token;

    @Bean(value = "okHttpClient")
    public OkHttpClient okHttpClient() {
        String host = tgProxyProperties.getHost();
        int port = tgProxyProperties.getPort();
        return new TelegramOkHttpClientFactory.SocksProxyOkHttpClientCreator(() ->
                new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port))).get();
    }

    @Bean(value = "telegramClient")
    public TelegramClient telegramClient(@Qualifier("okHttpClient") OkHttpClient okHttpClient) {
        return new OkHttpTelegramClient(okHttpClient, token);
    }

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication(@Qualifier("okHttpClient") OkHttpClient okHttpClient){
        return new TelegramBotsLongPollingApplication(ObjectMapper::new, () -> okHttpClient);
    }
}
