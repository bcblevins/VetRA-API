package com.bcb.vetra.services;

import com.bcb.vetra.models.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.mail.*;

/**
 * Service class for sending notifications to users when they receive an email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageNotification {
    MailSender mailSender;

    /**
     * Sends an email to the user.
     *
     * @param email   The email address of the user.
     * @param message The message the user received in VetRA.
     * @return True if the email was sent successfully, false otherwise.
     */
    public boolean sendEmail(String email, Message message, boolean implemented) {
        if (!implemented) {
            return false;
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setFrom("example@email.com");
        msg.setSubject("You received a new message in VetRA");
        msg.setText(
                "Hello, \n" +
                        "You received the following message in VetRA: \n" +
                        "\n" +
                        message.getSubject() + "\n" +
                        "\n" +
                        message.getBody() + "\n" +
                        "\n" +
                        "Please log in to VetRA to reply to this message."
        );
        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            return false;
        }
        return true;
    }
}
