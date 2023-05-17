package com.mobile.helping.testslot;

import static com.mobile.helping.testslot.Var.ITEM_COUNT;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.helping.testslot.databinding.ItemSlotBinding;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ItemViewHolder> {

    private int[] images;

    public SlotAdapter(int[] images){
        this.images = images;
    }

    @NonNull
    @Override
    public SlotAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSlotBinding binding =
                ItemSlotBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotAdapter.ItemViewHolder holder, int position) {
        int imageResId = images[position%images.length];
        holder.binding.slotImage.setImageResource(imageResId);


    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemSlotBinding binding;

        public ItemViewHolder(@NonNull ItemSlotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public int getImageResourceId(int position) {
        int imageResId = images[position % images.length];
        return imageResId;
    }

}
