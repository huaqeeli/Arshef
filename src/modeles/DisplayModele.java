package modeles;

public class DisplayModele {

    String displayid, displayDate, destination, topic, displayType, notes,squ;
    int squence;

    public DisplayModele(String displayid, String displayDate, String destination, String topic, String displayType, String notes, int squence) {
        this.displayid = displayid;
        this.displayDate = displayDate;
        this.destination = destination;
        this.topic = topic;
        this.displayType = displayType;
        this.notes = notes;
        this.squence = squence;
    }

    public String getSqu() {
        return squ;
    }

    public void setSqu(String squ) {
        this.squ = squ;
    }

    public DisplayModele() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "DisplayModele{" + "displayid=" + displayid + ", displayDate=" + displayDate + ", destination=" + destination + ", topic=" + topic + ", displayType=" + displayType + ", notes=" + notes + ", squence=" + squence + '}';
    }

   

}
