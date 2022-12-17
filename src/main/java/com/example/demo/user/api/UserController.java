package com.example.demo.user.api;

import com.example.demo.user.dto.UserRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    //회원 가입 기능
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO reqDto){
        try {
            //userRegDto를 서비스에 전송
            //userEntity로 변환
            UserEntity entity = new UserEntity(reqDto);
            log.info("/auth/signup POST!! - {}", entity);

            UserEntity user = userService.createServ(entity);

            return ResponseEntity.ok().body(new UserResponseDTO(user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
