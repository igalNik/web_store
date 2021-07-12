package com.example.store_webApp;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.*;

@ManagedBean
@ViewScoped
public class ItemRegistrationBean implements Serializable {

    @ManagedProperty(value = "#{itemService}")
    private ItemService itemService;


    // file parameters for image upload
    private Part file;
    private String message;
    private final String PATH = "/Users/igal/IdeaProjects/store_webApp/src/main/webapp";
    private String fileName;
    public Part getFile() {
        return file;
    }


    // holds the new item
    private ItemEntity item;
    // bool that define if render "fromExist" panel grid
    private boolean fromExist;
    // holds the id of existing item
    private long itemToLoadId;

    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public ItemRegistrationBean() {
        this.item = new ItemEntity();
    }

    public boolean isFromExist() {
        return fromExist;
    }
    public void setFromExist(boolean fromExist) {
        this.fromExist = fromExist;
    }

    public long getItemToLoadId() {
        return itemToLoadId;
    }
    public void setItemToLoadId(long itemToLoadId) {
        this.itemToLoadId = itemToLoadId;
    }

    public ItemEntity getItem() {
        return item;
    }
    public void setItem(ItemEntity item) {
        this.item = item;
    }


    // retrieves existing item from DB
    public void loadItem(){
        item = new ItemEntity(itemService.entityManager.find(ItemEntity.class, itemToLoadId));
        item.setImageName("");
    }

    // saving new item to DB and uploading image to server
    public String submit() {


        if (    item.getGender()!=null
             && item.getType()!=null
             && item.getSeason()!=null
             && item.getStyle()!=null
             && item.getColor()!=null
             && item.getSize()!=null
             && item.getDescription()!=null
             && item.getPatternType()!=null
             && item.getQuantity()!=null
             && item.getPrice()!=null)
        {
            // define file name
            item.setImageName(fileName = getFileNameFromPart(file));
            // add item to DB and save result in res
            boolean res = itemService.addItem(item);
            if (!res){ // persist failed -> return to same page
                UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot(); // get view
                UIComponent component = view.findComponent("itemRegistrationForm:submitBtn"); // find button component
                FacesMessage message = new FacesMessage("item already exist"); // create message
                message.setSeverity(FacesMessage.SEVERITY_ERROR); // set message type
                FacesContext.getCurrentInstance().addMessage(component.getClientId(), message); // add message to the button component
                return "";
            }

            // upload image
            try {
                uploadFile();

            } catch (IOException e) {
                /* image upload failed
                 *  creating and setting message for the client
                 * */
                UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot(); // get view
                UIComponent component = view.findComponent("itemRegistrationForm:submitBtn"); // find button component
                FacesMessage message = new FacesMessage("image upload failed"); // create message
                message.setSeverity(FacesMessage.SEVERITY_ERROR); // set message type
                FacesContext.getCurrentInstance().addMessage(component.getClientId(), message); // add message to the button component
                return "";
            }
            return "inventory-management";
        }
        return "";
    }


    // methods for file upload
    public void setFile(Part file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPATH() {
        return PATH;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String uploadFile() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        boolean fileSuccess = false;
        try{
            if (file.getSize() > 0) {
                fileName = getFileNameFromPart(file);

                File outputFile = new File(  PATH
                        + File.separator + "resources"
                        + File.separator + "images"
                        + File.separator + fileName);

                inputStream = file.getInputStream();
                outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();
                fileSuccess = true;
            }

            if (fileSuccess) {
                System.out.println("File uploaded to : " + PATH);
                /**
                 * set the success message when the file upload is successful
                 */
                setMessage("File successfully uploaded to " + PATH);
            } else {
                /**
                 * set the error message when error occurs during the file upload
                 */
                setMessage("Error, select file!");
            }
            /**
             * return to the same view
             */
            return null;
        }catch (Exception e){
            message = "image upload failed";
            return null;
        }
    }

    public String getFileNameFromPart(Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                return fileName;
            }
        }
        return null;
    }
}
