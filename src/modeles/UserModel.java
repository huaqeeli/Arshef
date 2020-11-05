
package modeles;
 

public class UserModel {
    String militaryid,name,nationalid,usertype;
    int squnce;

    public UserModel(String militaryid, String name, String nationalid, String usertype, int squnce) {
        this.militaryid = militaryid;
        this.name = name;
        this.nationalid = nationalid;
        this.usertype = usertype;
        this.squnce = squnce;
    }

    public String getMilitaryid() {
        return militaryid;
    }

    public void setMilitaryid(String militaryid) {
        this.militaryid = militaryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
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
        return "UserModel{" + "militaryid=" + militaryid + ", name=" + name + ", nationalid=" + nationalid + ", usertype=" + usertype + ", squnce=" + squnce + '}';
    }
    
}
