
package modeles;


public class IndexingModel {
   
   String squnce,circularid,circulardate,destination, topic, saveFile;

    public IndexingModel(String squnce, String circularid, String circulardate, String destination, String topic, String saveFile) {
        this.squnce = squnce;
        this.circularid = circularid;
        this.circulardate = circulardate;
        this.destination = destination;
        this.topic = topic;
        this.saveFile = saveFile;
    }

    public IndexingModel() {
       
    }

    public String getSqunce() {
        return squnce;
    }

    public void setSqunce(String squnce) {
        this.squnce = squnce;
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
   
}
