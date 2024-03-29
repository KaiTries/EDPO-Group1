package io.flowing.retail.inventory.application;

import java.time.LocalDateTime;
import java.util.List;

import io.flowing.retail.inventory.domain.*;
import org.springframework.stereotype.Component;

@Component
public class InventoryService {


    /**
     * reserve goods on stock for a defined period of time
     *
     * @param reason         A reason why the goods are reserved (e.g. "customer order")
     * @param refId          A reference id fitting to the reason of reservation (e.g. the order id), needed to find reservation again later
     * @param expirationDate Date until when the goods are reserved, afterwards the reservation is removed
     * @return if reservation could be done successfully
     */
    public boolean reserveGoods(List<Item> items, String reason, String refId, LocalDateTime expirationDate) {
        // TODO: Implement
        return true;
    }

    /**
     * Order to pick the given items in the warehouse. The inventory is decreased.
     * Reservation fitting the reason/refId might be used to fulfill the order.
     * <p>
     * If no enough items are on stock - an exception is thrown.
     * Otherwise a unique pick id is returned, which can be used to
     * reference the bunch of goods in the shipping area.
     *
     * @param items  to be picked
     * @param reason for which items are picked (e.g. "customer order")
     * @param refId  Reference id fitting to the reason of the pick (e.g. "order id"). Used to determine which reservations can be used.
     * @return a unique pick ID
     */
    public String pickItems(List<Item> items, String reason, String refId) throws PickOrderNotFulfilledException {
        PickOrder pickOrder = new PickOrder().setItems(items);

        String message;
        try {
            Inventory.getInstance().decreaseInventory(pickOrder);
            System.out.println("# Items picked: " + pickOrder);
            message = pickOrder.getPickId();
        } catch (NotEnoughGoodsException e) {
            System.out.println("# Not enough goods to fulfill pick order: " + pickOrder);
            throw new PickOrderNotFulfilledException();
        }

        return message;
    }

    /**
     * New goods are arrived and inventory is increased
     */
    public void topUpInventory(String articleId, int amount) {
        // TODO: Implement
    }

}
