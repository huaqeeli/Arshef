
package modeles;

import controllers.DatabaseAccess;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CoursesModel {
    private int id;
    private int sequence;
    private String militaryId;
    private String name;
    private String rank;
    private String unit;
    private String coursname;
    private String coursNumber;
    private String coursplace;
    private String coursDuration;
    private String startDate;
    private String endDate;
    private String estimate;

    public CoursesModel(int id,String militaryId, String name, String rank, String coursname) {
        this.id = id;
        this.militaryId = militaryId;
        this.name = name;
        this.rank = rank;
        this.coursname = coursname;
    }

//    public CoursesModel(int sequence, String militaryId, String rank, String name, String unit) {
//        this.sequence = sequence;
//        this.militaryId = militaryId;
//        this.rank = rank;
//        this.name = name;
//        this.unit = unit;
//    }

    public CoursesModel(String militaryId, String name, String rank, String coursname, String coursNumber, String coursplace, String coursDuration, String estimate) {
        this.militaryId = militaryId;
        this.name = name;
        this.rank = rank;
        this.coursname = coursname;
        this.coursNumber = coursNumber;
        this.coursplace = coursplace;
        this.coursDuration = coursDuration;
        this.estimate = estimate;
    }

    public CoursesModel(int sequence, String coursname, String coursNumber, String coursplace, String coursDuration, String startDate, String endDate, String estimate) {
        this.sequence = sequence;
        this.coursname = coursname;
        this.coursNumber = coursNumber;
        this.coursplace = coursplace;
        this.coursDuration = coursDuration;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimate = estimate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMilitaryId() {
        return militaryId;
    }

    public void setMilitaryId(String militaryId) {
        this.militaryId = militaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCoursname() {
        return coursname;
    }

    public void setCoursname(String coursname) {
        this.coursname = coursname;
    }

    public String getCoursNumber() {
        return coursNumber;
    }

    public void setCoursNumber(String coursNumber) {
        this.coursNumber = coursNumber;
    }

    public String getCoursplace() {
        return coursplace;
    }

    public void setCoursplace(String coursplace) {
        this.coursplace = coursplace;
    }

    public String getCoursDuration() {
        return coursDuration;
    }

    public void setCoursDuration(String coursDuration) {
        this.coursDuration = coursDuration;
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "CoursesModel{" + "id=" + id + ", militaryId=" + militaryId + ", name=" + name + ", rank=" + rank + ", unit=" + unit + ", coursname=" + coursname + ", coursNumber=" + coursNumber + ", coursplace=" + coursplace + ", coursDuration=" + coursDuration + ", estimate=" + estimate + '}';
    }
    
    static public String getCoursId(String coursname) throws SQLException {
        String coursid = null;
        try {
            ResultSet rs = DatabaseAccess.select("coursnames", "CORSNAME ='"+coursname+"'");
            if (rs.next()) {
                coursid = rs.getString("COURSID");
            }
        } catch (IOException ex) {
            Logger.getLogger(CoursesModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coursid;
    }
}
