package com.cqtest.stateequationssolver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    @OnClick({R.id.entrance_gas, R.id.entrance_liquid, R.id.entrance_hs,R.id.entrance_fugacity,R.id.entrance_bubble,R.id.entrance_dew,R.id.entrance_activity})
    void onClickEntry(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.entrance_gas:
                intent.setClass(this, GasStateActivity.class);
                break;
            case R.id.entrance_liquid:
                intent.setClass(this, LiquidActivity.class);
                break;
            case R.id.entrance_hs:
                intent.setClass(this, GasHSActivity.class);
                break;
            case R.id.entrance_fugacity:
                intent.setClass(this, FugacityActivity.class);
                break;
            case R.id.entrance_bubble:
                intent.setClass(this, WilsonBubbleActivity.class);
                break;
            case R.id.entrance_dew:
                intent.setClass(this, WilsonDewActivity.class);
                break;
            case R.id.entrance_activity:
                intent.setClass(this, ActivityCoefficientActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }

}
