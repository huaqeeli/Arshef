
package modeles;


public class CourrsNamesModel {
    String coursid,coursname;

    public CourrsNamesModel(String coursid, String coursname) {
        this.coursid = coursid;
        this.coursname = coursname;
    }

    public String getCoursid() {
        return coursid;
    }

    public void setCoursid(String coursid) {
        this.coursid = coursid;
    }

    public String getCoursname() {
        return coursname;
    }

    public void setCoursname(String coursname) {
        this.coursname = coursname;
    }

    @Override
    public String toString() {
        return "CourrsNamesModel{" + "coursid=" + coursid + ", coursname=" + coursname + '}';
    }
    
}
