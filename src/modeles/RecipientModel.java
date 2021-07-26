
package modeles;


public class RecipientModel {
    String regisNo,recipientDate,circularNo,circularDate,circularDir,topic,saveFile;
    

    public RecipientModel(String regisNo, String recipientDate, String circularNo, String circularDate, String circularDir, String topic, String saveFile) {
        this.regisNo = regisNo;
        this.recipientDate = recipientDate;
        this.circularNo = circularNo;
        this.circularDate = circularDate;
        this.circularDir = circularDir;
        this.topic = topic;
        this.saveFile = saveFile;
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
