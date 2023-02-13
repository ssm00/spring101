package book101.spring101_v1.controller;

import book101.spring101_v1.dto.ResponseDTO;
import book101.spring101_v1.dto.UserDto;
import book101.spring101_v1.model.UserEntity;
import book101.spring101_v1.security.TokenProvider;
import book101.spring101_v1.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            UserEntity user = UserEntity.builder()
                    .email(userDto.getEmail())
                    .username(userDto.getUsername())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            UserEntity registeredUser = userService.create(user);

            UserDto responseDto = UserDto.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();
            return ResponseEntity.ok().body(responseDto);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {
        UserEntity user = userService.getByCredential(userDto.getEmail(), userDto.getPassword(),passwordEncoder);

        if (user != null) {
            final String token = tokenProvider.create(user);
            final UserDto responseUserDto = UserDto.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDto);
        }else{
            ResponseDTO responseDto = ResponseDTO.builder()
                    .error("Login Failed")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDto);
        }

    }
}
