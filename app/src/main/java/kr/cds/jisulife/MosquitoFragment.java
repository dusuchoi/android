package kr.cds.jisulife;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MosquitoFragment extends Fragment {
    private boolean INTERNET_STATE = true;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView12;
    TextView textView13;
    Button btnHouse;
    Button btnWater;
    Button btnPark;
    private String value;   //모기지수
    ImageView imageView;
    ScrollView scrollView;
    String key1;
    int textColor = 0;
    String date;
    String grade;
    public static MosquitoEntry mosquitoEntry;
    private ProgressBar progressBar;

    public void shareKakao() {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getActivity());
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoBuilder.addText("오늘의 모기지수는 " + grade + " " + value + "입니다.");
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder, getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mosquito_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            INTERNET_STATE = checkInternet();
            thread("MOSQUITO_VALUE_HOUSE");
            return (true);
        }
        if (item.getItemId() == R.id.share) {
            shareKakao();
            return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("모기예보");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        return inflater.inflate(R.layout.fragment_mosquito, container, false);

    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
        textView2 = getView().findViewById(R.id.textView2);
        textView3 = getView().findViewById(R.id.textView3);
        textView4 = getView().findViewById(R.id.textView4);
        textView12 = getView().findViewById(R.id.textView12);
        textView13 = getView().findViewById(R.id.textView13);
        imageView = getView().findViewById(R.id.indicator);
        scrollView = getView().findViewById(R.id.backGround);
        btnHouse = getView().findViewById(R.id.btnHouse);
        btnPark = getView().findViewById(R.id.btnPark);
        btnWater = getView().findViewById(R.id.btnWater);
        scrollView = getView().findViewById(R.id.backGround);
        key1 = getResources().getString(R.string.key1);
        progressBar = getView().findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        btnHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread("MOSQUITO_VALUE_HOUSE");
            }
        });

        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread("MOSQUITO_VALUE_WATER");
            }
        });

        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread("MOSQUITO_VALUE_PARK");
            }
        });


        if (checkInternet() == false) {
            INTERNET_STATE = false;

            if (getPreferences() == false)
                Toast.makeText(getActivity(), "네트워크가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }

        thread("MOSQUITO_VALUE_HOUSE");
    }

    public void thread(final String criteria) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (INTERNET_STATE) {
                    value = getXmlData(getDateString(), criteria); //모기지수
                } else
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        // TODO Auto-generated method stub
                        date = getDateStringToShow();
                        textView2.setText(date);
                        textView3.setText(value);  //TextView에 문자열  valueTextView 출력
                        textView4.setText(getGrade(value));

                        mosquitoEntry = MainActivity.dbHelper.fetchEntryByIndex(getGradeDetail(Double.valueOf(value)));
                        grade = mosquitoEntry.getmGrade();

                        textView12.setText(mosquitoEntry.getDefence_activity());
                        textView13.setText(mosquitoEntry.getAggressive_activity());

                        setColor();
                        TranslateAnimation anim = new TranslateAnimation
                                (Animation.RELATIVE_TO_SELF, 0,           // fromXDelta
                                        Animation.RELATIVE_TO_SELF, Float.valueOf(value) < 120 ? (int)(Float.valueOf(value) / 4.3) : 26,  // toXDelta
                                        Animation.RELATIVE_TO_SELF, 0,    // fromYDelta
                                        Animation.RELATIVE_TO_SELF, 0);     // toYDelta
                        anim.setDuration(1000);
                        anim.setFillAfter(true);
                        imageView.startAnimation(anim);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        t.start();
    }

    private long getGradeDetail(double value) {
        //1단계
        if (0 <= value && value <= 60) {
            if (0 <= value && value <= 10) {
                return 1;
            } else if (10.1 <= value && value <= 20) {
                return 2;
            } else if (20.1 <= value && value <= 30) {
                return 3;
            }

            //2단계
            else if (30.1 <= value && value <= 40) {
                return 4;
            } else if (40.1 <= value && value <= 50) {
                return 5;
            } else if (50.1 <= value && value <= 60) {
                return 6;
            }
        } else if (60.1 <= value && value <= 120) {
            //3단계
            if (60.1 <= value && value <= 70) {
                return 7;
            } else if (70.1 <= value && value <= 80) {
                return 8;
            } else if (80.1 <= value && value <= 90) {
                return 9;
            }

            //4단계
            else if (90.1 <= value && value <= 100) {
                return 10;
            } else if (100.1 <= value && value <= 110) {
                return 11;
            } else if (110.1 <= value && value <= 120) {
                return 12;
            }
        }
        return 12;

    }

    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            }
            return true;
        } else {
            return false;
        }
    }

    //값 불러오기
    private boolean getPreferences() {
        SharedPreferences pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        value = pref.getString("valueTextView", "");
        if (value == "")
            return false;
        date = pref.getString("dateView", "");
        textView2.setText(date);
        textView3.setText(value);
        textView4.setText(getGrade(value));
        return true;
    }

    // 값 저장하기
    private void savePreferences() {
        SharedPreferences pref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("valueTextView", value);
        editor.putString("dateView", date);
        editor.putInt("color", textColor);
        editor.putString("grade", grade);
        editor.commit();
    }


    private void setColor() {
        textView3.setBackgroundResource(R.drawable.rounded_corner);
        GradientDrawable drawable1 = (GradientDrawable) textView3.getBackground();
        textView4.setBackgroundResource(R.drawable.rounded_corner);
        GradientDrawable drawable2 = (GradientDrawable) textView4.getBackground();
        drawable1.setColor(textColor);
        drawable2.setColor(textColor);
    }

    public String getGrade(String data) {
        double value = Double.valueOf(data);
        if (value <= 30) {
            textColor = Color.rgb(255, 205, 189);
            return "1단계(쾌적)";
        } else if (30.1 <= value && value <= 60) {
            textColor = Color.rgb(255, 153, 51);
            return "2단계(관심)";
        } else if (60.1 <= value && value <= 90) {
            textColor = Color.rgb(255, 178, 102);
            return "3단계(주의)";
        } else if (90.1 <= value && value <= 120) {
            textColor = Color.RED;
            return "4단계(불쾌)";
        } else
            return "4단계(불쾌)";
    }

    public String getXmlData(String date, String criteria) {
        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://openapi.seoul.go.kr:8088/"
                + key1    //요청 URL
                + "/xml"
                + "/MosquitoStatus"
                + "/1/5"
                + "/" + date;

        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기
            String tag;
            xpp.next();
            int eventType = xpp.getEventType();
            label1:
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //태그 이름 얻어오기

                        if (tag.equals(criteria)) {
                            xpp.next();
                            buffer.append(xpp.getText()); //grade 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n");          //줄바꿈 문자 추가
                        }
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();    //태그 이름 얻어오기

                        if (tag.equals(criteria)) {
                            buffer.append("\n"); // 첫번째 검색결과종료..줄바꿈
                            break label1;
                        }
                        break;
                }
                eventType = xpp.next();

            }
            if (buffer.toString().trim().equals("")) {
                throw new Exception("");
            }
        } catch (Exception e) {
            return getXmlData(getYesterday(), "MOSQUITO_VALUE_HOUSE");
        }
        return buffer.toString().trim();
    }


    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA); // HH:mm:ss
        return df.format(new Date());
    }

    public String getDateStringToShow() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd(" + getDay() + ")", Locale.KOREA);
        return df.format(new Date());
    }

    public String getDay() {
        String day = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;
        }
        return day;
    }

    public String getYesterday() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        return df.format(yesterday());
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public void onDestroy() {
        super.onDestroy();
        savePreferences();
    }


}