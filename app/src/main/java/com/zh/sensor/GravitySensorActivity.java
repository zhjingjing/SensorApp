package com.zh.sensor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zh.sensor.databinding.ActivityGravitySensorBinding;

public class GravitySensorActivity extends AppCompatActivity {

    private ActivityGravitySensorBinding binding;
    public static void launch(Context context){
        context.startActivity(new Intent(context,GravitySensorActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this,R.layout.activity_gravity_sensor);
        binding.setPresenter(this);


    }


}
