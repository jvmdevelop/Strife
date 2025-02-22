package com.jvmdevelop.strife.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChatDto {
    private String title;
    private List<Integer> userIds;
    private Long recipientId;
    private boolean isTetATet;
}
