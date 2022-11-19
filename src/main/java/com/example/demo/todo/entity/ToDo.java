package com.example.demo.todo.entity;

import lombok.*;

@Setter @Getter @ToString
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //전체 필드 초기화 생성자
//@Data를 사용하면 모든 것을 자동으로 만들어주지만 Setter만 빼는 등 세세한 부분을 수정할 수 없다.
//역할: 하나의 할 일 데이터의 집합 객체
public class ToDo {
    private long id; //할일들을 식별하는 번호
    private String userId; //할 일을 등록한 회원의 식별자
    private String title; //할 일 내용
    private boolean done; //할 일 완료 여부
}
