package io.flowing.retail.inventory.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Class Inventory, implemented as a singleton
 * to store the current amount in the inventory
 * of any given Item.
 */
public final class Inventory {

    /**
     * Attributes
     */
    private static Inventory INSTANCE;
    private final Map<Item, Integer> inventory;

    /**
     * Constructors
     */
    private Inventory() {
        inventory = new HashMap<>();
        inventory.put(new Item("article1", 20), 150);
        inventory.put(new Item("article2", 24), 100);
    }

    public static Inventory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Inventory();
        }
        return INSTANCE;
    }


    public boolean enoughGoods(PickOrder pickOrder){
//        System.out.println(pickOrder);
        return true;
    }

    public void decreaseInventory(PickOrder pickOrder){
//        System.out.println(pickOrder);
    }


    @Override
    public String toString() {
        return "Inventory [inventory=" + inventory.toString() + "]";
    }

}
