package com.example.server.cart.service;

import com.example.server.cart.domain.ShoppingItem;
import com.example.server.cart.repository.ShoppingItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingCartServiceImpl(ShoppingItemRepository shoppingItemRepository) {
        this.shoppingItemRepository = shoppingItemRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingItem> findAll() {
        return shoppingItemRepository.findAll();
    }

    @Override
    public ShoppingItem addItem(String name, Integer quantity, String unit) {
        ShoppingItem item = ShoppingItem.create(name, quantity, unit);
        return shoppingItemRepository.save(item);
    }

    @Override
    public ShoppingItem updateItem(Long id, String name, Integer quantity, String unit) {
        ShoppingItem item = shoppingItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("장바구니 품목을 찾을 수 없습니다."));
        item.update(name, quantity, unit);
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        shoppingItemRepository.deleteById(id);
    }

    @Override
    public void clear() {
        shoppingItemRepository.deleteAllInBatch();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShoppingItem> findAllById(List<Long> ids) {
        return shoppingItemRepository.findAllById(ids);
    }
}
