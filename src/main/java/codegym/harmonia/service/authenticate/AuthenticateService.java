package codegym.harmonia.service.authenticate;

import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.repository.IAuthenticateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticateService implements IAuthenticateService {

    @Autowired
    private IAuthenticateRepository authenticateRepository;

    @Override
    public List<User> findAll() {
        return authenticateRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public User save(User user) {
        return authenticateRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        authenticateRepository.deleteById(id);
    }

    @Override
    public List<User> findByUsername(String username) {
        return authenticateRepository.findByUsername(username);
    }

    @Override
    public List<User> findByEmail(String email) {
        return authenticateRepository.findByEmail(email);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return authenticateRepository.findByRole(role);
    }
}
