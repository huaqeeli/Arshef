
package modeles;


public class SecretModel {
    private int squnse;
    private String id,circularid,circulardate,receiptNumber,receiptNumberDate,destination,topic,saveFile,note,recordYear;

    public SecretModel() {
    }

    public SecretModel(int squnse, String id, String circularid, String circulardate, String receiptNumber, String receiptNumberDate, String destination, String topic, String saveFile, String note, String recordYear) {
        this.squnse = squnse;
        this.id = id;
        this.circularid = circularid;
        this.circulardate = circulardate;
        this.receiptNumber = receiptNumber;
        this.receiptNumberDate = receiptNumberDate;
        this.destination = destination;
        this.topic = topic;
        this.saveFile = saveFile;
        this.note = note;
        this.recordYear = recordYear;
    }

   

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getReceiptNumberDate() {
        return receiptNumberDate;
    }

    public void setReceiptNumberDate(String receiptNumberDate) {
        this.receiptNumberDate = receiptNumberDate;
    }

    public int getSqunse() {
        return squnse;
    }

    public void setSqunse(int squnse) {
        this.squnse = squnse;
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

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
    }

    

}
