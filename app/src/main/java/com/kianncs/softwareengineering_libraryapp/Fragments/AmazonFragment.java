package com.kianncs.softwareengineering_libraryapp.Fragments;

import com.kianncs.softwareengineering_libraryapp.Entity.BookAdapter;
import com.kianncs.softwareengineering_libraryapp.R;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.API;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.APIManager;
import com.kianncs.softwareengineering_libraryapp.YKpackage.API.AmazonAPI;
import com.kianncs.softwareengineering_libraryapp.YKpackage.entity.Result;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AmazonFragment extends Fragment  {


    ArrayList<Result> abc;
    ListView listView;
    ArrayList<Result> ALResult = new ArrayList<Result>();
    BookAdapter adapter;
    EditText searchQuery;
    ImageView imAlert;
    TextView tvNoResultFound;
    Button buttonNext, buttonBack;

    static int pageNumber;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_amazon,container,false);
        listView = (ListView) v.findViewById(R.id.ListViewAmazonFragment);
        buttonNext = (Button) v.findViewById(R.id.bRightAmazon);
        buttonBack = (Button) v.findViewById(R.id.bLeftAmazon);
        imAlert = (ImageView) v.findViewById(R.id.imAlertAmazon);
        tvNoResultFound = (TextView) v.findViewById(R.id.tvNoResultFoundAmazon);
        searchQuery = (EditText) v.findViewById(R.id.searchAmazonFragmentQuery);
        progressBar = new ProgressDialog(v.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("Getting Result");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        buttonNext.setEnabled(false);
        buttonBack.setEnabled(false);
        if(pageNumber>1)
        {
            buttonNext.setEnabled(true);
            buttonBack.setEnabled(true);
        }

        if(adapter==null){
            adapter = new BookAdapter(getActivity(),R.layout.row_layout);
        }
        listView.setAdapter(adapter);

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });




        searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    imAlert.setVisibility(View.GONE);
                    tvNoResultFound.setVisibility(View.GONE);
                    if (v.getText().toString().isEmpty()) {
                        validateForm();
                    } else {
                        buttonNext.setEnabled(true);
                        buttonBack.setEnabled(false);
                        pageNumber = 1;
                        progressBar.show();
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            adapter.clear();
                            new RetrieveFeedTask().execute();
                        }
                        handled = true;
                    }
                }
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);
                return handled;
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber += 1;
                buttonBack.setEnabled(true);
                progressBar.show();
                adapter.clear();
                new RetrieveFeedTask().execute();

                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageNumber -= 1;
                if (pageNumber == 1) {
                    buttonBack.setEnabled(false);
                }
                progressBar.show();
                adapter.clear();
                new RetrieveFeedTask().execute();

                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent browserIntent = new Intent (Intent.ACTION_VIEW, Uri.parse(ALResult.get(position).getViewItemURL()));
                startActivity(browserIntent);
            }
        });

        return v;
    }

    private boolean validateForm(){
        boolean result = true;
        if (TextUtils.isEmpty(searchQuery.getText().toString())) {
            searchQuery.setError("Required");
            result = false;
        } else {
            searchQuery.setError(null);
        }

        return result;
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        String search="";

        protected void onPreExecute() {
            search = searchQuery.getText().toString();
        }

        protected String doInBackground(Void... urls) {
            APIManager apiManager = new APIManager("amazon");
            ALResult = apiManager.getApiSearchResult(search, Integer.toString(pageNumber));
            return "";
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            for(Result result: ALResult){
                progressBar.setProgress(progressBarStatus);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(result.getTitle()).append("\n");

                stringBuilder.append(result.getCurrentPrice()).append("\n");

                stringBuilder.append(result.getImage()).append("\n");

                stringBuilder.append(result.getViewItemURL()).append("\n");

                stringBuilder.append("======================================").append("\n");

                adapter.add(result);
            }
            progressBar.dismiss();
            if(ALResult.isEmpty()){
                imAlert.setVisibility(View.VISIBLE);
                tvNoResultFound.setVisibility(View.VISIBLE);
            }
            Log.i("INFO", response);
        }
    }


}
