package repository;

import model.Member;
import model.Transaction;

import java.util.List;

public interface TransactionRepository {

    boolean dataExist(String id);

    int getTotalPages();

    String getAllData(Integer page);


    void getData(String id);

    void add(Transaction transaction, Member member, List<String> kodeBuku);

    void update(String id, Transaction transaction);

    boolean checkBookId(String id);

    boolean checkMemberId(String id);

}
