
package modeles;


public class OriginalCensusModel {
    String uint, OFcensus, SRcensus;

    public OriginalCensusModel(String uint, String OFcensus, String SRcensus) {
        this.uint = uint;
        this.OFcensus = OFcensus;
        this.SRcensus = SRcensus;
    }

    public String getUint() {
        return uint;
    }

    public void setUint(String uint) {
        this.uint = uint;
    }

    public String getOFcensus() {
        return OFcensus;
    }

    public void setOFcensus(String OFcensus) {
        this.OFcensus = OFcensus;
    }

    public String getSRcensus() {
        return SRcensus;
    }

    public void setSRcensus(String SRcensus_) {
        this.SRcensus = SRcensus_;
    }

    @Override
    public String toString() {
        return "OriginalCensusModel{" + "uint=" + uint + ", OFcensus=" + OFcensus + ", SRcensus_=" + SRcensus + '}';
    }
    
}
