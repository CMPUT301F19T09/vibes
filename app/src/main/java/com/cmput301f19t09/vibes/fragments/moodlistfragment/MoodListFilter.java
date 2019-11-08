package com.cmput301f19t09.vibes.fragments.moodlistfragment;

import android.widget.Filter;

/*
    this class will be used to perform filtering on MoodListAdapters (i.e. only showing happy or sad moods)
 */
public class MoodListFilter extends Filter
{
    @Override
    protected FilterResults performFiltering(CharSequence constraint)
    {
        return null;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results)
    {

    }
}
