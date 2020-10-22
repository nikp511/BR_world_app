package adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.movie.brworld.R;

import java.util.List;

import pojo.PojoAllCategory;


/**
 * Created by Rp on 6/14/2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    Context context;

    CustomClick customClick;
    private List<PojoAllCategory.MovieDatum> mArrayMovie;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvMovieName, tvMovieCat, tvMovieDis;

        public MyViewHolder(View view) {
            super(view);

            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            tvMovieName = view.findViewById(R.id.tvMovieName);
            tvMovieCat = view.findViewById(R.id.tvMovieCat);
            tvMovieDis = view.findViewById(R.id.tvMovieDis);

        }

    }


    public SearchAdapter(Context context, List<PojoAllCategory.MovieDatum> mArrayMovie, CustomClick customClick) {
        this.mArrayMovie = mArrayMovie;
        this.context = context;
        this.customClick = customClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context).load(mArrayMovie.get(position).getMovieImage()).into(holder.ivImage);
        holder.tvMovieName.setText("" + mArrayMovie.get(position).getMovieName());
        holder.tvMovieCat.setText("" + mArrayMovie.get(position).getCategoryName());
        holder.tvMovieDis.setText("" + mArrayMovie.get(position).getDiscription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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


