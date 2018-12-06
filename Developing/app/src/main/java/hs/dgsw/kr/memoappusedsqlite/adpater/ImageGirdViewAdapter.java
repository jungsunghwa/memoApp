package hs.dgsw.kr.memoappusedsqlite.adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import hs.dgsw.kr.memoappusedsqlite.Activity.MemoActivity;
import hs.dgsw.kr.memoappusedsqlite.R;
import hs.dgsw.kr.memoappusedsqlite.database.DatabaseHelper;
import hs.dgsw.kr.memoappusedsqlite.model.Image;

public class ImageGirdViewAdapter extends BaseAdapter {

  private ArrayList<Image> imageList = new ArrayList<>();
  private DatabaseHelper databaseHelper;

  public ImageGirdViewAdapter(ArrayList<Image> imageList) {
    this.imageList = imageList;
  }

  @Override
  public int getCount() {
    return imageList.size();
  }

  @Override
  public Object getItem(int i) {
    return imageList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return imageList.get(i).hashCode();
  }

  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    final Context context = viewGroup.getContext();

    databaseHelper = new DatabaseHelper(context);

    if (view == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.image_grid_view_item, viewGroup, false);
    }

    ImageView imageView = view.findViewById(R.id.image);

    Glide.with(view).load(imageList.get(i).getUri()).into(imageView);

    view.setOnLongClickListener( v -> {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);

      builder
              .setMessage("사진을 삭제하시겠습니까?")
              .setPositiveButton("Yes", (dialog, which) -> {
                databaseHelper.deleteImageByIdx(imageList.get(i).getIdx());
                imageList.remove(i);
                notifyDataSetChanged();
              })
              .setNegativeButton("No", (dialog, which) -> {})
              .show();

        return false;
    });
    return view;
  }
}
