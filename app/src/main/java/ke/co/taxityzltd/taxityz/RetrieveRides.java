package ke.co.taxityzltd.taxityz;


public class RetrieveRides {

    private String Longitude;
    private String Latitude;
    private String Email;


    public RetrieveRides(){

    }

    public RetrieveRides(String longitude, String latitude, String email) {
        Longitude = longitude;
        Latitude = latitude;
        Email = email;
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
}
