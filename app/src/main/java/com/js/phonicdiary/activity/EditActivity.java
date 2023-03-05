package com.js.phonicdiary.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bilibili.magicasakura.widgets.TintEditText;
import com.bilibili.magicasakura.widgets.TintImageView;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.bilibili.magicasakura.widgets.TintToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.js.phonicdiary.DiaryApplication;
import com.js.phonicdiary.R;
import com.js.phonicdiary.activity.base.BaseActivity;
import com.js.phonicdiary.adapter.details.BannerAdapter;
import com.js.phonicdiary.bean.DiaryInfo;
import com.js.phonicdiary.bean.PhotoInfo;
import com.js.phonicdiary.callback.VoiceCallBack;
import com.js.phonicdiary.dialog.VoiceDialog;
import com.js.phonicdiary.manager.BackupManager;
import com.js.phonicdiary.manager.DiaryDbManager;
import com.js.phonicdiary.manager.PhotoDbManager;
import com.js.phonicdiary.manager.VoiceDbManager;
import com.js.phonicdiary.utils.AndroidBugWorkaround;
import com.js.phonicdiary.utils.AppPreferences;
import com.js.phonicdiary.utils.DateUtil;
import com.js.phonicdiary.utils.GlideEngine;
import com.js.phonicdiary.utils.LogE;
import com.js.phonicdiary.utils.PerformEdit;
import com.js.phonicdiary.utils.PermissionUtil;
import com.js.phonicdiary.utils.StatusBarUtil;
import com.js.phonicdiary.utils.Utils;
import com.js.phonicdiary.view.IndentTextWatcher;
import com.js.phonicdiary.view.listener.Softkeyboardlistener;
import com.yanzhenjie.permission.Action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日记编辑页面
 */
public class EditActivity extends BaseActivity {

    private RecyclerView mEditPhotoRlv;
    private TintToolbar mEditToolbar;
    private NestedScrollView mEditScroll;
    private TintEditText mEditTitle;
    private TintEditText mEditMessage;
    private FloatingActionButton mEditVoice;
    private TextView mEditPhotoCount;
    private FrameLayout mEditPhotoFl;
    private LinearLayout mEditActionLl;
    private ImageView mEditUndo;
    private ImageView mEditRedo;
    private TintTextView mEditNumber;
    private ImageView mEditSave;

    private TextView mEditVoiceCount;

    private TextView mEditDate;
    private TintEditText mEditWeather;

    private TintImageView mEditBack;
    private TintImageView mEditPhoto;
    private TintImageView mEditDone;
    private TintImageView mEditEdit;

    // 撤销与返回
    private PerformEdit mPerformEdit;
    private BannerAdapter bannerAdapter;
    // 当前显示的item
    private int visibleItem = 1;
    // 录音弹窗
    private AlertDialog alertDialog;

