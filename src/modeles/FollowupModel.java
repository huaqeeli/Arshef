
package modeles;


public class FollowupModel {
    int squnce,openStat;
    String circularid,circulardate,topic,required,status,completiondate,tableName,tableId;

    public FollowupModel() {
    }

    public FollowupModel(int squnce, int openStat, String circularid, String circulardate, String topic, String required, String status, String completiondate, String tableName, String tableId) {
        this.squnce = squnce;
        this.openStat = openStat;
        this.circularid = circularid;
        this.circulardate = circulardate;
        this.topic = topic;
        this.required = required;
        this.status = status;
        this.completiondate = completiondate;
        this.tableName = tableName;
        this.tableId = tableId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

   

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletiondate() {
        return completiondate;
    }

    public void setCompletiondate(String completiondate) {
        this.completiondate = completiondate;
    }

    public int getOpenStat() {
        return openStat;
    }

    public void setOpenStat(int openStat) {
        this.openStat = openStat;
    }
    
}
