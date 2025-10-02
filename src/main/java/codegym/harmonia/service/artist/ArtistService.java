package codegym.harmonia.service.artist;

import codegym.harmonia.model.User;
import codegym.harmonia.model.UserRole;
import codegym.harmonia.repository.IAuthenticateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService implements IArtistService {

    @Autowired
    private IAuthenticateRepository userRepository;

    @Override
    public List<User> findAllArtists() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == UserRole.ARTIST)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User artist) {
        artist.setRole(UserRole.ARTIST); // đảm bảo luôn lưu với role ARTIST
        return userRepository.save(artist);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
