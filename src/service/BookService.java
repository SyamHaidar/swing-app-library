package service;

import model.Book;

public interface BookService {

    boolean dataExist(String id);

    String getAllData();

    void getData(String id);

    void add(Book book);

    void edit(String id, Book book);

    void update(String id, Book book);

    void remove(String id);

}
