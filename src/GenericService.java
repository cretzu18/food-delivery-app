public interface GenericService<T>
{
    void create(T entity);
    List<T> read();
    T readOneEntity(int id);
    void update(T entity);
    void delete(int id);
}