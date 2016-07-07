package dnn.com.enviaremail;

/**
 * Created by criare on 06/07/2016.
 */

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

public class Mail {

    final String emailPort = "587";
    final String smtpAuth = "true";
    final String starttls = "true";
    String emailHost;

    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public Mail() {

    }

    public Mail(String fromEmail, String fromPassword,
                List toEmailList, String emailSubject, String emailBody, String emailHost) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmailList = toEmailList;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.emailHost = emailHost;


        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {
            Log.i("GMail", "toEmail: " + toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public Retorno sendEmail(Activity sendMailActivity) throws AddressException, MessagingException {
        Retorno r = new Retorno();
        try {
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(emailHost, fromEmail, fromPassword);
            Log.i("GMail", "allrecipients: " + emailMessage.getAllRecipients());
            transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
            transport.close();
            Log.i("GMail", "Email sent successfully.");
            r.setOk(true);
        } catch (Exception e) {
            Log.i("GMail", e.toString());
            r.setOk(false);
            r.setErro(e.toString());
        }
        return r;
    }

    public static class Retorno {
        private boolean ok;
        private String erro;

        public Retorno() {
        }

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }

        public String getErro() {
            return erro;
        }

        public void setErro(String erro) {
            this.erro = erro;
        }
    }

}
