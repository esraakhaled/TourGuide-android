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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsFragment extends Fragment {
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


    public RestaurantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tour_list, container, false);
        //create and setup the {@link AudioManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        //get strings stored inside strings.xml files
        String comic_place = getString(R.string.comice);
        String comic_addr = getString(R.string.comice_address);
        String lastrance_place = getString(R.string.lastrance);
        String lastrance_addr= getString(R.string.lastrance_addr);
        String hugoco_place = getString(R.string.hugoco);
        String hugoco_addr = getString(R.string.hugoco_addr);
        String au_petit_place = getString(R.string.au_petit_tonneau);
        String au_petit_addr = getString(R.string.au_petit_addr);
        String restaurant_devid_place = getString(R.string.restaurant_david);
        String restaurant_david_addr = getString(R.string.restaurant_david_addr);
        String larpege_place = getString(R.string.larpege);
        String larpege_addr = getString(R.string.larpege_addr);
        String veal_sweet_place = getString(R.string.veal_sweet_breads);
        String veal_sweet_addr = getString(R.string.veal_sweet_breads_addr);
        String josephine_place = getString(R.string.josephine);
        String josephine_addr = getString(R.string.josephine_addr);
        String  lassiette_place= getString(R.string.lassiette);
        String lassiette_addr = getString(R.string.lassiette_addr);
        String lesevero_place = getString(R.string.lesevero);
        String lesevero_addr = getString(R.string.lesevero_addr);

        final ArrayList<Tour> tours = new ArrayList<Tour>();
        tours.add(new Tour(comic_place,comic_addr , R.raw.one, R.drawable.comice));
        tours.add(new Tour( lastrance_place,lastrance_addr, R.raw.two, R.drawable.astrance));
        tours.add(new Tour(hugoco_place, hugoco_addr, R.raw.three, R.drawable.hugo));
        tours.add(new Tour(au_petit_place, au_petit_addr, R.raw.four, R.drawable.au_petit));
        tours.add(new Tour(restaurant_devid_place,restaurant_david_addr, R.raw.five, R.drawable.restaurant_david_toutain_official));
        tours.add(new Tour(larpege_place, larpege_addr, R.raw.six, R.drawable.arpege));
        tours.add(new Tour(veal_sweet_place, veal_sweet_addr, R.raw.seven, R.drawable.veal_sweet_bread));
        tours.add(new Tour(josephine_place, josephine_addr, R.raw.eight, R.drawable.josephine));
        tours.add(new Tour(lassiette_place, lassiette_addr, R.raw.nine, R.drawable.lassite));
        tours.add(new Tour(lesevero_place,lesevero_addr, R.raw.ten, R.drawable.lesevero));

        TourAdapter adapter = new TourAdapter(getActivity(), tours, R.color.category_restaurants);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

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
