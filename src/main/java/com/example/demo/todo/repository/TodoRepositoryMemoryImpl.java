package com.example.demo.todo.repository;

import com.example.demo.todo.entity.ToDo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//역할: 할 일 데이터를 메모리에 CRUD하는 역할
@Repository //@Component를 가지고 있다.
public class TodoRepositoryMemoryImpl implements TodoRepository{
//메모리 저장소
    /**
     * key: 할 일의 식별 id값
     * value: 할 일 집합객체
     * static: DB가 여러개일 필요 없고 하나만 있으면 되니까
     * final: 불변성을 줘서 HashMap을 지킬 수 있다.
     */
    private static final Map<Long, ToDo> toDoMap = new HashMap<>();

    static { //보통 생성자를 통해 초기화를 해주는데 static은 생성자와 연동이 안된다.
        toDoMap.put(1L, new ToDo(1L, "김철수", "저녁밥 만들기", false));
        toDoMap.put(2L, new ToDo(2L, "박영희", "산책가기", false));
        toDoMap.put(3L, new ToDo(3L, "김민수", "노래연습하기", true));
    }

    @Override
    public boolean save(ToDo todo) {
        if(todo == null) return false;

        toDoMap.put(todo.getId(), todo);
        return false;
    }

    @Override
    public List<ToDo> findAll() {

        List<ToDo> toDoList = new ArrayList<>();

        for (Long id : toDoMap.keySet()) {
            ToDo todo = toDoMap.get(id);
            toDoList.add(todo);
        }

        return toDoList;
    }

    @Override
    public ToDo findOne(long id) {
        return toDoMap.get(id);
    }

    @Override
    public boolean remove(long id) {
        ToDo todo = toDoMap.remove(id); //삭제를 실패하면 null을 뱉어낸다.
        return todo != null; //todo가 null이면 false를 반환하고 null이 아니면 true를 반환
    }
}
