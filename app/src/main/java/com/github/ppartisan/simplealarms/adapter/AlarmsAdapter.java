package com.github.ppartisan.simplealarms.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ppartisan.simplealarms.R;
import com.github.ppartisan.simplealarms.model.Alarm;
import com.github.ppartisan.simplealarms.util.AlarmUtils;

import java.util.List;

public final class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {

    private List<Alarm> mAlarms;

    private String[] mDays;
    private int mAccentColor = -1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context c = parent.getContext();
        final View v = LayoutInflater.from(c).inflate(R.layout.alarm_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mAccentColor == -1) {
            final Context c = holder.itemView.getContext();
            mAccentColor = ContextCompat.getColor(c, R.color.colorAccent);
            mDays = c.getResources().getStringArray(R.array.days_abbreviated);
        }

        final Alarm alarm = mAlarms.get(position);

        holder.time.setText(AlarmUtils.getReadableTime(alarm.getTime()));
        holder.amPm.setText(AlarmUtils.getAmPm(alarm.getTime()));
        holder.label.setText(alarm.getLabel());
        holder.days.setText(buildSelectedDays(alarm));

    }

    @Override
    public int getItemCount() {
        return (mAlarms == null) ? 0 : mAlarms.size();
    }

    private Spannable buildSelectedDays(Alarm alarm) {

        final int numDays = 7;
        final SparseBooleanArray days = alarm.getDays();

        final SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan span;

        int startIndex, endIndex;
        for (int i = 0; i < numDays; i++) {

            startIndex = builder.length();

            final String dayText = mDays[i];
            builder.append(dayText);
            builder.append(" ");

            endIndex = startIndex + dayText.length();

            final boolean isSelected = days.valueAt(i);
            if(isSelected) {
                span = new ForegroundColorSpan(mAccentColor);
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return builder;

    }

    public void setAlarms(List<Alarm> alarms) {
        mAlarms = alarms;
        notifyDataSetChanged();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, amPm, label, days;

        ViewHolder(View itemView) {
            super(itemView);

            time = (TextView) itemView.findViewById(R.id.ar_time);
            amPm = (TextView) itemView.findViewById(R.id.ar_am_pm);
            label = (TextView) itemView.findViewById(R.id.ar_label);
            days = (TextView) itemView.findViewById(R.id.ar_days);

        }
    }

}