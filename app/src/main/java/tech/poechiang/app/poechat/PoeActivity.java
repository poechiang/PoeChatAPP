package tech.poechiang.app.poechat;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import tech.poechiang.app.poechat.core.StatusBarTheme;

public abstract class PoeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

//        调整状态栏文字颜色
        adjustStatusBarFore(StatusBarTheme.DARK);

        Toolbar mToolbar = getToolbar();
        if (mToolbar != null) {
            //将Toolbar显示到界面
            mToolbar.setTitle(getTitle());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            setSupportActionBar(mToolbar);
            if (isNavigateBackShowing()){
                mToolbar.setNavigationIcon(R.drawable.ic_navigate_before_black_24dp);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
            else {
                mToolbar.setNavigationIcon(null);
                mToolbar.setNavigationOnClickListener(null);
            }

        }
    }


    /**
     * 调整状态栏主题：
     * @param theme StatusBarTheme.DARK:|StatusBarTheme.DARK:白字暗底|黑字浅底
     */
    protected void adjustStatusBarFore(StatusBarTheme theme){
        View decor = this.getWindow().getDecorView();
        if (theme == StatusBarTheme.DARK) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if(theme == StatusBarTheme.LIGHT){
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    /**
     * 设置头部标题
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {

        Toolbar mToolbar = getToolbar();

        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
        }

    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar(){
        return null;
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     * @return
     */
    protected boolean isNavigateBackShowing(){
        return true;
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

}
