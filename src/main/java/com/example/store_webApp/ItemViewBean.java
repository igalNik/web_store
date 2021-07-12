package com.example.store_webApp;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Named
@ViewScoped
public class ItemViewBean implements Serializable {

    @Inject
    private ItemService itemService;
    private String searchValue;

    private List<ItemEntity> itemsToShow;

    @PostConstruct
    public void initialize() {
        searchValue = "";
        itemsToShow = itemService.itemsEntityList;
    }
    public void search(){
        List<ItemEntity> allItems = itemService.getItemEntityList();
        itemsToShow = new ArrayList<>();
        if(searchValue == null || searchValue.equals("")){
            itemsToShow = allItems;
            return;
        }
        // check which item contain search value and put it on items to show
        for (ItemEntity item : allItems ) {
            if(isValInItem(item, searchValue))
                itemsToShow.add(item);
        }

        return;
    }
    public String goToItem(ItemEntity item){
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("id", item);
        return "item-details-page?faces-redirect=true&includeViewParams=true";
    }

    public boolean isValInItem(ItemEntity item,String value){
        return item.getGender().toLowerCase().contains(searchValue.toLowerCase())
                || item.getType().toLowerCase().contains(searchValue.toLowerCase())
                || item.getStyle().toLowerCase().contains(searchValue.toLowerCase())
                || item.getSeason().toLowerCase().contains(searchValue.toLowerCase())
                || item.getDescription().toLowerCase().contains(searchValue.toLowerCase())
                || item.getPatternType().toLowerCase().contains(searchValue.toLowerCase())
                || item.getMaterial().toLowerCase().contains(searchValue.toLowerCase())
                || item.getColor().toLowerCase().contains(searchValue.toLowerCase())
                || item.getSize().toLowerCase().contains(searchValue.toLowerCase());
    }

    public List<ItemEntity> getItemsToShow() {
        return itemsToShow;
    }

    public void setItemsToShow(List<ItemEntity> itemsToShow) {
        this.itemsToShow = itemsToShow;
    }

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }
}
