package kr.cds.jisulife;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import kr.cds.jisulife.lifeIndex.AsthmaWhoIndex;
import kr.cds.jisulife.lifeIndex.BrainWhoIndex;
import kr.cds.jisulife.lifeIndex.DsplsIndex;
import kr.cds.jisulife.lifeIndex.FsnIndex;
import kr.cds.jisulife.lifeIndex.HeatLifeIndex;
import kr.cds.jisulife.lifeIndex.InflWhoListIndex;
import kr.cds.jisulife.lifeIndex.SensorytmeLifeIndex;
import kr.cds.jisulife.lifeIndex.SkinWhoIndex;
import kr.cds.jisulife.lifeIndex.UltraIndex;
import kr.cds.jisulife.lifeIndex.WinterLifeIndex;

public class WeatherFragment extends Fragment {

    private static final float ULTRA_DIVIDE_VALUE = 0.44f;
    private static final float DSPLS_DIVIDE_VALUE = 8.0f;
    private static final float FSN_DIVIDE_VALUE = 6.0f;
    private static final float HEATLIFE_DIVIDE_VALUE = 9.9f;
    private static final float WINTERLIFE_DIVIDE_VALUE = 8.7f;
    private static float SENSORYTEM_DIVIDE_VALUE = -1.25f;
    private static final float INFLWHOLIST_DIVIDE_VALUE = 0.09f;
    private String[] value;
    private String[] showDate;
    private String date;
    private boolean INTERNET_STATE = false;
    private UltraIndex ultraIndex;
    private DsplsIndex dsplsIndex;
    private FsnIndex fsnIndex;
    private HeatLifeIndex heatLifeIndex;
    private InflWhoListIndex inflWhoIndex;
    private SensorytmeLifeIndex sensorytemLifeIndex;
    private WinterLifeIndex winterLifeIndex;
    private AsthmaWhoIndex asthmaWhoIndex;
    private BrainWhoIndex brainWhoIndex;
    private SkinWhoIndex skinWhoIndex;
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;
    boolean threadEnd = false;
    private String key;
    private String queryUrlUltra;
    private String queryUrlDspls;
    private String queryUrlFsn;
    private String queryUrlHeatLife;
    private String queryWinterLife;
    private String querySeonsorytemLife;
    private String queryInflWhoList;
    private String queryasthmaWho;
    private String querybrainWho;
    private String queryskinWho;
    private ProgressBar progressBar;
    private boolean timeout = true;
    private static String location;

