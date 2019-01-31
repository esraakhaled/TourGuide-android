package com.example.android.tourguide;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private MediaPlayer mediaPlayer;
    /**
     * handles audio focus when playing a sound file
     **/
    private AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //pause playback
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
            }

        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();

        }
    };


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tour_list, container, false);
        //create and setup the {@link AudioManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        String paris_saint_gemain_place = getString(R.string.paris_saint_germain);
        String paris_saint_germain_addr = getString(R.string.paris_saint_germain_addr);
        String billet_musee_place = getString(R.string.billet_musee);
        String billet_musee_addr = getString(R.string.billet_musee_addr);
        String meeting_de_paris_place = getString(R.string.meeting_de_paris);
        String meeting_de_paris_addr = getString(R.string.meeting_de_paris_addr);
        String deaf_parade_place = getString(R.string.deaf_parade);
        String deaf_parade_addr = getString(R.string.deaf_parade_addr);
        String grand_prix_place = getString(R.string.grand_prix_d_amerique);
        String grand_prix_addr = getString(R.string.grand_prix_d_amerique_addr);
        String architects_place = getString(R.string.architects);
        String architects_addr = getString(R.string.architects_addr);
        String i_love_the_gos_place = getString(R.string.i_love_the_gos);
        String i_love_the_gos_addr = getString(R.string.i_love_the_gos_addr);
        String dub_station_place = getString(R.string.dub_station);
        String dub_station_addr = getString(R.string.dub_station_addr);
        String chicago_paris_place = getString(R.string.chicago_paris);
        String chicago_paris_addr = getString(R.string.chicago_paris_addr);
        String liam_finn_place = getString(R.string.liam_finn);
        String liam_finn_addr = getString(R.string.liam_finn_addr);

        final ArrayList<Tour> tours = new ArrayList<>();
        tours.add(new Tour(paris_saint_gemain_place, paris_saint_germain_addr, R.raw.one, R.drawable.le_parc_des_princes));
        tours.add(new Tour(billet_musee_place,billet_musee_addr, R.raw.two, R.drawable.centre_pompdio));
        tours.add(new Tour(meeting_de_paris_place,meeting_de_paris_addr, R.raw.three, R.drawable.accor_hotel));
        tours.add(new Tour(deaf_parade_place ,deaf_parade_addr, R.raw.four, R.drawable.super_sonic));
        tours.add(new Tour(grand_prix_place,grand_prix_addr, R.raw.five, R.drawable.hippo_drome));
        tours.add(new Tour(architects_place ,architects_addr, R.raw.six, R.drawable.olympia_bruno));
        tours.add(new Tour(i_love_the_gos_place,i_love_the_gos_addr, R.raw.seven, R.drawable.paris));
        tours.add(new Tour(dub_station_place, dub_station_addr, R.raw.eight, R.drawable.trabendo));
        tours.add(new Tour(chicago_paris_place, chicago_paris_addr, R.raw.nine, R.drawable.theatre_mogador));
        tours.add(new Tour(liam_finn_place, liam_finn_addr, R.raw.ten, R.drawable.cafe_dela_dance));

        TourAdapter adapter = new TourAdapter(getActivity(), tours, R.color.category_events);

        ListView listView =  rootView.findViewById(R.id.list);

        listView.setAdapter(adapter);
        // Set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the {@link Word} object at the given position the user clicked on
                Tour tour = tours.get(position);
                //Release the media player if it currently exists because we are about to
                //play a different sound file.
                releaseMediaPlayer();
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // we have audio focus now


                    // Create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mediaPlayer = MediaPlayer.create(getActivity(), tour.getmAudioResourceId());

                    // Start the audio file
                    mediaPlayer.start();
                    //set up a listener on a mediaPlayer so that we can stop and release the media player once the sounds finished playing
                    mediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

}
