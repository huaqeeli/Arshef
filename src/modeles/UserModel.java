package modeles;

public class UserModel {

    String militaryid,rank, name, usertype,uint;
    int squnce;

    public UserModel(String militaryid, String rank, String name, String usertype, int squnce) {
        this.militaryid = militaryid;
        this.rank = rank;
        this.name = name;
        this.usertype = usertype;
        this.squnce = squnce;
    }

    public UserModel(String militaryid, String rank, String name, String usertype, String uint, int squnce) {
        this.militaryid = militaryid;
        this.rank = rank;
        this.name = name;
        this.usertype = usertype;
        this.uint = uint;
        this.squnce = squnce;
    }

    public UserModel() {
    }

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
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

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    @Override
    public String toString() {
        return "UserModel{" + "militaryid=" + militaryid + ", rank=" + rank + ", name=" + name + ", usertype=" + usertype + ", squnce=" + squnce + '}';
    }

}
