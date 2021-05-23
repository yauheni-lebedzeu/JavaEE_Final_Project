package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.UserDTO;

public interface MailSendingService {

    void sendPasswordToUser(UserDTO userDTO);

}
