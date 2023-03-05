package com.js.phonicdiary.adapter.details;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.js.phonicdiary.R;
import com.js.phonicdiary.adapter.baseadapter.BaseRecyleAdapter;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.manager.PhotoDbManager;
import com.wanglu.photoviewerlibrary.OnLongClickListener;
import com.wanglu.photoviewerlibrary.PhotoViewer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 详情页轮播图展示
 *
 * @author 兵兵
 */
public class BannerAdapter extends BaseRecyleAdapter<PhotoInfo> {

    private AppCompatActivity activity;
    private RecyclerView recyclerView;

    public BannerAdapter(Context context, RecyclerView recyclerView) {
        super(context);
        this.activity = (AppCompatActivity) context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerHolder(inflater.inflate(R.layout.item_edit_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        BannerHolder bannerHolder = (BannerHolder) holder;
        if (bannerHolder != null) {
            final PhotoInfo photo = list.get(position);
            Glide.with(activity)
                    .load(photo.getPath())
                    .into(bannerHolder.mItemEditBannerImage);
            bannerHolder.mItemEditBannerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ArrayList<String> photolist = new ArrayList<>();
                    for (PhotoInfo photo : list) {
                        photolist.add(photo.getPath());
                    }
                    PhotoViewer.INSTANCE
                            .setData(photolist)
                            .setCurrentPage(position)
                            .setImgContainer(recyclerView)
                            .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                                @Override
                                public void show(ImageView iv, String url) {
                                    // 设置自己加载图片的框架来加载图片
                                    Glide.with(iv.getContext()).load(url).into(iv);
                                }
                            }).setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public void onLongClick(View view) {
                            // 长按图片。编辑
                        }
                    }).start(activity);

                }
            });

            bannerHolder.mItemEditBannerImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // 长按删除
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.tip)
                            .setMessage(R.string.Whether_to_delete_the_picture)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                       // 取消
                                }
                            }).setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 数据库中删除
                            PhotoDbManager.delete(photo, new Observer<String>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(String s) {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(0,list.size());
                                }
                            });

                        }
                    }).show();
                    return true;
                }
            });
        }
    }

    /**
     * 日记界面
     */
    private class BannerHolder extends RecyclerView.ViewHolder {

        private ImageView mItemEditBannerImage;


        public BannerHolder(View itemView) {
            super(itemView);

            mItemEditBannerImage = (ImageView) itemView.findViewById(R.id.item_edit_banner_image);


        }
    }
}
