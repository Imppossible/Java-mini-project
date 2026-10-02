public class PasswordAnalyzer {
    private String password;

    // Constructor
    public PasswordAnalyzer(String password) {
        this.password = password;
    }

    // Encapsulation
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLength() {
        return password != null ? password.length() : 0;
    }
}