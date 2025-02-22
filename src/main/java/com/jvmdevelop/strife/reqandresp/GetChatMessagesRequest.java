package com.jvmdevelop.strife.reqandresp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class GetChatMessagesRequest {
    private Long chatId;
    private Integer offset = 0;

}
