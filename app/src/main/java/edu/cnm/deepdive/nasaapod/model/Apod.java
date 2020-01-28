package edu.cnm.deepdive.nasaapod.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Apod {

  @Expose
  private Date date;

  @Expose
  private String title;

  @Expose
  @SerializedName("explanation")
  private String description;

  @Expose
  private String copyright;

  @Expose
  @SerializedName("media_type")
  private String mediaType;

  @Expose
  @SerializedName("service_version")
  private String serviceVersion;

  @Expose
  private String url;

  @Expose
  @SerializedName("hdurl")
  private String hdUrl;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCopyright() {
    return copyright;
  }

  public void setCopyright(String copyright) {
    this.copyright = copyright;
  }

  public String getMediaType() {
    return mediaType;
  }

  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getServiceVersion() {
    return serviceVersion;
  }

  public void setServiceVersion(String serviceVersion) {
    this.serviceVersion = serviceVersion;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHdUrl() {
    return hdUrl;
  }

  public void setHdUrl(String hdUrl) {
    this.hdUrl = hdUrl;
  }
}
