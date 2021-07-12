package com.example.store_webApp;

import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ManagedBean
@ApplicationScoped
public class ItemService {

    private String searchValue = "";

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("clothingStorePU");

    EntityManager entityManager = entityManagerFactory.createEntityManager();

    EntityTransaction entityTransaction = entityManager.getTransaction();

    TypedQuery<ItemEntity> findAllProducts = entityManager.createQuery(
            "SELECT clothing FROM ItemEntity AS clothing", ItemEntity.class);

    List<ItemEntity> itemsEntityList = findAllProducts.getResultList();

    Map<Long, ItemEntity> itemsMap = itemsEntityList.stream().collect(Collectors.toMap(ItemEntity::getId, itemEntity -> itemEntity));


    public ItemEntity getItem(long id){
        return itemsMap.get(id);
    }

     /*
     persisting entity to DB
     return true if success and false for failure
     */
    public boolean persis(ItemEntity item) {
        boolean result;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("clothingStorePU");
        EntityManager entityManager         = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try{
            entityManager.persist(item);

            entityTransaction.commit();
            result = true;
        }
        catch(Exception e){
            entityTransaction.rollback();
            result = false;
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }
    /*
     merging entity to DB
     return true if success and false for failure
     */
    public boolean merge(ItemEntity item) {
        boolean result;
        EntityManager entityManager         = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try{
            entityManager.merge(item);
            entityTransaction.commit();
            result = true;
        }
        catch(Exception e){
            entityTransaction.rollback();
            result = false;
        }finally{
            entityManager.close();
        }
        return result;
    }
    /*
     removing entity from DB
     return true if success and false for failure
     */
    public boolean remove(ItemEntity item) {
        boolean result;
        EntityManager entityManager         = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        try{
            entityManager.remove(entityManager.find(ItemEntity.class,item.getId()));
            entityTransaction.commit();
            result = true;
        }
        catch(Exception e){
            entityTransaction.rollback();
            result = false;
        }finally{
            entityManager.close();
        }
        return result;
    }

    /*
     removing entity from DB
     return true if success and false for failure
     */
    public ItemEntity find(ItemEntity item) {
        ItemEntity result = null;
        EntityManager entityManager         = entityManagerFactory.createEntityManager();
        try{
            result = entityManager.find(ItemEntity.class,item.getId());
        }
        catch(Exception e){

        }finally{
            entityManager.close();
        }
        return result;
    }

    public boolean addItem(ItemEntity item){
        if (isAlreadyExist(item)){
            FacesMessage msg = new FacesMessage("item already exist");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return false;
        }
        if (persis(item)){
            itemsEntityList.add(item);
            FacesMessage msg = new FacesMessage("item: " + item.getId() + " persist successes");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return true;
        }
        FacesMessage msg = new FacesMessage("item: " + item.getId() + " persist failed");
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return false;
    }
    public void updateItem(ItemEntity item){

        if (checkIfItemExistInDB(item)){ // massage defined in method checkIfItemExistInDB
            item = find(item);
            return;
        }
        if (merge(item)){
            itemsEntityList.add(item);
            FacesMessage msg = new FacesMessage("item: " + item.getId() + " updated");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        FacesMessage msg = new FacesMessage("item: " + item.getId() + " update failed");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    public void removeItem(ItemEntity item){
        if (remove(item)){
            removeFromList(item);
            FacesMessage msg = new FacesMessage("item: " + item.getId() + " removed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        }
        FacesMessage msg = new FacesMessage("item: " + item.getId() + " remove failed");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<ItemEntity> getItemEntityList() {
        return itemsEntityList;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void updateAfterOrder(ItemEntity item){

        try{
            entityTransaction.begin();
            entityManager.merge(entityManager.find(ItemEntity.class,item.getId()));
            entityTransaction.commit();
        }catch (Exception e){
            FacesMessage msg = new FacesMessage("Operation failed");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }
    public boolean isAlreadyExist(ItemEntity item){
        for (ItemEntity itemInList:itemsEntityList) {
            if (itemInList.equals(item))
                return true;
        }
    return false;
    }
    public ItemEntity getItemFromList(Long id){
        for (ItemEntity itemInList:itemsEntityList) {
            if (itemInList.getId() == id)
                return itemInList;
        }
        return null;
    }
    // on remove action
    public void onRemove(ItemEntity item){
        removeItem(item);
    }
    // edit listener
    public void onEdit(RowEditEvent event) {
        ItemEntity item = (ItemEntity) event.getObject();
        updateItem(item);
    }
    //edit cancel listener
    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edit Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    public void removeFromList(ItemEntity item) {
        itemsEntityList.removeIf(itItems -> itItems.getId() == item.getId());
    }
    public boolean checkIfItemExistInDB(ItemEntity item){
        // create query to get same item
        TypedQuery<ItemEntity> findItem = entityManager.createQuery(
                "SELECT item FROM ItemEntity item WHERE item.gender=:gender AND item.type=:itemType AND item.style=:style AND item.season=:season AND item.description=:description AND item.color=:color AND item.size=:size AND item.material=:material AND item.patternType=:patternType",
                ItemEntity.class);

        // set query parameters
        findItem.setParameter("gender", item.getGender());
        findItem.setParameter("itemType", item.getType());
        findItem.setParameter("style", item.getStyle());
        findItem.setParameter("season", item.getSeason());
        findItem.setParameter("description", item.getDescription());
        findItem.setParameter("color", item.getColor());
        findItem.setParameter("size", item.getSize());
        findItem.setParameter("material", item.getMaterial());
        findItem.setParameter("patternType", item.getPatternType());

        try {
            List<ItemEntity> exist = findItem.getResultList();
            if (exist.size() > 0) { // if found dont save in DB
                FacesMessage msg = new FacesMessage("Not Saved! Item record: " + item.getId() + " - already exist in " + exist.get(0).getId());
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return true;
            }
            else
                return false;

        } catch (NoResultException e) {
            return false;
        }
    }
}
