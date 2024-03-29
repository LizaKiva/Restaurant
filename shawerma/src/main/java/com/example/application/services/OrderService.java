package com.example.application.services;

import com.example.application.data.Order;
import com.example.application.data.OrderRepository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Optional<Order> get(Long id) {
        return repository.findById(id);
    }

    public Order update(Order entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Order> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Order> list(Pageable pageable, Specification<Order> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Order> getAll() {
        return repository.findAll();
    }

    public void saveOrder(Order order) {
        if (order == null) {
            System.err.println("Null order");
            return;
        }

        repository.save(order);
    }

    public void deleteOrder(Order order) {
        repository.delete(order);
    }

    public Order findByUsername(String username) {
        for (Order order : repository.findAll()) {
            if (order.getUsername().equals(username)) {
                return order;
            }
        }
        return null;
    }

}
