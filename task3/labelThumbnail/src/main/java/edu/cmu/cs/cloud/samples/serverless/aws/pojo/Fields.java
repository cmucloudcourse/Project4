package edu.cmu.cs.cloud.samples.serverless.aws.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Fields {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("labels")
    @Expose
    private List<String> labels = null;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

}
