package com.kiko.chat.presentation.chat.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kiko.chat.R;
import com.kiko.chat.domain.entity.Message;
import com.kiko.chat.domain.util.DateUtil;
import com.kiko.chat.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context context;
    private List<Message> messages;
    private ImageLoader imageLoader;

    public ChatAdapter(Context context, List<Message> messages, ImageLoader imageLoader) {
        this.context = context;
        this.messages = messages;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_chat_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = messages.get(i);

        String msg = message.getMassage();
        viewHolder.txtMessage.setText(msg);

        viewHolder.txtDate.setText(DateUtil.getDateString(message.getTimestamp()));


        int color = fetchColor(R.attr.colorAccent);
        int gravity = Gravity.LEFT;

        if (message.isSendByCurrentUser()) {
            gravity = Gravity.RIGHT;
            color = fetchColor(R.attr.colorPrimary);
        }

        viewHolder.messageConteiner.setBackgroundColor(color);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.linearLayout.getLayoutParams();
        params.gravity = gravity;
        viewHolder.linearLayout.setLayoutParams(params);

        if (message.isImage()) {
            viewHolder.imageContent.setVisibility(View.VISIBLE);
            viewHolder.txtMessage.setVisibility(View.GONE);

            imageLoader.loadImage(message.getMassage(), viewHolder.imageContent, 1000);
        } else {
            viewHolder.txtMessage.setVisibility(View.VISIBLE);
            viewHolder.imageContent.setVisibility(View.GONE);
        }

        if (message.isRead() && message.isSendByCurrentUser()) {
            viewHolder.imageView.setImageResource(R.drawable.ic_done_all_black_24dp);
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        } else if (message.isSent() && message.isSendByCurrentUser()) {
            viewHolder.imageView.setImageResource(R.drawable.ic_done_black_24dp);
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
        } else if (message.isSendByCurrentUser()) {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private int fetchColor(int color) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data,
                new int[]{color});
        int returnColor = a.getColor(0, 0);
        a.recycle();
        return returnColor;
    }

    private int findPos(Message newMsg) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getuID().equals(newMsg.getuID())) {
                return i;
            }
        }

        return -1;
    }

    private void notifyDataChanged(List<Message> oldMessages, List<Message> newMessages) {
        sortDataSet();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ChatDiffCallback(oldMessages, newMessages));
        diffResult.dispatchUpdatesTo(this);
    }

    void sortDataSet() {
        messages.sort((o1, o2) -> Long.compare(o2.getTimestamp(), o1.getTimestamp()));
    }

    public boolean add(Message message) {
        int position = findPos(message);
        List<Message> old = new ArrayList<>(messages);
        if (position == -1) {
            messages.add(0, message);
            notifyDataChanged(old, messages);
            return true;
        } else {
            messages.set(position, message);
            notifyDataChanged(old, messages);
        }
        return false;
    }

    public List<Message> getDataSet() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        if (this.messages.size() == 0) {
            this.messages = messages;
            notifyDataChanged(new ArrayList<>(), messages);
            // notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtMessage)
        public TextView txtMessage;

        @BindView(R.id.txtDate)
        public TextView txtDate;

        @BindView(R.id.messageConteiner)
        public View messageConteiner;

        @BindView(R.id.linearLayout)
        public LinearLayout linearLayout;

        @BindView(R.id.progressBar)
        public ProgressBar progressBar;

        @BindView(R.id.imageView)
        public ImageView imageView;

        @BindView(R.id.imageContent)
        public ImageView imageContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
