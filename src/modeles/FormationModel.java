
package modeles;


public class FormationModel {
    private int squnce,markState;
    private String militaryID,personalID,name,rank,uint,note,specializ,markColor;

    public FormationModel() {
    }

    public FormationModel(int squnce, String militaryID, String personalID, String name, String rank, String uint, String note) {
        this.squnce = squnce;
        this.militaryID = militaryID;
        this.personalID = personalID;
        this.name = name;
        this.rank = rank;
        this.uint = uint;
        this.note = note;
    }

    public FormationModel(int squnce, int markState, String militaryID, String personalID, String name, String rank, String uint, String note, String specializ, String markColor) {
        this.squnce = squnce;
        this.markState = markState;
        this.militaryID = militaryID;
        this.personalID = personalID;
        this.name = name;
        this.rank = rank;
        this.uint = uint;
        this.note = note;
        this.specializ = specializ;
        this.markColor = markColor;
    }

    public String getMarkColor() {
        return markColor;
    }

    public void setMarkColor(String markColor) {
        this.markColor = markColor;
    }

    public String getSpecializ() {
        return specializ;
    }

    public void setSpecializ(String specializ) {
        this.specializ = specializ;
    }

    public int getMarkState() {
        return markState;
    }

    public void setMarkState(int markState) {
        this.markState = markState;
    }

   

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    public String getMilitaryID() {
        return militaryID;
    }

    public void setMilitaryID(String militaryID) {
        this.militaryID = militaryID;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
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

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }
    
}
