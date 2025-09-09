package codegym.harmonia.repository;

import codegym.harmonia.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFavoritedRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByIdUserId(Long userId);

    List<Favorite> findByIdSongId(Long songId);

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserIdAndSongId(Long userId, Long songId);
}
