package ec.com.todo.apptasks.user.repository;

import ec.com.todo.apptasks.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     boolean existsByEmail(String  email);
     boolean existsByUsername(String username);


}
