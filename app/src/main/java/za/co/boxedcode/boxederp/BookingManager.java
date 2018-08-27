package za.co.boxedcode.boxederp;

public class BookingManager {
    public String user_id, from_date, to_date, registration, brand, model, color, location, booking_date, key;

    public BookingManager() {
    }

    public BookingManager(String user_id, String from_date, String to_date, String registration, String brand, String model, String color, String location, String booking_date, String key) {
        this.user_id = user_id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.registration = registration;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.location = location;
        this.booking_date = booking_date;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getRegistration() {
        return registration;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public String getLocation() {
        return location;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
