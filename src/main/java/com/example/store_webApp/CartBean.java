package com.example.store_webApp;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ManagedBean
@SessionScoped
public class CartBean implements Serializable {

    private List<ItemLineEntity> lines = new ArrayList<>();

    private int totalQuantity = 0;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    public List<ItemLineEntity> getLines() {
        return lines;
    }

    public void resetLines (){
        this.lines = new ArrayList<>();
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void addItem(ItemEntity item) {
        if (item == null) return;

        if (item.getQuantity() == totalQuantity || item.getQuantity() == 0){
            FacesMessage msg = new FacesMessage("no more items in stock");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        // Find the line for this item, increment quantity and amount if found
        for (ItemLineEntity line : lines) {
            if (line.getItem().getId() == item.getId()) {
                if (line.getItem().getQuantity()>0) {
                    line.incrementQuantityAndAmount();
                    incrementTotalQuantityAndAmount(line.getItem().getPrice());
                }
                return;
            }
        }

        // No line for this product yet, add a new line
        lines.add(new ItemLineEntity(item));
        incrementTotalQuantityAndAmount(item.getPrice());
    }

    public void removeItem(ItemEntity item) {
        Iterator<ItemLineEntity> it = lines.iterator();
        while (it.hasNext()) {
            ItemLineEntity line = it.next();

            // If this is the line for this product, decrement quantity and amount;
            // remove the line if the quantity has become 0
            if (line.getItem().getId() == item.getId()) {
                if (line.decrementQuantityAndAmount()) {
                    it.remove();
                }

                decrementTotalQuantityAndAmount(item.getPrice());
            }
        }
    }

    private void incrementTotalQuantityAndAmount(BigDecimal itemPrice) {
        totalQuantity++;
        totalAmount = totalAmount.add(itemPrice);
    }

    private void decrementTotalQuantityAndAmount(BigDecimal itemPrice) {
        totalQuantity--;
        totalAmount = totalAmount.subtract(itemPrice);
    }
    public void empty() {
        resetLines();
        totalQuantity = 0;
        totalAmount = BigDecimal.ZERO;
    }

}


