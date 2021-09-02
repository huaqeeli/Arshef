package modeles;

public class InternalIncomingModel {

    String regisNo, recipientDate, circularNo, circularDate, circularDir, topic, saveFile, notes,recordYear;

    public InternalIncomingModel(String regisNo, String recipientDate, String circularNo, String circularDate, String circularDir, String topic, String saveFile, String notes, String recordYear) {
        this.regisNo = regisNo;
        this.recipientDate = recipientDate;
        this.circularNo = circularNo;
        this.circularDate = circularDate;
        this.circularDir = circularDir;
        this.topic = topic;
        this.saveFile = saveFile;
        this.notes = notes;
        this.recordYear = recordYear;
    }

    public InternalIncomingModel() {
    }

    public String getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
    }

   

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public String getRegisNo() {
        return regisNo;
    }

    public void setRegisNo(String regisNo) {
        this.regisNo = regisNo;
    }

    public String getRecipientDate() {
        return recipientDate;
    }

    public void setRecipientDate(String recipientDate) {
        this.recipientDate = recipientDate;
    }

    public String getCircularNo() {
        return circularNo;
    }

    public void setCircularNo(String circularNo) {
        this.circularNo = circularNo;
    }

    public String getCircularDate() {
        return circularDate;
    }

    public void setCircularDate(String circularDate) {
        this.circularDate = circularDate;
    }

    public String getCircularDir() {
        return circularDir;
    }

    public void setCircularDir(String circularDir) {
        this.circularDir = circularDir;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
