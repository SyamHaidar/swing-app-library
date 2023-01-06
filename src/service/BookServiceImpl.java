package service;

import model.Book;
import repository.BookRepository;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean dataExist(String id) {
        return bookRepository.dataExist(id);
    }

    @Override
    public String getAllData() {
        return bookRepository.getAllData();
    }

    @Override
    public void getData(String id) {
        bookRepository.getData(id);
    }

    @Override
    public void add(Book book) {
        bookRepository.add(book);
    }

    @Override
    public void edit(String id, Book book) {
        bookRepository.edit(id, book);
    }

    @Override
    public void update(String id, Book book) {
        bookRepository.update(id, book);
    }

    @Override
    public void remove(String id) {
        bookRepository.remove(id);
    }

}
