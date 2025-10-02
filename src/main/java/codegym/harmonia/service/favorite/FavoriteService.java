package codegym.harmonia.service.favorite;

import codegym.harmonia.model.Favorite;
import codegym.harmonia.model.Song;
import codegym.harmonia.repository.IFavoritedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService implements IFavoriteService {
    @Autowired
    IFavoritedRepository favoritedRepository;

    @Override
    public List<Favorite> findByUserId(Long id) {
        return favoritedRepository.findById_UserId(id);
    }

    @Override
    public boolean existed(Long userId, Long songId) {
        return favoritedRepository.existsById_UserIdAndId_SongId(userId, songId);
    }

    @Override
    public void delete(Long userId, Long songId) {
        favoritedRepository.deleteById_UserIdAndId_SongId(userId, songId);
    }

    @Override
    public List<Favorite> findAll() {
        return null;
    }

    @Override
    public Optional<Favorite> findById(Long id) {
        return Optional.empty();
    }


    @Override
    public Favorite save(Favorite favorite) {
        return favoritedRepository.save(favorite);
    }

    @Override
    public void delete(Long id) {
    }
}
