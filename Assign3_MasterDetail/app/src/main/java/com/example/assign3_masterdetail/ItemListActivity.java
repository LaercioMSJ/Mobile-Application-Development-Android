package com.example.assign3_masterdetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assign3_masterdetail.country.CountryContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemListActivity extends AppCompatActivity {

    public static String[] countryNames;

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;

    DBAdapter db = new DBAdapter(this);

    View recyclerView;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        countryNames = getResources().getStringArray(R.array.countries);
        int COUNT = countryNames.length;

        if(CountryContent.ITEMS.isEmpty()) {
            for (int i = 1; i <= COUNT; i++) {
                CountryContent.addItem(CountryContent.createCountryItem(i, countryNames[i - 1]));
            }
        }

        // SQLite CRUD
        try{
            String destPath = "/data/data/" + getPackageName() +"/database/MyDB";
            File f = new File(destPath);
            if(!f.exists()){
                CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getResources().getString(R.string.fab_text), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                prefsEditor = prefs.edit();
                prefsEditor.clear();
                prefsEditor.apply();
                setupRecyclerView((RecyclerView) recyclerView);
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, CountryContent.ITEMS, mTwoPane));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<CountryContent.CountryItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CountryContent.CountryItem item = (CountryContent.CountryItem) view.getTag();
                addCountry(item.content);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    context.startActivity(intent);
                }

                savePreferences(item.content);
                setupRecyclerView((RecyclerView) recyclerView);
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<CountryContent.CountryItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);
            holder.mImageView.setImageResource(mValues.get(position).flag);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);

            prefs = getPreferences(MODE_PRIVATE);
            if (prefs.getBoolean(mValues.get(position).content, false)) {
                holder.itemView.setEnabled(false);
                holder.mIdView.setAlpha(0.2f);
                holder.mContentView.setAlpha(0.2f);
                holder.mImageView.setAlpha(0.2f);
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final ImageView mImageView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mImageView = (ImageView) view.findViewById(R.id.imgFlag);
            }
        }
    }//end class SimpleItemRecyclerViewAdapter

    public void savePreferences(String countryName) {
        prefs = getPreferences(MODE_PRIVATE);
        prefsEditor = prefs.edit();
        prefsEditor.putBoolean(countryName, true);
        prefsEditor.apply();
    }//end method savePreferences

    public void CopyDB(InputStream inputStream, OutputStream outputStream) throws IOException {
        //copy 1k bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while((length = inputStream.read(buffer)) > 0)
        {
            outputStream.write(buffer,0,length);
        }
        inputStream.close();
        outputStream.close();
    }//end method CopyDB

    public void addCountry(String country) {
        //add countries - CREATE
        //It takes all the countries contained in the database. If country is not in the database, then it is created
        db.open();
        Cursor c = db.getAllCountry();
        if (c.moveToFirst()) {
            List<String> temp = new ArrayList<String>();
            do {
                temp.add(c.getString(1));
            } while(c.moveToNext());

            if (!temp.contains(country)) {
                long id = db.insertCountry(country, "0");
            }

        //If the database is empty, then all countries are created
        } else {
            long id = db.insertCountry(country, "0");
        }
        db.close();
    }
}