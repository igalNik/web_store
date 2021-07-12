package com.example.store_webApp;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ItemDetailsBean implements Serializable {
//    @Inject
//    ItemService itemService;
    private ItemEntity item;

    @PostConstruct
    public void initialize() {
        item = (ItemEntity) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("id");
//        item = itemService.getItemFromList(id);

    }
    public ItemEntity getItem() {
        return item;
    }

}
