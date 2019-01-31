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
public class AttractionsFragment extends Fragment {
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


    public AttractionsFragment() {
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
        String louvre_place = getString(R.string.louvre_place);
        String louvre_addr = getString(R.string.louvre_addr);
        String la_Tour_eiffel = getString(R.string.la_tour_eiffel);
        String la_tour_eiffel_addr = getString(R.string.la_tour_eiffel_addr);
        String notre_dame_place = getString(R.string.notre_dame);
        String notre_dame_addr = getString(R.string.notre_dame_addr);
        String arc_de_triomphe = getString(R.string.arc_de_triomphe);
        String arc_de_triomphe_addr = getString(R.string.arc_de_triomphe_addr);
        String pere_lachaise_place = getString(R.string.pere_lachaise);
        String pere_lachaise_addr = getString(R.string.pere_lachaise_addr);
        String sainte_chapelle = getString(R.string.sainte_chapelle);
        String sainte_chapelle_addr = getString(R.string.sainte_chapelle_addr);
        String palais_garnier_place = getString(R.string.palais_garnier);
        String palais_garnier_addr = getString(R.string.palais_garnier_addr);
        String les_invalides_place = getString(R.string.les_invalides);
        String les_invalides_addr = getString(R.string.les_invalides_addr);
        String chateau_de_chenonceaux_place = getString(R.string.chateau_de_chenonceaux);
        String chateau_de_chenonceaux_addr = getString(R.string.chateau_de_chenonceaux_addr);
        String basilique_du_sacre_place = getString(R.string.basilique_du_sacre);
        String basilique_du_sacre_addr = getString(R.string.basilique_du_sacre_addr);


        final ArrayList<Tour> tours = new ArrayList<>();
        tours.add(new Tour(louvre_place,louvre_addr, R.raw.one, R.drawable.louvre));
        tours.add(new Tour(la_Tour_eiffel, la_tour_eiffel_addr, R.raw.two, R.drawable.eiffel_tour));
        tours.add(new Tour(notre_dame_place, notre_dame_addr, R.raw.three, R.drawable.notre_dame));
        tours.add(new Tour(arc_de_triomphe, arc_de_triomphe_addr, R.raw.four, R.drawable.arc_de_triomphe));
        tours.add(new Tour(pere_lachaise_place, pere_lachaise_addr, R.raw.five, R.drawable.pere_lachaise));
        tours.add(new Tour(sainte_chapelle, sainte_chapelle_addr, R.raw.six, R.drawable.sainte_chapelle));
        tours.add(new Tour(palais_garnier_place,palais_garnier_addr, R.raw.seven, R.drawable.palais_garnier));
        tours.add(new Tour(les_invalides_place, les_invalides_addr, R.raw.eight, R.drawable.les_invalides));
        tours.add(new Tour(chateau_de_chenonceaux_place,chateau_de_chenonceaux_addr, R.raw.nine, R.drawable.chateau_de_chenonceau));
        tours.add(new Tour(basilique_du_sacre_place, basilique_du_sacre_addr, R.raw.ten, R.drawable.sacre_coeur));

        TourAdapter adapter = new TourAdapter(getActivity(), tours, R.color.category_top_attractions);

        ListView listView = rootView.findViewById(R.id.list);

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
