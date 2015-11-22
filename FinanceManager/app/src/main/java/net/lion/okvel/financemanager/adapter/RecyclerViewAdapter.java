package net.lion.okvel.financemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lion.okvel.financemanager.R;
import net.lion.okvel.financemanager.bean.RecyclerViewItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private List<RecyclerViewItem> dataList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.amountTextView.setText(dataList.get(position).getAmount());
        holder.circleImageView.setImageResource(dataList.get(position).getColorId());
        holder.categoryTextView.setText(dataList.get(position).getCategory());
    }

    @Override
    public int getItemCount()
    {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView amountTextView;
        public TextView categoryTextView;
        public CircleImageView circleImageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            amountTextView = (TextView) itemView.findViewById(R.id.item_title);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_circle);
            categoryTextView = (TextView) itemView.findViewById(R.id.item_category);
        }
    }

    public RecyclerViewAdapter(List<RecyclerViewItem> list)
    {
        dataList = list;
    }
}
