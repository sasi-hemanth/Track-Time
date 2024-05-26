package com.example.trackhours;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeEntry implements Parcelable {
    private String date;
    private String punchInTime;
    private String punchOutTime;
    private String totalTime;
    private String username;

    public TimeEntry() {

    }

    public TimeEntry(String date, String punchInTime, String punchOutTime, String totalTime, String username) {
        this.date = date;
        this.punchInTime = punchInTime;
        this.punchOutTime = punchOutTime;
        this.totalTime = totalTime;
        this.username = username;
    }

    protected TimeEntry(Parcel in) {
        date = in.readString();
        punchInTime = in.readString();
        punchOutTime = in.readString();
        totalTime = in.readString();
        username = in.readString();
    }

    public static final Creator<TimeEntry> CREATOR = new Creator<TimeEntry>() {
        @Override
        public TimeEntry createFromParcel(Parcel in) {
            return new TimeEntry(in);
        }

        @Override
        public TimeEntry[] newArray(int size) {
            return new TimeEntry[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPunchInTime() {
        return punchInTime;
    }

    public void setPunchInTime(String punchInTime) {
        this.punchInTime = punchInTime;
    }

    public String getPunchOutTime() {
        return punchOutTime;
    }

    public void setPunchOutTime(String punchOutTime) {
        this.punchOutTime = punchOutTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(punchInTime);
        dest.writeString(punchOutTime);
        dest.writeString(totalTime);
        dest.writeString(username);
    }
}
