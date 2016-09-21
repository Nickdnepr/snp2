package homework.nick.searchandplay2.events;

import homework.nick.searchandplay2.model.Info;

import java.util.List;

/**
 * Created by Nick on 10.09.16.
 */
public class TrackChangeEvent extends MEvent {

    private List<Info> playList; // the current playlist
    private int position; // position in the playlist

    public TrackChangeEvent(String message, List<Info> playlist, int position) {
        super(message);
        this.playList = playlist;
        this.position = position;
    }

    public List<Info> getPlayList() {
        return playList;
    }

    public int getPosition() {
        return position;
    }
}
