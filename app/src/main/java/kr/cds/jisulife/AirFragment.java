package kr.cds.jisulife;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import kr.go.seoul.airquality.AirQualityButtonTypeA;
import kr.go.seoul.airquality.AirQualityTypeMini;


public class AirFragment extends Fragment {
    private AirQualityTypeMini airQualityTypeMini;
    private AirQualityButtonTypeA airQualityButtonTypeA;
    private String airQuailityKey;
    public AirFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("대기정보");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        return inflater.inflate(R.layout.fragment_air, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        airQualityTypeMini = (AirQualityTypeMini) getActivity().findViewById(R.id.airButton);
        airQualityButtonTypeA = (AirQualityButtonTypeA) getActivity().findViewById(R.id.airButton2);
        airQuailityKey = getString(R.string.airQuailityKey);
        airQuailityKey = getString(R.string.airQuailityKey);
        airQualityTypeMini.setOpenAPIKey(airQuailityKey);
        airQualityButtonTypeA.setOpenAPIKey(airQuailityKey);
        airQualityButtonTypeA.setButtonImage(R.drawable.airbutton);
        airQualityButtonTypeA.setButtonText("크게보기");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
