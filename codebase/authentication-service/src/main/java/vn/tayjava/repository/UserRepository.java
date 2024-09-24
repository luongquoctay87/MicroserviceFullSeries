package vn.tayjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tayjava.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String userName);
}
