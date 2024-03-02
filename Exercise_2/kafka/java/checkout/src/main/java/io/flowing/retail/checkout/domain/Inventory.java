package io.flowing.retail.checkout.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Item> inventory;

    /**
     * Constructors
     */
    private Inventory() {
        inventory = new HashSet<>();
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

    public Item takeItem(String articleId, int amount) throws NotEnoughGoodsException {
        for (Item inventoryItem : inventory){
            if (inventoryItem.getArticleId().equals(articleId) && inventoryItem.getAmount() >= amount) {
                inventoryItem.setAmount(inventoryItem.getAmount() - amount);
                Item newItem = new Item();
                newItem.setArticleId(articleId);
                newItem.setAmount(amount);
                return newItem;
            }
        }
        throw new NotEnoughGoodsException();
    }

    public void setInventory(Item[] inventory){
        Inventory.getInstance().inventory = new HashSet<>(Arrays.asList(inventory));
    }

    @Override
    public String toString() {
        return "Inventory [inventory=" + inventory.toString() + "]";
    }

}
