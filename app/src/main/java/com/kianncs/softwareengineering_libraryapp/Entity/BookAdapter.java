package com.kianncs.softwareengineering_libraryapp.Entity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiann on 19/10/2016.
 */

public class BookAdapter extends ArrayAdapter{
    List list = new ArrayList();

    public BookAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class DataHandler{
        ImageView image;
        TextView title;
        TextView price;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }


    public void clear(){
        list.clear();
    }


    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        DataHandler handler;
        if(convertView == null){
            //to check in the first row is created, if is not created it will created a layout
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_layout, parent, false);
            handler = new DataHandler();
            //creating the items for list view
            handler.image = (ImageView) row.findViewById(R.id.bookImages);
            handler.title = (TextView) row.findViewById(R.id.bookTitles);
            handler.price = (TextView) row.findViewById(R.id.bookPrice);
            row.setTag(handler);
        }
        else{
            handler = (DataHandler) row.getTag();
        }
        Result result;
        result = (Result) this.getItem(position);
        if(result.getImage() != null) {
            handler.image.setImageBitmap(result.getImage());
        }
        handler.title.setText(result.getTitle());
        handler.price.setText(result.getCurrentPrice());

        return row;
    }
}
