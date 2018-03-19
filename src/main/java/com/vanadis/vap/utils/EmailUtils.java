package com.vanadis.vap.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailUtils {

    public static void send(JavaMailSender mailSender, String Sender, String mail, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Sender);
        message.setTo(mail); //自己给自己发送邮件
        message.setSubject(title);
        message.setText(content);
        mailSender.send(message);
    }
}
