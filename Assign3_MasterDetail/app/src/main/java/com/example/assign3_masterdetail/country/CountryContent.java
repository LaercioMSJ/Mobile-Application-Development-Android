package com.example.assign3_masterdetail.country;

import android.content.res.Resources;
import android.util.Log;

import com.example.assign3_masterdetail.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryContent {

    public static final Integer[] flagsId = {R.drawable.brazil, R.drawable.canada, R.drawable.china,
            R.drawable.france, R.drawable.germany, R.drawable.india, R.drawable.japan,
            R.drawable.south_korea, R.drawable.united_kingdom, R.drawable.united_states_of_america};

    public static final String[][] detailsArray = {{"203,429,773","3,286,470"}, {"34,030,589","3,855,081"}, {"1,336,718,015","3,705,386"},
            {"65,312,249","211,208"}, {"81,471,834","137,846"}, {"1,189,172,906","1,269,338"}, {"126,475,664","145,882"},
            {"48,754,657","38,023"}, {"62,698,362","94,525"}, {"313,232,044","3,718,691"}};

    public static final List<CountryItem> ITEMS = new ArrayList<CountryItem>();

    public static final Map<String, CountryItem> ITEM_MAP = new HashMap<String, CountryItem>();

    public static void addItem(CountryItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static CountryItem createCountryItem(int position, String name) {
        return new CountryItem(String.valueOf(position), name, detailsArray[position-1][0], detailsArray[position-1][1], flagsId[position-1]);
    }

    public static class CountryItem {
        public final String id;
        public final String content;
        public final String population;
        public final String area;
        public final Integer flag;

        public CountryItem(String id, String content, String population, String area, Integer flag) {
            this.id = id;
            this.content = content;
            this.population = population;
            this.area = area;
            this.flag = flag;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}