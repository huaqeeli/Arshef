
package modeles;


public class AnalyticsModel {

    public AnalyticsModel() {
    }
     int squnce;
     String id,militaryid,rank,name,uint,stage,analyticesDate;

    public AnalyticsModel(int squnce, String id, String militaryid, String rank, String name, String uint, String stage, String analyticesDate) {
        this.squnce = squnce;
        this.id = id;
        this.militaryid = militaryid;
        this.rank = rank;
        this.name = name;
        this.uint = uint;
        this.stage = stage;
        this.analyticesDate = analyticesDate;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMilitaryid() {
        return militaryid;
    }

    public void setMilitaryid(String militaryid) {
        this.militaryid = militaryid;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getAnalyticesDate() {
        return analyticesDate;
    }

    public void setAnalyticesDate(String analyticesDate) {
        this.analyticesDate = analyticesDate;
    }
     
}
