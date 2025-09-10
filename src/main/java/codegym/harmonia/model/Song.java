package codegym.harmonia.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "song")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long songId;
    private String title;
    private String file;
    private String cover;
    private Integer playCount;

    private LocalDateTime createdAt;

    // N-1: một bài hát phải thuộc về 1 nghệ sĩ (User có role=ARTIST)
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    // 1-N: một bài hát có thể nằm trong nhiều Favorite
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;
}