package modeles;

public class ArchefModel {

    String circularid, circularDate, receiptNumber, receiptDate, topic, destination, saveFile, action,recordYear;
    int squnce;

    public ArchefModel(String circularid, String circularDate, String receiptNumber, String receiptDate, String topic, String destination, String saveFile, String action, int squnce) {
        this.circularid = circularid;
        this.circularDate = circularDate;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.topic = topic;
        this.destination = destination;
        this.saveFile = saveFile;
        this.action = action;
        this.squnce = squnce;
    }

    public ArchefModel() {
    }

    public ArchefModel(String circularid, String circularDate, String receiptNumber, String receiptDate, String topic, String destination, String saveFile, String action, String recordYear) {
        this.circularid = circularid;
        this.circularDate = circularDate;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.topic = topic;
        this.destination = destination;
        this.saveFile = saveFile;
        this.action = action;
        this.recordYear = recordYear;
    }

    public String getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
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

    public String getCircularDate() {
        return circularDate;
    }

    public void setCircularDate(String circularDate) {
        this.circularDate = circularDate;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    @Override
    public String toString() {
        return "ArchefModel{" + "circularid=" + circularid + ", circularDate=" + circularDate + ", receiptNumber=" + receiptNumber + ", receiptDate=" + receiptDate + ", topic=" + topic + ", destination=" + destination + ", saveFile=" + saveFile + ", action=" + action + ", squnce=" + squnce + '}';
    }


}
