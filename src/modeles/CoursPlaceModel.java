
package modeles;

public class CoursPlaceModel {
    String placeid,placeName;

    public CoursPlaceModel(String placeid, String placeName) {
        this.placeid = placeid;
        this.placeName = placeName;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "CoursPlaceModel{" + "placeid=" + placeid + ", placeName=" + placeName + '}';
    }
    
}
