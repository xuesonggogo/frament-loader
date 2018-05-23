package com.lixuesong.fragment_loader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.fragmentUtils.fragment.BaseActivity;
import com.fragmentUtils.fragment.ContentFragment;

public class MainActivity extends BaseActivity {
    private FrameLayout frameLayout;
    private Button btn, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.testbtn);
        btn.setOnClickListener(this);
        btn2 = findViewById(R.id.testbtn2);
        btn2.setOnClickListener(this);

    }

    @Override
    protected void setFrameID() {
        frameLayout = findViewById(R.id.fl_main_content);
        frameID = R.id.fl_main_content;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        ContentFragment ft;

        if (v.getId() == R.id.testbtn) {
            ft = new MCarTipsInfoFragment();
            mNaviFragmentManager.showFragment(0, null, ft);
        } else if (v.getId() == R.id.testbtn2) {
            ft = new MCarTipsInfoFragment2();
            mNaviFragmentManager.showFragment(0, null, ft);
        }

    }

    public ContentFragment getFragment() {
        return null;
    }


}
