package codegym.harmonia.service.authenticate;

import codegym.harmonia.model.DTO.SongSummaryDTO;
import codegym.harmonia.model.DTO.UserDTO;
import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.repository.IAuthenticateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return authenticateRepository.findById(id).orElse(null);
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

    @Override
    public UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),  // enum -> String
                user.getAvatar(),
                user.getCreateAt(),
                user.getSongs() != null
                        ? user.getSongs().stream()
                        .map(song -> new SongSummaryDTO(song.getSongId(), song.getTitle()))
                        .collect(Collectors.toList())
                        : null
        );
    }
}
