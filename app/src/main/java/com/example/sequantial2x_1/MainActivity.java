package com.example.sequantial2x_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    EditText insert_value;
    TextView result;
    Interpreter interpreter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert_value=findViewById(R.id.editText);
        result=findViewById(R.id.textView);
        try {
            interpreter=new Interpreter(loadModelfile(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private MappedByteBuffer  loadModelfile() throws Exception{
        AssetFileDescriptor assetFileDescriptor=this.getAssets().openFd("linear.tflite");
        FileInputStream fileInputStream=new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel=fileInputStream.getChannel();
        long startoffset= assetFileDescriptor.getStartOffset();
        long lenth=assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,lenth);
    }

    public float doInterface(String val) {
        float[] input=new float[1];
        input[0]=Float.parseFloat(val);
        float[][] output=new float[1][1];
        interpreter.run(input,output);
        return output[0][0];

    }
    @SuppressLint("SetTextI18n")
    public void onpredict(View view) {
float f= doInterface(insert_value.getText().toString());
result.setText("Result is "+f);
    }

}