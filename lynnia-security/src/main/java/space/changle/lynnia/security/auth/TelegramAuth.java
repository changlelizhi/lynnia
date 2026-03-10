package space.changle.lynnia.security.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.changle.lynnia.common.enums.Result;
import space.changle.lynnia.common.exception.LynniaException;
import space.changle.lynnia.common.result.TmaUser;
import space.changle.lynnia.common.util.JsonUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/3 21:44
 * @description
 */
@Slf4j
@Component
public class TelegramAuth {
    private static final String ALGORITHM = "HmacSHA256";

    private static final String WEB_APP_DATA = "WebAppData";

    @Value("${telegram.token}")
    private String botToken;

    public boolean isValid(String tmaInitData) {
        if (StringUtils.isBlank(tmaInitData)) {
            throw new LynniaException(Result.AUTH_INIT_MISSING_PARAM);
        }
        Pair<String, String> result = parseInitData(tmaInitData);
        String hash = result.first();
        String initData = result.second();
        byte[] secretKey = new HmacUtils(ALGORITHM, WEB_APP_DATA).hmac(botToken);
        String initDataHash = new HmacUtils(ALGORITHM, secretKey).hmacHex(initData);
        return initDataHash.equals(hash);
    }

    public TmaUser getUser(String tmaInitData) {
        Map<String, String> initData = parseQueryString(tmaInitData);
        return JsonUtils.parse(initData.get("user"), TmaUser.class);
    }


    private Pair<String, String> parseInitData(String tmaInitData) {
        Map<String, String> initDataMap = parseQueryString(tmaInitData);
        String authDate = initDataMap.get("auth_date");
        if (StringUtils.isBlank(authDate)) {
            throw  new LynniaException(Result.AUTH_INIT_MISSING_PARAM);
        }
        Instant authInstant = Instant.ofEpochSecond(Long.parseLong(authDate));
        if (authInstant.isBefore(Instant.now().minus(2, ChronoUnit.HOURS))) {
            throw new LynniaException(Result.TOKEN_EXPIRED);
        }
        String hash = initDataMap.remove("hash");
        List<String> separatedData = initDataMap.entrySet().stream()
                .map((v) -> v.getKey() + "=" + v.getValue())
                .collect(Collectors.toList());
        return new Pair<>(hash, String.join("\n", separatedData));
    }

    private Map<String, String> parseQueryString(String tmaInitData) {
        Map<String, String> parameters = new TreeMap<>();
        String[] pairs = tmaInitData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8) : pair;
            String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8) : null;
            parameters.put(key, value);
        }
        return parameters;
    }
    private static Map<String, String> sortMap(Map<String, String> map) {
        return new TreeMap<>(map);
    }

    public record Pair<F, S>(F first, S second){

    }

}
