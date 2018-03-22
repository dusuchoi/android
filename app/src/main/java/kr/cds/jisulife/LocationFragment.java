package kr.cds.jisulife;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LocationFragment extends Fragment {
    private static final String STATE_LOCATION = "location";
    ListView listview = null;
    TextView textView;
    Bundle bundle;
    CommunicationListener mCallback;
    String location;
    String[] texts;
    StringBuffer stringBuffer;
    String chooseLocation;
    ArrayAdapter adapter;
    HashMap<String, String> map;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (CommunicationListener) context;

        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("위치설정");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map = new HashMap<String, String>();
        bundle = new Bundle();

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        listview = (ListView) getView().findViewById(R.id.listview);
        textView = (TextView) getView().findViewById(R.id.currentTextView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                chooseLocation = (String) parent.getItemAtPosition(position);
                textView.setText(chooseLocation);
                mCallback.sendData(map.get(chooseLocation));
                Toast.makeText(getActivity(),"저장완료",Toast.LENGTH_SHORT).show();
            }
        });

        loadAddressData();

        EditText editTextFilter = (EditText) getView().findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText);
                } else {
                    listview.clearTextFilter();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        loadPreferences();
    }

    private void loadAddressData() {

        AssetManager am = getResources().getAssets();
        InputStream is = null;

        stringBuffer = new StringBuffer();
        try {
            is = am.open("locationFile.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));


            while ((location = bufferedReader.readLine()) != null) {
                texts = location.split("\t");
                for (int i = 2; i < texts.length; i++) {
                    stringBuffer.append(texts[i] + " ");
                }
                location = stringBuffer.toString();
                adapter.add(location);
                map.put(location, texts[1]);
                stringBuffer = new StringBuffer();
            }
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void loadPreferences() {
        SharedPreferences pref = getContext().getSharedPreferences("locationPref", Context.MODE_PRIVATE);
        chooseLocation = pref.getString("location", "서울특별시");
        textView.setText(chooseLocation);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
    private void savePreferences() {
        SharedPreferences pref = getActivity().getSharedPreferences("locationPref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("location", chooseLocation);
        editor.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        savePreferences();
    }
}



