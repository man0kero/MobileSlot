package com.mobile.helping.testslot;

import static com.mobile.helping.testslot.Var.ITEM_COUNT;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.helping.testslot.databinding.ItemSlotBinding;
import com.mobile.helping.testslot.databinding.ItemTestSlotBinding;

public class SlotAdapter_test extends RecyclerView.Adapter<SlotAdapter_test.ItemViewHolder> {

    private int[] images;

    public SlotAdapter_test(int[] images){
        this.images = images;
    }

    @NonNull
    @Override
    public SlotAdapter_test.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTestSlotBinding binding =
                ItemTestSlotBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SlotAdapter_test.ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter_test.ItemViewHolder holder, int position) {
        int imageResId = images[position%images.length];
        holder.binding.slotImage.setImageResource(imageResId);
//        holder.binding.position.setText(String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemTestSlotBinding binding;

        public ItemViewHolder(@NonNull ItemTestSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public int getImageResourceId(int position) {
        int imageResId = images[position % images.length];
        return imageResId;
    }

}
