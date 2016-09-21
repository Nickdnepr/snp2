package homework.nick.searchandplay2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;
import homework.nick.searchandplay2.R;
import homework.nick.searchandplay2.events.MEvent;
import homework.nick.searchandplay2.model.Info;
import homework.nick.searchandplay2.service.PlayerService;
import homework.nick.searchandplay2.utils.Constants;

import homework.nick.searchandplay2.utils.MRecylerAdapter;
import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 04.08.16.
 */

public class SearchFragment extends Fragment {
    private ImageView searchButton;
    private EditText searchRequestField;
    private List<Info> playList = new ArrayList<>();
    private Gson gson = new GsonBuilder().create();
    private RecyclerView recyclerView;
    private MRecylerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        searchButton = (ImageView) getView().findViewById(R.id.search_button);
        searchRequestField = (EditText) getView().findViewById(R.id.search_request);
        recyclerView= (RecyclerView) getView().findViewById(R.id.search_fragment_recycler_view);
        adapter = new MRecylerAdapter(playList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        Intent intent = new Intent(getActivity(), PlayerService.class);
        getActivity().startService(intent);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MEvent("test EventBus"));
                makeRequest(searchRequestField.getText().toString());
            }
        });



    }

    private void makeRequest(String request) {
        AndroidHttpClient httpClient = new AndroidHttpClient("http://api.soundcloud.com");
        ParameterMap params = httpClient.newParams()
                .add("q", request)
                .add("limit", "100")
                .add("client_id", Constants.USER_ID);
        httpClient.setMaxRetries(5);
        httpClient.get("/tracks.json", params, new AsyncCallback() {
            @Override
            public void onComplete(HttpResponse httpResponse) {
                Log.i("tag", httpResponse.getBodyAsString());
                String s = httpResponse.getBodyAsString();
                Type listType = new TypeToken<ArrayList<Info>>() {
                }.getType();
                playList.clear();
                List<Info> list = gson.fromJson(s, listType);
                playList.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception e) {
                Log.i("REQUEST_ERROR", e.getMessage());
            }
        });
    }
}
