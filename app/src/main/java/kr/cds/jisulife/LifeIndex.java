package kr.cds.jisulife;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;

import java.util.Comparator;

public class LifeIndex extends AppCompatActivity implements Comparator{


    private int value;
    private int color;
    public String name;
    public Drawable mainImage;
    public Drawable indicator;
    public LifeIndex indexClass;
    public float divideValue;
    private String date;
    public int grade;

    public void setValue(String value) {
        this.value = Integer.valueOf(value);
    }

    public String getValue() {
        return String.valueOf(value);
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getGradeToInt() {
        return grade;
    }

    public String getGradeToString(String data) {
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
        return 1;
    }


}
