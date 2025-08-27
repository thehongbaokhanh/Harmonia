package codegym.harmonia.controller;

import codegym.harmonia.model.Favorite;
import codegym.harmonia.model.FavoriteId;
import codegym.harmonia.model.Song;
import codegym.harmonia.model.User;
import codegym.harmonia.service.favorite.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class favoritecontroller {
    @Autowired
    private IFavoriteService favoriteService;

    @RequestMapping("/homeWorkSpace/favorite/list")
    public List<Favorite> favorite() {
        return favoriteService.findAll();
    }

    @DeleteMapping("/homeWorkSpace/favorite/remove")
    public String removeFavorite(@RequestBody Long userId, @RequestBody Long songId) {
        boolean existed = favoriteService.existed(userId, songId);
        if (!existed) {
            return "This song is not in your favorite list.";
        }
        favoriteService.delete(userId, songId);
        return "Removed favorite successfully.";
    }

    @PostMapping("/homeWorkSpace/favorite/add")
    public String add(@RequestBody Long userId,@RequestBody Long songId) {
        boolean existed = favoriteService.existed(userId, songId);
        if (existed) {
            return "The song is already in your favorite list.";
        } else {
            // Tạo FavoriteId
            FavoriteId favoriteId = new FavoriteId(userId, songId);

            // Tạo entity Favorite
            Favorite favorite = Favorite.builder()
                    .id(favoriteId)
                    .user(User.builder().userId(userId).build())
                    .song(Song.builder().songId(songId).build())
                    .createdAt(LocalDateTime.now())
                    .build();

            favoriteService.save(favorite);
            return "Add favorite successfully.";
        }
    }
}
