package modeles;

public class PersonalModel {

    private String militaryId;
    private String name;
    private String rank;
    private String unit;
    private String personalid;

    public PersonalModel(String militaryId, String rank, String name, String personalid, String unit) {
        this.militaryId = militaryId;
        this.rank = rank;
        this.name = name;
        this.personalid = personalid;
        this.unit = unit;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPersonalid() {
        return personalid;
    }

    public void setPersonalid(String personalid) {
        this.personalid = personalid;
    }

    @Override
    public String toString() {
        return "PersonalModel{" + "militaryId=" + militaryId + ", name=" + name + ", rank=" + rank + ", unit=" + unit + ", personalid=" + personalid + '}';
    }

}
