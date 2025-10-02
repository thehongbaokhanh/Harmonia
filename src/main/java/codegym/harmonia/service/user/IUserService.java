package codegym.harmonia.service.user;

import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    void delete(Long id);
    List<User> findByRole(UserRole role);
}
