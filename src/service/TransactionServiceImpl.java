package service;

import model.Member;
import model.Transaction;
import repository.TransactionRepository;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean dataExist(String id) {
        return transactionRepository.dataExist(id);
    }

    @Override
    public int getTotalPages() {
        return transactionRepository.getTotalPages();
    }

    @Override
    public String getAllData(Integer page) {
        return transactionRepository.getAllData(page);
    }

    @Override
    public void getData(String id) {
        transactionRepository.getData(id);
    }

    @Override
    public void add(Transaction transaction, Member member, List<String> kodeBuku) {
        transactionRepository.add(transaction, member, kodeBuku);
    }

    @Override
    public void update(String id, Transaction transaction) {
        transactionRepository.update(id, transaction);
    }

    @Override
    public boolean checkBookId(String id) {
        return transactionRepository.checkBookId(id);
    }

    @Override
    public boolean checkMemberId(String id) {
        return transactionRepository.checkMemberId(id);
    }

}
