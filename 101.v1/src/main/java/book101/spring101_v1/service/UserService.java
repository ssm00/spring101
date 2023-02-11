package book101.spring101_v1.service;


import book101.spring101_v1.model.UserEntity;
import book101.spring101_v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null){
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already Exist");
            throw new RuntimeException("email already Exist");
        }
        return userRepository.save(userEntity);
    }

    public UserEntity getByCredential(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
}
