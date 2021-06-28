package kr.cds.jisulife.lifeIndex;
import android.graphics.Color;

public class AirDiffusionIndex extends LifeIndex {

    private int value;
    private int color;
    private int grade;

    private String data[]={
            "▶기상조건에 의해 대기변화 가능성이 낮음",
            "▶ 기상조건에 의해 대기변화 가능성이 보통",
                    "▶ 기상조건에 의해 대기변화 가능성이 높음",
            "▶ 기상조건에 의해 대기변화 가능성이 매우 높음"};

    @Override
    public void setValue(String value) {
        this.value = Integer.valueOf(value);
    }
    @Override
    public String getValue() {
        return String.valueOf(value);
    }
    @Override
    public int getGradeToInt() {
        return this.grade;
    }


    @Override
    public String getGradeToString(String data) {

        value = Integer.valueOf(data);
        if (value == 100) {
            color= Color.rgb(229,229,229);
            grade=0;
            return "낮음";
        } else if (value == 75) {
            color=Color.rgb(254,217,142);
            grade=1;
            return "보통";
        } else if (value == 50) {
            color=Color.rgb(253,141,60);
            grade=2;
            return "높음";
        } else
            color=Color.rgb(240,59,32);
            grade=3;
            return "매우높음";
    }
    public int getColor(){
        return color;
    }

    public String getData(){
        switch (grade){
            case 0:
                return data[0];
            case 1:
                return data[1];
            case 2:
                return data[2];
            case 3:
                return data[3];
        }
        return null;
    }
}
