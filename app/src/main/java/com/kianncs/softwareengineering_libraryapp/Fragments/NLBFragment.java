package com.kianncs.softwareengineering_libraryapp.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kianncs.softwareengineering_libraryapp.Entity.BookAdapter;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.API;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.AmazonAPI;


/**
 * A simple {@link Fragment} subclass.
 */
public class NLBFragment extends Fragment {

    ListView listView;
    EditText searchQuery;
    BookAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nlb,container,false);
        listView = (ListView) v.findViewById(R.id.ListViewNLBFragment);
        searchQuery = (EditText) v.findViewById(R.id.searchAmazonFragmentQuery);
        adapter = new BookAdapter(getActivity(),R.layout.row_layout);
        listView.setAdapter(adapter);


        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return v;
    }
}
