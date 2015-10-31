package net.lion.okvel.financemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.lion.okvel.financemanager.R;

/**
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private String[] dataSet;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.textView.setText(dataSet[position]);
    }

    @Override
    public int getItemCount()
    {
        return dataSet.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    public RecyclerViewAdapter(String[] data)
    {
        dataSet = data;
    }
}
