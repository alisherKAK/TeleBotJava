package interfaces;

import model.BaseModel;

import java.util.List;

public interface IRepository<T extends BaseModel> {
    void add(T item);

    T get(Long id);
    List<T> getAll();

    void update(T item);
    void update(T item, Long id);

    void delete(T item);
    void delete(Long id);

    boolean has(T item);
    boolean has(Long id);
}
