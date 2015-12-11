package net.lion.okvel.financemanager.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.entity.RecyclerViewItem;
import net.lion.okvel.financemanager.util.MoneyUtil;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<RecyclerViewItem> dataList;
    private Resources resources;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StringBuilder builder = new StringBuilder();
        String amount = MoneyUtil.moneyToString(dataList.get(position).getAmount().abs().toString());

        builder.append(amount)
                .append(" ")
                .append(dataList.get(position).getCurrency());
        holder.amountTextView.setText(builder.toString());
        holder.circleImageView.setImageResource(dataList.get(position).getImageId());

        builder = new StringBuilder();
        builder.append(resources.getString(R.string.recycler_view_item_category))
                .append(" ")
                .append(dataList.get(position).getCategory());
        holder.categoryTextView.setText(builder.toString());

        GregorianCalendar calendar = dataList.get(position).getDate();
        builder = new StringBuilder();
        builder.append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(".")
                .append(calendar.get(Calendar.MONTH) + 1)
                .append(".")
                .append(calendar.get(Calendar.YEAR));
        holder.dateTextView.setText(builder.toString());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTextView;
        public TextView categoryTextView;
        public TextView dateTextView;
        public CircleImageView circleImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            amountTextView = (TextView) itemView.findViewById(R.id.item_title);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_circle);
            categoryTextView = (TextView) itemView.findViewById(R.id.item_category);
            dateTextView = (TextView) itemView.findViewById(R.id.item_date);
        }
    }

    public RecyclerViewAdapter(List<RecyclerViewItem> list, Resources resources) {
        dataList = list;
        this.resources = resources;
    }
}
