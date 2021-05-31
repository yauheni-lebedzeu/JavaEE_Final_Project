package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.service.MailSendingService;
import com.gmail.yauheniylebedzeu.service.exception.UserServiceException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class MailSendingServiceImpl implements MailSendingService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendPasswordToUser(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("New password");
        mailMessage.setText(password);
        mailMessage.setFrom("LemotTest@yandex.ru");
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            throw new UserServiceException(String.format("Couldn't send password to the \"%s\"", email));
        }
    }
}