package com.example.assign3_masterdetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign3_masterdetail.country.CountryContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The country content this fragment is presenting.
     */
    private CountryContent.CountryItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the country content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = CountryContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail_image, container, false);

        DBAdapter db = new DBAdapter(getActivity());

        String rating = "0";
        //get a single country rating - READ
        db.open();
        Cursor c = db.getCountry(mItem.content);
        if(c.moveToFirst())
            rating = c.getString(2);
        else
            Toast.makeText(getContext(),getResources().getString(R.string.no_found),Toast.LENGTH_LONG).show();
        db.close();

        // Show the country content as text in a TextView and in ImageView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(String.format(getResources().getString(R.string.details), mItem.population, mItem.area));
            ((ImageView) rootView.findViewById(R.id.item_image)).setImageResource(mItem.flag);
            Animation myAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade);
            (rootView.findViewById(R.id.item_image)).startAnimation(myAnimation);

            ((RatingBar) rootView.findViewById(R.id.ratingCountry)).setRating(Float.parseFloat(rating));

            (rootView.findViewById(R.id.btnUpdate)).setEnabled(true);
            (rootView.findViewById(R.id.btnUpdate)).setOnClickListener(view -> {
                //update a country rating - UPDATE
                db.open();
                if(db.updateCountry(c.getInt(0), c.getString(1), String.valueOf(((RatingBar) rootView.findViewById(R.id.ratingCountry)).getRating()))) {
                    Toast.makeText(getContext(), getResources().getString(R.string.update_successful), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.update_failed), Toast.LENGTH_LONG).show();
                }
                db.close();
            });

            (rootView.findViewById(R.id.btnReset)).setOnClickListener(view -> {
                //delete a contact - DELETE
                db.open();
                if(db.deleteCountry(c.getString(1))) {
                    Toast.makeText(getContext(), getResources().getString(R.string.delete_successful), Toast.LENGTH_LONG).show();
                    ((RatingBar) rootView.findViewById(R.id.ratingCountry)).setRating(Float.parseFloat("0"));
                    (rootView.findViewById(R.id.btnUpdate)).setEnabled(false);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.delete_failed), Toast.LENGTH_LONG).show();
                }
                db.close();
            });
        }

        return rootView;
    }
}