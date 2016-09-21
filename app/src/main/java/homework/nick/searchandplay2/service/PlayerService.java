package homework.nick.searchandplay2.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import homework.nick.searchandplay2.events.EventToActivity;
import homework.nick.searchandplay2.events.MEvent;
import homework.nick.searchandplay2.events.TrackChangeEvent;
import homework.nick.searchandplay2.model.Info;
import homework.nick.searchandplay2.utils.Constants;
import homework.nick.searchandplay2.utils.PlayerCommands;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;


public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer player;
    private List<Info> playlist;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            Log.i("eventBusException", e.getMessage());
        }
        initPlayer();
        Log.i("service", "service started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Subscribe
    public void onEvent(MEvent event) {
        Log.i("event", event.getMessage());
        if (player != null) {
            if (event.getMessage().equals(PlayerCommands.PAUSE_COMMAND)) {
                player.pause();
                EventBus.getDefault().post(new EventToActivity(PlayerCommands.PAUSE_COMMAND));
            }

            if (event.getMessage().equals(PlayerCommands.START_COMMAND)) {
                player.start();
                EventBus.getDefault().post(new EventToActivity(PlayerCommands.START_COMMAND));
            }
        }
    }

    @Subscribe
    public void onEvent(TrackChangeEvent event) throws IOException {
        Log.i("event", "track change");
        playlist=event.getPlayList();
        initPlayer();
        Info info = playlist.get(event.getPosition());
        if (info.getPath_to_file()!=null){
            Log.i("player", "starting from card");
            player.setDataSource(info.getPath_to_file());
        }else {
            Log.i("player", "starting from internet");
            player.setDataSource(info.getStream_url()+"?client_id="+ Constants.USER_ID);
            Log.i("player debug", info.getStream_url()+"?client_id="+ Constants.USER_ID);
        }
        player.setOnPreparedListener(this);
        player.prepare();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initPlayer() {
        if (player != null) {
            player.pause();
        }
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new EventToActivity(PlayerCommands.START_COMMAND));
        Log.i("player", "ready");
        mp.start();
    }
}
