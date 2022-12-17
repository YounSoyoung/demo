package com.example.demo.user.service;

import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    //회원 가입 기능
    public UserEntity createServ(final UserEntity userEntity) throws RuntimeException{ //객체의 불변성을 위해 final을 달아준다.
        if(userEntity == null || userEntity.getEmail() == null){
            throw new RuntimeException("Invalid args!");
        }

        //register하기 전에 패스워드를 인코딩
        String rawPw = userEntity.getPassword();
        userEntity.setPassword(encoder.encode(rawPw));
        boolean flag = userRepository.register(userEntity);

        return flag ? getByCredential(userEntity.getEmail()) : null;
    }

    public UserEntity getByCredential(String email){
        return userRepository.findUserByEmail(email);
    }

    //로그인 검증 메서드
    public UserEntity validateLogin(final String email, final String password){ //내부적으로 이메일과 패스워드도 바뀌면 안되므로 final

        //회원가입을 했는가
        UserEntity user = getByCredential(email);

        if(user == null) throw new RuntimeException("가입된 회원이 아닙니다"); //throw에 걸리면 return 효과가 나서 밑에 코드들이 실행되지 않는다

        //패스워드가 일치하는가
        if(!encoder.matches(password, user.getPassword())){
            throw new RuntimeException("비밀번호가 틀렸습니다");
        }

        return user; //로그인 성공시 회원정보 리턴

    }

    // 이메일 중복체크(회원가입할 때)
    public boolean isDuplicate(String email){
        return userRepository.existByEmail(email);
    }
}
