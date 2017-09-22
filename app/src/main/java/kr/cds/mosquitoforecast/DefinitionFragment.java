package kr.cds.mosquitoforecast;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DefinitionFragment extends Fragment {      //모기예보제란

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("모기예보제란");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        return inflater.inflate(R.layout.fragment_definition, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView textView = (TextView)getView().findViewById(R.id.textView15);
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://health.seoul.go.kr/mosquito"));
                startActivity(intent);
            }
        });

    }



}
