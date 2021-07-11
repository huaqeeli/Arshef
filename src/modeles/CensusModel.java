
package modeles;

public class CensusModel {
    String uint,originalCensus,currentCensus,OrdinaryVacation,OccasionalVacation,Sickleave,Quarantine,InareaTraining,OutareaTraining,OutKingdomTraining ,
            OfficialMission,JoblMission,hospital,outKingdomJob,outOftheForce,alternates,administrativeleave,late,Absence,Prison;

    public CensusModel(String uint, String originalCensus, String currentCensus, String OrdinaryVacation, String OccasionalVacation, String Sickleave, String Quarantine, String InareaTraining, String OutareaTraining, String OutKingdomTraining, String OfficialMission, String JoblMission, String hospital, String outKingdomJob, String outOftheForce, String alternates, String administrativeleave, String late, String Absence, String Prison) {
        this.uint = uint;
        this.originalCensus = originalCensus;
        this.currentCensus = currentCensus;
        this.OrdinaryVacation = OrdinaryVacation;
        this.OccasionalVacation = OccasionalVacation;
        this.Sickleave = Sickleave;
        this.Quarantine = Quarantine;
        this.InareaTraining = InareaTraining;
        this.OutareaTraining = OutareaTraining;
        this.OutKingdomTraining = OutKingdomTraining;
        this.OfficialMission = OfficialMission;
        this.JoblMission = JoblMission;
        this.hospital = hospital;
        this.outKingdomJob = outKingdomJob;
        this.outOftheForce = outOftheForce;
        this.alternates = alternates;
        this.administrativeleave = administrativeleave;
        this.late = late;
        this.Absence = Absence;
        this.Prison = Prison;
    }

    public String getOutKingdomTraining() {
        return OutKingdomTraining;
    }

    public void setOutKingdomTraining(String OutKingdomTraining) {
        this.OutKingdomTraining = OutKingdomTraining;
    }

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }

    public String getOriginalCensus() {
        return originalCensus;
    }

    public void setOriginalCensus(String originalCensus) {
        this.originalCensus = originalCensus;
    }

    public String getCurrentCensus() {
        return currentCensus;
    }

    public void setCurrentCensus(String currentCensus) {
        this.currentCensus = currentCensus;
    }

    public String getOrdinaryVacation() {
        return OrdinaryVacation;
    }

    public void setOrdinaryVacation(String OrdinaryVacation) {
        this.OrdinaryVacation = OrdinaryVacation;
    }

    public String getOccasionalVacation() {
        return OccasionalVacation;
    }

    public void setOccasionalVacation(String OccasionalVacation) {
        this.OccasionalVacation = OccasionalVacation;
    }

    public String getSickleave() {
        return Sickleave;
    }

    public void setSickleave(String Sickleave) {
        this.Sickleave = Sickleave;
    }

    public String getQuarantine() {
        return Quarantine;
    }

    public void setQuarantine(String Quarantine) {
        this.Quarantine = Quarantine;
    }

    public String getInareaTraining() {
        return InareaTraining;
    }

    public void setInareaTraining(String InareaTraining) {
        this.InareaTraining = InareaTraining;
    }

    public String getOutareaTraining() {
        return OutareaTraining;
    }

    public void setOutareaTraining(String OutareaTraining) {
        this.OutareaTraining = OutareaTraining;
    }

    public String getOfficialMission() {
        return OfficialMission;
    }

    public void setOfficialMission(String OfficialMission) {
        this.OfficialMission = OfficialMission;
    }

    public String getJoblMission() {
        return JoblMission;
    }

    public void setJoblMission(String JoblMission) {
        this.JoblMission = JoblMission;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getOutKingdomJob() {
        return outKingdomJob;
    }

    public void setOutKingdomJob(String outKingdomJob) {
        this.outKingdomJob = outKingdomJob;
    }

    public String getOutOftheForce() {
        return outOftheForce;
    }

    public void setOutOftheForce(String outOftheForce) {
        this.outOftheForce = outOftheForce;
    }

    public String getAlternates() {
        return alternates;
    }

    public void setAlternates(String alternates) {
        this.alternates = alternates;
    }

    public String getAdministrativeleave() {
        return administrativeleave;
    }

    public void setAdministrativeleave(String administrativeleave) {
        this.administrativeleave = administrativeleave;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getAbsence() {
        return Absence;
    }

    public void setAbsence(String Absence) {
        this.Absence = Absence;
    }

    public String getPrison() {
        return Prison;
    }

    public void setPrison(String Prison) {
        this.Prison = Prison;
    }

    @Override
    public String toString() {
        return "CensusModel{" + "originalCensus=" + originalCensus + ", currentCensus=" + currentCensus + ", OrdinaryVacation=" + OrdinaryVacation + ", OccasionalVacation=" + OccasionalVacation + ", Sickleave=" + Sickleave + ", Quarantine=" + Quarantine + ", InareaTraining=" + InareaTraining + ", OutareaTraining=" + OutareaTraining + ", OfficialMission=" + OfficialMission + ", JoblMission=" + JoblMission + ", hospital=" + hospital + ", outKingdomJob=" + outKingdomJob + ", outOftheForce=" + outOftheForce + ", alternates=" + alternates + ", administrativeleave=" + administrativeleave + ", late=" + late + ", Absence=" + Absence + ", Prison=" + Prison + '}';
    }
    
}
