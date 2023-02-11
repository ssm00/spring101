package book101.spring101_v1.repository;

import book101.spring101_v1.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByEmail(String email);

    Boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);

}
