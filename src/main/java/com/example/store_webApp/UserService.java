package com.example.store_webApp;

import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class UserService implements Serializable {


    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
                                                                                    "clothingStorePU");
    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private final EntityTransaction entityTransaction = entityManager.getTransaction();

    TypedQuery<UsersEntity> findAllUsers = entityManager.createQuery(
            "SELECT user FROM UsersEntity AS user", UsersEntity.class);

    List<UsersEntity> usersEntityList = findAllUsers.getResultList();

    public String addUser(UsersEntity user) {

        try {
            entityTransaction.begin();
            entityManager.merge(user);
            entityTransaction.commit();
            usersEntityList.add(getRegisteredUser(user.getEmail(), user.getPassword()));
        } catch (Exception e) {
            entityTransaction.rollback();
        }
        return "index";
    }

    public String removeUser(UsersEntity user){
        entityTransaction.begin();
        entityManager.remove(entityManager.find(UsersEntity.class,user.getId()));
        entityTransaction.commit();
        return "index";
    }

    // Return's registered user if exist
    public UsersEntity getRegisteredUser(String email, String password){
        // create query to get user
        TypedQuery<UsersEntity> findUser = entityManager.createQuery(
                "SELECT user FROM UsersEntity user WHERE user.email=:email AND user.password=:password",
                UsersEntity.class);

        // set query email parameter
        findUser.setParameter("email", email);

        // set query password parameter
        findUser.setParameter("password", password);

        try {
            return findUser.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    // checks if user already exist in DB
    public boolean checkIfEmailExist(String email){
        // create query to get user with asked email
        TypedQuery<UsersEntity> findUser = entityManager.createQuery(
                "SELECT user FROM UsersEntity user WHERE user.email=:email",
                UsersEntity.class);

        // set query email parameter
        findUser.setParameter("email", email);

        try {
            findUser.getSingleResult();
            FacesMessage msg = new FacesMessage("User: " + email + " - already exist");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
    public String removeUserById(Long userId){

        // Find the Order
        for (UsersEntity user : usersEntityList) {
            if (user.getId() == userId) {
                try {
                    entityTransaction.begin();
                    // Update the requested user
                    entityManager.remove(entityManager.find(UsersEntity.class,user.getId()));
                    entityTransaction.commit();
                    removeFromList(user);
                    return "Successes";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "Failure";
    }
    public String updateUserById(Long userId){
        // Find the Order
        for (UsersEntity user : usersEntityList) {
            if (user.getId() == userId) {
                try {
                    entityTransaction.begin();
                    // Update the requested order
                    entityManager.merge(entityManager.find(UsersEntity.class,user.getId()));
                    entityTransaction.commit();
                    return "Successes";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "Failure";
    }
    public void onRemove(UsersEntity user){
        String removeResult = removeUserById(user.getId());
        FacesMessage msg = new FacesMessage("user: " + user.getId() + " removed",removeResult);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void onEdit(RowEditEvent event) {
        UsersEntity user = (UsersEntity) event.getObject();
        String updateResult = updateUserById(user.getId());
        FacesMessage msg = new FacesMessage("User: " + user.getId() + " edited",updateResult);
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    public void removeFromList(UsersEntity user) {
        usersEntityList.removeIf(itUsers -> itUsers.getId() == user.getId());
    }
    public void updateUserInList(UsersEntity updatedUser){
        for (UsersEntity user: usersEntityList) {
            if (user.getId() == updatedUser.getId()){
                user.setUserType(updatedUser.getUserType());
                user.setFirstName(updatedUser.getFirstName());
                user.setLastName(updatedUser.getLastName());
                user.setUserId(updatedUser.getUserId());
                user.setEmail(updatedUser.getEmail());
                user.setPhone(updatedUser.getPhone());
                user.setCity(updatedUser.getCity());
                user.setAddress(updatedUser.getAddress());
                user.setZip(updatedUser.getZip());
                user.setPassword(updatedUser.getPassword());
            }
        }
    }
    public List<UsersEntity> getItemsEntityList() {
        return usersEntityList;
    }

    public void setItemsEntityList(List<UsersEntity> itemsEntityList) {
        this.usersEntityList = itemsEntityList;
    }
}



