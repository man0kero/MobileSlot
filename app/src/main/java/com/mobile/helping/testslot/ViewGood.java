package com.mobile.helping.testslot;

import static android.content.ContentValues.TAG;

import static com.mobile.helping.testslot.Var.balance;
import static com.mobile.helping.testslot.Var.images1;
import static com.mobile.helping.testslot.Var.images2;
import static com.mobile.helping.testslot.Var.images3;
import static com.mobile.helping.testslot.Var.rate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.mobile.helping.testslot.databinding.GoodViewBinding;

import java.util.Random;

public class ViewGood extends AppCompatActivity {

    private GoodViewBinding binding;
    private Random random = new Random();

    private SlotAdapter slotAdapter1;
    private SlotAdapter slotAdapter2;
    private SlotAdapter slotAdapter3;

    private SlotAdapter backSlotAdapter1;
    private SlotAdapter backSlotAdapter2;
    private SlotAdapter backSlotAdapter3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.good_view);

        binding.slotMachineLay.setVisibility(View.INVISIBLE);
        binding.backSlotMachineLay.setVisibility(View.VISIBLE);

        binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
        binding.rateScore.setText(String.format("%,d", rate).replace(',', '_'));

        provideForegroundSLots();
        provideBackgroundSLots();

        binding.playBtn.setOnClickListener(v -> {
            binding.winLoseText.setVisibility(View.INVISIBLE);
//            binding.backSlotMachineLay.setVisibility(View.VISIBLE);
//            binding.slotMachineLay.setVisibility(View.INVISIBLE);
//            binding.playBtn.setEnabled(false);
            playGame();
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

    private void provideForegroundSLots() {
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

    private void provideBackgroundSLots() {
        backSlotAdapter1 = new SlotAdapter(images3);
        backSlotAdapter2 = new SlotAdapter(images2);
        backSlotAdapter3 = new SlotAdapter(images1);

        binding.backLine1.setAdapter(backSlotAdapter1);
        binding.backLine1.setLayoutManager(new LinearLayoutManager(this));

        binding.backLine2.setAdapter(backSlotAdapter2);
        binding.backLine2.setLayoutManager(new LinearLayoutManager(this));

        binding.backLine3.setAdapter(backSlotAdapter3);
        binding.backLine3.setLayoutManager(new LinearLayoutManager(this));
    }

    private void playGame() {
        startAnim();

        int position1 = random.nextInt(slotAdapter1.getItemCount());
        int position2 = random.nextInt(slotAdapter2.getItemCount());
        int position3 = random.nextInt(slotAdapter3.getItemCount());

        LinearLayoutManager layoutManager1 = (LinearLayoutManager) binding.line1.getLayoutManager();
        layoutManager1.scrollToPositionWithOffset(position1, 0);

        LinearLayoutManager layoutManager2 = (LinearLayoutManager) binding.line2.getLayoutManager();
        layoutManager2.scrollToPositionWithOffset(position2, 0);

        LinearLayoutManager layoutManager3 = (LinearLayoutManager) binding.line3.getLayoutManager();
        layoutManager3.scrollToPositionWithOffset(position3, 0);

        Log.d(TAG,
                "position1: " + position1 +
                        "\nposition2: " + position2 +
                        "\nposition3: " + position3);

        checkResults(position1, position2, position3);
    }

    private void startAnim() {
        int animpos = 0;
        if(animpos == backSlotAdapter1.getItemCount()){
            animpos = 0;
        } else {
            animpos = backSlotAdapter1.getItemCount();
        }

        binding.backLine1.smoothScrollToPosition(animpos);
        binding.backLine2.smoothScrollToPosition(animpos);
        binding.backLine3.smoothScrollToPosition(animpos);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.backSlotMachineLay.setVisibility(View.INVISIBLE);
                binding.slotMachineLay.setVisibility(View.VISIBLE);
                binding.playBtn.setEnabled(true);
                binding.winLoseText.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    private void checkResults(int position1, int position2, int position3) {
        int drawable1 = slotAdapter1.getImageResourceId(position1+1);
        int drawable2 = slotAdapter2.getImageResourceId(position2+1);
        int drawable3 = slotAdapter3.getImageResourceId(position3+1);


        if (drawable1 == drawable2 && drawable2 == drawable3) {
            balance += rate;
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
            binding.winLoseText.setText("WIN");
            binding.winLoseText.setTextColor(Color.GREEN);
            binding.winLoseText.setVisibility(View.VISIBLE);


        } else {
            balance -= rate;
            binding.balanceScore.setText(String.format("%,d", balance).replace(',', '_'));
            binding.winLoseText.setText("LOSE");
            binding.winLoseText.setTextColor(Color.RED);
            binding.winLoseText.setVisibility(View.VISIBLE);
        }
    }
}