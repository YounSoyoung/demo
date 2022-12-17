package com.example.demo.todo.api;

import com.example.demo.todo.dto.FindAllDTO;
import com.example.demo.todo.dto.TodoDTO;
import com.example.demo.todo.entity.ToDo;
import com.example.demo.todo.repository.TodoRepository;
import com.example.demo.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/todos")
@RequiredArgsConstructor //이 어노테이션을 사용하면 생성자 주입이 자동으로 된다.
@CrossOrigin //다른 서버의 요청 허용
public class TodoApiController {

    private final TodoService service;

//    @Autowired //의존성 주입 방법 중 생성자 주입을 사용해야하는 이유) 생성자는 한 번만 부를 수 있고 setter는 여러 번 부를 수 있다. 그러므로 불변을 위해(안정성을 위해) setter 말고 생성자 주입을 사용한다.
//    public TodoApiController(TodoRepository repository) { //클래스 안에 생성자가 하나만 존재하면 @Autowired를 생략해도 자동으로 @Autowired가 붙는다.
//        this.repository = repository;
//    }

    //할 일 목록 전체조회 요청
    @GetMapping
    public FindAllDTO todos(){
        log.info("/api/todos/ GET request!");

        //FindAllDTO findAllDTO = service.findAllServ();
        return service.findAllServ();
    }

    //할 일 목록 등록 요청
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ToDo newTodo){ //ResponseEntity<?>: 상태 코드를 같이 보낸다.
        newTodo.setUserId("noname");
        log.info("/api/todos POST request! - {}", newTodo);

        try {
            FindAllDTO dto = service.createServ(newTodo);

            if(dto == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(dto);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //할 일 개별 조회 요청
    //URI : /api/todos/3 => 3번 할 일 조회해서 클라이언트에게 리턴
    @GetMapping("/{id}")
    public ResponseEntity<?> todo(@PathVariable String  id){

        log.info("/api/todos/{} GET request!", id);

        if(id == null) return ResponseEntity.badRequest().build();

//        try{
//            return ResponseEntity.ok().body(service.findOneServ(id));
//        }catch (Exception e){
//            return ResponseEntity.notFound().build();
//        }
        TodoDTO dto = service.findOneServ(id);
        if(dto == null) return ResponseEntity.notFound().build();
        return  ResponseEntity.ok().body(dto);
    }

    //할 일 삭제 요청
    //URI: /api/todos/3 : DELETE
    // -> 3번 할 일을 삭제 후 삭제된 이후 갱신된 할 일 목록 리턴
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        log.info("/api/todos/{} DELETE request!", id);

        try{
            FindAllDTO dtos = service.deleteServ(id);
            return ResponseEntity.ok().body(dtos);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    //할 일 수정 요청
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ToDo toDo){
        log.info("/api/todos PUT request! - {}", toDo);

        try{
            FindAllDTO dtos = service.update(toDo);
            return ResponseEntity.ok().body(dtos);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }


}
