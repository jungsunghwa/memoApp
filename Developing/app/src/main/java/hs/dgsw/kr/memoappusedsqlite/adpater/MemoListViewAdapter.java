package hs.dgsw.kr.memoappusedsqlite.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.model.Memo;
import hs.dgsw.kr.memoappusedsqlite.Activity.MemoActivity;
import hs.dgsw.kr.memoappusedsqlite.R;

public class MemoListViewAdapter extends BaseAdapter {

    private ArrayList<Memo> memoList = new ArrayList<>();

    public MemoListViewAdapter(ArrayList<Memo> memoList) {
        this.memoList = memoList;
    }

    @Override
    public int getCount() {
        return memoList.size();
    }

    @Override
    public Object getItem(int i) {
        return memoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return memoList.get(i).getIdx();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Memo memo = memoList.get(i);
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.memo_list_view_item, viewGroup, false);
        }

        TextView titleTextView = view.findViewById(R.id.title_tv);
        TextView dateTextView = view.findViewById(R.id.date_tv);

        if (memo.getContent().isEmpty()){
            titleTextView.setText("사진");
        }else{
            titleTextView.setText(memo.getContent());
        }
        dateTextView.setText(memo.getDate());

        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, MemoActivity.class);
            intent.putExtra("memoIdx", memo.getIdx());

            context.startActivity(intent);
        });

        return view;
    }

    public void swapItems(ArrayList<Memo> memoList) {
        this.memoList.clear();
        this.memoList.addAll(memoList);
        notifyDataSetChanged();
    }
}
