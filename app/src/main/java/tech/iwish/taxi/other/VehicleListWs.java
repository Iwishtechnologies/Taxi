package tech.iwish.taxi.other;

public class VehicleListWs {
    String lat ;
    String longs;

    public VehicleListWs(String lat, String longs) {
        this.lat = lat;
        this.longs = longs;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
    }
}
