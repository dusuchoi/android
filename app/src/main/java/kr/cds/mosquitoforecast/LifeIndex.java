package kr.cds.mosquitoforecast;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import java.util.Comparator;

public class LifeIndex extends AppCompatActivity implements Comparator{
    public String value;
    private int color;
    public String name;
    public Drawable mainImage;
    public Drawable indicator;
    public LifeIndex indexClass;
    public float divideValue;
    public String date;

    public void setValue(String value) {
        this.value = value;
    }

    public String getGradeIndex(String data) {
            return null;

    }

    public int getColor(){
        return color;
    }


    public String getData() {
        return "확인";
    }

    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }


}
