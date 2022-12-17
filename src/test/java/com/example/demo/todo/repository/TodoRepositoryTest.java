package com.example.demo.todo.repository;

import com.example.demo.todo.entity.ToDo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//모든 공통된 요청을 처리하는 것은 필터, 특정 요청만 처리하는 것은 인터셉터

@SpringBootTest
class TodoRepositoryTest {
    @Autowired TodoRepository repository;

    @Test
    @DisplayName("특정 할일 데이터를 수정해야 한다")
    void modifyTest() {
        // given
        ToDo toDo = new ToDo();
        toDo.setTitle("하하호호 수정하기");
        toDo.setDone(true);
        toDo.setId("4f6f83b8-5d65-4c68-be1c-d5c4e46c0c2e");


        // when
        boolean flag = repository.modify(toDo);

        // then
        assertTrue(flag);

    }
}