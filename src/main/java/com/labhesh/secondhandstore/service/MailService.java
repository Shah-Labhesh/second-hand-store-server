package com.labhesh.secondhandstore.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.labhesh.secondhandstore.enums.OtpType;
import com.labhesh.secondhandstore.exception.InternalServerException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.application.name}")
    private String companyName;

    public void sendMail(String to, String subject, String text) throws InternalServerException {
        try {
            MimeMessage email = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject + " - " + companyName);
            helper.setText(text, true); // Set to true for HTML content
            mailSender.send(email);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new InternalServerException(e.getMessage());
        }
    }

    private String getOtpEmail(String name, String otp, OtpType type) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"en\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>OTP Email</title>\n" +
               "    <style>\n" +
               "        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap');\n" +
               "        body {\n" +
               "            font-family: 'Poppins', sans-serif;\n" +
               "            background-color: #f4f4f4;\n" +
               "            margin: 0;\n" +
               "            padding: 0;\n" +
               "        }\n" +
               "        .container {\n" +
               "            width: 100%;\n" +
               "            max-width: 600px;\n" +
               "            margin: 0 auto;\n" +
               "            background-color: #ffffff;\n" +
               "            padding: 20px;\n" +
               "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
               "        }\n" +
               "        .header {\n" +
               "            text-align: center;\n" +
               "            padding: 20px 0;\n" +
               "            background-color: #031038;\n" +
               "            color: #ffffff;\n" +
               "            border: 2px solid #031038;\n" +
               "            border-radius: 10px;\n" +
               "        }\n" +
               "        .header h1 {\n" +
               "            margin: 0;\n" +
               "            font-size: 24px;\n" +
               "            font-weight: 600;\n" +
               "        }\n" +
               "        .content {\n" +
               "            margin: 20px 0;\n" +
               "            line-height: 1.6;\n" +
               "        }\n" +
               "        .otp {\n" +
               "            display: block;\n" +
               "            width: fit-content;\n" +
               "            margin: 20px auto;\n" +
               "            padding: 15px 30px;\n" +
               "            font-size: 1.5em;\n" +
               "            color: #ffffff;\n" +
               "            background-color: #031038;\n" +
               "            border: 2px solid #031038;\n" +
               "            border-radius: 5px;\n" +
               "        }\n" +
               "        .footer {\n" +
               "            text-align: center;\n" +
               "            padding: 20px 0;\n" +
               "            color: #777;\n" +
               "        }\n" +
               "        .footer p {\n" +
               "            margin: 5px 0;\n" +
               "        }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class=\"container\">\n" +
               "        <div class=\"header\">\n" +
               "            <h1>" + getSubjet(type) + "</h1>\n" +
               "        </div>\n" +
               "        <div class=\"content\">\n" +
               "            <p>Dear <strong>" + name + "</strong>,</p>\n" +
               "            <p>We have received a request to verify your identity. Please use the following One-Time Password (OTP) to complete the verification process:</p>\n" +
               "            <span class=\"otp\"><strong>" + otp + "</strong></span>\n" +
               "            <p>This OTP is valid for 10 minutes. If you did not request this verification, please ignore this email or contact our support team immediately.</p>\n" +
               "            <p>For your security, please do not share this OTP with anyone. Our team will never ask you for your OTP over the phone or through any other means.</p>\n" +
               "            <p>If you have any questions or need assistance, please do not hesitate to contact our support team.</p>\n" +
               "            <p>Thank you for your prompt attention to this matter.</p>\n" +
               "            <p>Best regards,<br>" + companyName + "</p>\n" +
               "        </div>\n" +
               "        <div class=\"footer\">\n" +
               "            <p>&copy; " + LocalDate.now().getYear() + " " + companyName + ". All rights reserved.</p>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }

    private String getSubjet(OtpType type) {
        switch (type) {
            case EMAIL_VERIFICATION:
                return "Email Verification OTP";
            case PASSWORD_RESET:
                return "Password Reset OTP";
            default:
                return "One-Time Password";
        }
    }

    public void sendOtpEmail(String to, String name, String otp, OtpType type) {
        try {
            sendMail(to, getSubjet(type), getOtpEmail(name, otp, type));
        } catch (InternalServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
