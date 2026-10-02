import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordSecurityAnalyze extends JFrame {

    private JPasswordField txtPassword;   
    private JTextField txtVisiblePassword; 
    private JButton btnCheck;
    private JTextArea txtResult;

    public PasswordSecurityAnalyze() {
        setTitle("Password Security Analyzer");
        setSize(1020, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitle = new JLabel("Password Security Analyzer");
        lblTitle.setBounds(30, 20, 320, 20);
        add(lblTitle);

        JLabel lblPrompt = new JLabel("Enter Password:");
        lblPrompt.setBounds(30, 60, 380, 20);
        add(lblPrompt);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 90, 450, 35);
        add(txtPassword);

        txtVisiblePassword = new JTextField();
        txtVisiblePassword.setBounds(500, 90, 490, 35);
        add(txtVisiblePassword);

        // Sync two text boxes
        txtPassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { syncText(1); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { syncText(1); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { syncText(1); }
        });

        txtVisiblePassword.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { syncText(2); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { syncText(2); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { syncText(2); }
        });

        btnCheck = new JButton("CHECK PASSWORD SECURITY");
        btnCheck.setBounds(30, 140, 960, 40);
        add(btnCheck);

        txtResult = new JTextArea();
        txtResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResult);
        scrollPane.setBounds(30, 200, 960, 310);
        add(scrollPane);

        btnCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                PasswordAnalyzer analyzer = new PasswordAnalyzer(new String(txtPassword.getPassword()).trim());
                String password = analyzer.getPassword();

                if (password.isEmpty()) {
                    txtResult.setText("Please enter a password first!");
                    return;
                }

                boolean hasLower = false;
                boolean hasUpper = false;
                boolean hasDigit = false;
                boolean hasSymbol = false;
                int poolSize = 0;

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
                BaseSecurityChecker checker = new EntropySecurityChecker();
                boolean isCommon = checker.isCommonPassword(password);
                String commonMatchResult = isCommon ? "FOUND (Very Dangerous!)" : "NOT FOUND";
                String status = checker.evaluateSecurity(password);

                double combinations = Math.pow(poolSize, password.length());
                double guessesPerSecond = 10_000_000_000.0; 
                double secondsToCrack = combinations / guessesPerSecond;
                String timeText = formatCrackTime(secondsToCrack);

                StringBuilder sb = new StringBuilder();
                sb.append("=== SECURITY AUDIT REPORT ===\n");
                sb.append("Password: ").append(password).append("\n");
                sb.append("Length: ").append(analyzer.getLength()).append(" characters\n");
                sb.append(String.format("Shannon Entropy: %.2f bits\n", entropy));
                sb.append("Common Password Match: ").append(commonMatchResult).append("\n");
                sb.append("Estimated Brute-Force Time:\n ").append(timeText).append(" (").append(status).append(")\n\n");
                sb.append("--- Details ---\n");
                sb.append("- Lowercase: ").append(hasLower ? "Yes" : "No").append("\n");
                sb.append("- Uppercase: ").append(hasUpper ? "Yes" : "No").append("\n");
                sb.append("- Numbers: ").append(hasDigit ? "Yes" : "No").append("\n");
                sb.append("- Symbols: ").append(hasSymbol ? "Yes" : "No").append("\n");

                txtResult.setText(sb.toString());
            }
        });
    }

    private boolean isUpdating = false;
    private void syncText(int source) {
        if (isUpdating) return;
        isUpdating = true;
        if (source == 1) {
            String psw = new String(txtPassword.getPassword());
            if (!txtVisiblePassword.getText().equals(psw)) {
                txtVisiblePassword.setText(psw);
            }
        } else {
            String txt = txtVisiblePassword.getText();
            String psw = new String(txtPassword.getPassword());
            if (!psw.equals(txt)) {
                txtPassword.setText(txt);
            }
        }
        isUpdating = false;
    }

    private String formatCrackTime(double seconds) {
        if (seconds < 1) return "< 1 Second";
        if (seconds < 60) return String.format("~%.0f Seconds", seconds);
        if (seconds < 3600) return String.format("~%.0f Minutes", seconds / 60);
        if (seconds < 86400) return String.format("~%.0f Hours", seconds / 3600);
        if (seconds < 31536000) return String.format("~%.0f Days", seconds / 86400);
        
        double years = seconds / 31536000;
        if (years > 1_000_000_000) return "~Over 1 Billion Years";
        return String.format("~%,.0f Years", years);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PasswordSecurityAnalyze().setVisible(true);
            }
        });
    }
}