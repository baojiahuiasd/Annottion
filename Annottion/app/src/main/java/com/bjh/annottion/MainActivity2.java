package com.bjh.annottion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.bjh.annotion_lib.BindView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity2 extends AppCompatActivity {
    @BindView(value = R.id.buttons)
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addTime();
        button.setText("AAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    private void addTime() {
        Class bindViewClazz = null;
        try {
            bindViewClazz = Class.forName(this.getClass().getName() + "_ViewBinding");
            Method method = bindViewClazz.getMethod("bind", this.getClass());
            method.invoke(bindViewClazz.newInstance(), this);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}