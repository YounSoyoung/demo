//package com.example.demo.todo.repository;
//
//import com.example.demo.todo.entity.ToDo;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest //스프링 컨테이너를 테스트 클래스에서 사용가능하게 해준다.
//class TodoRepositoryMemoryImplTest {
//
//    @Autowired
//    TodoRepository repository;
//
//    //Test는 단언을 사용해야한다. 단언: 강력하게 주장한다.
//    @Test
//    @DisplayName("저장소에서 목록을 조회했을 때 할 일의 개수가 3개여야 한다.")
//    void findAllTest(){
//        //given : 테스트 시 필요한 데이터
//
//        //when : 테스트의 실제 상황
//        List<ToDo> toDoList = repository.findAll();
//
//        //then : 테스트 결과 단언
//        assertEquals(3, toDoList.size());
//        assertNotNull(toDoList);
//    }
//
//    @Test
//    @DisplayName("아이디가 2번인 할일 데이터를 조회했을 때 그 작성자 이름이 박영희이어야 한다.")
//    void findOneTest(){
//        //given
//        String id = 2L;
//
//        //when
//        ToDo todo = repository.findOne(id);
//
//        //then
//        assertEquals("박영희", todo.getUserId());
//        assertFalse(todo.isDone());
//    }
//
//    @Test
//    @DisplayName("1번 할일을 삭제한 후 다시 조회했을 때 null이 나와야한다.")
//    void removeTest() {
//        // given
//        long id = 1L;
//
//        // when
//        boolean flag = repository.remove(id);
//        ToDo todo = repository.findOne(id);
//
//        // then
//        assertTrue(flag);
//        assertNull(todo);
//        assertEquals(2, repository.findAll().size());
//
//    }
//
//    @Test
//    @DisplayName("4번 할일을 등록했을 때 전체목록의 개수가 4개여야 한다.")
//    void saveTest() {
//        // given
//        ToDo newTodo = new ToDo(4L, "말똥이", "낮잠자기", false);
//
//        // when
//        boolean flag = repository.save(newTodo);
//
//        List<ToDo> toDoList = repository.findAll();
//
//        // then
//        assertTrue(flag);
//        assertEquals(4, toDoList.size());
//
//    }
//
//}