package com.wander_book.response;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String content) {

}
