package io.flowing.retail.shipping.messages.payload;

public class CustomerPayload {

  private String name;
  private String address;

  private String email;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }

  public String getEmail() {return email;}
  public void setEmail(String email) {this.email = email;}
}
