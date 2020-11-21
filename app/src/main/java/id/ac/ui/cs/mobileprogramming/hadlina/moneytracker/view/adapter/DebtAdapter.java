package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ItemDebtBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.ViewHolder> {

    private List<Debt> debtList;
    private OnItemClickListener clickListener;

    public void setDebtList (List<Debt> debtList) {
        this.debtList = debtList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDebtBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_debt, parent, false
        );
        return new DebtAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Debt debt = debtList.get(position);
        holder.bind(debt, this.clickListener);
    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDebtBinding binding;

        private ViewHolder(@NonNull ItemDebtBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Debt debt, final OnItemClickListener clickListener) {
            binding.setModel(debt);
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onItemClick(view, debt);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Debt debt);
    }
}
