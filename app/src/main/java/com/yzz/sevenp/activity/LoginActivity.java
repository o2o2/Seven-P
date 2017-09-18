package com.yzz.sevenp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yzz.sevenp.R;
import com.yzz.sevenp.activity.base.BaseActivity;
import com.yzz.sevenp.api.LoginApi;
import com.yzz.sevenp.bean.Login;
import com.yzz.sevenp.network.RetrofitManager;
import com.yzz.sevenp.utils.PreferenceUtils;
import com.yzz.sevenp.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hyphenate.EMError.USER_REG_FAILED;
import static com.hyphenate.EMError.USER_ALREADY_EXIST;
import static com.hyphenate.EMError.USER_ALREADY_LOGIN;
import static com.hyphenate.EMError.USER_AUTHENTICATION_FAILED;
import static com.yzz.sevenp.utils.ToastUtils.showToastNull;

public class LoginActivity extends BaseActivity {


    SharedPreferences sp;
    SharedPreferences.Editor edit;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.loginShow)
    Button loginShow;
    @BindView(R.id.registerShow)
    TextView registerShow;
    @BindView(R.id.log_tv_phone)
    TextView logTvPhone;
    @BindView(R.id.reg_phone_input)
    TextView regPhoneInput;
    @BindView(R.id.log_tv_pwd)
    TextView logTvPwd;
    @BindView(R.id.log_pwd_input)
    TextView logPwdInput;
    private String phone = "";
    private String password = "";
    private boolean flg;
    String uid;

    @Override
    protected void initTitleBar(HeaderBuilder builder) {
        builder.goneToolbar();
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_login;
    }


    private void initData() {
        //注册环信账号
        flg = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("user_id", MODE_PRIVATE);
                uid = sharedPreferences.getString("userId", "0");
                //注册失败会抛出HyphenateException
                try {
                    EMClient.getInstance().createAccount(uid, "111111");//同步方法
                    Log.e("TAG", "环信账号注册成功");
                    flg = true;
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Log.e("TAG", "" + e.getErrorCode());
                    if (e.getErrorCode() == USER_REG_FAILED) {//208:注册失败
                        Log.e("TAG", "环信账号注册失败");
                        flg = false;
                    }
                    if (e.getErrorCode() == USER_ALREADY_EXIST) {//203:用户已经存在
                        Log.e("TAG", "环信账号用户已经存在");
                        flg = true;
                    }
                }

                //登录环信账号
                if (flg) {
                    EMClient.getInstance().login(uid, "111111", new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            //两个方法是为了保证进入主页面后本地会话和群组都 load 完毕
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.e("TAG", "登录聊天服务器成功！");
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            Log.e("main", "登录聊天服务器失败！");
                            if (code == USER_ALREADY_LOGIN) {//200
                                Log.e("TAG", "用户已登录");
                            }
                            if (code == USER_AUTHENTICATION_FAILED) {//202
                                Log.e("TAG", "用户id或密码错误");
                            }
                        }
                    });
                } else {
                    Log.e("TAG", "环信账号没有注册");
                }
            }
        }).start();
    }

    //sign in的业务逻辑处理
    private void signin() {

        sp = getSharedPreferences("user_id", MODE_PRIVATE);
        edit = sp.edit();
        // 1 获取输入的用户名和密码
        final String signinPhone = loginPhone.getText().toString().trim();
        final String signinPwd = loginPassword.getText().toString().trim();
        // 2 校验输入的用户名和密码不为空


        showToastNull(LoginActivity.this, signinPhone, "输入的电话号码不能为空！");

        logTvPwd.setVisibility(View.VISIBLE);

        showToastNull(LoginActivity.this, signinPwd, "输入的密码不能为空！");


        // 3 登录逻辑处理
        Toast.makeText(LoginActivity.this, "开始登录！", Toast.LENGTH_SHORT).show();
        Log.e("TAG", "号码：" + signinPhone + "\t" + "密码：" + signinPwd);
        LoginApi signInApi = RetrofitManager.getTestRetrofit().create(LoginApi.class);
        FormBody formBody = new FormBody.Builder()//用户注册参数
                .add("phone", signinPhone)//手机号
                .add("password", signinPwd)// 密码
                .build();
        Call<Login> signInBeanCall = signInApi.getLive(formBody);
        signInBeanCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.body().getError_code() != 20019) {
                    if (response.body().getResult().getId() != 0) {

                        Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();

                        PreferenceUtils.putJsessionId(String.valueOf(response.body().getResult().getId()));
                        edit.putString("id", String.valueOf(response.body().getResult().getId()));
                        Log.d("aaa", String.valueOf(response.body().getResult().getId()));
                        edit.putString("user_name", response.body().getResult().getUser_data().getUser_name());
                        edit.putString("avatar", response.body().getResult().getUser_data().getAvatar());
                        edit.putString("sign", response.body().getResult().getUser_data().getSign());
                        edit.putString("phone", response.body().getResult().getUser_data().getPhone());
                        edit.commit();
                        Intent intent = new Intent(LoginActivity.this, LiveHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败！用户名或密码错误。", Toast.LENGTH_SHORT).show();
                    loginPassword.setText("");
                    logTvPwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
//                Toast.makeText(LoginActivity.this,"登录失败！用户名或密码错误。",Toast.LENGTH_SHORT).show();
                ToastUtils.showLong("登录失败！用户名或密码错误。");
                loginPassword.setText("");
                logTvPhone.setVisibility(View.GONE);
            }
        });
    }

    //EditText的监听
    TextWatcher mTextWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            regPhoneInput.setText(temp.length() + "/20");
            if (temp.length() > 0) {
                logTvPhone.setVisibility(View.VISIBLE);
            } else {
                logTvPhone.setVisibility(View.GONE);
            }


        }
    };

    TextWatcher mTextWatcher1 = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            logPwdInput.setText(temp.length() + "/20");
            if (temp.length() > 0) {
                logTvPwd.setVisibility(View.VISIBLE);
            } else {
                logTvPwd.setVisibility(View.GONE);
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        loginPhone.addTextChangedListener(mTextWatcher);
        loginPassword.addTextChangedListener(mTextWatcher1);
    }


    @OnClick({R.id.loginShow, R.id.registerShow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginShow:
                signin();
                break;
            case R.id.registerShow:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}

