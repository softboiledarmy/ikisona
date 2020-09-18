package events.tgh2020.hackathon2020;

import android.graphics.Color;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    final PraiseBot pBot = new PraiseBot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////ここから金井////////////////////////////////
        // ListViewに表示する項目を生成
        final ArrayList<String> noodleList= new ArrayList<>();

        noodleList.add("ごはんたべる");
        noodleList.add("あさおきる");
        noodleList.add("本を40P読む");
        noodleList.add("先輩にメールを出す");
        noodleList.add("出前を注文する");

        noodleList.add("線形代数の課題をやる");
        noodleList.add("腹筋30回やる");
        noodleList.add("レストラン予約する");
        noodleList.add("10時からのmtgに出席する");

        /**
         * Adapterを生成
         * android.R.layout.simple_list_item_1 : リストビュー自身のレイアウト。今回はAndroid標準のレイアウトを使用。
         * noodleList : Adapterのコンストラクタの引数としてListViewに表示する項目のリストを渡す。
         */
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, noodleList);

        // idがlistのListViewを取得
        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);


        //***** 消したことのStringを返す*****//
        //final String[] deleteItem = new String[1];
        //********************************//

        // リスト項目を長押しクリックした時の処理。ここでリストが消える
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            /**
             * @param parent ListView
             * @param view 選択した項目
             * @param position 選択した項目の添え字
             * @param id 選択した項目のID
             */
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //deleteItem[0] = (String)((TextView)view).getText();
                String deleteItem = (String)((TextView)view).getText();

                Avatar a = new Avatar();
                a.execute(deleteItem);

                // 項目を追加する
//                arrayAdapter.add("「"+deleteItem + "」を達成したよ！");

                // 選択した項目を削除する
                arrayAdapter.remove(deleteItem);

                return false;
            }
        });

        final EditText praise = findViewById(R.id.praise);
        final Button button = findViewById(R.id.button);
        final ImageView avater_nani = findViewById(R.id.avater_nani);

        //plus押したら現れる
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.VISIBLE);
                praise.setVisibility(View.VISIBLE);
                avater_nani.setVisibility(view.VISIBLE);


            }
        });

        //Enter押したら消える
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //キーボード非表示
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                final String task = praise.getText().toString();
                //リストに追加
                noodleList.add(task);
                listView.setAdapter(arrayAdapter);
                praise.setText("");
                button.setVisibility(View.INVISIBLE);
                praise.setVisibility(View.INVISIBLE);
                avater_nani.setVisibility(View.INVISIBLE);
                avater_nani.setImageResource(R.drawable.avater_fight);
                avater_nani.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO: ここで処理を実行する
                        avater_nani.setImageResource(R.drawable.avater_nanisiyo);
                        avater_nani.setVisibility(View.INVISIBLE);

                    }
                }, 5000);





            }
        });




        //////////////ここまで金井/////////////////////


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    public void avatarToast(String talkString, int bodyId) {
        FrameLayout fl = new FrameLayout(this);
        /*
        lp.topMargin = 上margin;
        lp.bottomMargin = 下margin;
        lp.leftMargin = 左margin;
        lp.rightMargin = 右margin;
         */
        ImageView iv = new ImageView(this);
        iv.setImageResource(bodyId);
        FrameLayout.LayoutParams ivLp = new FrameLayout.LayoutParams(MP,MP);
        ivLp.gravity = Gravity.CENTER;
        fl.addView(iv, ivLp);

        TextView tv = new TextView(this);
        tv.setText(talkString);
        tv.setGravity(Gravity.CENTER);
        //tv.setTextColor(Color.RED);
        //tv.setBackgroundColor(Color.LTGRAY);
        tv.setBackground(getResources().getDrawable(R.drawable.pink_hukidasi_sennnasi));
        tv.setTextSize(25.0f);
        FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(MP,WC);
        tvLp.bottomMargin = 410;
        tvLp.gravity = Gravity.CENTER;
        fl.addView(tv, tvLp);

        Toast toast = new Toast(this);
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(fl);
        toast.show();
    }

    private class PraiseBot {
        private final String[] talkStringArray = {"すごい！", "えらい！", "がんばった！", "やったね！"};
        private final int[] bodyIdArray = {R.drawable.avater_pink, R.drawable.avater_pink2, R.drawable.avater_pink3};
        private Random r = new Random();

        public String getTalk(String task) {
            String gobi2 = task.substring(task.length()-2,task.length());
            String gobi1 = task.substring(task.length()-1,task.length());
            String reply_message = "";
            if(gobi2.equals("する")){
                reply_message = task.substring(0,task.length()-2)+"してえらい！";
            }else if(gobi1.equals("る")){
                reply_message = task.substring(0,task.length()-1)+"てすごい！";
            }else{
                final String[] talkStringArray = {"すごい！", "えらい！", "がんばった！","神！","すてき！"};
                int randomIndex = r.nextInt(talkStringArray.length);
                reply_message = talkStringArray[randomIndex];
            }
            return reply_message;
        }

        public int getBody() {
            int randomIndex = r.nextInt(bodyIdArray.length);
            return bodyIdArray[randomIndex];
        }
    }

    //うまくいかないので保留 使用時はAndroidManifest.xmlの<uses-permission android:name="android.permission.INTERNET" />を有効に
    private class Avatar extends AsyncTask<String, String, String> {
        /**
         * バックグラウンドスレッドで、HTTP通信を行い、応答データを取得して文字列として返す。
         * （この戻り値は、次に実行されることになっているonPostExecute()の引数としてわたります）
         *
         * @param string
         * @return
         */
        @Override
        protected String doInBackground(String... string) {
            StringBuilder rawResult = new StringBuilder();
            try {
                String finishedTask = string[0];
                String json = "{\"question\":\"" + finishedTask + "\"}";

                Map<String, String> httpHeaders = new LinkedHashMap<String, String>();
                httpHeaders.put("Authorization", "EndpointKey 7ff5e408-ed17-499d-a169-d88367e5123e");
                MediaType mediaTypeJson = MediaType.parse("application/json;");
                RequestBody requestBody = RequestBody.create(mediaTypeJson, json);
                Request request = new Request.Builder()
                        .url("https://ikisona-qna.azurewebsites.net/qnamaker/knowledgebases/75c7ab9d-458a-47de-9a10-9fa2a1453a0f/generateAnswer/")
                        .headers(Headers.of(httpHeaders))
                        .post(requestBody)
                        .build();
                OkHttpClient client = new OkHttpClient.Builder()
                        .build();
                Response response = client.newCall(request).execute();
                int responseCode = response.code();
                System.out.println("responseCode: " + responseCode);
                rawResult.append(response.body().string());
            } catch (Exception e) { // 正確には、IOExceptionとMalformedURLExceptionが起こりえます。
                e.printStackTrace();
            }

            return rawResult.toString();
        }

        /**
         * doInBackgroundの仕事が終わったらUIスレッドで呼び出されることになっています。
         * 文字列をいったんJSONに変換してから、適切な項目をUIに貼り付けています。
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonRoot = new JSONObject(result);
                JSONArray answers = jsonRoot.getJSONArray("answers");
                JSONObject o = answers.getJSONObject(0);
                String s = o.getString("answer");

                avatarToast(s, pBot.getBody());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}