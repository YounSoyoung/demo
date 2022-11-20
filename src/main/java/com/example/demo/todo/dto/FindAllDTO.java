package com.example.demo.todo.dto;

import com.example.demo.todo.entity.ToDo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class FindAllDTO { //전체 목록 조회할 때 필요한 데이터만 가지고 있는다.
    private int count; // 할 일 목록의 개수
    private List<TodoDTO> todos; //userId가 빠진 할 일 목록

    public FindAllDTO(List<ToDo> toDoList) {
        this.count = toDoList.size();
        this.convertDtoList(toDoList);
    }

    //List<ToDo>를 받으면 List<TodoDto>로 변환하는 메서드
    public void convertDtoList(List<ToDo> toDoList) {
        List<TodoDTO> dtos = new ArrayList<>();

        for(ToDo toDo : toDoList) {
            dtos.add(new TodoDTO(toDo));
        }

        this.todos = dtos;
    }
}
