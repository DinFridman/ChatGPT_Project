package com.example.chatgptproject.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetailsDTO {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
