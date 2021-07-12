package com.example.store_webApp;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item_line", schema = "public", catalog = "clothingStore")
public class ItemLineEntity {

    private Long id;
    private int quantity;
    private BigDecimal amount;
    private ItemEntity item;
    public ItemLineEntity() {

    }

    public ItemLineEntity(ItemEntity item) {
        this.item = item;
        this.quantity = 1;
        this.amount = item.getPrice();

    }

    @Id
    @GeneratedValue
    @Basic(optional = true)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "amount", nullable = true, precision = 2)
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @ManyToOne
    public ItemEntity getItem() {
        return item;
    }
    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public void incrementQuantityAndAmount() {
        if (quantity < item.getQuantity()) {
            quantity++;
            amount = amount.add(item.getPrice());
        }
    }


    public boolean decrementQuantityAndAmount() {
        quantity--;
        amount = amount.subtract(item.getPrice());
        return quantity == 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemLineEntity that = (ItemLineEntity) o;

        if (quantity != that.quantity) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + quantity;
        return result;
    }
}