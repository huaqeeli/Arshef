
package modeles;


public class SecretModel {
    private int squnse;
    private String circularid,circulardate,destination,topic,saveFile,note,recordYear;

    public SecretModel() {
    }

    public SecretModel(int squnse, String circularid, String circulardate, String destination, String topic, String saveFile, String note, String recordYear) {
        this.squnse = squnse;
        this.circularid = circularid;
        this.circulardate = circulardate;
        this.destination = destination;
        this.topic = topic;
        this.saveFile = saveFile;
        this.note = note;
        this.recordYear = recordYear;
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
