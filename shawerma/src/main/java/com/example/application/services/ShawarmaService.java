package com.example.application.services;

import com.example.application.data.Shawarma;
import com.example.application.data.ShawarmaRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ShawarmaService {

    private final ShawarmaRepository repository;

    public ShawarmaService(ShawarmaRepository repository) {
        this.repository = repository;
    }

    public Optional<Shawarma> get(Long id) {
        return repository.findById(id);
    }

    public Shawarma update(Shawarma entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Shawarma> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Shawarma> list(Pageable pageable, Specification<Shawarma> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Shawarma> getAll() {
        return repository.findAll();
    }

    public void saveShawarma(Shawarma shawarma) {
        if (shawarma == null) {
            System.err.println("Null Shawarma");
            return;
        }

        repository.save(shawarma);
    }

    public void deleteShawarma(Shawarma shawarma) {
        repository.delete(shawarma);
    }

}
