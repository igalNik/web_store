package com.example.store_webApp;

import javax.enterprise.context.SessionScoped;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserManagerBean implements Serializable {

    private UsersEntity currentUser;

    private UserType userType = UserType.GUEST;

    public UsersEntity getCurrentUser() {
        return currentUser;
    }


    public String signOut() {
        // End the session, removing any session state, including the current user and content of the cart
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        // Redirect is necessary to let the browser make a new GET request
        // So the previous user does not stay connected
        return "index?faces-redirect=true";
    }

    public boolean isSignedIn() {
        return userType != UserType.GUEST;
    }

    public boolean isManager() {
        return userType == UserType.MANAGER;
    }

    public boolean isUser() {
        return userType == UserType.USER;
    }
    //get user with the entered email and password
    //if found: set user
    public void logIn(UsersEntity user){
            currentUser = user;
            this.setUserType(user.getUserType());
    }
    // receives user type as string and setting userType as Enum
    private void setUserType(String userType) {
        switch (currentUser.getUserType()) {
            case "USER":
                this.userType = UserType.USER;

            case "MANAGER":
                this.userType = UserType.MANAGER;

        }
    }
}
