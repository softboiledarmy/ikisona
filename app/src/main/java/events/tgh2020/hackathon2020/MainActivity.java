package events.tgh2020.hackathon2020;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
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
    // private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    public void avatarToast(String talkString, int bodyId) {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);

        TextView tv = new TextView(this);
        tv.setText(talkString);
        tv.setTextColor(Color.RED);
        tv.setTextSize(50.0f);
        ll.addView(tv, new LinearLayout.LayoutParams(WC, WC));

        ImageView iv = new ImageView(this);
        iv.setImageResource(bodyId);
        ll.addView(iv, new LinearLayout.LayoutParams(WC, WC));

        Toast toast = new Toast(this);
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(ll);
        toast.show();
    }

    private class PraiseBot {
        private final String[] talkStringArray = {"すごい！", "えらい！", "がんばった！"};
        private final int[] bodyIdArray = {R.drawable.superluck};
        private Random r = new Random();

        public String getTalk() {
            int randomIndex = r.nextInt(talkStringArray.length);
            return talkStringArray[randomIndex];
        }

        public int getBody() {
            int randomIndex = r.nextInt(bodyIdArray.length);
            return bodyIdArray[randomIndex];
        }
    }

    /* うまくいかないので保留 使用時はAndroidManifest.xmlの<uses-permission android:name="android.permission.INTERNET" />を有効に
    private class Avatar extends AsyncTask<String, String, String> {
        /**
         * バックグラウンドスレッドで、HTTP通信を行い、応答データを取得して文字列として返す。
         * （この戻り値は、次に実行されることになっているonPostExecute()の引数としてわたります）
         *
         * @param string
         * @return
         /
        @Override
        protected String doInBackground(String... string) {
            StringBuilder rawResult = new StringBuilder();

            try {
                URL url = new URL("https://ikisona-qna.azurewebsites.net/qnamaker/knowledgebases/75c7ab9d-458a-47de-9a10-9fa2a1453a0f/generateAnswer/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "EndpointKey 7ff5e408-ed17-499d-a169-d88367e5123e");
                con.setRequestProperty("Content-type", "application/json");

                String finishedTask = ((EditText)findViewById(R.id.etQ)).getText().toString();
                String json = "{'question':" + finishedTask + "}";
                final OutputStream out = con.getOutputStream();
                final PrintStream ps = new PrintStream(out, true, "UTF-8");
                ps.print(json);
                ps.close();

                con.connect();

                final int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));

                    String line;
                    while ((line = br.readLine()) != null) {
                        rawResult.append(line);
                    }
                }
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
         /
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonRoot = new JSONObject(result);
                JSONArray answers = jsonRoot.getJSONArray("answers");

                JSONObject o = answers.getJSONObject(0);
                String s = o.getString("answer");

                avatarToast(s, R.drawable.superluck);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    */
}