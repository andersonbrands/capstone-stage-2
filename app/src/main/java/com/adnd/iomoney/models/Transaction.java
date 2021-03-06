package com.adnd.iomoney.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.adnd.iomoney.BR;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(
                entity = Account.class,
                parentColumns = "id",
                childColumns = "account_id",
                onDelete = CASCADE
        ),
        indices = {@Index("account_id")}
)
public class Transaction extends BaseObservable {

    private int account_id;

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String description = "";

    private float value;

    private String tags;

    private boolean hasLocation = false;

    private String locationLabel = "";

    private Double lat;

    private Double lon;

    private Date date;

    public Transaction clean() {
        if (!hasLocation) {
            lat = null;
            lon = null;
            locationLabel = "";
        }
        return this;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd\nMMM");
        return sdf.format(date);
    }

    @Bindable
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        if (this.value != value) {
            this.value = value;
            notifyPropertyChanged(BR.value);
        }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Bindable
    public boolean isHasLocation() {
        return hasLocation;
    }

    public void setHasLocation(boolean hasLocation) {
        if (this.hasLocation != hasLocation) {
            this.hasLocation = hasLocation;
            notifyPropertyChanged(BR.hasLocation);
        }
    }

    @Bindable
    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        if (!this.locationLabel.equals(locationLabel)) {
            this.locationLabel = locationLabel;
            notifyPropertyChanged(BR.locationLabel);
        }
    }

    public boolean hasNoCoordinates() {
        return lat == null || lon == null;
    }

    @Bindable
    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        if (this.lat == null || this.lat.equals(lat)) {
            this.lat = lat;

            notifyPropertyChanged(BR.lat);
        }
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Bindable
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (this.date == null || this.date.getTime() != date.getTime()) {
            this.date = date;

            notifyPropertyChanged(BR.date);
        }
    }
}
