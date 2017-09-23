package kr.cds.mosquitoforecast;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class ApplicationBase extends Application {      //폰트설정
    public void onCreate(){
        super.onCreate();


        Typekit.getInstance();
                //.addNormal(Typekit.createFromAsset(this,"SeoulNamsanM.ttf"))
                //.addBold(Typekit.createFromAsset(this,"SeoulNamsanB.ttf"));

    }

}
