package codegym.harmonia.service.artist;

import codegym.harmonia.model.User;

import java.util.List;

public interface IArtistService {
    List<User> findAllArtists();
    User findById(Long id);
    User save(User artist);
    void delete(Long id);
}
