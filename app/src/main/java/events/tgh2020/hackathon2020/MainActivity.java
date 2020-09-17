package events.tgh2020.hackathon2020;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

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
        noodleList.add("ホームパーティーを開く");

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
                String deleteItem = (String)((TextView)view).getText();

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        final Avatar avatar = new Avatar((ImageView)findViewById(R.id.avatar_body), (TextView)findViewById(R.id.avatar_talk));

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
    
    public void avatarToast(Avatar avatar) {
        // avatar.sayPraise();

        View v = this.getLayoutInflater().inflate(R.layout.toast_avatar, null);

        Toast toast = new Toast(getApplicationContext());
        // toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(v);
        toast.show();
    }
}