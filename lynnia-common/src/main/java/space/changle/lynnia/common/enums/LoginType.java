package space.changle.lynnia.common.enums;

/**
 * @author 长乐
 * @version 1.0.0
 * @date 2026/3/1 00:20
 * @description
 */
public enum LoginType {
    TMA,

    WEB;

    public String authority() {
        return "CLIENT_" + this.name();
    }
}
