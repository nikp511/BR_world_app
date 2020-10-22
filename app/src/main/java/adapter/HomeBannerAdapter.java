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

import pojo.PojoHome;


/**
 * Created by Rp on 6/14/2016.
 */
public class HomeBannerAdapter extends RecyclerView.Adapter<HomeBannerAdapter.MyViewHolder> {
    Context context;

    CustomClick customClick;
    private List<String> mArrayData;
    private List<String> mArrayDataId;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);

            ivImage = (ImageView) view.findViewById(R.id.ivImage);

        }

    }


    public HomeBannerAdapter(Context context, List<String> mArrayData, List<String> mArrayDataId, CustomClick customClick) {
        this.mArrayData = mArrayData;
        this.context = context;
        this.customClick = customClick;
        this.mArrayDataId = mArrayDataId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_page_banners, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Glide.with(context).load(mArrayData.get(position)).into(holder.ivImage);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customClick.itemClick(mArrayDataId.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayData.size();
    }


    public interface CustomClick {

        void itemClick(String id);

    }


}


