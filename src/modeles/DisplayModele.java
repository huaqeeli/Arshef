package modeles;

public class DisplayModele {

    String displayid, displayDate, destination, topic, displayType, notes, squnces, name;
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

    public DisplayModele(String displayid, String displayDate, String destination, String topic, String displayType, String notes, String squnces, String name, int squence) {
        this.displayid = displayid;
        this.displayDate = displayDate;
        this.destination = destination;
        this.topic = topic;
        this.displayType = displayType;
        this.notes = notes;
        this.squnces = squnces;
        this.name = name;
        this.squence = squence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DisplayModele(String topic, String notes, String squnces) {
        this.topic = topic;
        this.notes = notes;
        this.squnces = squnces;
    }

    public DisplayModele() {
    }

    public String getSqunces() {
        return squnces;
    }

    public void setSqunces(String squnces) {
        this.squnces = squnces;
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
        return "DisplayModele{" + "displayid=" + displayid + ", displayDate=" + displayDate + ", destination=" + destination + ", topic=" + topic + ", displayType=" + displayType + ", notes=" + notes + ", squnces=" + squnces + ", squence=" + squence + '}';
    }

}
