package adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.movie.brworld.R;

import java.util.List;

import pojo.PojoHome;


/**
 * Created by Rp on 6/14/2016.
 */
public class HomeAllCategoryAdapter extends RecyclerView.Adapter<HomeAllCategoryAdapter.MyViewHolder> {
    Context context;

    CustomClick customClick;
    private List<PojoHome.MovieDatum> mArrayData;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RecyclerView rvData;

        public MyViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            rvData = view.findViewById(R.id.rvData);

        }

    }


    public HomeAllCategoryAdapter(Context context, List<PojoHome.MovieDatum> mArrayData, CustomClick customClick) {
        this.mArrayData = mArrayData;
        this.context = context;
        this.customClick = customClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_page_category, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvName.setText("" + mArrayData.get(position).getCategoryName());

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rvData.setLayoutManager(mLayoutManager3);
        holder.rvData.setItemAnimator(new DefaultItemAnimator());

        HomeAllCategorySubAdapter allCategorySubAdapter = new HomeAllCategorySubAdapter(context,
                mArrayData.get(position).getData(), customSubClick);
        holder.rvData.setAdapter(allCategorySubAdapter);

    }

    HomeAllCategorySubAdapter.CustomClick customSubClick = new HomeAllCategorySubAdapter.CustomClick() {
        @Override
        public void itemClick(String movieId) {
            customClick.itemClick(movieId);
        }
    };

    @Override
    public int getItemCount() {
        return mArrayData.size();
    }

    public interface CustomClick {

        void itemClick(String id);


    }


}


