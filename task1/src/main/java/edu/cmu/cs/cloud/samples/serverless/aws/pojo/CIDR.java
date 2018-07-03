package edu.cmu.cs.cloud.samples.serverless.aws.pojo;

public class CIDR {

    private String cidrSignature;
    private String firstAddress;
    private String lastAddress;
    private int addressCount;

    public CIDR(){}

    public CIDR(String cidrSignature){
        this.cidrSignature = cidrSignature;
    }

    public CIDR(String cidrSignature, String firstAddress, String lastAddress, int addressCount) {
        this.cidrSignature = cidrSignature;
        this.firstAddress = firstAddress;
        this.lastAddress = lastAddress;
        this.addressCount = addressCount;
    }

    public String getCidrSignature() {
        return cidrSignature;
    }

    public void setCidrSignature(String cidrSignature) {
        this.cidrSignature = cidrSignature;
    }

    public String getFirstAddress() {
        return firstAddress;
    }

    public void setFirstAddress(String firstAddress) {
        this.firstAddress = firstAddress;
    }

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    public int getAddressCount() {
        return addressCount;
    }

    public void setAddressCount(int addressCount) {
        this.addressCount = addressCount;
    }



}
