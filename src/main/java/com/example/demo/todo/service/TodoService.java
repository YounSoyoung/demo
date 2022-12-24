package com.example.demo.todo.service;

import com.example.demo.todo.dto.FindAllDTO;
import com.example.demo.todo.dto.TodoDTO;
import com.example.demo.todo.entity.ToDo;
import com.example.demo.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//역할: 컨트롤러와 저장소 사이의 잡일 처리 역할
@Service
@Slf4j
@RequiredArgsConstructor //final을 초기화하는 생성자가 만들어진다.
//@NoArgsConstructor //매개변수가 없는 생성자가 만들어진다.
//@AllArgsConstructor //final을 뺀 나머지 매개변수를 받는 생성자가 만들어진다.
public class TodoService {
    private final TodoRepository repository;

    /**
     * - 할 일 목록조회 중간처리
     * 1. 컨트롤러에게 userId를 뺀 할일 리스트를 전달한다.
     * 2. 할일 목록의 카운트를 세서 따로 추가해서 전달한다.
     *
     *
     */

    public FindAllDTO findAllServ(String userId){
//        List<ToDo> toDoList = repository.findAll();
//
//        FindAllDTO findAllDTO = new FindAllDTO(toDoList);
//
//        return findAllDTO;
        //변수들(toDoList, findAllDTO)이 한번만 사용되기 때문에 아래처럼 정리할 수 있다.(리펙토링 단축키: Ctrl + Alt + n)
        List<ToDo> all = repository.findAll(userId);
        log.info("db todos: {}", all);
        return new FindAllDTO(all);

//        for(ToDo toDo : toDoList){
//            TodoDTO dto = new TodoDTO();
//            dto.setId(toDo.getId());
//            dto.setTitle(toDo.getTitle());
//            dto.setDone(toDo.isDone());
//
//            dtos.add(dto);


    }


    public FindAllDTO createServ(final ToDo newTodo) {

        if(newTodo == null){
            log.warn("newTodo cannnot be null");
            throw new RuntimeException("newTodo cannot be null!");
        }

        boolean flag = repository.save(newTodo);

        if(flag) log.info("새로운 할일 [Id: {}] 이 저장되었습니다. {}", newTodo.getId(), newTodo);

        return flag ? findAllServ(newTodo.getUserId()) : null; //새로운 todo를 삽입한 후 전체목록을 반환한다.
    }

    public TodoDTO findOneServ(String id) {
        ToDo toDo = repository.findOne(id);
        log.info("findOneServ return data - {}", toDo);

        return toDo != null ? new TodoDTO(toDo) : null;

        //return new TodoDTO(repository.findOne(id));
    }

    public FindAllDTO deleteServ(String id, String userId) {

        boolean flag = repository.remove(id);

        //삭제 실패한 경우
        if(!flag) {
            log.warn("delete fail!! not found id [{}]", id);
            throw new RuntimeException("delete fail!");
        }
        return findAllServ(userId);
    }

    public FindAllDTO update(ToDo toDo){
        boolean flag = repository.modify(toDo);

        return flag ? findAllServ(toDo.getUserId()) : new FindAllDTO();
    }
}
