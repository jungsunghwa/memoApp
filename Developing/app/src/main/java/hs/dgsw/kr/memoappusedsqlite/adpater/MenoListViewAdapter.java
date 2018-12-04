package hs.dgsw.kr.memoappusedsqlite.adpater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.Memo;

public class MenoListViewAdapter extends BaseAdapter {

    private ArrayList<Memo> memoList = new ArrayList<>();

    public MenoListViewAdapter(ArrayList<Memo> memoList) {
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

        return view;
    }
}
