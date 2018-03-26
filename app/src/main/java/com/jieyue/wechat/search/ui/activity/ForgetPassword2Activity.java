package com.jieyue.wechat.search.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.AESUtils;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.Md5Util;
import com.jieyue.wechat.search.utils.UtilTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by song on 2018/2/9 0017.
 * 忘记密码的界面2
 * */
public class ForgetPassword2Activity extends BaseActivity implements View.OnFocusChangeListener {
    @BindView(R.id.forgetPassWord_password_input)
    EditText forgetPassWord_password_input;
    @BindView(R.id.forgetPassWord_ConfirmPassWord)
    EditText forgetPassWord_ConfirmPassWord;
    @BindView(R.id.forgetPassWord_button)
    TextView forgetPassWord_button;
    @BindView(R.id.iv_del_password)
    ImageView iv_del_password;
    @BindView(R.id.iv_del_confirmpassword)
    ImageView iv_del_confirmpassword;
    private String userNameS;
    private String codeStr;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_forget_password2);
    }

    @Override
    public void dealLogicBeforeInitView() {

        userNameS = getBundleStr("userNameS");
        codeStr = getBundleStr("codeStr");

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("找回密码");
        forgetPassWord_password_input.addTextChangedListener(watcher);
        forgetPassWord_ConfirmPassWord.addTextChangedListener(watcher);
        forgetPassWord_password_input.setOnFocusChangeListener(this);
        forgetPassWord_ConfirmPassWord.setOnFocusChangeListener(this);
        forgetPassWord_button.setEnabled(false);
    }

    @Override
    public void dealLogicAfterInitView() {

    }

    @OnClick({ R.id.forgetPassWord_button,R.id.iv_del_password,R.id.iv_del_confirmpassword})
    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.forgetPassWord_button:
                getDataFromView();
                break;
            case R.id.iv_del_password:
                forgetPassWord_password_input.setText("");
                iv_del_password.setVisibility(View.GONE);
                break;
            case R.id.iv_del_confirmpassword:
                forgetPassWord_ConfirmPassWord.setText("");
                iv_del_confirmpassword.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }
    private void getDataFromView() {
        String passWordStr = forgetPassWord_password_input.getText().toString();
        String confirmPassWordStr = forgetPassWord_ConfirmPassWord.getText().toString();

        if (userNameS == null || userNameS.trim().length() == 0) {
            toast("请输入手机号码");
            return;
        } else if (userNameS.trim().length() != 11) {
            toast("请输入11位数字手机号码");
            return;
        } else {
            String startElement = userNameS.substring(0, 1);
            if (!startElement.equals("1")) {
                toast("手机号码不正确");
                return;
            }
        }
        if (codeStr == null || codeStr.trim().length() <= 0) {
            toast("请输入验证码");
            return;
        }

        if (passWordStr == null || passWordStr.trim().length() <= 0) {
            toast("请输入新密码");
            return;
        } else {
            boolean isTrue = UtilTools.checkPassWordInput(passWordStr);
            if (!isTrue) {
                toast("新密码必须为6-12位字母+数字密码");
                return;
            }
        }

        if (confirmPassWordStr == null || confirmPassWordStr.trim().length() <= 0) {
            toast("请输入确认密码");
            return;
        } else if (!passWordStr.equals(confirmPassWordStr)) {
            toast("两次密码输入不一致");
            return;
        }

        submit(passWordStr);
    }

    private void submit( String passWord) {

        RequestParams params = new RequestParams(UrlConfig.URL_FORGET_PASSWORD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("phoneNumber", userNameS);
        params.add("password", Md5Util.MD5(passWord));
        startRequest(Task.FORGET_PASSWORD, params, null);

    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        if (data != null) {
            switch (tag){
                case Task.FORGET_PASSWORD: {
                    if (handlerRequestErr(data)) {
                        toast(data.getRspMsg());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                break;
                default:
                    break;

            }
        }
    }

    private boolean flag1 = false;
    private boolean flag2 = false;
    /**
     * 监听所有EditText是否输入内容  然后判断高亮button
     * */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            flag1 = forgetPassWord_password_input.getText().toString().trim().length() > 0;
            flag2 = forgetPassWord_ConfirmPassWord.getText().toString().trim().length() > 0;

            if (flag1 && passWordHasFocus){
                iv_del_password.setVisibility(View.VISIBLE);
            }else {
                iv_del_password.setVisibility(View.GONE);
            }
            if (flag2 && confiremPassWordHasFocus){
                iv_del_confirmpassword.setVisibility(View.VISIBLE);
            }else {
                iv_del_confirmpassword.setVisibility(View.GONE);
            }

            if (flag1 && flag2 ) {
                forgetPassWord_button.setEnabled(true);
                forgetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_login_button));
                ll_btn.setBackground(getResources().getDrawable(R.drawable.bg_loan_pic_shadow));
            } else {
                forgetPassWord_button.setBackground(getResources().getDrawable(R.drawable.bg_button_disable));
                forgetPassWord_button.setEnabled(false);
                ll_btn.setBackground(null);
            }




        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    private boolean passWordHasFocus = false;
    private boolean confiremPassWordHasFocus = false;
    /**
     * EditText 焦点监听
     * */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.forgetPassWord_password_input:      //手机号焦点变化
                passWordHasFocus = hasFocus;

                if (hasFocus && flag1){
                    iv_del_password.setVisibility(View.VISIBLE);
                }else{
                    iv_del_password.setVisibility(View.GONE);
                }
                break;
            case R.id.forgetPassWord_ConfirmPassWord:      //密码焦点变化
                confiremPassWordHasFocus = hasFocus;
                if (hasFocus && flag2){
                    iv_del_confirmpassword.setVisibility(View.VISIBLE);
                }else{
                    iv_del_confirmpassword.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}
