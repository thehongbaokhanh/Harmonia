package codegym.harmonia.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("userId") // ánh xạ userId trong FavoriteId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("songId") // ánh xạ songId trong FavoriteId
    @JoinColumn(name = "song_id")
    private Song song;

    private LocalDateTime createdAt;
}
