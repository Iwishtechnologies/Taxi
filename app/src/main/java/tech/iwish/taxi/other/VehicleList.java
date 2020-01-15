package tech.iwish.taxi.other;

public class VehicleList {
    String 	catagory_id ;
    String 	catagory_name;
    String vehicle_type;
    String waitingRate_m;
    String totrate;
    String tottime;

    public VehicleList(String catagory_id, String catagory_name, String vehicle_type, String waitingRate_m, String totrate, String tottime) {
        this.catagory_id = catagory_id;
        this.catagory_name = catagory_name;
        this.vehicle_type = vehicle_type;
        this.waitingRate_m = waitingRate_m;
        this.totrate = totrate;
        this.tottime = tottime;
    }

    public String getCatagory_id() {
        return catagory_id;
    }

    public void setCatagory_id(String catagory_id) {
        this.catagory_id = catagory_id;
    }

    public String getCatagory_name() {
        return catagory_name;
    }

    public void setCatagory_name(String catagory_name) {
        this.catagory_name = catagory_name;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getWaitingRate_m() {
        return waitingRate_m;
    }

    public void setWaitingRate_m(String waitingRate_m) {
        this.waitingRate_m = waitingRate_m;
    }

    public String getTotrate() {
        return totrate;
    }

    public void setTotrate(String totrate) {
        this.totrate = totrate;
    }

    public String getTottime() {
        return tottime;
    }

    public void setTottime(String tottime) {
        this.tottime = tottime;
    }
}
