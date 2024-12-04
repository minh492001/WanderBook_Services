package com.wander_book.dto.response;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String content) {

}
