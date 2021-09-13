package modeles;

public class DisplayModele {

    String displayid, displayDate, distnation, topic, displayType, notes, squnces, name,circularid,circulardate,action;
    int tablesquence;

    public DisplayModele(String displayid, String displayDate, String distnation, String topic, String displayType, String notes, int squence) {
        this.displayid = displayid;
        this.displayDate = displayDate;
        this.distnation = distnation;
        this.topic = topic;
        this.displayType = displayType;
        this.notes = notes;
        this.tablesquence = squence;
    }

    public DisplayModele(String displayid, String displayDate, String distnation, String topic, String displayType, String notes, String squnces, String name, int squence) {
        this.displayid = displayid;
        this.displayDate = displayDate;
        this.distnation = distnation;
        this.topic = topic;
        this.displayType = displayType;
        this.notes = notes;
        this.squnces = squnces;
        this.name = name;
        this.tablesquence = squence;
    }
    public DisplayModele() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCircularid() {
        return circularid;
    }

    public void setCircularid(String circularid) {
        this.circularid = circularid;
    }

    public String getCirculardate() {
        return circulardate;
    }

    public void setCirculardate(String circulardate) {
        this.circulardate = circulardate;
    }

    public int getTablesquence() {
        return tablesquence;
    }

    public void setTablesquence(int tablesquence) {
        this.tablesquence = tablesquence;
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

    public String getDistnation() {
        return distnation;
    }

    public void setDistnation(String distnation) {
        this.distnation = distnation;
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
        return tablesquence;
    }

    public void setSquence(int squence) {
        this.tablesquence = squence;
    }

    @Override
    public String toString() {
        return "DisplayModele{" + "displayid=" + displayid + ", displayDate=" + displayDate + ", distnation=" + distnation + ", topic=" + topic + ", displayType=" + displayType + ", notes=" + notes + ", squnces=" + squnces + ", squence=" + tablesquence + '}';
    }

}
