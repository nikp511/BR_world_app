package pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PojoAllCategory {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data {

        @SerializedName("movie_Data")
        @Expose
        private List<MovieDatum> movieData = null;

        public List<MovieDatum> getMovieData() {
            return movieData;
        }

        public void setMovieData(List<MovieDatum> movieData) {
            this.movieData = movieData;
        }

    }



    public class MovieDatum {

        @SerializedName("movie_id")
        @Expose
        private String movieId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_image")
        @Expose
        private String categoryImage;
        @SerializedName("movie_name")
        @Expose
        private String movieName;
        @SerializedName("menu_category")
        @Expose
        private String menuCategory;
        @SerializedName("movie_image")
        @Expose
        private String movieImage;
        @SerializedName("path")
        @Expose
        private String path;
        @SerializedName("discription")
        @Expose
        private String discription;

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getMenuCategory() {
            return menuCategory;
        }

        public void setMenuCategory(String menuCategory) {
            this.menuCategory = menuCategory;
        }

        public String getMovieImage() {
            return movieImage;
        }

        public void setMovieImage(String movieImage) {
            this.movieImage = movieImage;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getDiscription() {
            return discription;
        }

        public void setDiscription(String discription) {
            this.discription = discription;
        }

    }

}
