package com.wander_book.service;

import com.wander_book.dto.response.MailBody;

public interface IEmailService {
    void sendVerificationCode(MailBody mailBody);
}
