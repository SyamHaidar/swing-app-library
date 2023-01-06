package repository;

import model.Book;

public interface BookRepository {

    boolean dataExist(String id);

    String getAllData();

    void getData(String id);

    String getTopBook();

    void add(Book book);

    void edit(String id, Book book);

    void update(String id, Book book);

    void remove(String id);

}
