package io.flowing.retail.checkout.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
  
  private String orderId = "checkout-generated-" + UUID.randomUUID().toString();
  private Customer customer;
  private List<Item> items = new ArrayList<>();

  private int totalSum = 0;

  public void addItem(String articleId, int amount) throws NotEnoughGoodsException {
    Inventory inventory = Inventory.getInstance();
    try {
      Item item = inventory.takeItem(articleId, amount);
      Item existingItem = removeItem(articleId);
      if (existingItem!=null) {
        item.setAmount(amount + existingItem.getAmount());
      }
      items.add(item);
      totalSum += amount;
    } catch (NotEnoughGoodsException e) {
      System.out.println("# Not enough goods to fulfill order of " + articleId);
      // put items back to inventory
      items.forEach(i -> inventory.getInventory().add(i));
      throw e;
    }
  }

  public Item removeItem(String articleId) {
    for (Item item : items) {
      if (articleId.equals(item.getArticleId())) {
        items.remove(item);
        return item;
      }
    }
    return null;
  }
  
  public String getOrderId() {
    return orderId;
  }
  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
  public List<Item> getItems() {
    return items;
  }
  public void setItems(List<Item> items) {
    this.items = items;
  }
  public int getTotalSum() {return totalSum;}
  public void setTotalSum(int totalSum) {this.totalSum = totalSum;}
  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", items=" + items +  ", totalSum=" + totalSum + "]";
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
