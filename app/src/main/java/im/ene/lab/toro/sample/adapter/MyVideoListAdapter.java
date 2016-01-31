/*
 * Copyright 2016 eneim@Eneim Labs, nam@ene.im
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.ene.lab.toro.sample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import im.ene.lab.toro.BaseAdapter;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.sample.data.SimpleVideoObject;
import im.ene.lab.toro.sample.data.VideoSource;
import im.ene.lab.toro.sample.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eneim on 1/30/16.
 */
public class MyVideoListAdapter extends ToroAdapter<BaseAdapter.ViewHolder> {

  private Cursor mVideosCursor;

  private List<SimpleVideoObject> mVideos = new ArrayList<>();

  public MyVideoListAdapter(Context context) {
    super();
    Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {
        MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE
    };
    for (String item : VideoSource.SOURCES) {
      mVideos.add(new SimpleVideoObject(item));
    }

    this.mVideosCursor = context.getContentResolver().query(uri, projection, null, null, null);
//    if (mVideosCursor != null) {
//      while (mVideosCursor.moveToNext()) {
//        mVideos.add(new SimpleVideoObject(mVideosCursor.getString(
//            mVideosCursor.getColumnIndex(MediaStore.Video.Media.DATA))));
//      }
//    }

    setHasStableIds(true);
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    mVideosCursor.close();
    mVideosCursor = null;
    super.onDetachedFromRecyclerView(recyclerView);
  }

  @Override
  public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, @BaseViewHolder.Type int
      viewType) {
    return BaseViewHolder.createViewHolder(parent, viewType);
  }

  @BaseViewHolder.Type @Override public int getItemViewType(int position) {
    Object item = getItem(position);
    return item instanceof SimpleVideoObject ? BaseViewHolder.VIEW_TYPE_VIDEO
        : BaseViewHolder.VIEW_TYPE_NO_VIDEO;
  }

  @Override public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  private Object getItem(int position) {
    return mVideos.get(position % mVideos.size());
  }

  @Override public void onBindViewHolder(BaseAdapter.ViewHolder holder, int position) {
    holder.bind(getItem(position));
  }

  @Override public int getItemCount() {
    return 100;
  }
}