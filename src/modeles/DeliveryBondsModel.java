package modeles;

public class DeliveryBondsModel {

    String id,bondId, bondDate, circularNumber, uintt;
    int squnce;

    public DeliveryBondsModel(String id, String bondId, String bondDate, String circularNumber, String uintt, int squnce) {
        this.id = id;
        this.bondId = bondId;
        this.bondDate = bondDate;
        this.circularNumber = circularNumber;
        this.uintt = uintt;
        this.squnce = squnce;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public DeliveryBondsModel() {
    }

    public String getBondDate() {
        return bondDate;
    }

    public void setBondDate(String bondDate) {
        this.bondDate = bondDate;
    }

    public String getCircularNumber() {
        return circularNumber;
    }

    public void setCircularNumber(String circularNumber) {
        this.circularNumber = circularNumber;
    }

    public String getUintt() {
        return uintt;
    }

    public void setUintt(String uintt) {
        this.uintt = uintt;
    }

    public int getSqunce() {
        return squnce;
    }

    public void setSqunce(int squnce) {
        this.squnce = squnce;
    }

    
}
