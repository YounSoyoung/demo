package com.example.demo.user.api;

import com.example.demo.error.ErrorDTO;
import com.example.demo.security.TokenProvider;
import com.example.demo.user.dto.UserRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;
import com.example.demo.user.entity.UserEntity;
import com.example.demo.user.service.UserService;
import com.example.demo.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final TokenProvider provider;

    //application.properties 파일에 지정된 값을 불러옴
    @Value("${upload.path}") //lombok에 있는 Value가 아닌 스프링프레임워크에 있는 것이다
    private String uploadRootPath;

    //회원 가입 기능
    //브라우저로부터 여러 타입의 정보가 넘어오기 때문에 RequestBody가 아닌 RequestPart로 바꿔주고 키값을 적어 해당하는 값을 가져온다
    //프로필 사진은 현재 필수값이 아니므로 required = false로 설정해야한다(기본값이 true이기 때문에 안바꿔주면 걸린다)
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestPart("userInfo") UserRequestDTO reqDto, @RequestPart(value = "profileImg", required = false) MultipartFile profileImg){

        try {
            //userRegDto를 서비스에 전송
            //userEntity로 변환
            UserEntity entity = new UserEntity(reqDto);
            log.info("/auth/signup POST!! - userInfo: {}", entity);

            if(profileImg != null){
                log.info("profileImg: {}", profileImg.getOriginalFilename());

                //1. 서버에 이미지를 저장 - 이미지를 서버에 업로드

                //1-a. 파일 저장 위치를 지정하여 파일 객체에 포장
                String originalFilename = profileImg.getOriginalFilename();

                //1-a-1. 파일명이 중복되지 않도록 변경
                String uploadFileName = UUID.randomUUID() + "_" + originalFilename;

                //1-a-2. 업로드 폴더를 날짜별로 생성
                String newUploadPath = FileUploadUtil.makeUploadDirectory(uploadRootPath);

                File uploadFile = new File(newUploadPath + "/" + uploadFileName);

                //1-b. 파일을 해당 경로에 업로드
                profileImg.transferTo(uploadFile);

                //2. 데이터베이스에 이미지 정보를 저장 - 누가 어떤 사진을 올렸는가

                //2-a. newUploadPath에서 rootPath를 제거
                //ex) new: D:/upload/2023/01/07
                  // root: D:/upload
                // new - root == /2023/01/07
                String savePath = newUploadPath.substring(uploadRootPath.length());

                entity.setProfileImg(savePath + File.separator + uploadFileName);
            }

            UserEntity user = userService.createServ(entity);

            return ResponseEntity.ok().body(new UserResponseDTO(user));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserRequestDTO dto){
        log.info("/auth/signin POST!! - login info: {}", dto);

        try {
            UserEntity user = userService.validateLogin(dto.getEmail(), dto.getPassword());

            //토큰 생성(로그인이 성공했기 때문에)
            final String token = provider.create(user);

            UserResponseDTO responseDTO = new UserResponseDTO(user);
            responseDTO.setToken(token); //발행한 토큰을 응답정보에 포함

            return ResponseEntity.ok().body(responseDTO);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }

    }

    // //RequestBody나 PathVariable이 없으므로 /auth/check?email=apple@gmail.com
    @GetMapping("/check")
    public ResponseEntity<?> checkEmail(String email) { //RequestBody나 PathValuable이 없으므로
        boolean flag = userService.isDuplicate(email);
        log.info("{} 중복여부?? - {}", email, flag);
        return ResponseEntity.ok().body(flag);
    }


    //클라이언트가 프로필사진을 요청할 시 프로필 사진을 전달해주는 요청처리
    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(@AuthenticationPrincipal String userId) throws IOException {

        log.info("/auth/load-profile GET - userId: {}", userId);

        //해당 유저의 아이디를 통해 프로필 사진의 경로를 DB에서 조회
        //ex) /2023/01/07/skjeijefjie_파일명.확장자
        String profilePath = userService.getProfilePath(userId);

        //ex) E:/upload/2023/01/07/~~~
        String fullPath = uploadRootPath + File.separator + profilePath;

        //해당 경로를 파일 객체로 포장
        File targetFile = new File(fullPath);

        //혹시 해당 파일이 존재하지 않으면 예외가 발생 (FileNotFoundException)
        if(!targetFile.exists()) return ResponseEntity.notFound().build();

        // 파일 데이터를 바이트배열로 포장(blob 데이터)
        byte[] rawImageData = FileCopyUtils.copyToByteArray(targetFile); //예외를 메서드 시그니처에 추가

        //응답 헤더 정보 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(FileUploadUtil.getMediaType(profilePath));

        //클라이언트에 순수 이미지파일 데이터 리턴
        return ResponseEntity.ok().headers(headers).body(rawImageData);

    }

}
