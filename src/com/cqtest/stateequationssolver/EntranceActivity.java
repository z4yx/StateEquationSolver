package com.cqtest.stateequationssolver;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class EntranceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.entrance_gas, R.id.entrance_liquid})
    void onClickEntry(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.entrance_gas:
                intent.setClass(this, GasActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }

}
