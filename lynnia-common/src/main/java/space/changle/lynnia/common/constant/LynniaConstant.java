package space.changle.lynnia.common.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/10 11:38
 * @description
 */
public class LynniaConstant {

    public static final ZoneId SYSTEM_ZONE = ZoneId.of("Asia/Shanghai");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");



}
