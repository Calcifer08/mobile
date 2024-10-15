package ru.mirea.lozhnichenkoas.currencyscope.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.mirea.lozhnichenkoas.currencyscope.R;
import ru.mirea.lozhnichenkoas.domain.models.Currency;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder> {

    public interface OnCurrencyClickListener{
        void onCurrencyClick(Currency currency);
    }

    private List<Currency> currencyList;
    private OnCurrencyClickListener onCurrencyClickListener;

    public CurrencyAdapter(List<Currency> currencyList, OnCurrencyClickListener onCurrencyClickListener) {
        this.currencyList = currencyList;
        this.onCurrencyClickListener = onCurrencyClickListener;
    }

    public static class CurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView currencyNominal;
        TextView currencyName;
        TextView currencyCode;
        TextView currencyValue;
        ImageView currencyIcon;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            currencyNominal = itemView.findViewById(R.id.textItemCurrencyNominal);
            currencyName = itemView.findViewById(R.id.textItemCurrencyName);
            currencyCode = itemView.findViewById(R.id.textItemCurrencyCode);
            currencyValue = itemView.findViewById(R.id.textItemCurrencyValue);
            currencyIcon = itemView.findViewById(R.id.imageItemCurrencyIcon);
        }

        public void bind(Currency currency, OnCurrencyClickListener listener) {
            itemView.setOnClickListener(v -> listener.onCurrencyClick(currency));
        }
    }

    @NonNull
    @Override
    public CurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency, parent, false);
        return new CurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyViewHolder holder, int position) {
        Currency currency = currencyList.get(position);
        holder.currencyNominal.setText(String.valueOf(currency.getNominal()));
        holder.currencyName.setText(currency.getName());
        holder.currencyCode.setText(currency.getCharCode());
        holder.currencyValue.setText(String.valueOf(currency.getValue()));
        holder.currencyIcon.setImageResource(currency.getIconId());

        int backgroundColor = (position % 2 == 0) ? R.color.white : R.color.light_grey;
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), backgroundColor));

        holder.bind(currency, onCurrencyClickListener);
    }

    @Override
    public int getItemCount(){
        return currencyList.size();
    }
}
