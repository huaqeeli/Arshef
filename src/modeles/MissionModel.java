
package modeles;

public class MissionModel {
    String missionid,missionname,startdate,enddate;
    int squnce;

    public MissionModel(String missionid, String missionname, String startdate, String enddate, int squnce) {
        this.missionid = missionid;
        this.missionname = missionname;
        this.startdate = startdate;
        this.enddate = enddate;
        this.squnce = squnce;
    }

    public MissionModel() {
    }

    public String getMissionid() {
        return missionid;
    }

    public void setMissionid(String missionid) {
        this.missionid = missionid;
    }

    public String getMissionname() {
        return missionname;
    }

    public void setMissionname(String missionname) {
        this.missionname = missionname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }
    
}
