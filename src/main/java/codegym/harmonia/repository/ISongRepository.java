package codegym.harmonia.repository;

import codegym.harmonia.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISongRepository extends JpaRepository<Song, Long> {
}
