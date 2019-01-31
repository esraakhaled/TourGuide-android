package com.example.android.tourguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TourAdapter extends ArrayAdapter<Tour> {
    /**
     * Resource ID for the background color for this list of tours
     */
    private int mColorResourceId;

    public TourAdapter(Context context, ArrayList<Tour> tours, int colorResourceId) {
        super(context, 0, tours);
        mColorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link AndroidWord} object located at this position in the list
        Tour currentTour = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID place_text_view
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.place_text_view);
        // Get the name of place from the current Tour object and
        // set this text on the name TextView
        nameTextView.setText(currentTour.getmNameOfPlace());
        // Find the TextView in the list_item.xml layout with the ID address_text_view
        TextView addressTextView = (TextView) listItemView.findViewById(R.id.address_text_view);
        // Get the address from the current AndroidTour object and
        // set this text on the address TextView
        addressTextView.setText(currentTour.getmAddress());


        //find the ImageView in the list_item.xml layout with the ID imageView
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);
        if (currentTour.hasImage()) {

            //get image resource from current android object and
            //set this image on image  ImageView
            imageView.setImageResource(currentTour.getmImageResourceId());
            //make sure view is visible
            imageView.setVisibility(View.VISIBLE);
        } else {
            //otherwise hide the view
            imageView.setVisibility(View.GONE);
        }
        // Set the theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        return listItemView;

    }
}

