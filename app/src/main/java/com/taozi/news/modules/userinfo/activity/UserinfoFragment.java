package com.taozi.news.modules.userinfo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taozi.news.R;
import com.taozi.news.activity.BaseFragment;
import com.taozi.news.activity.MainActivity;
import com.taozi.news.activity.MainFragment;
import com.taozi.news.bean.UserSettingManager;
import com.taozi.news.modules.setting.activity.AboutSettingActivity;
import com.taozi.news.modules.setting.activity.AppSettingActivity;
import com.taozi.news.modules.setting.activity.BindSettingActivity;
import com.taozi.news.modules.setting.activity.CollectSettingActivity;
import com.taozi.news.modules.setting.activity.MessageSettingActivity;
import com.taozi.news.modules.setting.activity.RecommendSettingActivity;
import com.taozi.news.modules.setting.activity.UpdateSettingActivity;
import com.taozi.news.modules.userlogin.activity.LoginActivity;
import com.taozi.news.util.StatueUtil;
import com.taozi.news.util.ThreadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Tao Yimin on 2016/10/8.
 */
public class UserinfoFragment extends BaseFragment {
    private ViewGroup mLayout;
    private View mStatue;
    private ImageView mImageView;
    public boolean haveStatue;
    private Button mMessageButton, mCollectButton;
    private ViewGroup mLayoutLogin, mLayoutBind, mLayoutCache, mLayoutFont, mLayoutImage, mLayoutRecommend, mLayoutUpdate, mLayoutApp, mLayoutAbout;
    private TextView mCacheTextView, mFontSizeTextView, mImageTextView;
    private double cacheSize;
    private int fontSize;
    private int imageMode;
    private String[] fontArray = new String[]{"小字体   ", "中字体   ", "大字体   ", "超大字体   "};
    private String[] imageArray = new String[]{"显示图片   ", "智能显示图片   ", "不显示图片   ", "仅WI-FI下显示图片   "};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final static int TAKE_PHOTO = 1;
    private final static int CROP_PHOTO = 2;
    private final static int SELECT_PHOTO = 3;
    private Uri imageUri;

    @Override
    protected int setContentViewId() {
        return R.layout.fragment_userinfo;
    }

    @Override
    protected void findViews(View view) {
        mImageView = (ImageView) view.findViewById(R.id.userinfo_head);
        mStatue = view.findViewById(R.id.userinfo_statue);
        mLayout = (ViewGroup) view.findViewById(R.id.userinfo_layout);
        mMessageButton = (Button) view.findViewById(R.id.userinfo_message);
        mCollectButton = (Button) view.findViewById(R.id.userinfo_collect);
        mCacheTextView = (TextView) view.findViewById(R.id.userinfo_tv_cache);
        mFontSizeTextView = (TextView) view.findViewById(R.id.userinfo_tv_fontsize);
        mImageTextView = (TextView) view.findViewById(R.id.userinfo_tv_image);
        mLayoutLogin = (ViewGroup) view.findViewById(R.id.userinfo_btn_login);
        mLayoutBind = (ViewGroup) view.findViewById(R.id.userinfo_btn_bind);
        mLayoutCache = (ViewGroup) view.findViewById(R.id.userinfo_btn_cache);
        mLayoutFont = (ViewGroup) view.findViewById(R.id.userinfo_btn_font);
        mLayoutImage = (ViewGroup) view.findViewById(R.id.userinfo_btn_image);
        mLayoutRecommend = (ViewGroup) view.findViewById(R.id.userinfo_btn_recommend);
        mLayoutUpdate = (ViewGroup) view.findViewById(R.id.userinfo_btn_update);
        mLayoutApp = (ViewGroup) view.findViewById(R.id.userinfo_btn_app);
        mLayoutAbout = (ViewGroup) view.findViewById(R.id.userinfo_btn_about);
    }

