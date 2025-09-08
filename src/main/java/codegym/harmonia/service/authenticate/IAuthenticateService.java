package codegym.harmonia.service.authenticate;

import codegym.harmonia.model.DTO.UserDTO;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.service.IGeneralService;

import java.util.List;

public interface IAuthenticateService extends IGeneralService<User> {
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    List<User> findByRole(UserRole role);
    UserDTO getUserDTO(User user);
}
