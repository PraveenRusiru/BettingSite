package org.exampl.backend.utill;



import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
 @Component
public class SendMailUtill {
    public boolean sendEmailWithGmail( String to, String subject, String body,boolean isHtml) {
        String PASSWORD="jqjj udvn onus frmu";
        String from="praveenrusiru752@gmail.com";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.starttls.enable", "true");

        props.put("mail.smtp.host", "smtp.gmail.com");

        props.put("mail.smtp.port", "587");

        Session session=Session.getInstance(props,new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from,PASSWORD);
            }
        });

        try {
            // Create a new MimeMessage object to represent the email message.
            Message message = new MimeMessage(session);

            // Set the sender's email address using the `from` parameter.
            message.setFrom(new InternetAddress(from));

            // Set the recipient(s) of the email. The `to` parameter is parsed to handle multiple recipients, if necessary.
            // `Message.RecipientType.TO` defines that this is the primary recipient (To field).
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set the subject of the email using the `subject` parameter.
            message.setSubject(subject);

            if (isHtml) {
                // Set the email content as HTML
                message.setContent(body, "text/html");
            } else {
                // Set the email content as plain text
                message.setText(body);
            }

            // Send the email message using the `Transport.send()` method.
            // This sends the email through the SMTP server configured in the session.
            Transport.send(message);
            return true;

//            new Alert(Alert.AlertType.INFORMATION, "Email sent successfully!").show();
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
//            new Alert(Alert.AlertType.ERROR, "Failed to send email.").show();
        }
        //props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }
     public boolean sendWelcomeEmail( String to, String username) {
         String subject = "Welcome to CricketBet! 🎉";

         // Generate the HTML email content using the template
         String htmlBody = generateWelcomeEmailTemplate(username);

         return sendEmailWithGmail( to, subject, htmlBody, true);
     }
     // 2. Password Reset Email
     public boolean sendPasswordResetEmail(String to, String username, String resetToken) {
         String subject = "Reset Your " + "CricketBet" + " Password";
         String resetLink = "APP_URL" + "/reset-password?token=" + resetToken;
         String htmlBody = generatePasswordResetTemplate(username, resetLink);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 3. Two-Factor Authentication Enabled Email
     public boolean send2FAEnabledEmail(String to, String username) {
         String subject = "Two-Factor Authentication Enabled 🔒";
         String htmlBody = generate2FAEnabledTemplate(username);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 4. Two-Factor Authentication Disabled Email
     public boolean send2FADisabledEmail(String to, String username) {
         String subject = "Two-Factor Authentication Disabled";
         String htmlBody = generate2FADisabledTemplate(username);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 5. Login Alert Email
     public boolean sendLoginAlertEmail(String to, String username, String device, String location, String time) {
         String subject = "New Login to Your " + "CricketBet" + " Account";
         String htmlBody = generateLoginAlertTemplate(username, device, location, time);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 6. Bet Confirmation Email
     public boolean sendBetConfirmationEmail(String to, String username, String match, String betType,
                                             String amount, String potentialWin, String betId) {
         String subject = "Your Bet on " + match + " is Confirmed!";
         String htmlBody = generateBetConfirmationTemplate(username, match, betType, amount, potentialWin, betId);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 7. Withdrawal Request Email
     public boolean sendWithdrawalRequestEmail(String to, String username, String amount, String method) {
         String subject = "Withdrawal Request Received";
         String htmlBody = generateWithdrawalRequestTemplate(username, amount, method);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 8. Deposit Confirmation Email
     public boolean sendDepositConfirmationEmail(String to, String username, String amount, String method) {
         String subject = "Deposit Received - Your " + "CricketBet" + " Account";
         String htmlBody = generateDepositConfirmationTemplate(username, amount, method);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 9. Account Verification Email
     public boolean sendAccountVerificationEmail(String to, String username, String verificationLink) {
         String subject = "Verify Your " + "CricketBet" + " Account";
         String htmlBody = generateAccountVerificationTemplate(username, verificationLink);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }

     // 10. Promotional Email
     public boolean sendPromotionalEmail(String to, String username, String promotionTitle, String promotionDetails) {
         String subject = "Special Promotion: " + promotionTitle;
         String htmlBody = generatePromotionalTemplate(username, promotionTitle, promotionDetails);
         return sendEmailWithGmail(to, subject, htmlBody, true);
     }
     // Method to generate the welcome email HTML template
     private String generateWelcomeEmailTemplate(String username) {
         return "<!DOCTYPE html>\n" +
                 "<html lang=\"en\">\n" +
                 "<head>\n" +
                 "    <meta charset=\"UTF-8\">\n" +
                 "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                 "    <title>Welcome to CricketBet</title>\n" +
                 "    <link href='https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap' rel='stylesheet'>\n" +
                 "    <style>\n" +
                 "        /* Reset styles for email compatibility */\n" +
                 "        body, table, td, p, a { \n" +
                 "            -webkit-text-size-adjust: 100%; \n" +
                 "            -ms-text-size-adjust: 100%; \n" +
                 "            margin: 0;\n" +
                 "            padding: 0;\n" +
                 "        }\n" +
                 "        table, td { \n" +
                 "            mso-table-lspace: 0pt; \n" +
                 "            mso-table-rspace: 0pt; \n" +
                 "            border-collapse: collapse; \n" +
                 "        }\n" +
                 "        img { \n" +
                 "            -ms-interpolation-mode: bicubic; \n" +
                 "            border: 0; \n" +
                 "            height: auto; \n" +
                 "            line-height: 100%; \n" +
                 "            outline: none; \n" +
                 "            text-decoration: none; \n" +
                 "            display: block;\n" +
                 "        }\n" +
                 "        /* Main styles */\n" +
                 "        body {\n" +
                 "            background-color: #f8fafc;\n" +
                 "            color: #334155;\n" +
                 "            font-family: 'Inter', Arial, sans-serif;\n" +
                 "            line-height: 1.6;\n" +
                 "        }\n" +
                 "        .container {\n" +
                 "            max-width: 600px;\n" +
                 "            margin: 0 auto;\n" +
                 "            width: 100%;\n" +
                 "        }\n" +
                 "        .email-wrapper {\n" +
                 "            background-color: #f8fafc;\n" +
                 "            padding: 20px 0;\n" +
                 "            width: 100%;\n" +
                 "        }\n" +
                 "        .email-card {\n" +
                 "            background-color: #ffffff;\n" +
                 "            border-radius: 12px;\n" +
                 "            overflow: hidden;\n" +
                 "            box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);\n" +
                 "            margin: 0 auto;\n" +
                 "            width: 100%;\n" +
                 "        }\n" +
                 "        .email-header {\n" +
                 "            background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);\n" +
                 "            color: white;\n" +
                 "            padding: 40px 30px;\n" +
                 "            text-align: center;\n" +
                 "        }\n" +
                 "        .logo {\n" +
                 "            font-size: 28px;\n" +
                 "            font-weight: 800;\n" +
                 "            color: white;\n" +
                 "            margin-bottom: 15px;\n" +
                 "            display: inline-block;\n" +
                 "        }\n" +
                 "        .logo-accent {\n" +
                 "            color: #fcd34d;\n" +
                 "        }\n" +
                 "        .email-body {\n" +
                 "            padding: 40px 30px;\n" +
                 "        }\n" +
                 "        h1 {\n" +
                 "            color: #1e40af;\n" +
                 "            font-size: 28px;\n" +
                 "            font-weight: 700;\n" +
                 "            margin-top: 0;\n" +
                 "            margin-bottom: 20px;\n" +
                 "        }\n" +
                 "        h2 {\n" +
                 "            color: #1e3a8a;\n" +
                 "            font-size: 20px;\n" +
                 "            font-weight: 600;\n" +
                 "            margin-top: 30px;\n" +
                 "            margin-bottom: 15px;\n" +
                 "        }\n" +
                 "        p {\n" +
                 "            line-height: 1.6;\n" +
                 "            margin-bottom: 20px;\n" +
                 "            font-size: 16px;\n" +
                 "            color: #475569;\n" +
                 "        }\n" +
                 "        .highlight-box {\n" +
                 "            background-color: rgba(59, 130, 246, 0.08);\n" +
                 "            border-left: 4px solid #3b82f6;\n" +
                 "            padding: 20px;\n" +
                 "            border-radius: 4px;\n" +
                 "            margin: 25px 0;\n" +
                 "        }\n" +
                 "        .cta-button {\n" +
                 "            display: inline-block;\n" +
                 "            background: linear-gradient(to right, #1d4ed8, #3b82f6);\n" +
                 "            color: white;\n" +
                 "            text-decoration: none;\n" +
                 "            padding: 16px 32px;\n" +
                 "            border-radius: 8px;\n" +
                 "            text-align: center;\n" +
                 "            font-weight: 600;\n" +
                 "            margin: 30px auto;\n" +
                 "            font-size: 16px;\n" +
                 "            box-shadow: 0 4px 6px rgba(29, 78, 216, 0.18);\n" +
                 "            border: none;\n" +
                 "            cursor: pointer;\n" +
                 "        }\n" +
                 "        .features-grid {\n" +
                 "            display: grid;\n" +
                 "            grid-template-columns: repeat(2, 1fr);\n" +
                 "            gap: 20px;\n" +
                 "            margin: 30px 0;\n" +
                 "        }\n" +
                 "        .feature-item {\n" +
                 "            background-color: #f8fafc;\n" +
                 "            border-radius: 8px;\n" +
                 "            padding: 20px;\n" +
                 "            text-align: center;\n" +
                 "            transition: transform 0.2s, box-shadow 0.2s;\n" +
                 "        }\n" +
                 "        .feature-item:hover {\n" +
                 "            transform: translateY(-3px);\n" +
                 "            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.05);\n" +
                 "        }\n" +
                 "        .feature-icon {\n" +
                 "            background-color: #3b82f6;\n" +
                 "            color: white;\n" +
                 "            width: 60px;\n" +
                 "            height: 60px;\n" +
                 "            border-radius: 50%;\n" +
                 "            display: flex;\n" +
                 "            align-items: center;\n" +
                 "            justify-content: center;\n" +
                 "            margin: 0 auto 15px;\n" +
                 "            font-size: 20px;\n" +
                 "            box-shadow: 0 6px 12px rgba(67, 97, 238, 0.2);\n"+
                 "        }\n" +
                 "        .feature-text {\n" +
                 "            font-weight: 500;\n" +
                 "            color: #1e293b;\n" +
                 "        }\n" +
                 "        .email-footer {\n" +
                 "            background-color: #f1f5f9;\n" +
                 "            padding: 30px;\n" +
                 "            text-align: center;\n" +
                 "            font-size: 14px;\n" +
                 "            color: #64748b;\n" +
                 "        }\n" +
                 "        .social-links {\n" +
                 "            margin: 20px 0;\n" +
                 "        }\n" +
                 "        .social-links a {\n" +
                 "            display: inline-block;\n" +
                 "            margin: 0 12px;\n" +
                 "            color: #3b82f6;\n" +
                 "            text-decoration: none;\n" +
                 "            font-weight: 500;\n" +
                 "        }\n" +
                 "        .unsubscribe {\n" +
                 "            color: #94a3b8;\n" +
                 "            font-size: 13px;\n" +
                 "            margin-top: 20px;\n" +
                 "        }\n" +
                 "        .divider {\n" +
                 "            height: 1px;\n" +
                 "            background-color: #e2e8f0;\n" +
                 "            margin: 30px 0;\n" +
                 "        }\n" +
                 "        /* Responsive styles */\n" +
                 "        @media screen and (max-width: 600px) {\n" +
                 "            .email-body {\n" +
                 "                padding: 30px 20px;\n" +
                 "            }\n" +
                 "            .email-header {\n" +
                 "                padding: 30px 20px;\n" +
                 "            }\n" +
                 "            h1 {\n" +
                 "                font-size: 24px;\n" +
                 "            }\n" +
                 "            .features-grid {\n" +
                 "                grid-template-columns: 1fr;\n" +
                 "            }\n" +
                 "            .cta-button {\n" +
                 "                padding: 14px 25px;\n" +
                 "                font-size: 15px;\n" +
                 "            }\n" +
                 "        }\n" +
                 "    </style>\n" +
                 "</head>\n" +
                 "<body>\n" +
                 "    <table role=\"presentation\" class=\"email-wrapper\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                 "        <tr>\n" +
                 "            <td align=\"center\">\n" +
                 "                <table role=\"presentation\" class=\"container\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                 "                    <tr>\n" +
                 "                        <td>\n" +
                 "                            <!-- Email Card -->\n" +
                 "                            <table role=\"presentation\" class=\"email-card\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                 "                                <!-- Header -->\n" +
                 "                                <tr>\n" +
                 "                                    <td class=\"email-header\">\n" +
                 "                                        <div class=\"logo\">Cricket<span class=\"logo-accent\">Bet</span></div>\n" +
                 "                                        <h1 style=\"color: white; margin-bottom: 0;\">Welcome to the Team! 🎉</h1>\n" +
                 "                                    </td>\n" +
                 "                                </tr>\n" +
                 "                                \n" +
                 "                                <!-- Body -->\n" +
                 "                                <tr>\n" +
                 "                                    <td class=\"email-body\">\n" +
                 "                                        <p>Hello <strong>" + username + "</strong>,</p>\n" +
                 "                                        \n" +
                 "                                        <p>We're delighted to welcome you to <strong>CricketBet</strong>, where cricket enthusiasts come together for an unparalleled betting experience. Get ready to enjoy competitive odds, live match action, and seamless transactions.</p>\n" +
                 "                                        \n" +
                 "                                        <div class=\"highlight-box\">\n" +
                 "                                            <p style=\"margin: 0; color: #1e40af; font-weight: 500;\">Your account has been successfully activated! You can now deposit funds, place bets on live matches, and withdraw your winnings with ease.</p>\n" +
                 "                                        </div>\n" +
                 "                                        \n" +
                 "                                        <h2>Premium Features</h2>\n" +
                 "                                        \n" +
                 "                                        <div class=\"features-grid\">\n" +
                 "                                            <div class=\"feature-item\">\n" +
                 "                                                <div class=\"feature-icon\">"+""+"</div>\n" +
                 "                                                <div class=\"feature-text\">Live Match Betting</div>\n" +
                 "                                            </div>\n" +
                 "                                            \n" +
                 "                                            <div class=\"feature-item\">\n" +
                 "                                                <div class=\"feature-icon\">"+""+"</div>\n" +
                 "                                                <div class=\"feature-text\">Instant Withdrawals</div>\n" +
                 "                                            </div>\n" +
                 "                                            \n" +
                 "                                            <div class=\"feature-item\">\n" +
                 "                                                <div class=\"feature-icon\">"+"🛡"+"</div>\n" +
                 "                                                <div class=\"feature-text\">Bank-Level Security</div>\n" +
                 "                                            </div>\n" +
                 "                                            \n" +
                 "                                            <div class=\"feature-item\">\n" +
                 "                                                <div class=\"feature-icon\">"+"📊"+"</div>\n" +
                 "                                                <div class=\"feature-text\">Real-Time Statistics</div>\n" +
                 "                                            </div>\n" +
                 "                                        </div>\n" +
                 "                                        \n" +
                 "                                        <div style=\"text-align: center;\">\n" +
                 "                                            <a href=\"https://cricketbet.com/dashboard\" class=\"cta-button\">Explore Dashboard</a>\n" +
                 "                                        </div>\n" +
                 "                                        \n" +
                 "                                        <div class=\"divider\"></div>\n" +
                 "                                        \n" +
                 "                                        <h2>Get Started</h2>\n" +
                 "                                        <p>Verify your account to unlock full access to all features and start placing bets on your favorite cricket matches.</p>\n" +
                 "                                        \n" +
                 "                                        <p>Need assistance? Our support team is available 24/7 at <a href=\"mailto:support@cricketbet.com\" style=\"color: #3b82f6;\">support@cricketbet.com</a> or via live chat.</p>\n" +
                 "                                        \n" +
                 "                                        <p>Best regards,<br>\n" +
                 "                                        <strong>The CricketBet Team</strong></p>\n" +
                 "                                    </td>\n" +
                 "                                </tr>\n" +
                 "                                \n" +
                 "                                <!-- Footer -->\n" +
                 "                                <tr>\n" +
                 "                                    <td class=\"email-footer\">\n" +
                 "                                        <div class=\"social-links\">\n" +
                 "                                            <a href=\"https://twitter.com/cricketbet\" target=\"_blank\">Twitter</a>\n" +
                 "                                            <a href=\"https://facebook.com/cricketbet\" target=\"_blank\">Facebook</a>\n" +
                 "                                            <a href=\"https://instagram.com/cricketbet\" target=\"_blank\">Instagram</a>\n" +
                 "                                        </div>\n" +
                 "                                        \n" +
                 "                                        <p>CricketBet Ltd • 123 Cricket Avenue • Sports City</p>\n" +
                 "                                        \n" +
                 "                                        <p>© 2023 CricketBet. All rights reserved.</p>\n" +
                 "                                        \n" +
                 "                                        <p class=\"unsubscribe\">\n" +
                 "                                            You're receiving this email because you created an account with CricketBet.<br>\n" +
                 "                                            <a href=\"#\" style=\"color: #94a3b8;\">Unsubscribe</a> or <a href=\"#\" style=\"color: #94a3b8;\">manage preferences</a>\n" +
                 "                                        </p>\n" +
                 "                                    </td>\n" +
                 "                                </tr>\n" +
                 "                            </table>\n" +
                 "                        </td>\n" +
                 "                    </tr>\n" +
                 "                </table>\n" +
                 "            </td>\n" +
                 "        </tr>\n" +
                 "    </table>\n" +
                 "</body>\n" +
                 "</html>";
     }



 private String generatePasswordResetTemplate(String username, String resetLink) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Password Reset Request</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>We received a request to reset your password. Click the button below to create a new password:</p>" +
             "<a href='" + resetLink + "' class='cta-button'>Reset Password</a>" +
             "<p>This link will expire in 1 hour for security reasons.</p>" +
             "<p>If you didn't request a password reset, please ignore this email or contact support.</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }

 private String generate2FAEnabledTemplate(String username) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Two-Factor Authentication Enabled 🔒</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>Two-factor authentication has been successfully enabled on your account.</p>" +
             "<div class='highlight'>" +
             "<p>Your account is now more secure. You'll need to enter a verification code each time you log in.</p>" +
             "</div>" +
             "<p>If you did not enable this feature, please contact our support team immediately.</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }

 private String generate2FADisabledTemplate(String username) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Two-Factor Authentication Disabled</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>Two-factor authentication has been disabled on your account.</p>" +
             "<div class='highlight'>" +
             "<p>Your account security has been reduced. We recommend keeping 2FA enabled for maximum protection.</p>" +
             "</div>" +
             "<p>If you did not disable this feature, please contact our support team immediately.</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }

     private String generateLoginAlertTemplate(String username, String device, String location, String time) {
         return "<!DOCTYPE html>" +
                 "<html lang=\"en\">" +
                 "<head>" +
                 "    <meta charset=\"UTF-8\">" +
                 "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                 "    <title>Login Alert - CricketBet</title>" +
                 "    <style>" +
                 "        /* Reset styles for email compatibility */" +
                 "        body, table, td, a {" +
                 "            -webkit-text-size-adjust: 100%;" +
                 "            -ms-text-size-adjust: 100%;" +
                 "        }" +
                 "        table, td {" +
                 "            mso-table-lspace: 0pt;" +
                 "            mso-table-rspace: 0pt;" +
                 "            border-collapse: collapse;" +
                 "        }" +
                 "        img {" +
                 "            -ms-interpolation-mode: bicubic;" +
                 "            border: 0;" +
                 "            height: auto;" +
                 "            line-height: 100%;" +
                 "            outline: none;" +
                 "            text-decoration: none;" +
                 "        }" +
                 "        .container {" +
                 "            max-width: 600px;" +
                 "            margin: 0 auto;" +
                 "            font-family: 'Poppins', Arial, sans-serif;" +
                 "        }" +
                 "        /* Main styles */" +
                 "        body {" +
                 "            margin: 0;" +
                 "            padding: 0;" +
                 "            background-color: #f1f5f9;" +
                 "            color: #0f172a;" +
                 "            font-family: 'Poppins', Arial, sans-serif;" +
                 "        }" +
                 "        .email-container {" +
                 "            width: 100%;" +
                 "            background-color: #f1f5f9;" +
                 "            padding: 20px 0;" +
                 "        }" +
                 "        .email-content {" +
                 "            background-color: #ffffff;" +
                 "            border-radius: 16px;" +
                 "            overflow: hidden;" +
                 "            box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);" +
                 "            margin: 0 auto;" +
                 "            width: 100%;" +
                 "            max-width: 600px;" +
                 "        }" +
                 "        .email-header {" +
                 "            background: linear-gradient(135deg, #4361ee 0%, #3a0ca3 100%);" +
                 "            color: white;" +
                 "            padding: 30px 20px;" +
                 "            text-align: center;" +
                 "        }" +
                 "        .logo {" +
                 "            font-size: 28px;" +
                 "            font-weight: 800;" +
                 "            display: inline-block;" +
                 "            background: linear-gradient(135deg, #4361ee 0%, #f72585 100%);" +
                 "            -webkit-background-clip: text;" +
                 "            -webkit-text-fill-color: transparent;" +
                 "            margin-bottom: 15px;" +
                 "        }" +
                 "        .logo img {" +
                 "            vertical-align: middle;" +
                 "            margin-right: 10px;" +
                 "        }" +
                 "        .email-body {" +
                 "            padding: 30px;" +
                 "        }" +
                 "        h1 {" +
                 "            color: #4361ee;" +
                 "            font-size: 28px;" +
                 "            margin-top: 0;" +
                 "            margin-bottom: 20px;" +
                 "        }" +
                 "        h2 {" +
                 "            color: #3a0ca3;" +
                 "            font-size: 22px;" +
                 "            margin-top: 25px;" +
                 "            margin-bottom: 15px;" +
                 "        }" +
                 "        p {" +
                 "            line-height: 1.6;" +
                 "            margin-bottom: 20px;" +
                 "            font-size: 16px;" +
                 "        }" +
                 "        .highlight {" +
                 "            background-color: rgba(67, 97, 238, 0.1);" +
                 "            border-left: 4px solid #4361ee;" +
                 "            padding: 15px;" +
                 "            border-radius: 4px;" +
                 "            margin: 20px 0;" +
                 "        }" +
                 "        .cta-button {" +
                 "            display: block;" +
                 "            width: 100%;" +
                 "            max-width: 250px;" +
                 "            background: linear-gradient(135deg, #4361ee 0%, #f72585 100%);" +
                 "            color: white;" +
                 "            text-decoration: none;" +
                 "            padding: 15px 25px;" +
                 "            border-radius: 50px;" +
                 "            text-align: center;" +
                 "            font-weight: 600;" +
                 "            margin: 30px auto;" +
                 "            font-size: 18px;" +
                 "            box-shadow: 0 4px 6px rgba(67, 97, 238, 0.3);" +
                 "        }" +
                 "        .features {" +
                 "            margin: 30px 0;" +
                 "        }" +
                 "        .feature-item {" +
                 "            display: flex;" +
                 "            align-items: center;" +
                 "            margin-bottom: 15px;" +
                 "        }" +
                 "        .feature-icon {" +
                 "            background-color: #4361ee;" +
                 "            color: white;" +
                 "            width: 40px;" +
                 "            height: 40px;" +
                 "            border-radius: 50%;" +
                 "            display: flex;" +
                 "            align-items: center;" +
                 "            justify-content: center;" +
                 "            margin-right: 15px;" +
                 "            flex-shrink: 0;" +
                 "        }" +
                 "        .feature-text {" +
                 "            flex: 1;" +
                 "        }" +
                 "        .email-footer {" +
                 "            background-color: #f8fafc;" +
                 "            padding: 20px;" +
                 "            text-align: center;" +
                 "            font-size: 14px;" +
                 "            color: #64748b;" +
                 "        }" +
                 "        .social-links {" +
                 "            margin: 20px 0;" +
                 "        }" +
                 "        .social-links a {" +
                 "            display: inline-block;" +
                 "            margin: 0 10px;" +
                 "            color: #4361ee;" +
                 "            text-decoration: none;" +
                 "        }" +
                 "        .unsubscribe {" +
                 "            color: #64748b;" +
                 "            font-size: 12px;" +
                 "        }" +
                 "        /* Responsive styles */" +
                 "        @media screen and (max-width: 600px) {" +
                 "            .email-body {" +
                 "                padding: 20px;" +
                 "            }" +
                 "            h1 {" +
                 "                font-size: 24px;" +
                 "            }" +
                 "            h2 {" +
                 "                font-size: 20px;" +
                 "            }" +
                 "            .feature-item {" +
                 "                flex-direction: column;" +
                 "                text-align: center;" +
                 "            }" +
                 "            .feature-icon {" +
                 "                margin-right: 0;" +
                 "                margin-bottom: 10px;" +
                 "            }" +
                 "        }" +
                 "    </style>" +
                 "</head>" +
                 "<body>" +
                 "    <center class=\"email-container\">" +
                 "        <div class=\"email-content\">" +
                 "            <!-- Header -->" +
                 "            <div class=\"email-header\">" +
                 "                <div class=\"logo\">" +
                 "                    <img src=\"https://via.placeholder.com/40x40\" alt=\"CricketBet Logo\" width=\"40\" height=\"40\">" +
                 "                    CricketBet" +
                 "                </div>" +
                 "                <h1>New Login Alert 🚨</h1>" +
                 "            </div>" +
                 "            " +
                 "            <!-- Body -->" +
                 "            <div class=\"email-body\">" +
                 "                <p>Hi " + username + ",</p>" +
                 "                " +
                 "                <p>There was a new login to your account:</p>" +
                 "                " +
                 "                <div class=\"highlight\">" +
                 "                    <p><strong>Device:</strong> " + device + "</p>" +
                 "                    <p><strong>Location:</strong> " + location + "</p>" +
                 "                    <p><strong>Time:</strong> " + time + "</p>" +
                 "                </div>" +
                 "                " +
                 "                <p>If this was you, you can ignore this message. If you don't recognize this activity, please secure your account immediately.</p>" +
                 "                " +
                 "                <a href=\"" + "APP_URL" + "/security\" class=\"cta-button\">Review Account Security</a>" +
                 "            </div>" +
                 "            " +
                 "            <!-- Footer -->" +
                 "            <div class=\"email-footer\">" +
                 "                <div class=\"social-links\">" +
                 "                    <a href=\"https://twitter.com/cricketbet\">Twitter</a> • " +
                 "                    <a href=\"https://facebook.com/cricketbet\">Facebook</a> • " +
                 "                    <a href=\"https://instagram.com/cricketbet\">Instagram</a>" +
                 "                </div>" +
                 "                " +
                 "                <p>© 2023 CricketBet. All rights reserved.</p>" +
                 "                " +
                 "                <p class=\"unsubscribe\">" +
                 "                    You're receiving this email because you have an account with CricketBet.<br>" +
                 "                    If you no longer wish to receive these emails, you can <a href=\"#\">unsubscribe here</a>." +
                 "                </p>" +
                 "            </div>" +
                 "        </div>" +
                 "    </center>" +
                 "</body>" +
                 "</html>";
     }

     private String generateBetConfirmationTemplate(String username, String match, String betType,
                                                    String amount, String potentialWin, String betId) {
         return "<!DOCTYPE html>" +
                 "<html lang='en'>" +
                 "<head>" +
                 "<meta charset='UTF-8'>" +
                 "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                 "<title>Bet Confirmation</title>" +
                 "<style>" +
                 "  * { margin: 0; padding: 0; box-sizing: border-box; }" +
                 "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f7f9; color: #333; line-height: 1.6; }" +
                 "  .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; }" +
                 "  .email-header { background: linear-gradient(135deg, #1a2a57 0%, #3a6196 100%); padding: 30px 20px; text-align: center; border-radius: 8px 8px 0 0; }" +
                 "  .logo { font-size: 28px; font-weight: bold; color: #fff; margin-bottom: 15px; }" +
                 "  .logo span { color: #ffcc00; }" +
                 "  .email-header h1 { color: #fff; font-size: 28px; margin: 10px 0; }" +
                 "  .email-body { padding: 30px; }" +
                 "  .greeting { font-size: 18px; margin-bottom: 20px; color: #1a2a57; }" +
                 "  .message { margin-bottom: 25px; }" +
                 "  .bet-details { background-color: #f8f9fa; border-left: 4px solid #3a6196; padding: 20px; margin: 25px 0; border-radius: 0 8px 8px 0; }" +
                 "  .detail-row { display: flex; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid #eaeaea; }" +
                 "  .detail-row:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }" +
                 "  .detail-label { flex: 1; font-weight: 600; color: #1a2a57; }" +
                 "  .detail-value { flex: 2; font-weight: 500; }" +
                 "  .highlight { color: #2e7d32; font-weight: 700; }" +
                 "  .cta-button { display: inline-block; background: linear-gradient(135deg, #1a2a57 0%, #3a6196 100%); color: white; padding: 14px 35px; text-decoration: none; border-radius: 50px; font-weight: 600; margin: 20px 0; transition: all 0.3s ease; }" +
                 "  .cta-button:hover { background: linear-gradient(135deg, #3a6196 0%, #1a2a57 100%); transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.1); }" +
                 "  .good-luck { text-align: center; margin: 25px 0; font-style: italic; color: #1a2a57; }" +
                 "  .email-footer { background-color: #1a2a57; color: #fff; padding: 20px; text-align: center; border-radius: 0 0 8px 8px; }" +
                 "  .social-links { margin: 15px 0; }" +
                 "  .social-links a { color: #ffcc00; margin: 0 10px; text-decoration: none; }" +
                 "  .contact-info { font-size: 14px; margin-top: 15px; }" +
                 "  @media (max-width: 600px) { " +
                 "    .email-body { padding: 20px; } " +
                 "    .detail-row { flex-direction: column; } " +
                 "    .detail-label, .detail-value { flex: auto; } " +
                 "    .detail-label { margin-bottom: 5px; } " +
                 "  }" +
                 "</style>" +
                 "</head>" +
                 "<body>" +
                 "<div class='email-container'>" +
                 "  <div class='email-header'>" +
                 "    <div class='logo'>Cricket<span>Bet</span></div>" +
                 "    <h1>Bet Confirmed! 🎯</h1>" +
                 "  </div>" +
                 "  <div class='email-body'>" +
                 "    <p class='greeting'>Hi <strong>" + username + "</strong>,</p>" +
                 "    <p class='message'>Your bet has been successfully placed and is now confirmed. Here are the details of your wager:</p>" +
                 "    <div class='bet-details'>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Match:</div>" +
                 "        <div class='detail-value'>" + match + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Bet Type:</div>" +
                 "        <div class='detail-value'>" + betType + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Amount:</div>" +
                 "        <div class='detail-value'>$" + amount + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Potential Win:</div>" +
                 "        <div class='detail-value highlight'>$" + potentialWin + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Bet ID:</div>" +
                 "        <div class='detail-value'>" + betId + "</div>" +
                 "      </div>" +
                 "    </div>" +
                 "    <p class='good-luck'>May the odds be in your favor! 🏏</p>" +
                 "    <div style='text-align: center;'>" +
                 "      <a href='" + "APP_URL" + "/bets/" + betId + "' class='cta-button'>Track Your Bet</a>" +
                 "    </div>" +
                 "    <p style='text-align: center; font-size: 14px; color: #666;'>You can view your bet status and track its progress in your account dashboard at any time.</p>" +
                 "  </div>" +
                 "  <div class='email-footer'>" +
                 "    <p>© 2023 CricketBet. All rights reserved.</p>" +
                 "    <div class='social-links'>" +
                 "      <a href='#'>Help Center</a> | <a href='#'>Privacy Policy</a> | <a href='#'>Terms of Service</a>" +
                 "    </div>" +
                 "    <div class='contact-info'>" +
                 "      <p>Contact us: support@cricketbet.com | +1 (800) 123-4567</p>" +
                 "    </div>" +
                 "  </div>" +
                 "</div>" +
                 "</body>" +
                 "</html>";
     }

 private String generateWithdrawalRequestTemplate(String username, String amount, String method) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Withdrawal Request Received</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>We've received your withdrawal request:</p>" +
             "<div class='highlight'>" +
             "<p><strong>Amount:</strong> $" + amount + "</p>" +
             "<p><strong>Payment Method:</strong> " + method + "</p>" +
             "</div>" +
             "<p>Your withdrawal is being processed and should be completed within 24 hours.</p>" +
             "<p>If you did not initiate this withdrawal, please contact our support team immediately.</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }

     private String generateDepositConfirmationTemplate(String username, String amount, String method) {
         return "<!DOCTYPE html>" +
                 "<html lang='en'>" +
                 "<head>" +
                 "<meta charset='UTF-8'>" +
                 "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                 "<title>Deposit Confirmation</title>" +
                 "<style>" +
                 "  * { margin: 0; padding: 0; box-sizing: border-box; }" +
                 "  body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f7f9; color: #333; line-height: 1.6; }" +
                 "  .email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }" +
                 "  .email-header { background: linear-gradient(135deg, #1a2a57 0%, #3a6196 100%); padding: 30px 20px; text-align: center; }" +
                 "  .logo { font-size: 28px; font-weight: bold; color: #fff; margin-bottom: 15px; }" +
                 "  .logo span { color: #ffcc00; }" +
                 "  .email-header h1 { color: #fff; font-size: 28px; margin: 10px 0; display: flex; align-items: center; justify-content: center; }" +
                 "  .email-header h1 svg { margin-right: 10px; }" +
                 "  .email-body { padding: 30px; }" +
                 "  .greeting { font-size: 18px; margin-bottom: 20px; color: #1a2a57; }" +
                 "  .message { margin-bottom: 25px; line-height: 1.8; }" +
                 "  .transaction-details { background-color: #f8fafc; border-radius: 8px; padding: 25px; margin: 25px 0; border: 1px solid #eaeaea; }" +
                 "  .transaction-title { text-align: center; font-size: 18px; font-weight: 600; color: #1a2a57; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 1px dashed #d1d8e0; }" +
                 "  .detail-row { display: flex; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px solid #eaeaea; }" +
                 "  .detail-row:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }" +
                 "  .detail-label { flex: 1; font-weight: 600; color: #1a2a57; }" +
                 "  .detail-value { flex: 2; font-weight: 500; }" +
                 "  .amount { color: #2e7d32; font-weight: 700; font-size: 18px; }" +
                 "  .status-badge { display: inline-block; background-color: #e8f5e9; color: #2e7d32; padding: 5px 12px; border-radius: 50px; font-size: 14px; font-weight: 600; }" +
                 "  .cta-container { text-align: center; margin: 30px 0; }" +
                 "  .cta-button { display: inline-block; background: linear-gradient(135deg, #1a2a57 0%, #3a6196 100%); color: white; padding: 14px 35px; text-decoration: none; border-radius: 50px; font-weight: 600; transition: all 0.3s ease; box-shadow: 0 4px 6px rgba(26, 42, 87, 0.2); }" +
                 "  .cta-button:hover { background: linear-gradient(135deg, #3a6196 0%, #1a2a57 100%); transform: translateY(-2px); box-shadow: 0 6px 12px rgba(0,0,0,0.15); }" +
                 "  .secondary-cta { text-align: center; margin: 20px 0; }" +
                 "  .secondary-link { color: #3a6196; text-decoration: none; font-weight: 500; display: inline-block; margin: 0 10px; }" +
                 "  .secondary-link:hover { text-decoration: underline; }" +
                 "  .security-note { background-color: #fff8e1; border-left: 4px solid #ffc107; padding: 15px; margin: 25px 0; border-radius: 0 4px 4px 0; font-size: 14px; }" +
                 "  .icon { margin-right: 8px; vertical-align: middle; }" +
                 "  .email-footer { background-color: #1a2a57; color: #fff; padding: 25px; text-align: center; }" +
                 "  .footer-links { margin: 15px 0; }" +
                 "  .footer-links a { color: #ffcc00; margin: 0 12px; text-decoration: none; font-size: 14px; }" +
                 "  .contact-info { font-size: 14px; margin-top: 15px; line-height: 1.6; }" +
                 "  .copyright { margin-top: 20px; font-size: 12px; color: #c5cae9; }" +
                 "  @media (max-width: 600px) { " +
                 "    .email-body { padding: 20px; } " +
                 "    .detail-row { flex-direction: column; } " +
                 "    .detail-label, .detail-value { flex: auto; } " +
                 "    .detail-label { margin-bottom: 5px; } " +
                 "    .footer-links a { display: block; margin: 8px 0; } " +
                 "  }" +
                 "</style>" +
                 "</head>" +
                 "<body>" +
                 "<div class='email-container'>" +
                 "  <div class='email-header'>" +
                 "    <div class='logo'>Cricket<span>Bet</span></div>" +
                 "    <h1>" +
                 "      <svg width='24' height='24' viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'>" +
                 "        <path d='M9 16.2L4.8 12L3.4 13.4L9 19L21 7L19.6 5.6L9 16.2Z' fill='currentColor'/>" +
                 "      </svg>" +
                 "      Deposit Received Successfully" +
                 "    </h1>" +
                 "  </div>" +
                 "  <div class='email-body'>" +
                 "    <p class='greeting'>Hi <strong>" + username + "</strong>,</p>" +
                 "    <p class='message'>Thank you for your deposit! Your CricketBet account has been successfully credited and your funds are now available for betting.</p>" +
                 "    " +
                 "    <div class='transaction-details'>" +
                 "      <div class='transaction-title'>Transaction Details</div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Amount Deposited:</div>" +
                 "        <div class='detail-value amount'>$" + amount + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Payment Method:</div>" +
                 "        <div class='detail-value'>" + method + "</div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Status:</div>" +
                 "        <div class='detail-value'><span class='status-badge'>Completed</span></div>" +
                 "      </div>" +
                 "      <div class='detail-row'>" +
                 "        <div class='detail-label'>Processed On:</div>" +
                 "        <div class='detail-value'>" + java.time.LocalDate.now() + "</div>" +
                 "      </div>" +
                 "    </div>" +
                 "    " +
                 "    <div class='cta-container'>" +
                 "      <a href='" + "APP_URL" + "/deposit' class='cta-button'>Add More Funds</a>" +
                 "    </div>" +
                 "    " +
                 "    <div class='secondary-cta'>" +
                 "      <a href='" + "APP_URL" + "/bets' class='secondary-link'>Place a Bet</a>" +
                 "      <a href='" + "APP_URL" + "/account' class='secondary-link'>View Account</a>" +
                 "    </div>" +
                 "    " +
                 "    <div class='security-note'>" +
                 "      <svg class='icon' width='16' height='16' viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'>" +
                 "        <path d='M12 1L3 5V11C3 16.55 6.84 21.74 12 23C17.16 21.74 21 16.55 21 11V5L12 1ZM12 11.99H19C18.47 16.11 15.72 19.78 12 20.93V12H5V6.3L12 3.19V11.99Z' fill='#FFA000'/>" +
                 "      </svg>" +
                 "      <strong>Security Note:</strong> This deposit was processed securely. If you did not initiate this transaction, please contact our support team immediately." +
                 "    </div>" +
                 "  </div>" +
                 "  <div class='email-footer'>" +
                 "    <p>Thank you for choosing CricketBet</p>" +
                 "    <div class='footer-links'>" +
                 "      <a href='" + "APP_URL" + "/help'>Help Center</a>" +
                 "      <a href='" + "APP_URL" + "/security'>Security</a>" +
                 "      <a href='" + "APP_URL" + "/privacy'>Privacy Policy</a>" +
                 "      <a href='" + "APP_URL" + "/terms'>Terms of Service</a>" +
                 "    </div>" +
                 "    <div class='contact-info'>" +
                 "      <p>Contact us: support@cricketbet.com | +1 (800) 123-4567</p>" +
                 "      <p>Hours: 24/7</p>" +
                 "    </div>" +
                 "    <div class='copyright'>" +
                 "      <p>© 2023 CricketBet. All rights reserved.</p>" +
                 "    </div>" +
                 "  </div>" +
                 "</div>" +
                 "</body>" +
                 "</html>";
     }

 private String generateAccountVerificationTemplate(String username, String verificationLink) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Verify Your Email Address</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>Thank you for signing up! Please verify your email address to complete your registration.</p>" +
             "<a href='" + verificationLink + "' class='cta-button'>Verify Email Address</a>" +
             "<p>This link will expire in 24 hours for security reasons.</p>" +
             "<p>If you didn't create an account with us, please ignore this email.</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }

 private String generatePromotionalTemplate(String username, String promotionTitle, String promotionDetails) {
     return "<!DOCTYPE html>" +
             "<html>" +
             "<head>" +
             "<style>/* CSS styles */</style>" +
             "</head>" +
             "<body>" +
             "<div class='email-container'>" +
             "<div class='email-content'>" +
             "<div class='email-header'>" +
             "<div class='logo'>CricketBet</div>" +
             "<h1>Special Promotion: " + promotionTitle + "</h1>" +
             "</div>" +
             "<div class='email-body'>" +
             "<p>Hi " + username + ",</p>" +
             "<p>We have an exclusive promotion just for you!</p>" +
             "<div class='highlight'>" +
             "<p>" + promotionDetails + "</p>" +
             "</div>" +
             "<a href='" + "CricketBet" + "/promotions' class='cta-button'>Claim Offer Now</a>" +
             "<p>This offer is for a limited time only. Don't miss out!</p>" +
             "</div>" +
             "<div class='email-footer'>" +
             "<p>© 2023 " + "CricketBet" + ". All rights reserved.</p>" +
             "</div>" +
             "</div>" +
             "</div>" +
             "</body>" +
             "</html>";
 }
}
