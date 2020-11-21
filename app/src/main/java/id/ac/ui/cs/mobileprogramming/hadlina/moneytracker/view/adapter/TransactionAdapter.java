package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ItemTransactionBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private OnItemClickListener clickListener;

    public void setTransactionList (List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_transaction, parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.bind(transaction, this.clickListener);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransactionBinding binding;

        private ViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Transaction transaction, final OnItemClickListener clickListener) {
            binding.setModel(transaction);

            if (transaction.getType().equalsIgnoreCase("earnings")) binding.tvItemType.setImageResource(R.drawable.ic_arrow_upward_24dp);
            else binding.tvItemType.setImageResource(R.drawable.ic_arrow_downward_24dp);

            switch (transaction.getCategory().toLowerCase()) {
                case "food":
                    binding.itemImage.setImageResource(R.drawable.ic_restaurant_24dp);
                    break;
                case "vehicle":
                    binding.itemImage.setImageResource(R.drawable.ic_car_24dp);
                    break;
                case "entertainment":
                    binding.itemImage.setImageResource(R.drawable.ic_videogame_24dp);
                    break;
                case "income":
                    binding.itemImage.setImageResource(R.drawable.ic_money_24dp);
                    break;
                default:
                    binding.itemImage.setImageResource(R.drawable.ic_not_found_24dp);
                    break;
            }
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null)
                        clickListener.onItemClick(view, transaction);
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, Transaction transaction);
    }
}
