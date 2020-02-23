package tech.iwish.taxi.other;

public class RideHistroy {
    public String id;
    public String driver_id;
    public String pickup_city_name;
    public String drop_city_name;
    public String amount;
    public String vehicle_cat;
    public String date;
    public String time;
    public String status;
    public String 	trackingid;
    public String image;

    public RideHistroy(String id, String driver_id, String pickup_city_name, String drop_city_name, String amount, String vehicle_cat, String date, String time, String status, String trackingid, String image) {
        this.id = id;
        this.driver_id = driver_id;
        this.pickup_city_name = pickup_city_name;
        this.drop_city_name = drop_city_name;
        this.amount = amount;
        this.vehicle_cat = vehicle_cat;
        this.date = date;
        this.time = time;
        this.status = status;
        this.trackingid = trackingid;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getPickup_city_name() {
        return pickup_city_name;
    }

    public void setPickup_city_name(String pickup_city_name) {
        this.pickup_city_name = pickup_city_name;
    }

    public String getDrop_city_name() {
        return drop_city_name;
    }

    public void setDrop_city_name(String drop_city_name) {
        this.drop_city_name = drop_city_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVehicle_cat() {
        return vehicle_cat;
    }

    public void setVehicle_cat(String vehicle_cat) {
        this.vehicle_cat = vehicle_cat;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingid() {
        return trackingid;
    }

    public void setTrackingid(String trackingid) {
        this.trackingid = trackingid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
