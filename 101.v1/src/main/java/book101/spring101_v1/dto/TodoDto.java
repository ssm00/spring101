package book101.spring101_v1.dto;


import book101.spring101_v1.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDto {

    private String id;
    private String title;
    private boolean done;

    public TodoDto(TodoEntity entity) {
        id = entity.getId();
        title = entity.getTitle();
        done = entity.isDone();
    }

    public static TodoEntity toEntity(final TodoDto dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
