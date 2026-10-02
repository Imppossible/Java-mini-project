public class EntropySecurityChecker extends BaseSecurityChecker {
    @Override
    public String evaluateSecurity(String password) {
        if (password == null || password.isEmpty()) return "WEAK";
        
        if (isCommonPassword(password)) {
            return "FOUND (Very Dangerous!)";
        }
        
        int poolSize = 0;
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSymbol = false;
        
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= 'a' && c <= 'z') hasLower = true;
            else if (c >= 'A' && c <= 'Z') hasUpper = true;
            else if (c >= '0' && c <= '9') hasDigit = true;
            else hasSymbol = true;
        }

        if (hasLower) poolSize += 26;
        if (hasUpper) poolSize += 26;
        if (hasDigit) poolSize += 10;
        if (hasSymbol) poolSize += 32;

        double entropy = 0.0;
        if (poolSize > 0) {
            entropy = password.length() * (Math.log(poolSize) / Math.log(2));
        }

        if (entropy >= 64) {
            return "VERY SECURE";
        } else if (entropy >= 40) {
            return "MODERATE";
        } else {
            return "WEAK";
        }
    }
}