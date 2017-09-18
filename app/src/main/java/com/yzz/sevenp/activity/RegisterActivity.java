package com.yzz.sevenp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.api.RegisterApi;
import com.yzz.sevenp.api.UpLoadApi;
import com.yzz.sevenp.bean.Register;
import com.yzz.sevenp.bean.UploadBean;
import com.yzz.sevenp.network.RetrofitManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.reg_avatar)
    CircleImageView regAvatar;
    @BindView(R.id.reg_et_nickname)
    EditText regEtNickname;
    @BindView(R.id.reg_nickname_input)
    TextView regNicknameInput;
    @BindView(R.id.reg_et_phone)
    EditText regEtPhone;
    @BindView(R.id.reg_phone_input)
    TextView regPhoneInput;
    @BindView(R.id.reg_et_pwd)
    EditText regEtPwd;
    @BindView(R.id.reg_pwd_input)
    TextView regPwdInput;
    @BindView(R.id.reg_et_pwd2)
    EditText regEtPwd2;
    @BindView(R.id.reg_pwd2_input)
    TextView regPwd2Input;
    @BindView(R.id.reg_et_sign)
    EditText regEtSign;
    @BindView(R.id.reg_sign_input)
    TextView regSignInput;
    @BindView(R.id.reg_bt_reg)
    Button regBtReg;
    private String thumbnail_pic;//小图地址
    private String bmiddle_pic;//大图地址
    private String original_pic;//原图地址
    String Name;
    String Phone;
    String Pwd;
    String Pwd2;
    String Sign;

    //调用系统相册-选择图片
    private static final int CAMERA_CODE = 1;
    private static final int GALLERY_CODE = 2;
    private static final int CROP_CODE = 3;

    private File img;

    private Uri imageUri;

    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    private void register(){
        Name = regEtNickname.getText().toString().trim();
        Phone = regEtPhone.getText().toString().trim();
        Pwd = regEtPwd.getText().toString().trim();
        Pwd2 = regEtPwd2.getText().toString().trim();
        Sign = regEtSign.getText().toString().trim();
        if (Name.equals(null)){
            Toast.makeText(RegisterActivity.this,"注册失败,用户名不能为空！",Toast.LENGTH_SHORT).show();
        }else if(Phone.equals(null)){
            Toast.makeText(RegisterActivity.this,"注册失败,电话号码不能为空！",Toast.LENGTH_SHORT).show();
        }else if (!isMobile(Phone)){
            Toast.makeText(RegisterActivity.this,"注册失败,电话号码不合法",Toast.LENGTH_SHORT).show();
        }else if (Pwd.equals(Pwd2)) {
            RegisterApi registerApi = RetrofitManager.getTestRetrofit().create(RegisterApi.class);
            FormBody formBody = new FormBody.Builder()
                    .add("user_name", Name)
                    .add("phone", Phone)
                    .add("password", Pwd)
                    .add("sign", Sign)
                    .add("avatar", thumbnail_pic)
                    .build();
            Call<Register> registerCall = registerApi.getLive(formBody);
            registerCall.enqueue(new Callback<Register>() {

                @Override
                public void onResponse(Call<Register> call, Response<Register> response) {
                    boolean result = response.body().isResult();
                    if (result){
                        Toast.makeText(RegisterActivity.this,"注册成功！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"注册失败，手机号已被注册",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Register> call, Throwable t) {

                }
            });
        }else{
            regEtPwd.setText("");
            regEtPwd2.setText("");
            Toast.makeText(this,"您两次密码输入有误，请重新输入。",Toast.LENGTH_SHORT).show();
        }
    }

    public void setAvatar(){
        String[] choice = new String[]{"相机","相册","取消"};
        new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("请选择")
                .setItems(choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                //拍照选择
                                chooseFromCamera();
                                break;
                            case 1:
                                //从相册选取
                                chooseFromGallery();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_register;
    }

    private void upload(File file) {
        MultipartBody.Part filepart = MultipartBody.Part.createFormData(
                "pic", file.getName() + ".jpg",
                RequestBody.create(MediaType.parse("image/*"), file));
        UpLoadApi uploadApi = RetrofitManager.getTestRetrofit().create(UpLoadApi.class);
        Call<UploadBean> upload = uploadApi.upload(filepart);
        upload.enqueue(new Callback<UploadBean>() {

            @Override
            public void onResponse(Call<UploadBean> call, Response<UploadBean> response) {
                thumbnail_pic = response.body().getResult().getThumbnail_pic();
                bmiddle_pic = response.body().getResult().getBmiddle_pic();
                original_pic = response.body().getResult().getOriginal_pic();
            }

            @Override
            public void onFailure(Call<UploadBean> call, Throwable t) {

            }
        });
    }

    private void chooseFromCamera() {
        //构建隐式Intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //调用系统相机
        startActivityForResult(intent, CAMERA_CODE);
        // 来自相册

    }

    /**
     * 从相册选择图片
     */
    private void chooseFromGallery() {

        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         */
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CAMERA_CODE:
                startImageZoom(imageUri);
                break;
            case GALLERY_CODE:
                if (data != null) {
                    startImageZoom(data.getData());
                }
                break;
            case CROP_CODE:
                if (data == null){
                    return;
                }else{
                    Bitmap bitmap = null;
                    try {
                        Log.d("bitmap","bitmap::"+"0000000"+data.getExtras().getParcelable("data"));
                        Bundle bundle = data.getExtras();
                        bitmap = bundle.getParcelable("data");
                        Log.d("bitmap","bitmap::"+bitmap);
                        regAvatar.setImageBitmap(bitmap);
                        saveBitmap(bitmap, "temp");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将Bitmap写入SD卡中的一个文件中,并返回写入文件的Uri
     * @param bm
     * @param dirPath
     * @return
     */
    private void saveBitmap(Bitmap bm, String dirPath) {
        //新建文件夹用于存放裁剪后的图片
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/" + dirPath);
        Log.d("tmpDir","tmpDir:"+tmpDir);

        if (!tmpDir.exists()){
            tmpDir.mkdirs();
        }

        //新建文件存储裁剪后的图片
        img = new File(Environment.getExternalStorageDirectory() + "/avator.jpg");

        try {
            //打开文件输出流
            FileOutputStream fos = new FileOutputStream(img);
            //将bitmap压缩后写入输出流(参数依次为图片格式、图片质量和输出流)
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            //刷新输出流
            fos.flush();
            //关闭输出流
            fos.close();
            //返回File类型的Uri
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 上传头像
         */
        upload(img);
    }

    /**
     * 通过Uri传递图像信息以供裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri){
        //构建隐式Intent来启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据uri和类型为图片类型
        intent.setDataAndType(uri, "image/*");
        //显示View为可裁剪的
        intent.putExtra("crop", true);
        //裁剪的宽高的比例为1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //输出图片的宽高均为150
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后的数据是通过Intent返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_CODE);
    }

    /**
     * 计算字符串中中文字符数
     * @param strName
     * @return
     */
    int countChinese(String strName){
        int count = 0;
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                count ++;
            }
        }
        return count;
    }
    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    //申请权限
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.CAMERA)) {
                /**
                 * 弹dialog
                 */
                Toast.makeText(this, "我需要申请照相机权限，请到设置中设置。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "用户拒绝了", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        requestPermission();
        regAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvatar();
            }
        });
        regBtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

}
