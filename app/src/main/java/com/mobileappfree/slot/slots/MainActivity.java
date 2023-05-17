package com.mobileappfree.slot.slots;

import static android.content.ContentValues.TAG;
import static com.mobileappfree.slot.slots.Var.balance;
import static com.mobileappfree.slot.slots.Var.images1;
import static com.mobileappfree.slot.slots.Var.images2;
import static com.mobileappfree.slot.slots.Var.images3;
import static com.mobileappfree.slot.slots.Var.rate;


import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.ads.AdRequest;
import com.mobileappfree.slot.slots.ads.AdOpen;
import com.mobileappfree.slot.slots.ads.InterstitialAdImpl;
import com.mobileappfree.slot.slots.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AdRequest adRequest;
    private InterstitialAdImpl interstitialAd;

    private SlotAdapter slotAdapter1;
    private SlotAdapter slotAdapter2;
    private SlotAdapter slotAdapter3;

    private Handler handler;
    private Runnable checkScrollingRunnable;

    private boolean isFirstListScrollingFinished = false;
    private boolean isSecondListScrollingFinished = false;
    private boolean isThirdListScrollingFinished = false;
    private boolean isPlaying = false;

    private int previousPosition1 = 0;
    private int previousPosition2 = 0;
    private int previousPosition3 = 0;
    private long resultLine1;
    private long resultLine2;
    private long resultLine3;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        provideSlotsAdapters();
        setClickListeners();

        loadAds();

        handler = new Handler();
        checkScrollingRunnable = new Runnable() {
            @Override
            public void run() {
                checkScrollingStatus();
                handler.postDelayed(this, 2000);
            }
        };

        binding.balanceScore.setText(String.format("%,d", balance).replace(',', ' '));
        binding.rateScore.setText(String.format("%,d", rate).replace(',', ' '));
    }

    private void loadAds() {
        Application application = getApplication();
        ((AdOpen) application).showAdIfAvailable(MainActivity.this,
                new AdOpen.OnShowAdCompleteListener() {
                    @Override
                    public void onShowAdComplete() {
                        Log.d(TAG, "onShowAdComplete: ");
                    }
                });
        adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        interstitialAd = new InterstitialAdImpl();
        interstitialAd.loadInterstitialAd(this);
    }

    private void setClickListeners() {
        binding.scrollOff.setOnClickListener(v -> {
            Toast.makeText(this, "Press Play", Toast.LENGTH_SHORT).show();
        });

        binding.playBtn.setOnClickListener(v -> {
                binding.winLoseTextRight.setVisibility(View.INVISIBLE);
                binding.winLoseTextLeft.setVisibility(View.INVISIBLE);
                binding.playBtn.setEnabled(false);
                playGame();
                count++;
        });

        binding.minus.setOnClickListener(v -> {
            rate -= 500;
            if (rate <= 500) {
                rate = 500;
                binding.rateScore.setText(String.format("%,d", rate).replace(',', ' '));
            } else {
                binding.rateScore.setText(String.format("%,d", rate).replace(',', ' '));
            }
            interstitialAd.showAds(this);
        });

        binding.plus.setOnClickListener(v -> {
            rate += 500;
            if (rate >= 2000) {
                rate = 2000;
                binding.rateScore.setText(String.format("%,d", rate).replace(',', ' '));
            } else {
                binding.rateScore.setText(String.format("%,d", rate).replace(',', ' '));
            }
            interstitialAd.showAds(this);
        });
    }

    private void provideSlotsAdapters() {
        slotAdapter1 = new SlotAdapter(images1);
        slotAdapter2 = new SlotAdapter(images2);
        slotAdapter3 = new SlotAdapter(images3);

        binding.line1.setAdapter(slotAdapter1);
        binding.line1.setLayoutManager(new LinearLayoutManager(this));

        binding.line2.setAdapter(slotAdapter2);
        binding.line2.setLayoutManager(new LinearLayoutManager(this));

        binding.line3.setAdapter(slotAdapter3);
        binding.line3.setLayoutManager(new LinearLayoutManager(this));
    }

    private void playGame() {
        isPlaying = true;
        Random random = new Random();
        int position1;
        do {
            position1 = random.nextInt((slotAdapter1.getItemCount() - 5) - (5) + 1) + (5);
        } while (position1 == previousPosition1);
        previousPosition1 = position1;

        int position2;
        do {
            position2 = random.nextInt((slotAdapter1.getItemCount() - 5) - (5) + 1) + (5);
        } while (position2 == previousPosition2);
        previousPosition2 = position2;

        int position3;
        do {
            position3 = random.nextInt((slotAdapter1.getItemCount() - 5) - (5) + 1) + (5);
        } while (position3 == previousPosition3);
        previousPosition3 = position3;
        Log.d(TAG, "playGame: " + position1 + " " + position2 + " " + position3);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int middlePosition =
                            ((LinearLayoutManager) recyclerView.getLayoutManager())
                                    .findFirstVisibleItemPosition() + 1;
                    if (recyclerView == binding.line1) {
                        resultLine1 = slotAdapter1.getImageResourceId(middlePosition);
                    } else if (recyclerView == binding.line2) {
                        resultLine2 = slotAdapter2.getImageResourceId(middlePosition);
                    } else if (recyclerView == binding.line3) {
                        resultLine3 = slotAdapter3.getImageResourceId(middlePosition);
                    }
                }
            }
        };

        binding.line1.addOnScrollListener(scrollListener);
        binding.line1.smoothScrollToPosition(position1);

        binding.line2.addOnScrollListener(scrollListener);
        binding.line2.smoothScrollToPosition(position2);

        binding.line3.addOnScrollListener(scrollListener);
        binding.line3.smoothScrollToPosition(position3);

        handler.postDelayed(checkScrollingRunnable, 2000);
    }

    private void checkScrollingStatus() {
        if (isPlaying) {
            if (!binding.line1.getLayoutManager().isSmoothScrolling()) {
                isFirstListScrollingFinished = true;
            }

            if (!binding.line2.getLayoutManager().isSmoothScrolling()) {
                isSecondListScrollingFinished = true;
            }

            if (!binding.line3.getLayoutManager().isSmoothScrolling()) {
                isThirdListScrollingFinished = true;
            }

            if (isFirstListScrollingFinished && isSecondListScrollingFinished && isThirdListScrollingFinished) {
                isPlaying = false;
                binding.playBtn.setEnabled(true);

                isFirstListScrollingFinished = false;
                isSecondListScrollingFinished = false;
                isThirdListScrollingFinished = false;

                handler.removeCallbacks(checkScrollingRunnable);
                checkResults(resultLine1, resultLine2, resultLine3);
            }
        }
    }

    private void checkResults(long position1, long position2, long position3) {
        if (position1 == position2 && position2 == position3) {
            balance += rate;
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', ' '));
            textSetter("WIN");
            interstitialAd.showAds(this);
            Log.d(TAG, "checkResults: WIN");
        } else {
            balance -= rate;
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', ' '));
            textSetter("LOSE");
            interstitialAd.showAds(this);
            Log.d(TAG, "checkResults: LOSE");
        }
    }

    private void textSetter(String textResult) {
        if (textResult.equals("WIN")) {
            binding.winLoseTextRight.setTextColor(getColor(R.color.winColor));
            binding.winLoseTextLeft.setTextColor(getColor(R.color.winColor));
        } else {
            binding.winLoseTextRight.setTextColor(getColor(R.color.lose_color));
            binding.winLoseTextLeft.setTextColor(getColor(R.color.lose_color));
        }
        binding.winLoseTextRight.setText(textResult);
        binding.winLoseTextRight.setVisibility(View.VISIBLE);

        binding.winLoseTextLeft.setText(textResult);
        binding.winLoseTextLeft.setVisibility(View.VISIBLE);
    }
}