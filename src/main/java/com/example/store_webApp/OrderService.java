package com.example.store_webApp;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

@ManagedBean
@SessionScoped
public class OrderService implements Serializable {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("clothingStorePU");

    EntityManager entityManager = entityManagerFactory.createEntityManager();

    EntityTransaction entityTransaction = entityManager.getTransaction();

    TypedQuery<OrdersEntity> findAllOrders = entityManager.createQuery(
            "SELECT orders FROM OrdersEntity AS orders", OrdersEntity.class);

    List<OrdersEntity> ordersEntityList = findAllOrders.getResultList();

    List<OrderStatus> statuses = new ArrayList<OrderStatus>(EnumSet.allOf(OrderStatus.class));


    public void addOrder(OrdersEntity order){
        try {
            entityTransaction.begin();
            entityManager.persist(order);
            entityTransaction.commit();
            ordersEntityList.add(order);

        }catch (Exception e) {
            entityTransaction.rollback();
        }

    }

    public String removeOrderById(Long OrderId){

        // Find the Order
        for (OrdersEntity order : ordersEntityList) {
            if (order.getId() == OrderId) {
                try {
                    entityTransaction.begin();
                    // Update the requested order
                    entityManager.remove(entityManager.find(OrdersEntity.class,order.getId()));
                    entityTransaction.commit();
                    removeFromList(order);
                    return "Successes";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "Failure";
    }



    public String updateOrderById(Long OrderId){
            // Find the Order
            for (OrdersEntity order : ordersEntityList) {
                if (order.getId() == OrderId) {
                    try {
                        entityTransaction.begin();
                        // Update the requested order
                        entityManager.merge(entityManager.find(OrdersEntity.class,order.getId()));
                        entityTransaction.commit();
                        return "Successes";
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }
        return "Failure";
    }
    public void onRemove(OrdersEntity order){
        String updateResult = removeOrderById(order.getId());
        FacesMessage msg = new FacesMessage("Order: " + order.getId() + " removed",updateResult);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void onEdit(RowEditEvent event) {
        OrdersEntity order = (OrdersEntity) event.getObject();
        order.setEditedAt(LocalDateTime.now());
        String updateResult = updateOrderById(order.getId());
        FacesMessage msg = new FacesMessage("Order: " + order.getId() + " edited",updateResult);
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    public void removeFromList(OrdersEntity order) {
        ordersEntityList.removeIf(itOrder -> itOrder.getId() == order.getId());
    }
    public List<OrdersEntity> getOrdersEntityList() {
        return ordersEntityList;
    }

    public List<OrderStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<OrderStatus> statuses) {
        this.statuses = statuses;
    }


}
