package com.myapp.newsclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.myapp.newsclient.R;
import com.myapp.newsclient.utils.CacheUtils;


public class WelcomeActivity extends AppCompatActivity {

    private View mContainer;// 外侧容器
    public final static String	KEY_IS_FIRST	= "is_first";	// 第一次登录的标记

    private final static long DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContainer = findViewById(R.id.welcome_container);

        //实现动画
        AnimationSet set = new AnimationSet(false);

        //1.旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(DURATION);
        rotateAnimation.setFillEnabled(true);
        rotateAnimation.setFillAfter(true);

        //2.缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f,1f,0f,1f,Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(DURATION);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);

        //3.透明渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(DURATION);
        alphaAnimation.setFillEnabled(true);
        alphaAnimation.setFillAfter(true);

        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);

        mContainer.startAnimation(set);
        set.setAnimationListener(new WelcomeAnimationListener());
    }

    class WelcomeAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
// 当动画结束时调用

            boolean isFirst = CacheUtils.getBoolean(WelcomeActivity.this, KEY_IS_FIRST, true);// 默认是第一次打开应用

            // 页面跳转
            if (isFirst)
            {
                // 当应用程序第一次进入时，需要跳转到 引导页面
                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent);
            }
            else
            {
                // 否则需要跳转到 主页面 :TODO

                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
            }

            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
