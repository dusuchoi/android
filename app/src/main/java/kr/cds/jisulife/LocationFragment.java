package kr.cds.jisulife;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LocationFragment extends Fragment {
    ListView listview = null;
    TextView textView;
    Bundle bundle;
    CommunicationListener mCallback;
    String location;
    String[] texts;
    StringBuffer stringBuffer;
    String chooseLocation;
    HashMap<String, String> map;
    EditText editTextFilter;
    ListViewAdapter adapter;

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

        //adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        adapter = new ListViewAdapter();

        listview = (ListView) getView().findViewById(R.id.listview1);
        textView = (TextView) getView().findViewById(R.id.currentTextView);
        editTextFilter = (EditText) getView().findViewById(R.id.editTextFilter);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                chooseLocation = ((ListViewItem)parent.getAdapter().getItem(position)).getTitle();
                textView.setText(chooseLocation);
                mCallback.sendData(map.get(chooseLocation));
                Toast.makeText(getActivity(), "저장 완료", Toast.LENGTH_SHORT).show();
            }
        });

        loadAddressData();
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                ((ListViewAdapter)listview.getAdapter()).getFilter().filter(filterText) ;
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

    public class ListViewItem {
        private String titleStr;
        private String descStr;

        public void setTitle(String title) {
            titleStr = title;
        }

        public void setDesc(String desc) {
            descStr = desc;
        }

        public String getTitle() {
            return this.titleStr;
        }
    }

    public class ListViewAdapter extends BaseAdapter implements Filterable {
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList. (원본 데이터 리스트)
        private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
        // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
        private ArrayList<ListViewItem> filteredItemList = listViewItemList;
        Filter filter;

        public ListViewAdapter() {

        }

        @Override
        public int getCount() {
            return filteredItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return filteredItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item2, parent, false);
            }

            TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
            ListViewItem listViewItem = filteredItemList.get(position);

            titleTextView.setText(listViewItem.getTitle());

            return convertView;
        }



        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new ListFilter();
            }
            return filter;
        }
        public void add(String title) {
            ListViewItem item = new ListViewItem();
            item.setTitle(title);
            listViewItemList.add(item);
        }

        private class ListFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults() ;

                if (constraint == null || constraint.length() == 0) {
                    results.values = listViewItemList ;
                    results.count = listViewItemList.size() ;
                } else {
                    ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>() ;

                    for (ListViewItem item : listViewItemList) {
                        if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())){
                            itemList.add(item) ;
                        }
                    }
                    results.values = itemList ;
                    results.count = itemList.size() ;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredItemList = (ArrayList<ListViewItem>) results.values ;

                // notify
                if (results.count > 0) {
                    notifyDataSetChanged() ;
                } else {
                    notifyDataSetInvalidated() ;
                }
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


