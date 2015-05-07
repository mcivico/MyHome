package com.infobosccoma.projecte.myhome.Model;

/**
 * Created by Marc on 07/05/2015.
 */
public class shoppingcard {

    private int idShoppingCard,quantity,idFlatShopping;
    private String productName;

    public shoppingcard(int idShoppingCard, int quantity, int idFlatShopping, String productName) {
        this.idShoppingCard = idShoppingCard;
        this.quantity = quantity;
        this.idFlatShopping = idFlatShopping;
        this.productName = productName;
    }

    public int getIdShoppingCard() {
        return idShoppingCard;
    }

    public void setIdShoppingCard(int idShoppingCard) {
        this.idShoppingCard = idShoppingCard;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getIdFlatShopping() {
        return idFlatShopping;
    }

    public void setIdFlatShopping(int idFlatShopping) {
        this.idFlatShopping = idFlatShopping;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
