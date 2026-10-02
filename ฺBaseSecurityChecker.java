import java.util.Arrays;
import java.util.List;

public class BaseSecurityChecker {
    protected static final List<String> COMMON_PASSWORDS = Arrays.asList(
        "123456", "password", "123456789", "12345678", "12345", 
        "111111", "1234567", "sunshine", "qwerty", "iloveyou", 
        "admin", "welcome", "123123", "secret", "abc1234"
    );

    /
    public boolean isCommonPassword(String password) {
        return COMMON_PASSWORDS.contains(password);
    }


    public String evaluateSecurity(String password) {
        return "GENERAL";
    }
}