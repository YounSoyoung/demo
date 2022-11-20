package com.example.demo.rest;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/param")
public class RestParamController {

    @GetMapping("/user") //URL: /param/user?name=홍길동
    public String userName(HttpServletRequest request){
        //홍길동을 읽는다.
        String name = request.getParameter("name"); //getParameter는 무조건 리턴타입이 String
        return String.format("당신의 이름은 %s입니다.", name);
    }

    @GetMapping("/user2")
    public String userName(   // /param/user2?name=짹짹이&age=25
            @RequestParam(value = "name", required = false) String nn, //이름을 꼭 넣어야하면 required = true(기본값) 안넣어도 되면 false
                              @RequestParam(defaultValue = "10") int age            //두 가지 방식으로 name과 age를 나타낼 수 있다.
    ){

        return String.format("당신의 이름은 %s님이고, 나이는 %d세입니다.", nn, age);
    }

    // /param/user3?name=김철수&age=30&address=서울&height=171.88&hobby=축구&hobby=농구
    @GetMapping("/user3") //파라미터가 너무 많을 때
    //@RequestMapping(value="/user3", method = RequestMethod.GET)과 같다
    public String user3(UserDTO userInfo){
        return String.format(
                "당신의 이름은 %s님이고, 나이는 %d세이고, 주소는 %s이며, 키는 %.2fcm이며 취미는 %s들이다.",
                userInfo.getName(), userInfo.getAge(), userInfo.getAddress(), userInfo.getHeight(), userInfo.getHobby()
        );
    }

    @GetMapping("/user4/{userNum}")
    public String user4(@PathVariable int userNum){ //@PathVariable은 / 다음을 읽고 그 값을 userNum에 넣어준다. @RequestParam은 ? 다음을 읽는다.
        return String.format("회원번호는 %d번입니다.", userNum);
    }

    @Setter //DTO에서 Setter와 생성자는 필수 //필수
    @Getter
    @ToString
    @NoArgsConstructor //필수
    @AllArgsConstructor
    public static class UserDTO{
        private String name;
        private int age;
        private String address;
        private double height;
        private List<String> hobby;

    }
}
