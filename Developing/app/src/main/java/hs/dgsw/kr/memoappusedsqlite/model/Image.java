package hs.dgsw.kr.memoappusedsqlite.model;

import android.net.Uri;

public class Image {
  int idx;
  Uri uri;

  public Image(int idx, Uri uri) {
    this.idx = idx;
    this.uri = uri;
  }

  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public Uri getUri() {
    return uri;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }
}
