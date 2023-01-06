package service;

import repository.PayRepository;

public class PayServiceImpl implements PayService {

    private final PayRepository payRepository;

    public PayServiceImpl(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    @Override
    public String getAll() {
        return payRepository.getAll();
    }

    @Override
    public String total() {
        return payRepository.total();
    }

    @Override
    public double latePercentage() {
        return payRepository.latePercentage();
    }
}
