package com.wander_book.service;

import com.wander_book.response.MailBody;

public interface IEmailService {
    void sendVerificationCode(MailBody mailBody);
}
