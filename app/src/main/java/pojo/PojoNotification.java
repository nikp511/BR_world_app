package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Andoid-Devloper on 2/5/2019.
 */

public class PojoNotification {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("today_date")
    @Expose
    private Integer todayNotiCount;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getTodayNotiCount() {
        return todayNotiCount;
    }

    public void setTodayNotiCount(Integer todayNotiCount) {
        this.todayNotiCount = todayNotiCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("notification")
        @Expose
        private String notification;
        @SerializedName("movie_id")
        @Expose
        private String movieId;
        @SerializedName("dt_added")
        @Expose
        private String dtAdded;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getDtAdded() {
            return dtAdded;
        }

        public void setDtAdded(String dtAdded) {
            this.dtAdded = dtAdded;
        }

    }


}
