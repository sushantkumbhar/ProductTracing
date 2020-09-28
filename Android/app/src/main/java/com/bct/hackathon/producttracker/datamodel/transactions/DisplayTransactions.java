package com.bct.hackathon.producttracker.datamodel.transactions;

public class DisplayTransactions {

    private String from,to,timestamp,hash,prevHash,type;

    public DisplayTransactions(String from, String to, String timestamp,String type, String hash, String prevHash) {
        this.from = from;
        this.to = to;
        this.type=type;
        this.timestamp = timestamp;
        this.hash = hash;
        this.prevHash = prevHash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
