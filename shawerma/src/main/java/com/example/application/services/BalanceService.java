package com.example.application.services;

import com.example.application.data.Balance;
import com.example.application.data.BalanceRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    private final BalanceRepository repository;

    public BalanceService(BalanceRepository repository) {
        this.repository = repository;
    }

    public Optional<Balance> get(Long id) {
        return repository.findById(id);
    }

    public Balance update(Balance entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Balance> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Balance> list(Pageable pageable, Specification<Balance> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Balance> getAll() {
        return repository.findAll();
    }

    public void saveBalance(Balance balance) {
        if (balance == null) {
            System.err.println("Null balance");
            return;
        }

        repository.save(balance);
    }

    public void deleteBalance(Balance balance) {
        repository.delete(balance);
    }

}
