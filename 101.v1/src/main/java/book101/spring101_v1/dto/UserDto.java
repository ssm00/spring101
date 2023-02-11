package book101.spring101_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String token;
    private String email;
    private String username;
    private String password;
    private String id;
}
