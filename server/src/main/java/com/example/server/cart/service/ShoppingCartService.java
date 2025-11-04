package com.example.server.cart.service;

import com.example.server.cart.domain.ShoppingItem;

import java.util.List;

public interface ShoppingCartService {
    List<ShoppingItem> findAll();
    ShoppingItem addItem(String name, Integer quantity, String unit);
    ShoppingItem updateItem(Long id, String name, Integer quantity, String unit);
    void deleteItem(Long id);
    void clear();
    List<ShoppingItem> findAllById(List<Long> ids);
}
