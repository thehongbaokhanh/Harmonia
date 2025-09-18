package codegym.harmonia.repository;

import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAuthenticateRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
}
