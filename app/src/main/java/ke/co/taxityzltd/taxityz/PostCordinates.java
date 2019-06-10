package ke.co.taxityzltd.taxityz;

/**
 * Created by Amuri Bonface on 1/19/2018.
 */

public class PostCordinates {
    private String rideId;
    private String Longitude;
    private String Latitude;
    private String Email;
    private String myPhone;
    private int totalPrice;
    private String distance;
    private String myName;
    private String paymentMode;
    private String pickupName;
    private String destinationName;
    private String destinationLatitude;
    private String destinationLongitude;

    public PostCordinates(String rideId, String longitude, String latitude, String email, String myPhone, int totalPrice, String distance, String myFname, String myLname, String paymentMethod, String pickupName, String destinationName, String drop_latitude, String drop_longitude){

    }

    public PostCordinates(String rideId, String longitude, String latitude, String email, String myPhone, int totalPrice, String distance, String myName, String paymentMode, String pickupName, String destinationName, String destinationLatitude, String destinationLongitude) {
        this.rideId = rideId;
        Longitude = longitude;
        Latitude = latitude;
        Email = email;
        this.myPhone = myPhone;
        this.totalPrice = totalPrice;
        this.distance = distance;
        this.myName = myName;
        this.paymentMode = paymentMode;
        this.pickupName = pickupName;
        this.destinationName = destinationName;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
