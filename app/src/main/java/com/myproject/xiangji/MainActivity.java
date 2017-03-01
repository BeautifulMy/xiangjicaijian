package com.myproject.xiangji;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERE_REQUEST_CODE = 100;//请求相机码
    private static final int ALBUM_REQUEST_CODE = 101;//打开相册
    private static final int PHOTO_REQUEST_CUT = 102;
    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/myproject/camera/";
    private ImageView imageview;

    File tempFile= new File(Environment.getExternalStorageDirectory(), getPhotoFileName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        imageview = (ImageView) findViewById(R.id.imageview);
        imageview.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (imageview.getDrawable() == null) {
            final Dialog dialog = new Dialog(MainActivity.this, R.style.my_dialog);


            LinearLayout buttons = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog, null);

            dialog.setContentView(buttons);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogstyle);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = -20; // 新位置Y坐标
            lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
            buttons.measure(0, 0);
            lp.height = buttons.getMeasuredHeight();
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
            if (dialog.isShowing()) {
                dialog.hide();
            }
            dialog.show();
            Button paizhao = (Button) buttons.findViewById(R.id.paizhao);
            //打开相机拍照
            paizhao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.dismiss();
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String camerePath = SAVED_IMAGE_DIR_PATH + System.currentTimeMillis() + ".png";
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        File dir = new File(SAVED_IMAGE_DIR_PATH);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        Uri uri = Uri.fromFile(tempFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        startActivityForResult(intent, CAMERE_REQUEST_CODE);
                    } else {
                        Toast.makeText(MainActivity.this, "sd卡不存在,", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //打开相册
            Button xuanze = (Button) buttons.findViewById(R.id.xuanze);
            xuanze.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    dialog.dismiss();
//                    Intent intent = new Intent(Intent.ACTION_PICK, null);
//                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(intent, ALBUM_REQUEST_CODE);

                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                    //intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, ALBUM_REQUEST_CODE);
                }
            });
            Button quxiao = (Button) buttons.findViewById(R.id.quxiao);
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            /*Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(Uri.EMPTY, "image*//*");
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", "true");

            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);

            // outputX,outputY 是剪裁图片的宽高
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            intent.putExtra("noFaceDetection", true);
            System.out.println("22================");
            startActivityForResult(intent, PHOTO_REQUEST_CUT);*/
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == CAMERE_REQUEST_CODE) {
//                if (data != null) {
//                    Uri uri1 = data.getData();
//                    Bitmap bitmap = BitmapFactory.decodeFile(uri1.getPath());
//                    imageview.setImageBitmap(bitmap);
//                }
//                //startCamera(MainActivity.this, requestCode);
//            }
//            if (requestCode == ALBUM_REQUEST_CODE) {
//
//                Uri selectedImage = data.getData();
//                String[] filePathColumns = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePathColumns[0]);
//                String imagePath = c.getString(columnIndex);
//                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                imageview.setImageBitmap(bitmap);
//                c.close();
//            }
//        }
        switch (requestCode) {
            case CAMERE_REQUEST_CODE:
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;

            case ALBUM_REQUEST_CODE:
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null)
                    setPicToView(data);
                break;
        }
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            imageview.setBackgroundDrawable(drawable);
           // btn.setBackground(drawable);
        }
    }


    private void startCamera(MainActivity mainActivity, int requestCode) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            File outdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outdir.exists()) {
                outdir.mkdirs();
            }
            File outFile = new File(outdir, System.currentTimeMillis() + ".jpg");
            // 把文件地址转换成Uri格式
            Uri uri = Uri.fromFile(outFile);

            // 设置系统相机拍摄照片完成后图片文件的存放地址
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            mainActivity.startActivityForResult(intent, requestCode);
            Bitmap bitmap = BitmapFactory.decodeFile(outFile.getName());
            imageview.setImageBitmap(bitmap);
        }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}
