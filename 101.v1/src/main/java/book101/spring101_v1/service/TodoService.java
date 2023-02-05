package book101.spring101_v1.service;

import book101.spring101_v1.model.TodoEntity;
import book101.spring101_v1.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    public String testService() {
        return "test-service";
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        validation(entity);
        todoRepository.save(entity);
        return todoRepository.findByUserId(entity.getUserId());
    }

    private void validation(TodoEntity entity) {
        if(entity == null){
            log.warn("entity cannot be null");
            throw new RuntimeException("entity cannot be null");
        }

        if(entity.getUserId() == null){
            log.warn("unknown user");
            throw new RuntimeException("unknown user");
        }
    }

    public List<TodoEntity> findTodos(final String userId) {
        return todoRepository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        validation(entity);

        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            todoRepository.save(todo);
        });

        return findTodos(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        validation(entity);
        try{
            todoRepository.delete(entity);

        }catch (Exception e){
            log.error("error deleting entity", entity.getId(), e);
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        return findTodos(entity.getUserId());
    }

}
