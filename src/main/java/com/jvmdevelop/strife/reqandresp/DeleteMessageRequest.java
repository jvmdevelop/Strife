package com.jvmdevelop.strife.reqandresp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class DeleteMessageRequest {
    private Long messageId;

}
