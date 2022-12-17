package com.example.demo.todo.dto;

import com.example.demo.todo.entity.ToDo;
import lombok.*;
import lombok.extern.flogger.Flogger;

import java.util.Date;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO { //화면에 보여주고 싶은 정보만 가지고 있다.
    private String id;
    private String title;
    private boolean done;
    private Date regDate;

    //ToDo에서 dto가 필요한 필드를 빼오는 생성자
    public TodoDTO(ToDo toDo){
        this.id = toDo.getId();
        this.title = toDo.getTitle();
        this.done = toDo.isDone();
        this.regDate = toDo.getRegDate();
    }
}
