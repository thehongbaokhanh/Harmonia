package codegym.harmonia.repository;

import codegym.harmonia.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFavoritedRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findById_UserId(Long userId);

    List<Favorite> findById_SongId(Long songId);

    boolean existsById_UserIdAndId_SongId(Long userId, Long songId);

    void deleteById_UserIdAndId_SongId(Long userId, Long songId);
}
