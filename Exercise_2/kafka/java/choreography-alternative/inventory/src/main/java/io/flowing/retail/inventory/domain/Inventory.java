package io.flowing.retail.inventory.domain;

import java.util.*;

/**
 * Class Inventory, implemented as a singleton
 * to store the current amount in the inventory
 * of any given Item.
 * <p>
 * Pickorder: [item1,item1, item2] -> [{item1, 2}, {item2, 1}]
 */
public final class Inventory {

    /**
     * Attributes
     */
    private static Inventory INSTANCE;
    private final Set<Item> inventory;

    /**
     * Constructors
     */
    private Inventory() {
        inventory = new HashSet<>();
        inventory.add(new Item("article1", 20));
        inventory.add(new Item("article2", 24));
    }

    public static Inventory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        return INSTANCE;
    }

    public Set<Item> getInventory(){
        return inventory;
    }

    public void enoughGoods(PickOrder pickOrder) throws NotEnoughGoodsException {
        for (Item item : pickOrder.getItems()) {
            for (Item inventoryItem : inventory) {
                if (inventoryItem.getArticleId().equals(item.getArticleId())) {
                    if (inventoryItem.getAmount() < item.getAmount()) {
                        throw new NotEnoughGoodsException();
                    }
                }
            }
        }
    }

    public void decreaseInventory(PickOrder pickOrder) throws NotEnoughGoodsException {
        enoughGoods(pickOrder);
        pickOrder.getItems().forEach(item -> {
            inventory.forEach(inventoryItem -> {
                if (inventoryItem.getArticleId().equals(item.getArticleId())) {
                    inventoryItem.setAmount(inventoryItem.getAmount() - item.getAmount());
                }
            });
        });
    }

    @Override
    public String toString() {
        return "Inventory [inventory=" + inventory.toString() + "]";
    }

}
