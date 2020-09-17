package events.tgh2020.hackathon2020;

import android.widget.ImageView;
import android.widget.TextView;

public class Avatar {
    private ImageView myBody;
    private TextView myTalk;

    Avatar(ImageView iv, TextView tv) {
        myBody = iv;
        myTalk = tv;
    }

    public void sayPraise() {
        myTalk.setText("えらい！");
    }
}
