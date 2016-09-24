package homework.nick.searchandplay2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import homework.nick.searchandplay2.R;
import homework.nick.searchandplay2.activity.MainActivity;
import homework.nick.searchandplay2.events.EventToActivity;
import homework.nick.searchandplay2.utils.PlayerCommands;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nick on 24.09.16.
 */

public class Testing_Fragment extends Fragment {

    private Button b1;
    private Button b2;
    MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        b1= (Button) getView().findViewById(R.id.button_1);
        b2= (Button) getView().findViewById(R.id.button_2);
        activity= (MainActivity) getActivity();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new EventToActivity(PlayerCommands.START_COMMAND));
                activity.openBottom();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EventBus.getDefault().post(new EventToActivity(PlayerCommands.PAUSE_COMMAND));
                activity.closeBottom();
            }
        });
    }
}
