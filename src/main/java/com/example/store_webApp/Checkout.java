package com.example.store_webApp;

import com.example.store_webApp.*;
import org.intellij.lang.annotations.Pattern;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@ManagedBean
@ViewScoped
public class Checkout implements Serializable {
    // flags that indicates which section to show
    private boolean userAndShipping;
    private boolean payment;
    private boolean review;
    private boolean result_success;
    private boolean result_failure;
    private boolean result_payment;
    private boolean nextButton;
    private boolean backButton;
    private boolean placeOrderButton;
    private boolean homeButton;
    private boolean cancelButton;

    // services
    @ManagedProperty(value = "#{itemService}")
    private ItemService itemService;

    @ManagedProperty(value = "#{orderService}")
    private OrderService orderService;

    @ManagedProperty(value = "#{userManagerBean}")
    private UserManagerBean userManager;

    @ManagedProperty(value = "#{cartBean}")
    private CartBean cart;

    // order entity to persist
    private OrdersEntity order;

    // payment details
    private String cardType;
    private String nameOnCard;
    private String cardNumber;
    private String expiryDate;
    private String securityCode;

    @PostConstruct
    public void initialize() {
        userAndShipping = true;
        nextButton=true;
        cancelButton=true;
        if (userManager.isSignedIn()) {
            order = new OrdersEntity();
            order.setStatus(OrderStatus.IN_PROCESS.toString());

            // put cart items in the order
            order.setItems(cart.getLines());

            if (userManager.isSignedIn()){
                // set order for current user
                UsersEntity user = userManager.getCurrentUser();
                order.setUser(user);
                order.setOrderFor(user.getFirstName() + " " + user.getLastName());
                order.setCity(user.getCity());
                order.setAddress(user.getAddress());
                order.setZip(user.getZip());
                order.setPhone(user.getPhone());
                setNameOnCard(order.getUser().getFirstName() + " " + order.getUser().getLastName());
            }
        }
    }

    public void next(){
        if (userAndShipping){
            // if its user and shipping page and the fillings are filled correctly
            if (order.getOrderFor()!=null
                && order.getCity()!=null
                && order.getAddress()!=null
                && order.getZip()!=null
            ){

                userAndShipping = false;
                payment = true;
                backButton = true;
            }
        }
        else if(payment){
            // if its payment page and the fillings are filled correctly
            if(nameOnCard!=null
                && cardType!=null
                && cardNumber!=null
                && expiryDate !=null
                && securityCode!=null
            ){
                // move to review
                payment = false;
                review = true;
                nextButton = false;
                placeOrderButton = true;
            }
        }
    }
    // defines the which part to show
    public void back(){
        if(payment){
            payment = false;
            userAndShipping = true;
            backButton=false;
        }
        else if(review){
            review = false;
            payment = true;
            placeOrderButton=false;
            nextButton=true;

        }
    }
    // palace order method
    public void placeOrder (){
        if(makePayment()){
            result_payment=true;
        }
        else{
            result_payment =false;
            result_failure =true;
            return;
        }
        order.setAmount(cart.getTotalAmount());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROCESS.toString());
        userAndShipping=false;
        payment=false;
        review=false;
        result_success=false;
        nextButton=false;
        backButton=false;
        placeOrderButton=false;
        homeButton=true;
        cancelButton = false;

        // increase num of orders for all items
        for (ItemLineEntity line: order.getItems()) {
            ItemEntity item = line.getItem();
            int numOfOrders = item.getNumOfOrders();
            int itemQuantity = item.getQuantity();

            // increase num of orders for all items
            item.setNumOfOrders(numOfOrders + line.getQuantity());
            // decrease num of quantity in inventory for all items
            item.setQuantity(itemQuantity-line.getQuantity());
            try{
                itemService.updateAfterOrder(item);
            }catch (Exception e){

            }
        }

        try {

            orderService.addOrder(this.order);
        }
        catch (Exception e) {
            result_failure=true;
        }
        if (!result_failure)
            result_success=true;

        cart.empty();

    }
    // validator for expires date - checks if date in future and the date pattern
    public void validateExpiresDate(FacesContext context, UIComponent component, Object value){
        String date = (String) value;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        simpleDateFormat.setLenient(false);
        boolean expired;
        Date expiry;
        try{
            expiry = simpleDateFormat.parse(date);
        }catch (ParseException e){
            throw new ValidatorException(new FacesMessage("Date Patternt is: MM/YYYY"));
        }
        expired = expiry.before(new Date());
        if(expired){
            throw new ValidatorException(new FacesMessage("This card Expired"));
        }
    }
    // simulation of payment
    public boolean makePayment(){
        return true;
    }

    // setters for services
    public void setItemService(ItemService itemService) {
        this.itemService = itemService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void setUserManager(UserManagerBean userManager) {
        this.userManager = userManager;
    }

    public void setCart(CartBean cart) {
        this.cart = cart;
    }

    // getter and setter for order
    public OrdersEntity getOrder() {
        return order;
    }
    public void setOrder(OrdersEntity order) {
        this.order = order;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }
    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        String securedCardNumber;
        if(cardNumber != null && review) {
            securedCardNumber = cardNumber.replaceAll("\\w(?=\\w{4})", "*");
            return securedCardNumber;
        }
        else
            return cardNumber;
        }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getSecurityCode() {
        return securityCode;
    }
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public boolean isUserAndShipping() {
        return userAndShipping;
    }
    public void setUserAndShipping(boolean userAndShipping) {
        this.userAndShipping = userAndShipping;
    }

    public boolean isPayment() {
        return payment;
    }
    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public boolean isReview() {
        return review;
    }
    public void setReview(boolean review) {
        this.review = review;
    }

    public boolean isResult_success() {
        return result_success;
    }

    public void setResult_success(boolean result_success) {
        this.result_success = result_success;
    }

    public boolean isResult_failure() {
        return result_failure;
    }

    public void setResult_failure(boolean result_failure) {
        this.result_failure = result_failure;
    }

    public boolean isNextButton() {
        return nextButton;
    }

    public void setNextButton(boolean nextButton) {
        this.nextButton = nextButton;
    }

    public boolean isBackButton() {
        return backButton;
    }

    public void setBackButton(boolean backButton) {
        this.backButton = backButton;
    }

    public boolean isPlaceOrderButton() {
        return placeOrderButton;
    }

    public void setPlaceOrderButton(boolean placeOrderButton) {
        this.placeOrderButton = placeOrderButton;
    }

    public boolean isHomeButton() {
        return homeButton;
    }

    public void setHomeButton(boolean homeButton) {
        this.homeButton = homeButton;
    }

    public boolean isCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(boolean cancelButton) {
        this.cancelButton = cancelButton;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public boolean isResult_payment() {
        return result_payment;
    }

    public void setResult_payment(boolean result_payment) {
        this.result_payment = result_payment;
    }
}
