package modeles;

public class InternalExportsModel {

    String regisNO, exportsDate, destination, topic, saveFile, notes, recordYear,recordType;

    public InternalExportsModel() {
    }

    public InternalExportsModel(String regisNO, String exportsDate, String destination, String topic, String saveFile, String notes, String recordYear, String recordType) {
        this.regisNO = regisNO;
        this.exportsDate = exportsDate;
        this.destination = destination;
        this.topic = topic;
        this.saveFile = saveFile;
        this.notes = notes;
        this.recordYear = recordYear;
        this.recordType = recordType;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }
    public String getRecordYear() {
        return recordYear;
    }

    public void setRecordYear(String recordYear) {
        this.recordYear = recordYear;
    }

    public String getRegisNO() {
        return regisNO;
    }

    public void setRegisNO(String regisNO) {
        this.regisNO = regisNO;
    }

    public String getExportsDate() {
        return exportsDate;
    }

    public void setExportsDate(String exportsDate) {
        this.exportsDate = exportsDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