    // 공유(share)
    //////////////////////////////////////////////////////////////////////////
    public void shareKakao(LifeIndex lifeIndex, String name, String value) {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(getActivity());
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoBuilder.addText("오늘의 " + name + "는 " + lifeIndex.getGradeToString(value) + "(" + value + ") 입니다.");
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");
            kakaoLink.sendMessage(kakaoBuilder, getContext());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setShareDialog() {
        String[] array = {"자외선지수", "불쾌지수", "식중독지수", "열지수",
                "감기가능지수", "체감온도", "동파가능지수", "천식폐질환가능지수", "뇌졸증가능지수", "피부질환가능지수"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                if (pos == 0 && !value[0].equals("")) {
                    shareKakao(ultraIndex, "자외선지수", value[0]);
                } else if (pos == 1 && !value[1].equals("")) {
                    shareKakao(dsplsIndex, "불쾌지수", value[1]);
                } else if (pos == 2 && !value[2].equals("")) {
                    shareKakao(fsnIndex, "식중독지수", value[2]);
                } else if (pos == 3 && !value[3].equals("")) {
                    shareKakao(heatLifeIndex, "열지수", value[3]);
                } else if (pos == 4 && !value[4].equals("")) {
                    shareKakao(inflWhoIndex, "감기가능지수", value[4]);
                } else if (pos == 5 && !value[5].equals("")) {
                    shareKakao(sensorytemLifeIndex, "체감온도", value[5]);
                } else if (pos == 6 && !value[6].equals("")) {
                    shareKakao(winterLifeIndex, "동파가능지수", value[6]);
                } else if (pos == 7 && !value[7].equals("")) {
                    shareKakao(asthmaWhoIndex, "천식폐질환가능지수", value[7]);
                } else if (pos == 8 && !value[8].equals("")) {
                    shareKakao(brainWhoIndex, "뇌졸증가능지수", value[8]);
                } else if (pos == 9 && !value[9].equals("")) {
                    shareKakao(skinWhoIndex, "피부질환가능지수", value[9]);
                } else
                    Toast.makeText(getActivity(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    // 갱신, 추가, 삭제
    /////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.weather_actions, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            RefreshThread refreshThread = new RefreshThread();
            date = getDateString();
            refreshThread.run();
            /*try {
                RefreshThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            return (true);
        }
        if (item.getItemId() == R.id.add) {
            date = getDateString();
            setAddDialog();
            return (true);
        }
        if (item.getItemId() == R.id.share) {
            setShareDialog();
            return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setAddDialog() {
        String[] array = {"자외선지수 (3월~11월)", "불쾌지수 (6월~9월)", "식중독지수 (연중)", "열지수 (6월~9월)"
                , "감기가능지수 (9월~익년4월)", "체감온도 (11월~익년3월)", "동파가능지수 (12월~익년2월)", "천식폐질환가능지수(연중)", "뇌졸중가능지수(연중)", "피부질환가능지수(연중)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                progressBar.setVisibility(View.VISIBLE);
                String[] urls = {queryUrlUltra, queryUrlDspls, queryUrlFsn, queryUrlHeatLife, queryInflWhoList, querySeonsorytemLife, queryWinterLife, queryasthmaWho, querybrainWho, queryskinWho};
                if (checkInternet()) {
                    INTERNET_STATE = true;
                    if (pos == 0 && checkMonth(3, 11)) {
                        thread(urls[pos], date, pos);
                        ultraIndex.setValue(value[0]);
                        new AddThread(ultraIndex, "자외선지수", ULTRA_DIVIDE_VALUE, R.drawable.ultra, showDateTime(showDate[0])).start();
                    } else if (pos == 1 && checkMonth(6, 9)) {
                        thread(urls[pos], date, pos);
                        dsplsIndex.setValue(value[1]);
                        new AddThread(dsplsIndex, "불쾌지수", DSPLS_DIVIDE_VALUE, R.drawable.dspls, showDateTime(showDate[1])).start();
                    } else if (pos == 2) {
                        thread(urls[pos], date, pos);
                        fsnIndex.setValue(value[2]);
                        new AddThread(fsnIndex, "식중독지수", FSN_DIVIDE_VALUE, R.drawable.fsn, showDateTime(showDate[2])).start();
                    } else if (pos == 3 && checkMonth(6, 9)) {
                        thread(urls[pos], date, pos);
                        heatLifeIndex.setValue(value[3]);
                        new AddThread(heatLifeIndex, "열지수", HEATLIFE_DIVIDE_VALUE, R.drawable.heatlife, showDateTime(showDate[3])).start();
                    } else if (pos == 4 && checkMonth(9, 4)) {
                        thread(urls[pos], date, pos);
                        inflWhoIndex.setValue(value[4]);
                        new AddThread(inflWhoIndex, "감기가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.inflwholist, showDateTime(showDate[4])).start();
                    } else if (pos == 5 && checkMonth(11, 3)) {
                        thread(urls[pos], date, pos);
                        if (Integer.parseInt(value[5]) > 0)
                            SENSORYTEM_DIVIDE_VALUE = 100.0f;
                        sensorytemLifeIndex.setValue(value[5]);
                        new AddThread(sensorytemLifeIndex, "체감온도", SENSORYTEM_DIVIDE_VALUE, R.drawable.seonsorytem, showDateTime(showDate[5])).start();
                    } else if (pos == 6 && checkMonth(12, 2)) {
                        thread(urls[pos], date, pos);
                        winterLifeIndex.setValue(value[6]);
                        new AddThread(winterLifeIndex, "동파가능지수", WINTERLIFE_DIVIDE_VALUE, R.drawable.winterlife, showDateTime(showDate[6])).start();
                    } else if (pos == 7) {
                        thread(urls[pos], date, pos);
                        asthmaWhoIndex.setValue(value[7]);
                        new AddThread(asthmaWhoIndex, "천식폐질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.asthmawho, showDateTime(showDate[7])).start();
                    } else if (pos == 8) {
                        thread(urls[pos], date, pos);
                        brainWhoIndex.setValue(value[8]);
                        new AddThread(brainWhoIndex, "뇌졸중가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.brainwho, showDateTime(showDate[8])).start();
                    } else if (pos == 9) {
                        thread(urls[pos], date, pos);
                        skinWhoIndex.setValue(value[9]);
                        new AddThread(skinWhoIndex, "피부질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.skinwho, showDateTime(showDate[9])).start();
                    } else
                        Toast.makeText(getActivity(), "제공기간이 아닙니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "네트워크가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        builder.show();
    }

    public class RefreshThread extends Thread {
        String name;
        String[] urls = {queryUrlUltra, queryUrlDspls, queryUrlFsn, queryUrlHeatLife, queryInflWhoList,
                querySeonsorytemLife, queryWinterLife, queryasthmaWho, querybrainWho, queryskinWho};

        public RefreshThread() {
        }

        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    while (mAdapter.getCount() > 0) {
                        if (checkInternet()) {
                            INTERNET_STATE = true;
                            name = mAdapter.getItem(0).name;
                            if (name.equals("자외선지수") && checkMonth(3, 11)) {
                                thread(urls[0], date, 0);
                                mAdapter.remove(0);
                                ultraIndex.setValue(value[0]);
                                new AddThread(ultraIndex, "자외선지수", ULTRA_DIVIDE_VALUE, R.drawable.ultra, showDateTime(showDate[0])).start();
                            } else if (name.equals("불쾌지수") && checkMonth(6, 9)) {
                                thread(urls[1], date, 1);
                                mAdapter.remove(0);
                                dsplsIndex.setValue(value[1]);
                                new AddThread(dsplsIndex, "불쾌지수", DSPLS_DIVIDE_VALUE, R.drawable.dspls, showDateTime(showDate[1])).start();
                            } else if (name.equals("식중독지수")) {
                                thread(urls[2], date, 2);
                                mAdapter.remove(0);
                                fsnIndex.setValue(value[2]);
                                new AddThread(fsnIndex, "식중독지수", FSN_DIVIDE_VALUE, R.drawable.fsn, showDateTime(showDate[2])).start();
                            } else if (name.equals("열지수") && checkMonth(6, 9)) {
                                thread(urls[3], date, 3);
                                mAdapter.remove(0);
                                heatLifeIndex.setValue(value[3]);
                                new AddThread(heatLifeIndex, "열지수", HEATLIFE_DIVIDE_VALUE, R.drawable.heatlife, showDateTime(showDate[3])).start();
                            } else if (name.equals("감기가능지수") && checkMonth(9, 4)) {
                                thread(urls[4], date, 4);
                                mAdapter.remove(0);
                                inflWhoIndex.setValue(value[4]);
                                new AddThread(inflWhoIndex, "감기가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.inflwholist, showDateTime(showDate[4])).start();
                            } else if (name.equals("체감온도") && checkMonth(11, 3)) {
                                thread(urls[5], date, 5);
                                mAdapter.remove(0);
                                if (Integer.parseInt(value[5]) > 0)
                                    SENSORYTEM_DIVIDE_VALUE = 100.0f;
                                sensorytemLifeIndex.setValue(value[5]);
                                new AddThread(sensorytemLifeIndex, "체감온도", SENSORYTEM_DIVIDE_VALUE, R.drawable.seonsorytem, showDateTime(showDate[5])).start();
                            } else if (name.equals("동파가능지수") && checkMonth(12, 2)) {
                                thread(urls[6], date, 6);
                                mAdapter.remove(0);
                                winterLifeIndex.setValue(value[6]);
                                new AddThread(winterLifeIndex, "동파가능지수", WINTERLIFE_DIVIDE_VALUE, R.drawable.winterlife, showDateTime(showDate[6])).start();
                            } else if (name.equals("천식폐질환가능지수")) {
                                thread(urls[7], date, 7);
                                mAdapter.remove(0);
                                asthmaWhoIndex.setValue(value[7]);
                                new AddThread(asthmaWhoIndex, "천식폐질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.asthmawho, showDateTime(showDate[7])).start();
                            } else if (name.equals("뇌졸중가능지수")) {
                                thread(urls[8], date, 8);
                                mAdapter.remove(0);
                                brainWhoIndex.setValue(value[8]);
                                new AddThread(brainWhoIndex, "뇌졸중가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.brainwho, showDateTime(showDate[8])).start();
                            } else if (name.equals("피부질환가능지수")) {
                                thread(urls[9], date, 9);
                                mAdapter.remove(0);
                                skinWhoIndex.setValue(value[9]);
                                new AddThread(skinWhoIndex, "피부질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.skinwho, showDateTime(showDate[9])).start();
                            } else {
                                mAdapter.remove(0);
                                Toast.makeText(getActivity(), "제공기간이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "네트워크가 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public class AddThread extends Thread {
        private LifeIndex lifeIndex;
        private String name;
        private float divideValue;
        private int image;
        private String date;


        AddThread(LifeIndex lifeIndex, String name, float divideValue, int image, String date) {
            this.lifeIndex = lifeIndex;
            this.name = name;
            this.divideValue = divideValue;
            this.image = image;
            this.date = date;
        }

        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.addItem(lifeIndex, getResources().getDrawable(image), getResources().getDrawable(R.drawable.indicator),
                            name,  divideValue, date);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class removeThread extends Thread {
        int position;

        private removeThread(int position) {
            this.position = position;
        }

        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.remove(position);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    //리스트뷰(ListView)
    ///////////////////////////////////////////////////////////////

    public class ViewHolder {
        TextView name;
        TextView value;
        TextView content;
        TextView grade;
        ImageView mainImage;
        ImageView indicator;
        Button deleteButton;
        TextView dateView;

    }

    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<LifeIndex> mListData = new ArrayList<>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public int getCount() {
            return mListData.size();
        }

        @Override
        public LifeIndex getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, null);

                holder.mainImage = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.indicator = (ImageView) convertView.findViewById(R.id.indicator1);
                holder.name = (TextView) convertView.findViewById(R.id.textView1);
                holder.value = (TextView) convertView.findViewById(R.id.textView6);
                holder.content = (TextView) convertView.findViewById(R.id.textView9);
                holder.deleteButton = (Button) convertView.findViewById(R.id.delete_Button);
                holder.dateView = (TextView) convertView.findViewById(R.id.dateTextView);
                holder.grade = (TextView) convertView.findViewById(R.id.gradeTextView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            LifeIndex mData = mListData.get(position);
            LifeIndex inClass =  mData.indexClass;

            if (mData.mainImage != null) {
                holder.mainImage.setVisibility(View.VISIBLE);
                holder.mainImage.setImageDrawable(mData.mainImage);
            } else {
                holder.mainImage.setVisibility(View.GONE);
            }

            if (mData.indicator != null) {
                holder.indicator.setVisibility(View.VISIBLE);
                holder.indicator.setImageDrawable(mData.indicator);
            } else {
                holder.indicator.setVisibility(View.GONE);
            }

            holder.name.setText(mData.name);
            holder.value.setText(inClass.getGradeToString(inClass.getValue()) + "(" + (inClass.getValue()) + ")");
            setColor(holder.value, inClass);
            holder.content.setText(inClass.getData());
            mData.grade = inClass.getGradeToInt();

            holder.dateView.setText(mData.getDate());     //날짜

            //float scale = ((float)getScreenWidth()/1080);
            TranslateAnimation anim = new TranslateAnimation
                    (Animation.RELATIVE_TO_SELF, 0,   // fromXDelta
                            Animation.RELATIVE_TO_SELF, Float.valueOf(inClass.getValue()) / mData.divideValue,  // toXDelta
                            Animation.RELATIVE_TO_SELF, 0,    //fromYDelta
                            Animation.RELATIVE_TO_SELF, 0); //toYDelta

            anim.setDuration(1000);
            anim.setFillAfter(true);
            holder.indicator.startAnimation(anim);

            holder.deleteButton.setTag(position);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag();
                    new removeThread(position).start();
                }
            });

            convertView.setTag(holder);
            return convertView;
        }

        Comparator<LifeIndex> cmpAsc = new Comparator<LifeIndex>() {    //정렬

            @Override
            public int compare(LifeIndex o1, LifeIndex o2) {
                return o1.name.compareTo(o2.name);

            }
        };

        public void addItem(LifeIndex indexClass, Drawable mainImage, Drawable indicator,
                            String name,  float divideValue, String date) {
            LifeIndex addInfo = new LifeIndex();
            addInfo.indexClass = indexClass;
            addInfo.mainImage = mainImage;
            addInfo.indicator = indicator;
            addInfo.name = name;
            addInfo.divideValue = divideValue;
            addInfo.setDate(date);

            mListData.add(0, addInfo);

            Collections.sort(mListData, cmpAsc);
        }

        public void remove(int position) {
            mListData.remove(position);
            mAdapter.notifyDataSetChanged();
        }

    }


    //메인(main)
    ///////////////////////////////////////////////////////////////////////
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("생활기상");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        key = getResources().getString(R.string.key2);

        queryUrlUltra = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getUltrvLifeList?serviceKey="   //자외선지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryUrlDspls = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getDsplsLifeList?serviceKey="    //불쾌지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryUrlFsn = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getFsnLifeList?serviceKey="   //식중독지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryUrlHeatLife = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getHeatLifeList?serviceKey="  //열지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryInflWhoList = "http://newsky2.kma.go.kr/iros/RetrieveWhoIndexService2/getInflWhoList?serviceKey="  //감기가능지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        querySeonsorytemLife = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getSensorytemLifeList?serviceKey="  //체감온도
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryWinterLife = "http://newsky2.kma.go.kr/iros/RetrieveLifeIndexService2/getWinterLifeList?serviceKey="       //동파가능지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryasthmaWho = "http://newsky2.kma.go.kr/iros/RetrieveWhoIndexService2/getAsthmaWhoList?serviceKey="      //천식폐질환가능지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        querybrainWho = "http://newsky2.kma.go.kr/iros/RetrieveWhoIndexService2/getBrainWhoList?serviceKey="        //뇌졸중가능지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        queryskinWho = "http://newsky2.kma.go.kr/iros/RetrieveWhoIndexService2/getSkinWhoList?serviceKey="      //피부질환가능지수
                + key +
                "&areaNo=" +
                location +
                "&time=";
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    public void setLocation(String location){
        WeatherFragment.location = location;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        value = new String[10];
        showDate = new String[10];
        setRetainInstance(true);
        setHasOptionsMenu(true);

        ultraIndex = new UltraIndex();
        dsplsIndex = new DsplsIndex();
        fsnIndex = new FsnIndex();
        heatLifeIndex = new HeatLifeIndex();
        inflWhoIndex = new InflWhoListIndex();
        sensorytemLifeIndex = new SensorytmeLifeIndex();
        winterLifeIndex = new WinterLifeIndex();
        asthmaWhoIndex = new AsthmaWhoIndex();
        brainWhoIndex = new BrainWhoIndex();
        skinWhoIndex = new SkinWhoIndex();

        if (checkInternet()) {
            INTERNET_STATE = true;
        }

        mListView = (ListView) getView().findViewById(R.id.listView);
        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);
        loadPreferences();
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }
    /*
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void thread(final String url, final String date, final int index) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (INTERNET_STATE) {
                    try {
                        String[] data = getWeather(url, date);
                        showDate[index] = data[0];
                        value[index] = data[1];  //생활지수
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "서버가 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadEnd = true;
    }

    private void setColor(TextView textView, LifeIndex lifeIndex) {
        textView.setBackgroundResource(R.drawable.rounded_corner);
        GradientDrawable drawable = (GradientDrawable) textView.getBackground();
        drawable.setColor(lifeIndex.getColor());
    }

    CountDownTimer countDownTimer = new CountDownTimer(10 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
        }

        public void onFinish() {
            timeout = false;
        }
    };


    public String[] getWeather(String queryUrl, String date) throws Exception {
        Looper.prepare();
        countDownTimer.start();
        String temp = null;
        StringBuffer buffer = new StringBuffer();
        queryUrl += date;
        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));
            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if (tag.equals("date")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append(",");
                        }
                        if (tag.equals("h3")) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        if (tag.equals("today")) {
                            xpp.next();
                            temp = xpp.getText();
                            if (temp != null) {
                                buffer.append(temp);
                                buffer.append("\n");
                            }
                        }
                        if (tag.equals("tomorrow") && temp == null) {
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("/row")) buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
                if (timeout == false)
                    throw new Exception("timeout");
            }
            if (buffer.toString().trim().equals("")) {
                throw new Exception();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw e;
            //getWeather(queryUrl, getYesterday()); //공백일 경우 어제 날짜로 다시 받아온다.
        }
        return buffer.toString().trim().split(",");
    }


    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH", Locale.KOREA);
        return df.format(new Date());
    }

    public String showDateTime(String showDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH", Locale.KOREA);
        Date date = new Date();
        try {
            date = df.parse(showDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd(" + getDayOfWeek(showDate) + ") HH:00", Locale.KOREA);
        return sdf.format(date);
    }

    public boolean checkMonth(int startMonth, int endMonth) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (startMonth < endMonth) {
            if (startMonth <= currentMonth && currentMonth <= endMonth) {
                return true;
            } else
                return false;
        } else {
            if (startMonth <= currentMonth || currentMonth <= endMonth) {
                return true;
            } else
                return false;
        }

    }

    public String getDayOfWeek(String showDate) {
        String day = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH", Locale.KOREA);
        Date nDate = new Date();
        try {
            nDate = dateFormat.parse(showDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

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
            // not connected to the internet
        }
    }

    //값 불러오기
    private void loadPreferences() {
        SharedPreferences pref = getContext().getSharedPreferences("weatherPref", Context.MODE_PRIVATE);
        for (int i = 0; i < showDate.length; i++) {
            showDate[i] = pref.getString("date" + i, "");
        }
        String[] valueArray = {"ultra", "dspls", "fsn", "heat", "inflwho", "seonsorytem", "winter", "asthma", "brain", "skin"};
        for (int i = 0; i <= valueArray.length - 1; i++) {
            value[i] = pref.getString(valueArray[i], "");
        }
        for (int i = 0; ; i++) {
            String str = pref.getString(String.valueOf(i), "");

            if (!str.equals("")) {
                if (str.equals("자외선지수") && value[0] != null) {
                    ultraIndex.setValue(value[0]);
                    new AddThread(ultraIndex, "자외선지수", ULTRA_DIVIDE_VALUE, R.drawable.ultra, showDateTime(showDate[0])).start();
                }
                if (str.equals("불쾌지수") && value[1] != null) {
                    dsplsIndex.setValue(value[1]);
                    new AddThread(dsplsIndex, "불쾌지수", DSPLS_DIVIDE_VALUE, R.drawable.dspls, showDateTime(showDate[1])).start();
                }
                if (str.equals("식중독지수") && value[2] != null) {
                    fsnIndex.setValue(value[2]);
                    new AddThread(fsnIndex, "식중독지수", FSN_DIVIDE_VALUE, R.drawable.fsn, showDateTime(showDate[2])).start();
                }
                if (str.equals("열지수") && value[3] != null) {
                    heatLifeIndex.setValue(value[3]);
                    new AddThread(heatLifeIndex, "열지수", HEATLIFE_DIVIDE_VALUE, R.drawable.heatlife, showDateTime(showDate[3])).start();
                }
                if (str.equals("감기가능지수") && value[4] != null) {
                    inflWhoIndex.setValue(value[4]);
                    new AddThread(inflWhoIndex, "감기가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.inflwholist, showDateTime(showDate[4])).start();
                }
                if (str.equals("체감온도") && value[5] != null) {
                    sensorytemLifeIndex.setValue(value[5]);
                    new AddThread(sensorytemLifeIndex, "체감온도", SENSORYTEM_DIVIDE_VALUE, R.drawable.seonsorytem, showDateTime(showDate[5])).start();
                }
                if (str.equals("동파가능지수") && value[6] != null) {
                    winterLifeIndex.setValue(value[6]);
                    new AddThread(winterLifeIndex, "동파가능지수", WINTERLIFE_DIVIDE_VALUE, R.drawable.winterlife, showDateTime(showDate[6])).start();
                }
                if (str.equals("천식폐질환가능지수") && value[7] != null) {
                    asthmaWhoIndex.setValue(value[7]);
                    new AddThread(asthmaWhoIndex, "천식폐질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.asthmawho, showDateTime(showDate[7])).start();
                }
                if (str.equals("뇌졸중가능지수") && value[8] != null) {
                    brainWhoIndex.setValue(value[8]);
                    new AddThread(brainWhoIndex, "뇌졸중가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.brainwho, showDateTime(showDate[8])).start();
                }
                if (str.equals("피부질환가능지수") && value[9] != null) {
                    skinWhoIndex.setValue(value[9]);
                    new AddThread(skinWhoIndex, "피부질환가능지수", INFLWHOLIST_DIVIDE_VALUE, R.drawable.skinwho, showDateTime(showDate[9])).start();
                }
            } else
                break;
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    // 값 저장하기
    private void savePreferences() {
        SharedPreferences pref = getActivity().getSharedPreferences("weatherPref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            Log.i("저장", String.valueOf(i) + mAdapter.getItem(i).name);
            editor.putString(String.valueOf(i), mAdapter.getItem(i).name);
        }

        editor.putString("ultra", value[0]);
        editor.putString("dspls", value[1]);
        editor.putString("fsn", value[2]);
        editor.putString("heat", value[3]);
        editor.putString("inflwho", value[4]);
        editor.putString("seonsorytem", value[5]);
        editor.putString("winter", value[6]);
        editor.putString("asthma", value[7]);
        editor.putString("brain", value[8]);
        editor.putString("skin", value[9]);
        for (int i = 0; i < showDate.length; i++) {
            editor.putString("date" + i, showDate[i]);
        }
        editor.apply();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (value != null) {
            savePreferences();
        }
    }
}



