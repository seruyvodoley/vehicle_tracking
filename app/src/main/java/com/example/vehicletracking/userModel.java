package com.example.vehicletracking;

public class userModel {
    private String userName, userEmail, userPassword, userId;
    public userModel() {}
    public userModel(String userName, String userEmail, String userPassword, String userId){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserEmails() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String setUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
