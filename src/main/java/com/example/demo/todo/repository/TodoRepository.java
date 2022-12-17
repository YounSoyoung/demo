package com.example.demo.todo.repository;

import com.example.demo.todo.entity.ToDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
//역할: 할 일 데이터를 CRUD한다.
public interface TodoRepository {
    //할 일 생성 기능

    /**
     * 할 일 데이터를 저장소에 저장하는 기능
     * @param todo - 할 일 데이터의 집합
     * @return - 저장 성공시 true, 실패 시 false 반환
     */
    boolean save(ToDo todo); //생성이 되고 나서는 성공했는지 못했는지 알려줘야함으로 boolean 타입

    //할 일 목록 조회 기능
    List<ToDo> findAll();

    //할 일 개별조회 기능
    ToDo findOne(String id);

    //할일 삭제 기능
    boolean remove(String id);

    //할 일 수정
    boolean modify(ToDo todo);
}
