package com.mobile.helping.testslot;

import static android.content.ContentValues.TAG;

import static com.mobile.helping.testslot.Var.balance;
import static com.mobile.helping.testslot.Var.images1;
import static com.mobile.helping.testslot.Var.images2;
import static com.mobile.helping.testslot.Var.images3;
import static com.mobile.helping.testslot.Var.rate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.helping.testslot.databinding.SlotBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SlotBinding binding;

    SlotAdapter_test slotAdapter1;
    SlotAdapter_test slotAdapter2;
    SlotAdapter_test slotAdapter3;

    int previousPosition1 = 0;
    int previousPosition2 = 0;
    int previousPosition3 = 0;
    int position1;
    int position2;
    int position3;
    long resultLine1;
    long resultLine2;
    long resultLine3;
    RecyclerView.OnScrollListener scrollListener;


    private Handler handler = new Handler();
    private Runnable checkScrollingRunnable = new Runnable() {
        @Override
        public void run() {
            checkScrollingStatus();
            handler.postDelayed(this, 2000);
        }
    };

    private boolean isFirstListScrollingFinished = false;
    private boolean isSecondListScrollingFinished = false;
    private boolean isThirdListScrollingFinished = false;
    private boolean isPlaying = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.slot);

        provideSlotsAdapters();
        setClickListeners();



        binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
        binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));
    }

    private void setClickListeners() {
        binding.button.setOnClickListener(v -> {
            binding.winLoseText.setVisibility(View.INVISIBLE);
            binding.button.setEnabled(false);
            playGame();
        });

        binding.mainView.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewGood.class);
            this.startActivity(intent);
        });

        binding.minus.setOnClickListener(v -> {
            rate -= 500;
            if (rate <= 500) {
                rate = 500;
                binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));
            } else {
                binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));
            }
        });

        binding.plus.setOnClickListener(v -> {
            rate += 500;
            if (rate >= 2000) {
                rate = 2000;
                binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));
            } else {
                binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));
            }
        });
    }

    private void provideSlotsAdapters() {
        slotAdapter1 = new SlotAdapter_test(images1);
        slotAdapter2 = new SlotAdapter_test(images2);
        slotAdapter3 = new SlotAdapter_test(images3);

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
        do {
            position1 = random.nextInt(slotAdapter1.getItemCount());
        } while (position1 == previousPosition1);
        previousPosition1 = position1;

        do {
            position2 = random.nextInt(slotAdapter2.getItemCount());
        } while (position2 == previousPosition2);
        previousPosition2 = position2;

        do {
            position3 = random.nextInt(slotAdapter3.getItemCount());
        } while (position3 == previousPosition3);
        previousPosition3 = position3;
        Log.d(TAG, "playGame: " + position1 + " " + position2 + " " + position3);

        scrollListener = new RecyclerView.OnScrollListener() {
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
        if(isPlaying) {
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
                binding.button.setEnabled(true);
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
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
            binding.winLoseText.setText("WIN");
            binding.winLoseText.setTextColor(Color.GREEN);
            Log.d(TAG, "checkResults: WIN");
            binding.getRoot().setBackgroundColor(Color.GREEN);

        } else {
            balance -= rate;
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
            binding.winLoseText.setText("LOSE");
            binding.winLoseText.setTextColor(Color.RED);
            Log.d(TAG, "checkResults: LOSE");
        }
    }
}