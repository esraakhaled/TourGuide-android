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
public class PublicPlacesFragment extends Fragment {
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


    public PublicPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.tour_list, container, false);
        //create and setup the {@link AudioManager to request audio focus
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        String tuileries_place = getString(R.string.tuileries);
        String tuileries_addr = getString(R.string.tuileries_addr);
        String hotel_de_ville_place = getString(R.string.hotel_de_ville);
        String hotel_de_ville_addr = getString(R.string.hotel_de_ville_addr);
        String pont_des_arts_place = getString(R.string.ponts_des_arts);
        String ponts_des_arts_addr = getString(R.string.ponts_des_arts_addr);
        String luxembourg_gardens_place = getString(R.string.luxembourg_gardens);
        String luxembourg_gardens_addr = getString(R.string.luxembourg_gardens_addr);
        String place_des_vosges_place = getString(R.string.place_des_vosges);
        String place_des_vosges_addr = getString(R.string.place_des_vosges_addr);
        String champ_de_mars_place = getString(R.string.champ_de_mars);
        String champ_de_mars_addr = getString(R.string.champ_de_mars_addr);
        String square_du_vert_galant_place = getString(R.string.square_du_vert_galant);
        String square_du_vert_galant_addr = getString(R.string.square_du_vert_galant_addr);
        String canal_saint_place = getString(R.string.canal_saint);
        String canal_saint_addr = getString(R.string.canal_saint_addr);
        String parc_des_buttes_place = getString(R.string.parc_des_buttes);
        String parc_des_buttes_addr = getString(R.string.parc_des_buttes_addr);
        String promenade_plantee_place = getString(R.string.promenade_plantee);
        String promenade_plantee_addr = getString(R.string.promenade_plantee_addr);

        final ArrayList<Tour> tours = new ArrayList<>();
        tours.add(new Tour(tuileries_place,tuileries_addr, R.raw.one, R.drawable.tuileries));
        tours.add(new Tour(hotel_de_ville_place, hotel_de_ville_addr, R.raw.two, R.drawable.hotel_de_ville));
        tours.add(new Tour(pont_des_arts_place, ponts_des_arts_addr, R.raw.three, R.drawable.pont_des_arts));
        tours.add(new Tour(luxembourg_gardens_place, luxembourg_gardens_addr, R.raw.four, R.drawable.luxembourg_gardens));
        tours.add(new Tour(place_des_vosges_place, place_des_vosges_addr, R.raw.five, R.drawable.paris));
        tours.add(new Tour(champ_de_mars_place, champ_de_mars_addr, R.raw.six, R.drawable.champ_de_mars));
        tours.add(new Tour(square_du_vert_galant_place, square_du_vert_galant_addr, R.raw.seven, R.drawable.pont_neuf));
        tours.add(new Tour(canal_saint_place, canal_saint_addr, R.raw.eight, R.drawable.canal_saint_martin));
        tours.add(new Tour(parc_des_buttes_place, parc_des_buttes_addr, R.raw.nine, R.drawable.parc));
        tours.add(new Tour(promenade_plantee_place, promenade_plantee_addr, R.raw.ten, R.drawable.promenade_plantee));

        TourAdapter adapter = new TourAdapter(getActivity(), tours, R.color.category_public_places);

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
