package adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.movie.brworld.R;

import java.util.List;

import pojo.PojoNotification;


/**
 * Created by Rp on 6/14/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;


    private List<PojoNotification.Datum> mArrayNotification;

    CustomClick listner;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tvMessage, tvTime, tvDuration;
        LinearLayout llCall;

        public MyViewHolder(View view) {
            super(view);

            tvTime = view.findViewById(R.id.tvTime);
            tvMessage = view.findViewById(R.id.tvMessage);
            tvDuration = view.findViewById(R.id.tvDuration);


        }

    }


    public NotificationAdapter(Context context, List<PojoNotification.Datum> mArrayNotification, CustomClick listner) {
        this.mArrayNotification = mArrayNotification;
        this.context = context;
        this.listner = listner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);


        return new MyViewHolder(itemView);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PojoNotification.Datum newsOffer = mArrayNotification.get(position);

        holder.tvMessage.setText("" + newsOffer.getNotification());
        holder.tvTime.setText("" + newsOffer.getDtAdded());
        // holder.tvDuration.setText("From " + newsOffer.getNewsOfferDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.itemClick(mArrayNotification.get(position).getMovieId() + "");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayNotification.size();
    }

    public interface CustomClick {


        void itemClick(String movieId);


    }


}


