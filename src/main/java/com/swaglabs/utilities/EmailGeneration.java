package com.swaglabs.utilities;

import java.io.File;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import static com.swaglabs.driver.ListenerConfig.failedTestNames;
import static com.swaglabs.driver.ListenerConfig.failedTests;
import static com.swaglabs.driver.ListenerConfig.passedTestNames;
import static com.swaglabs.driver.ListenerConfig.passedTests;
import static com.swaglabs.driver.ListenerConfig.skippedTestNames;
import static com.swaglabs.driver.ListenerConfig.skippedTests;
import static com.swaglabs.utilities.ReportConstants.REPORT_PATH;

public class EmailGeneration {

    public static void sendReportEmail() {
        String from = requireEnvironmentVariable("SWAGLABS_MAIL_FROM");
        String to = requireEnvironmentVariable("SWAGLABS_MAIL_TO");
        String appPassword = requireEnvironmentVariable("SWAGLABS_MAIL_APP_PASSWORD");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("SwagLabs Automation - Test Execution Summary");

            int total = passedTests + failedTests + skippedTests;
            String statusText = (failedTests > 0) ? "❌ Failed" : "✔ Passed";
            String statusColor = (failedTests > 0) ? "red" : "green";

            String htmlContent = "<h3>SwagLabs Automation Test Summary</h3>" +
                    "<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; font-family: Arial;'>" +
                    "<tr style='background-color: #f2f2f2;'>" +
                    "<th>Project</th><th>Suite</th><th>Total</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Status</th>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>SwagLabs</td>" +
                    "<td>Regression Suite</td>" +
                    "<td>" + total + "</td>" +
                    "<td>" + passedTests + "</td>" +
                    "<td>" + failedTests + "</td>" +
                    "<td>" + skippedTests + "</td>" +
                    "<td style='color:" + statusColor + ";'><b>" + statusText + "</b></td>" +
                    "</tr>" +
                    "</table>" +
                    "<br><h4>✅ Passed Test Cases:</h4>" + buildHtmlList(passedTestNames) +
                    "<h4>❌ Failed Test Cases:</h4>" + buildHtmlList(failedTestNames) +
                    "<h4>⏭ Skipped Test Cases:</h4>" + buildHtmlList(skippedTestNames) +
                    "<p>See attached Extent Report for full execution details.</p>";

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html");

            MimeBodyPart attachmentPart = new MimeBodyPart();
            File reportFile = new File(REPORT_PATH);
            if (!reportFile.isFile() || reportFile.length() == 0) {
                throw new IllegalStateException("Extent report is missing or empty: " + reportFile.getAbsolutePath());
            }
            attachmentPart.attachFile(reportFile);
            attachmentPart.setFileName(reportFile.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Test report email sent to " + to);

        } catch (Exception e) {
            throw new IllegalStateException("Unable to send the Extent report email", e);
        }
    }

    private static String requireEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Email reporting is enabled, but the " + name + " environment variable is not set");
        }
        return value;
    }

    private static String buildHtmlList(List<String> testNames) {
        if (testNames.isEmpty()) return "<i>None</i>";
        StringBuilder builder = new StringBuilder("<ul>");
        for (String name : testNames) {
            builder.append("<li>").append(escapeHtml(name)).append("</li>");
        }
        builder.append("</ul>");
        return builder.toString();
    }

    private static String escapeHtml(String value) {
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
