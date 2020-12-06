package com.bsuir.project.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bsuir.project.quizapp.FirebaseInstance;
import com.bsuir.project.quizapp.R;
import com.bsuir.project.quizapp.entity.Question;
import com.bsuir.project.quizapp.fragment.MenuBannerFragment;
import com.bsuir.project.quizapp.fragment.MenuFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    //    private SQLiteOpenHelper dbHelper;
//    public static SQLiteDatabase db;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Question question = new Question("Какая таблица помогает извлечь квадратный корень из числа?", 5, "Таблица синусов", "Таблица формул", "Таблица квадратов", "Таблица Менделева", 3);
        //Question question = new Question("Вычислите значение выражения: √13 · √26 · √2.", 5, "13", "4", "26", "15", 3);
       // Question question = new Question("Найдите пятый член арифметической прогрессии: 15,  8,  ...  .", 5, "1", "-13", "-6", "7", 2);
/*        Question question = new Question("Решите неравенство:\n" +
                "\n" +
                "(х-7)(х+2)<=0", 5, "[-2;7)", "[-2;-7]", "[-2;7]", "(-2;7]", 3);*/
/*        Question question = new Question("Решите уравнение:\n" +
                "\n" +
                "7(х-50)+8х = 10", 5, "1", "24", "0", "40", 3);*/
       // Question question = new Question("Какому углу соответствует cosx = 0.5?", 5, "π/3", "π/6", "π/4", "π/2", 1);
/*        Question question = new Question("Найдите производную функции:\n" +
                "\n" +
                "6х2 + cos 3х - ех", 5, "12 + 3sin3x - ex", "12x - 3sin3x - ex", "6x - sin3x - ex", "6 - 3sin3x - ex", 2);*/
/*        Question question = new Question("Решите уравнение:\n" +
                "\n" +
                "64х – 8х – 56 = 0", 5, "8", "-7", "1", "7", 3);*/
       // Question question = new Question("Квадрат гипотенузы прямоугольного треугольника равен ...", 5, "Сумме квадратов катетов этого треугольника", "Сумме катетов этого треугольника", "Разности квадратов катетов", "Невозможно определить", 1);
/*        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("questions").push().setValue(question, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d("TAG", "onComplete: asdasd");
            }
        });*/

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


               MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        if (isOnline()) {
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mRewardedVideoAd.setRewardedVideoAdListener(this);
            loadRewardedVideoAd();

            MenuFragment menuFrag = new MenuFragment();
            MenuBannerFragment banner = new MenuBannerFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mainFragment, menuFrag, "MenuFragment");
            fragmentTransaction.add(R.id.topFragment, banner);
            fragmentTransaction.commit();

            FirebaseApp.initializeApp(this);
            FirebaseInstance.extractQuestionsFromFirebase();
            FirebaseInstance.extractRecordsFromFirebase();
        } else {
            Toast.makeText(this, "Необходимо подключение к интернету\nПерезапустите приложение при включенном интернете", Toast.LENGTH_LONG).show();
        }

//        DBHelper dbHelper = new DBHelper(this);
//        try {
//            db = dbHelper.getWritableDatabase();
//        } catch (SQLiteException e) {
//            Toast toast = Toast.makeText(this, "Не удалось получить ссылку на базу данных", Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    protected void onResumeFragments() {
        FirebaseInstance.extractRecordsFromFirebase();
        super.onResumeFragments();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        MenuFragment menuFrag = new MenuFragment();
        MenuBannerFragment banner = new MenuBannerFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, menuFrag, "MenuFragment");
        fragmentTransaction.replace(R.id.topFragment, banner);
        fragmentTransaction.commit();
    }

    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }


    //video ad
    @Override
    public void onRewardedVideoAdLoaded() {
      //  Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
      //  Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
       // Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
    }


    //Loading ad
    private RewardedVideoAd mRewardedVideoAd;
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }


    //Life-cycle
    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
