package com.beyond.basic.b2_board.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonDto {
    private Object result;
    private int status_code;
    private String status_message;
}
