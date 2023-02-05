package book101.spring101_v1.controller;

import book101.spring101_v1.dto.ResponseDTO;
import book101.spring101_v1.dto.TodoDto;
import book101.spring101_v1.model.TodoEntity;
import book101.spring101_v1.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDto dto) {
        try {
            String temporaryUserId = "temporary-user";
            TodoEntity entity = TodoDto.toEntity(dto);

            //persist시 생성
            entity.setId(null);

            //임시
            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.create(entity);

            List<TodoDto> todoDtos = entities.stream().map(TodoDto::new).toList();

            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(todoDtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> findTodos() {
        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = todoService.findTodos(temporaryUserId);

        List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();

        ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDto dto) {
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDto.toEntity(dto);

        entity.setUserId(temporaryUserId);

        List<TodoEntity> update = todoService.update(entity);

        List<TodoDto> dtos = update.stream().map(TodoDto::new).toList();

        ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDto dto) {
        try{
            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDto.toEntity(dto);

            entity.setUserId(temporaryUserId);

            List<TodoEntity> entities = todoService.delete(entity);

            List<TodoDto> dtos = entities.stream().map(TodoDto::new).toList();

            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);

        }catch (Exception e){
            String message = e.getMessage();

            ResponseDTO<TodoDto> response = ResponseDTO.<TodoDto>builder().error(message).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

}

