package space.changle.lynnia.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import space.changle.lynnia.common.enums.LoginType;
import space.changle.lynnia.security.enums.TokenType;

import java.util.Map;
import java.util.Set;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 00:44
 * @description 配置类
 */

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * 签发者
     */
    private String issuer;

    private Token access;

    private Token refresh;

    private Map<LoginType, Client> client;

    @Setter
    @Getter
    public static class Token {

        /**
         * 密钥
         */
        private String secret;

        /**
         * 过期时间
         */
        private long expireSeconds;
    }

    @Setter
    @Getter
    public static class Client {

        /**
         * issue-tokens: [access, refresh]
         */
        private Set<TokenType> issueTokens;

        public boolean canIssue(TokenType tokenType) {
            return issueTokens.contains(tokenType);
        }

    }

    public Client getClient(LoginType type) {

        Client clientType = client.get(type);
        if (clientType == null) {
            throw new IllegalArgumentException("Unsupported login type: " + type);
        }
        return clientType;
    }

}
