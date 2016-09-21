package homework.nick.searchandplay2.utils;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import homework.nick.searchandplay2.R;
import homework.nick.searchandplay2.events.TrackChangeEvent;
import homework.nick.searchandplay2.model.Info;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Nick on 02.09.16.
 */
public class MRecylerAdapter extends RecyclerView.Adapter<MRecylerAdapter.MViewHolder> {

    private List<Info> infoList;

    public MRecylerAdapter(List<Info> infoList) {
        this.infoList = infoList;
    }

    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MViewHolder holder, int position) {
        Info info = infoList.get(position);
        holder.setName(info.getTitle());
        holder.setDuration(StringGenerator.generateDurationString(info.getDuration()));
    }


    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView name;
        private TextView duration;

        public MViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.list_item_track_name);
            duration = (TextView) itemView.findViewById(R.id.list_item_track_duration);

        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public void setDuration(String duration) {
            this.duration.setText(duration);
        }

        @Override
        public void onClick(View v) {
//            Log.i("listener", String.valueOf(getAdapterPosition()));
            EventBus.getDefault().post(new TrackChangeEvent("start music", infoList, getAdapterPosition()));
        }
    }
}