    @Override
    protected void initEvent() {
        //设置头像的点击事件
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("设置头像");
                final String[] str = new String[]{"从相册中选择", "拍照"};
                dialog.setItems(str, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                setHeadByAlbum();
                                break;
                            case 1:
                                if(Build.VERSION.SDK_INT >= 23){
                                    applyPermission();
                                }else{
                                    setHeadByTakePhoto();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
        //设置登陆按钮的点击事件
        mLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        //设置消息按钮的点击事件
        mMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置收藏按钮的点击事件
        mCollectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CollectSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置绑定社交账号按钮的点击事件
        mLayoutBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BindSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置清理缓存按钮的点击事件
        mLayoutCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage("您确定要清理吗?")
                        .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DiskCache diskCache = ImageLoader.getInstance().getDiskCache();
                                diskCache.clear();
                                mCacheTextView.setText("0.0M");
                                Snackbar.make(mLayout, "缓存清理成功", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        //设置字体大小按钮的点击事件
        mLayoutFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setSingleChoiceItems(fontArray, fontSize, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fontSize = which;
                        SharedPreferences sp = getActivity().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("fontSize", fontSize);
                        editor.commit();
                        UserSettingManager.getIntance().setFontSize(fontSize);
                        mFontSizeTextView.setText(fontArray[which].replace("字体", ""));
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //设置图片显示模式按钮的点击事件
        mLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setSingleChoiceItems(imageArray, imageMode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageMode = which;
                        SharedPreferences sp = getActivity().getSharedPreferences("userSetting", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("imageMode", imageMode);
                        editor.commit();
                        UserSettingManager.getIntance().setImageMode(imageMode);
                        mImageTextView.setText(imageArray[which]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //设置推荐给好友按钮的点击事件
        mLayoutRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置检查更新按钮的点击事件
        mLayoutUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置应用推荐按钮的点击事件
        mLayoutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppSettingActivity.class);
                startActivity(intent);
            }
        });
        //设置关于按钮的点击事件
        mLayoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {
        //初始化状态栏
        haveStatue = StatueUtil.initStatue(getActivity(), mLayout, mStatue);
        //从本地存储读取用户设置
        fontSize = UserSettingManager.getIntance().getFontSize();
        imageMode = UserSettingManager.getIntance().getImageMode();
        mFontSizeTextView.setText(fontArray[fontSize].replace("字体", ""));
        mImageTextView.setText(imageArray[imageMode]);
        //更新缓存信息
        upDataCacheSize();

    }

    @Override
    protected void loadData() {

    }

    /**
     * 得到目录总大小
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                //如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0;
        }
    }

    /**
     * 更新缓存大小
     */
    public void upDataCacheSize() {
        //递归遍历文件是耗时操作
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DiskCache diskCache = ImageLoader.getInstance().getDiskCache();
                cacheSize = getDirSize(diskCache.getDirectory());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //保留两位小数
                        DecimalFormat df = new DecimalFormat("######0.00");
                        mCacheTextView.setText(df.format(cacheSize) + "M");
                    }
                });
            }
        }, ThreadTask.ThreadPeriod.PERIOD_HIGHT);
    }

    /**
     * 从相册选取图片设置头像
     */
    private void setHeadByAlbum(){
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, SELECT_PHOTO);
    }

    /**
     * 拍照设置头像
     */
    private void setHeadByTakePhoto() {
        File outputImages = new File(Environment.getExternalStorageDirectory() + "/News", "output_image.jpg");
        if (outputImages.exists()) {
            outputImages.delete();
        }
        try {
            outputImages.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImages);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //启动相机程序
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 申请相机权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void applyPermission() {
        int hasWriteContactsPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        setHeadByTakePhoto();
    }

    /**
     * 申请结果的回掉
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    setHeadByTakePhoto();
                } else {
                    // Permission Denied
                    Snackbar.make(mLayout, "拒绝授权可能会影响您的体验", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode==getActivity().RESULT_OK){
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    //启动裁剪程序
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if(resultCode==getActivity().RESULT_OK){
                    Bitmap bitmap=null;
                    try {
                        if(imageUri==null){
                            imageUri=data.getData();
                        }
                        bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        saveBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(bitmap==null){
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            bitmap = extras.getParcelable("data");
                            saveBitmap(bitmap);
                        }
                    }
                    MainFragment mainFragment= (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag("mainFragment");
                    mainFragment.initHead(true);
                    ((MainActivity)getActivity()).initHead(true);
                }
                break;
            case SELECT_PHOTO:
                if(resultCode==getActivity().RESULT_OK){
                    Intent intent=new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(data.getData(),"image/*");
                    intent.putExtra("crop",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,data.getData());
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将bitmap储存到SD卡中
     * @param bmp
     */
    public void saveBitmap(Bitmap bmp) {
        File f = new File(Environment.getExternalStorageDirectory() + "/News", "head.png");
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
