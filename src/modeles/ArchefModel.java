package modeles;

public class ArchefModel {

    String circularid, circularDate, receiptNumber, receiptDate, topic, destination, saveFile, circularType;
    int squnce;

    public ArchefModel(String circularid, String circularDate, String receiptNumber, String receiptDate, String destination, String topic, String saveFile, String circularType, int squnce) {
        this.circularid = circularid;
        this.circularDate = circularDate;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.destination = destination;
        this.topic = topic;
        this.saveFile = saveFile;
        this.circularType = circularType;
        this.squnce = squnce;
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

    public String getCircularType() {
        return circularType;
    }

    public void setCircularType(String circularType) {
        this.circularType = circularType;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    @Override
    public String toString() {
        return "ArchefModel{" + "circularid=" + circularid + ", circularDate=" + circularDate + ", receiptNumber=" + receiptNumber + ", receiptDate=" + receiptDate + ", topic=" + topic + ", destination=" + destination + ", saveFile=" + saveFile + ", circularType=" + circularType + ", squnce=" + squnce + '}';
    }

}
