package space.changle.lynnia.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/4 18:20
 * @description
 */
public class UserUtils {


    public static String getUserName(String firstName, String lastName){
        if (StringUtils.isBlank(firstName)){
            return lastName;
        }
        if (StringUtils.isBlank(lastName)){
            return firstName;
        }
        return firstName + " " + lastName;
    }

    public static String generateCode() {
        StringBuilder codeBuilder = new StringBuilder(6);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 6; i++) {
            int digit = random.nextInt(10);
            codeBuilder.append(digit);
        }
        return codeBuilder.toString();
    }
}
