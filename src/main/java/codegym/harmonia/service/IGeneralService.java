package codegym.harmonia.service;

import java.util.List;

public interface IGeneralService<T> {
    List<T> findAll();
    T findById(Long id);
    void save(T t);
    void delete(Long id);
}
