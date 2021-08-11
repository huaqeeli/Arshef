
package modeles;

public class AddNamesModel {
   String milataryId,rank, name;
   int  squinse;

    public AddNamesModel(String milataryId, String rank, String name, int squinse) {
        this.milataryId = milataryId;
        this.rank = rank;
        this.name = name;
        this.squinse = squinse;
    }

    public String getMilataryId() {
        return milataryId;
    }

    public void setMilataryId(String milataryId) {
        this.milataryId = milataryId;
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

    public int getSquinse() {
        return squinse;
    }

    public void setSquinse(int squinse) {
        this.squinse = squinse;
    }
   
}
