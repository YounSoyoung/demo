package com.example.demo.error;

import lombok.*;
import org.hibernate.annotations.NotFound;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private String message; //에러 메시지


}
