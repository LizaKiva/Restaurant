package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.util.HashMap;

@Entity
@Table(name = "client_order")
public class Order extends AbstractEntity {

    private String username;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Integer cost;
    private Integer time;
    private Float completed;
    private HashMap<Long, Integer> cart;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getTime() {
        return time;
    }
    public void setTime(Integer time) {
        this.time = time;
    }

    public Float getCompleted() {
        return completed;
    }
    public void setCompleted(Float completed) {
        this.completed = completed;
    }

    public HashMap<Long, Integer> getCart() {
        return cart;
    }
    public void setCart(HashMap<Long, Integer> cart) {
        this.cart = cart;
    }
}
