package tw.cody.a1a2b;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends Activity {
    private String answer;
    private int dig = 3, temp = -1;
    private EditText input;
    private TextView log;
    private int counter;
    private long lastTime = 0;
//    private AdView mAdView;
//    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadInterstitialAd();

        input = findViewById(R.id.input);
        log = findViewById(R.id.log);
        answer = createAnswer(dig);
        Log.v("cody", answer);
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }
    private void loadInterstitialAd() {
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-7237500042368815/6672215402");
//        mInterstitialAd.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
////                Toast.makeText(MainActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
//                if(mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
////                Toast.makeText(MainActivity.this, "onAdFailedToLoad()", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest);
    }

    //  Create an answer
    private String createAnswer(int dig){
//        HashSet<Integer> set = new HashSet<>();
//        while (set.size() <dig){
//            set.add((int)(Math.random()*10));
//        }
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) list.add(i);
        Collections.shuffle(list);

        StringBuffer sb = new StringBuffer();
//        for(Integer i : set){
//            sb.append(i);
//        }
        for (int i=0; i<dig; i++){
            sb.append(list.get(i));
        }
//        Log.v("cody", sb.toString());
        return sb.toString();
    }

    public void exit(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Exit?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show();
//        createAnswer(3);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("cody", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        Log.v("brad", "onBackPress");
        if (System.currentTimeMillis() - lastTime > 3*1000){
            lastTime = System.currentTimeMillis();
            Toast.makeText(this, "back one more", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        Log.v("cody", "finish");
    }

    public void setting(View view) {
        String[] items = {"3","4","5","6","7"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Select Game Mode")
                .setSingleChoiceItems(items, dig - 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        temp = which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dig = temp + 3;
                        newGame(null);
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        alertDialog.show();
    }

    public void newGame(View view) {
        Log.v("cody", "new game");
        counter = 0;
        input.setText("");
        log.setText("");
        answer = createAnswer(dig);

    }

    public void guess(View view) {
        counter++;
        String strInput = input.getText().toString();
        if (!isRightNumber(strInput)) {
            return;
        }
        Log.v("cody", "=> " + strInput);
        String result = checkAB(strInput);
//        log.append(strInput + " => " + result + "\n");
        log.append(counter + " : " + strInput + " => " + result + "\n");
        if (result.equals(dig + "A0B")){
            // WINNER
            showDialog(true);
        }else if (counter == 3){
            // LOSER
            showDialog(false);
        }
        input.setText("");
//        if (!isRightNumber(strInput)) {
//            Toast.makeText(this,"請輸入正確的位數",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        counter++;
//
//
//
//        input.setText("");
    }

    private boolean isRightNumber(String g){
        return g.matches("^[0-9]{" + dig + "}$");
    }

    private void showDialog(boolean isWinner){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(isWinner?"WINNER":"Loser")
                .setMessage(isWinner?"恭喜你答對了":"答案為"+answer)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGame(null);
                    }
                })
                .create();

        alertDialog.show();

    }


    private String checkAB(String guess){
        int a, b; a = b = 0;
        for (int i=0; i<guess.length(); i++){
            if (guess.charAt(i) == answer.charAt(i) ){
                a++;
            }else if (answer.indexOf(guess.charAt(i)) >= 0){
                b++;
            }
        }
        return a + "A" + b + "B";
    }
}