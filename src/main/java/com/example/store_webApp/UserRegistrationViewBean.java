package com.example.store_webApp;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@ManagedBean
@ViewScoped
public class UserRegistrationViewBean implements Serializable {
    @ManagedProperty(value = "#{userService}")
    UserService userService;

    private String email;

    private String firstName;
    private String lastName;
    private String userType;
    private String city;
    private String address;
    private String zip;
    private String phone;
    private String password;
    private String userId;
    private boolean isManager = false;

    private String nameMsg = "name should be 2-20 characters long and contains only characters";
    private String idMsg = "ID should be 9 characters long and contains only digits";

    public String getNameMsg() {
        return firstName;
    }

    public String getIdMsg() {
        return lastName;
    }


    @PostConstruct
    public void initialize() {
    }
    public String submit() {
        // check that all the field are valid
        if(firstName !=null && lastName !=null && userId !=null
            && phone !=null && email !=null && city !=null
            && address !=null && zip !=null && password !=null
            && !userService.checkIfEmailExist(email)){

            UsersEntity user = new UsersEntity();
            if (isManager) {
                user.setUserType("Manager");
            } else {
                user.setUserType("Client");
            }

            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserId(userId);
            user.setEmail(email);
            user.setPhone(phone);

            user.setCity(city);
            user.setAddress(address);
            user.setZip(zip);

            user.setPassword(password);

            // add user to data base
            return userService.addUser(user);
        }


        return "";
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }


}
