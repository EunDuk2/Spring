package com.example.chatserver.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberSaveReqDto {
    private String name;
    private String email;
    private String password;
}
