package devs.mulham.raee.sample;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.kmitl57.beelife.R;
import com.project.kmitl57.beelife.SetTimeDialog;
import com.project.kmitl57.beelife.SettingsActivity;

import static devs.mulham.raee.sample.CustomDialogClass.statusActivity;
import static devs.mulham.raee.sample.CustomDialogClass.typeshare;

public class ShareTypeDialog extends Dialog {
    Button req;
    public ShareTypeDialog(@NonNull final Context context) {
        super(context);
    }
    Button pub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_type_dialog);
        req = (Button) findViewById(R.id.req);
        pub = (Button) findViewById(R.id.pub);


        //----------------------------------------------


        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusActivity=3;
                        typeshare.setText("SocialPlan");
                        cancel();
                    }
                }, 300);

            }
        });
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusActivity=1;
                        typeshare.setText("PubShare");
                        cancel();
                    }
                }, 300);

            }
        });
    }
}
