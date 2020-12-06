package com.bsuir.project.quizapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bsuir.project.quizapp.FirebaseInstance;
import com.bsuir.project.quizapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, null);

        final Button playButton = v.findViewById(R.id.play);
        final Button recordsButton = v.findViewById(R.id.records);
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        final RecordsFragment recordsFragment = new RecordsFragment();
        final QuestionFragment questionFragment = new QuestionFragment();
        final QuizTopFragment quizTopFragment = new QuizTopFragment();
        final Bundle bundle = new Bundle();

        //adMob
        final AdView adView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        ///////////////////////////////////////
        final TextView textLevels;
        final Animation animAppears = AnimationUtils.loadAnimation(getContext(), R.anim.translate_to_screen);
        final Animation animGone = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
        final Animation scaleFromZero = AnimationUtils.loadAnimation(getContext(), R.anim.scale_from_zero);
        final Animation scaleToZero = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_zero);
        ///////////////////////////////

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.play:
                        if (new FirebaseInstance().getQuestions().isEmpty()){
                            Toast.makeText(getActivity(),"Пожалуйста, подождите...",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        bundle.putInt("level",5);
                        questionFragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.topFragment, quizTopFragment, "QuizTopFragment");
                        fragmentTransaction.replace(R.id.mainFragment, questionFragment, "QuestionFrag");
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.records:
                        if (new FirebaseInstance().getRecords().isEmpty()){
                            Toast.makeText(getActivity(),"Пожалуйста, подождите...",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        adView.setVisibility(View.INVISIBLE);

                        fragmentTransaction.replace(R.id.mainFragment, recordsFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
            }
        };
        playButton.setOnClickListener(clickListener);
        recordsButton.setOnClickListener(clickListener);
        return v;
    }

    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}
