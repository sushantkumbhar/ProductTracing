package com.bct.hackathon.producttracker.datamodel.transactions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parent {
    @SerializedName("current_transction")
    @Expose
    private CurrentTransction currentTransction;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("nonce")
    @Expose
    private Integer nonce;
    @SerializedName("previous_hash")
    @Expose
    private String previousHash;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public CurrentTransction getCurrentTransction() {
        return currentTransction;
    }

    public void setCurrentTransction(CurrentTransction currentTransction) {
        this.currentTransction = currentTransction;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}