    private DiaryInfo diaryInfo;
    // 时间
    private long time;
    /*
     *该日记第一次创建时间
     */
    private long createTime;
    /**
     * 键盘是否弹出
     */
    private boolean isShowKeyboard;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit);

        mEditPhotoRlv = (RecyclerView) findViewById(R.id.edit_photo_rlv);
        mEditScroll = (NestedScrollView) findViewById(R.id.edit_scroll);
        mEditTitle = (TintEditText) findViewById(R.id.edit_title);
        mEditMessage = (TintEditText) findViewById(R.id.edit_message);
        mEditVoice = (FloatingActionButton) findViewById(R.id.edit_voice);
        mEditPhotoFl = (FrameLayout) findViewById(R.id.edit_photo_fl);

        mEditToolbar = (TintToolbar) findViewById(R.id.edit_toolbar);


        mEditPhotoCount = (TextView) findViewById(R.id.edit_photo_count);

        mEditDate = (TextView) findViewById(R.id.edit_date);
        mEditWeather = (TintEditText) findViewById(R.id.edit_weather);


        mEditActionLl = (LinearLayout) findViewById(R.id.edit_action_ll);
        mEditUndo = (ImageView) findViewById(R.id.edit_undo);
        mEditRedo = (ImageView) findViewById(R.id.edit_redo);
        mEditNumber = (TintTextView) findViewById(R.id.edit_number);
        mEditSave = (ImageView) findViewById(R.id.edit_save);

        mEditVoiceCount = (TextView) findViewById(R.id.edit_voice_count);

        // Toolbar 控件
        mEditBack = (TintImageView) findViewById(R.id.edit_back);
        mEditPhoto = (TintImageView) findViewById(R.id.edit_photo);
        mEditDone = (TintImageView) findViewById(R.id.edit_done);
        mEditEdit = (TintImageView) findViewById(R.id.edit_edit);


        // 设置imageview 头部沉浸
        StatusBarUtil.setTranslucentForImageView(this, 0, mEditToolbar);
        StatusBarUtil.setImmersiveStatusBar(this, true);
        AndroidBugWorkaround.assistActivity(findViewById(R.id.edit_content));

        //创建PerformEdit，一定要传入不为空的EditText
        mPerformEdit = new PerformEdit(mEditMessage);

        mEditMessage.addTextChangedListener(new IndentTextWatcher());
        mEditMessage.setText("\t\t\t\t");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mEditPhotoRlv.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mEditPhotoRlv);
        bannerAdapter = new BannerAdapter(this, mEditPhotoRlv);
        mEditPhotoRlv.setAdapter(bannerAdapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mEditBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 监听标题键盘点击下一项
        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    mEditMessage.requestFocus();
                    mEditMessage.setSelection(mEditMessage.getText().length());
                    return true;
                }
                return false;
            }
        });


        mEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 选择添加图片
                EasyPhotos.createAlbum(EditActivity.this, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.js.phonicdiary.fileprovider")
                        .setCount(9)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                                if (photos != null && photos.size() > 0) {
                                    ArrayList<PhotoInfo> list = new ArrayList<>();
                                    PhotoInfo photoInfo = null;
                                    for (Photo photo : photos) {
                                        photoInfo = new PhotoInfo(System.currentTimeMillis() + photo.time, photo.name
                                                , photo.path, photo.type, photo.width, photo.height, photo.size, photo.duration, photo.time, createTime);
                                        list.add(photoInfo);
                                    }
                                    bannerAdapter.addDataList(list, false);
                                    bannerAdapter.notifyDataSetChanged();
                                    mEditPhotoCount.setText(visibleItem + "/" + bannerAdapter.getListSize());
                                    PhotoDbManager.insert(list);
                                    saveData();
                                }
                            }
                        });
            }
        });

        mEditEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 编辑
                setEdit(true);
                // 标题请求焦点
                if (AppPreferences.isShowDiaryTitle()) {
                    mEditTitle.setVisibility(View.VISIBLE);
                    // 标题请求焦点
                    mEditTitle.requestFocus();
                } else {
                    mEditTitle.setVisibility(View.GONE);
                    mEditMessage.requestFocus();
                    mEditMessage.setSelection(mEditMessage.getText().length());
                }
            }
        });

        mEditDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 保存
                saveData();
                finish();
            }
        });

        mEditVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看录音
                Intent intent = new Intent(EditActivity.this, VoiceActivity.class);
                intent.putExtra("createTime", createTime);
                startActivity(intent);
                mEditVoiceCount.setText("0");
                mEditVoiceCount.setVisibility(View.GONE);
            }
        });

        mEditVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 长按开始录音，松开结束录音
                if (PermissionUtil.checkSelfPermissionMicrophoneAndStorage(EditActivity.this)) {
                    alertDialog = VoiceDialog.showVoiceDialog(EditActivity.this, createTime, isShowKeyboard, new VoiceCallBack() {
                        @Override
                        public void recFinish(long length, String strLength, String path) {
                            // 录音成功
                            int count = Utils.intValueOf(mEditVoiceCount.getText().toString());
                            mEditVoiceCount.setText((count + 1) + "");
                            mEditVoiceCount.setVisibility(View.VISIBLE);
                            saveData();
                        }
                    });
                } else {
                    PermissionUtil.requestMicrophoneAndStoragePermission(EditActivity.this, new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
//                            alertDialog = VoiceDialog.showVoiceDialog(EditActivity.this);
                        }
                    });
                }
                return true;
            }
        });

        mEditVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    // 按下 处理相关逻辑
                } else if (action == MotionEvent.ACTION_UP) {
                    // 录音停止，如果在录音的话
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
                return false;
            }
        });

        mEditScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY,
                                       int oldScrollX, int oldScrollY) {
                if (scrollY - oldScrollY > 0) {
                    //上滑
                    mEditVoice.hide();
                } else if (scrollY - oldScrollY < 0) {
                    // 下滑
                    mEditVoice.show();
                }
            }
        });

        // 监听banner横向滚动
        mEditPhotoRlv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    if (manager != null) {
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        if (lastVisibleItem != -1) {
                            visibleItem = lastVisibleItem + 1;
                            mEditPhotoCount.setText(visibleItem + "/" + bannerAdapter.getListSize());
                        }
                    }
                }
            }
        });

        mEditUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPerformEdit != null) {
                    mPerformEdit.undo();
                }
            }
        });
        mEditRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPerformEdit != null) {
                    mPerformEdit.redo();
                }
            }
        });
        mEditSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 保存
                saveData();
                finish();
            }
        });

        // 字数监听
        mEditMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = mEditMessage.getText().toString();
                if (text.contains(" ") || text.contains("\t") || text.contains("\n")) {
                    text = text.replace(" ", "").replace("\t", "").replace("\n", "");
                }
                if (text != null) {
                    int content = text.length();
                    mEditNumber.setText("字数: " + content);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 监听软键盘
        Softkeyboardlistener.setListener(this, new Softkeyboardlistener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                // 显示
//                mEditVoice.setTranslationY(-height);
//                mEditActionLl.setTranslationY(-height);
                isShowKeyboard = true;
                mEditActionLl.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                // 隐藏
//                mEditVoice.setTranslationY(0);
//                mEditActionLl.setTranslationY(0);
                isShowKeyboard = false;
                mEditActionLl.setVisibility(View.GONE);
            }
        });

        mEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 修改时间
                Calendar ca = Calendar.getInstance();
                int mYear = ca.get(Calendar.YEAR);
                int mMonth = ca.get(Calendar.MONTH);
                int mDay = ca.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        time = DateUtil.getLongData(year, month, dayOfMonth);
                        mEditDate.setText(DateUtil.getDataTime(time));
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (diaryInfo != null) {
            // 查看详情
            if (diaryInfo.getTime() != 0) {
                mEditDate.setText(DateUtil.getDataTime(diaryInfo.getTime()));
            }
            if (diaryInfo.getWeather() != null) {
                mEditWeather.setText(diaryInfo.getWeather());
            }
            mEditTitle.setText(diaryInfo.getTitle());
            mEditMessage.setText(diaryInfo.getText());
//            mEditWeather.setText(diaryInfo.getWeather());
            bannerAdapter.addDataList((ArrayList<PhotoInfo>) diaryInfo.getPhotoInfos(), true);
            bannerAdapter.notifyDataSetChanged();
            setEdit(false);
            if (AppPreferences.isShowDiaryTitle()) {
                mEditTitle.setVisibility(View.VISIBLE);
            } else {
                mEditTitle.setVisibility(View.GONE);
            }
        } else {
            // 内容编辑
            // 初始化时间
            if (createTime != 0) {
                time = createTime;
            } else {
                time = System.currentTimeMillis();
            }
            mEditDate.setText(DateUtil.getDataTime(time));
            mEditWeather.setText("天气: 空");
            mEditEdit.setVisibility(View.GONE);
            mEditDone.setVisibility(View.VISIBLE);
            if (AppPreferences.isShowDiaryTitle()) {
                mEditTitle.setVisibility(View.VISIBLE);
                // 标题请求焦点
                mEditTitle.requestFocus();
            } else {
                mEditTitle.setVisibility(View.GONE);
                mEditMessage.requestFocus();
                mEditMessage.setSelection(mEditMessage.getText().length());
            }
        }

        // 判断是否展示天气
        if (AppPreferences.isShowDiaryWeather()) {
            mEditWeather.setVisibility(View.VISIBLE);
        } else {
            mEditWeather.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initIntent() {
        super.initIntent();
        Intent intent = getIntent();
        if (intent != null) {
            diaryInfo = (DiaryInfo) intent.getSerializableExtra("DiaryInfo");
        }
        if (diaryInfo == null) {
            createTime = System.currentTimeMillis();
        } else {
            createTime = diaryInfo.getCreateTime();
        }
    }

    /**
     * 保存数据
     */
    private void saveData() {
        String title = mEditTitle.getText().toString();
        String text = mEditMessage.getText().toString();
        String weather = mEditWeather.getText().toString();
        if (diaryInfo == null) {
            diaryInfo = new DiaryInfo();
            diaryInfo.setCreateTime(createTime);
            diaryInfo.setId(createTime);
        }
        diaryInfo.setTitle(title);
        diaryInfo.setText(text);
        diaryInfo.setWeather(weather);
        if (time != 0) {
            diaryInfo.setTime(time);
        }
        diaryInfo.setData(DateUtil.getDataForDay(diaryInfo.getTime()));
        diaryInfo.setImageSize(bannerAdapter.getListSize());
        // 插入或者修改数据
        DiaryDbManager.insertOrReplace(diaryInfo);
//        DiaryApplication.getInstance().getDaoSession().insertOrReplace(diaryInfo);
//        FileManager.writeTextFile(text,time);
        if (AppPreferences.isOpenAutoBackup()) {
            // 自动备份
            BackupManager.autoBackupData();
        }
    }

    /**
     * 设置是否可以编辑
     *
     * @param isEdit
     */
    private void setEdit(boolean isEdit) {
        mEditWeather.setFocusableInTouchMode(isEdit);
        mEditTitle.setFocusableInTouchMode(isEdit);
        mEditMessage.setFocusableInTouchMode(isEdit);
        mEditDate.setClickable(isEdit);

        if (LogE.isLog) {
            LogE.e("wbb", "wbb");
        }
        if (isEdit) {
            mEditDone.setVisibility(View.VISIBLE);
            mEditEdit.setVisibility(View.GONE);
        } else {
            mEditEdit.setVisibility(View.VISIBLE);
            mEditDone.setVisibility(View.GONE);
        }
    }

}
