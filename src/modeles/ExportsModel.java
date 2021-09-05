package modeles;

public class ExportsModel {

    String id, topic, destination, exportNum, exportDate, notes, saveFile, exportsImage, internalincomingnum, entryDate,recordYear;
    int squence;

    public ExportsModel(String id, String topic, String destination, String exportNum, String exportDate, String notes, String saveFile, String exportsImage, String internalincomingnum, String entryDate, String recordYear) {
        this.id = id;
        this.topic = topic;
        this.destination = destination;
        this.exportNum = exportNum;
        this.exportDate = exportDate;
        this.notes = notes;
        this.saveFile = saveFile;
        this.exportsImage = exportsImage;
        this.internalincomingnum = internalincomingnum;
        this.entryDate = entryDate;
        this.recordYear = recordYear;
    }

    public String getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
    }

   

    public ExportsModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternalincomingnum() {
        return internalincomingnum;
    }

    public void setInternalincomingnum(String internalincomingnum) {
        this.internalincomingnum = internalincomingnum;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getExportNum() {
        return exportNum;
    }

    public void setExportNum(String exportNum) {
        this.exportNum = exportNum;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public String getExportsImage() {
        return exportsImage;
    }

    public void setExportsImage(String exportsImage) {
        this.exportsImage = exportsImage;
    }

    public int getSquence() {
        return squence;
    }

    public void setSquence(int squence) {
        this.squence = squence;
    }

    @Override
    public String toString() {
        return "ExportsModel{" + "id=" + id + ", topic=" + topic + ", destination=" + destination + ", exportNum=" + exportNum + ", exportDate=" + exportDate + ", notes=" + notes + ", saveFile=" + saveFile + ", exportsImage=" + exportsImage + ", internalincomingnum=" + internalincomingnum + ", entryDate=" + entryDate + ", squence=" + squence + '}';
    }

}
