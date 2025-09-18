package codegym.harmonia.service.favorite;

import codegym.harmonia.model.Favorite;
import codegym.harmonia.service.IGeneralService;

import java.util.List;

public interface IFavoriteService extends IGeneralService<Favorite> {
    List<Favorite> findByUserId(Long id);

    boolean existed(Long userId, Long songId);

    void delete(Long userId, Long songId);
}
