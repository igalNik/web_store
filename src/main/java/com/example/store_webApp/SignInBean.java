package com.example.store_webApp;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SignInBean implements Serializable {

    @Inject
    private UserManagerBean userManager;
    @Inject
    private UserService userService;

    private String email;
    private String password;


    public String getEmail() {
            return email;
        }

    public void setEmail(String email) {
        this.email = email.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public String submit() {
        if (signIn(email, password)){
            return "index?faces-redirect=true";
        }
        //  Wrong user name or password
        //  create and set message for the client
        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot(); // get view
        UIComponent component = view.findComponent("signInForm:signInBtn"); // find button component
        FacesMessage message = new FacesMessage("incorrect username or password."); // create message
        message.setSeverity(FacesMessage.SEVERITY_ERROR); // set message type
        FacesContext.getCurrentInstance().addMessage(component.getClientId(), message); // add message to the button component
        return "";
    }
    //get user with the entered email and password
    //if found: set user in userManagerBean
    public boolean signIn(String email, String password){
        UsersEntity currentUser;
        currentUser = userService.getRegisteredUser(email, password);
        if (currentUser != null){
            userManager.logIn(currentUser);
            return true;
        }
        return false;
    }
}
