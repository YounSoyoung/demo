package com.example.demo.todo.entity;

import lombok.*;

@Setter @Getter @ToString
//@NoArgsConstructor //기본 생성자
@AllArgsConstructor //전체 필드 초기화 생성자
//@Data를 사용하면 모든 것을 자동으로 만들어주지만 Setter만 빼는 등 세세한 부분을 수정할 수 없다.
//역할: 하나의 할 일 데이터의 집합 객체
public class ToDo { //DB에 들어갈(저장될) 데이터들이 모두 있어야한다.(민감한 정보 포함)
    private long id; //할일들을 식별하는 번호
    private String userId; //할 일을 등록한 회원의 식별자
    private String title; //할 일 내용
    private boolean done; //할 일 완료 여부

    //일련번호
    private static long seq; //static이 생략되면 id는 모두 1이 된다. 객체 생성될 때마다 seq가 새로 생성되기 때문이다. 하지만 static이 있으면 객체 개수와 상관없이 seq가 하나만 생성되기 때문에 값이 누적되어 올라간다.

    public ToDo() {
        this.id = ++seq;
    }

    public ToDo(String title) {
        this(); //나의 또다른 생성자를 호출한다. (기본생성자인 ToDo() 생성자를 호출)
        this.title = title;
        this.userId = "noname";
        //this.done = false; //boolean의 기본값은 false이기 때문에 생략해도 된다.
    }
}
