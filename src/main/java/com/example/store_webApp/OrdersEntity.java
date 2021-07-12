package com.example.store_webApp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", schema = "public", catalog = "clothingStore")
public class OrdersEntity {

    private long id;
    private UsersEntity user;

    // shipping and receiver details
    private String orderFor; //full name
    private String city;
    private String address;
    private String zip;
    private String phone;

    private List<ItemLineEntity> items = new ArrayList<ItemLineEntity>();
    private BigDecimal amount = new BigDecimal(BigInteger.ZERO);
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;


    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @Basic(optional = true)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<ItemLineEntity> getItems() {
        return items;
    }
    public void setItems(List<ItemLineEntity> items) {
        this.items = items;
    }

    @ManyToOne
    public UsersEntity getUser() {
        return user;
    }
    public void setUser(UsersEntity user) {
        this.user = user;
    }

    @Basic
    @Column(name = "amount", nullable = true, precision = 2)
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "status", nullable = true, length = 255)
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "edited_at", nullable = true)
    public LocalDateTime getEditedAt() {
        return editedAt;
    }
    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    @Column(name = "order_for", nullable = true)
    public String getOrderFor() {
        return orderFor;
    }
    public void setOrderFor(String orderFor) {
        this.orderFor = orderFor;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Transient
    public String getFormattedCreatedAt(){
        if(createdAt != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return createdAt.format(formatter);
        }
        return "";
    }
    @Transient
    public String getFormattedEditedAt(){
        if(editedAt != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            return editedAt.format(formatter);
        }
        return "";
    }

}
