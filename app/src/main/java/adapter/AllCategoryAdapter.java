package adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.movie.brworld.R;

import java.util.List;

import pojo.PojoAllCategory;


/**
 * Created by Rp on 6/14/2016.
 */
public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.MyViewHolder> {
    Context context;

    CustomClick customClick;
    private List<PojoAllCategory.MovieDatum> mArrayMovie;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);

            ivImage = (ImageView) view.findViewById(R.id.ivImage);

        }

    }


    public AllCategoryAdapter(Context context, List<PojoAllCategory.MovieDatum> mArrayMovie, CustomClick customClick) {
        this.mArrayMovie = mArrayMovie;
        this.context = context;
        this.customClick = customClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_category, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Glide.with(context).load(mArrayMovie.get(position).getMovieImage()).into(holder.ivImage);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customClick.itemClick(mArrayMovie.get(position).getMovieId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayMovie.size();
    }


    public interface CustomClick {

        void itemClick(String movieId);


    }


}


