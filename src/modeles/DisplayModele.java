package modeles;

public class DisplayModele {

    String displayid, displayDate, destination, topic, displayType;
    int squence ;

    public DisplayModele( String displayid, String displayDate, String destination, String topic, String displayType, int squence) {
        this.displayid = displayid;
        this.displayDate = displayDate;
        this.destination = destination;
        this.topic = topic;
        this.displayType = displayType;
        this.squence = squence;
    }

   
    public String getDisplayid() {
        return displayid;
    }

    public void setDisplayid(String displayid) {
        this.displayid = displayid;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public int getSquence() {
        return squence;
    }

    public void setSquence(int squence) {
        this.squence = squence;
    }

    @Override
    public String toString() {
        return "DisplayModele{" + "displayid=" + displayid + ", displayDate=" + displayDate + ", destination=" + destination + ", topic=" + topic + ", displayType=" + displayType + ", squence=" + squence + '}';
    }

  
    
}
