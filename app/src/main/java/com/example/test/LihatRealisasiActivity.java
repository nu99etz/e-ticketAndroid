package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.network.BaseAPIService;
import com.example.test.network.ConnectorAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatRealisasiActivity extends AppCompatActivity implements View.OnClickListener{

    TextView keluhan,realisasi;
    String keluhan_nama, realisasi_nama,id;

    private Uri uri;
    Context mContext ;

    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;

    private Button realisasi_foto_1,realisasi_foto_2,realisasi_foto_3,realisasi_foto_4;

    ProgressDialog loading;
    BaseAPIService APIServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_realisasi);
        initcomponents();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {

            final String URL;
            keluhan_nama = extras.getString("result_keluhan");
            keluhan.setText(keluhan_nama);
        }
    }

    private void initcomponents() {
        keluhan = (TextView)findViewById(R.id.keluhan);
        realisasi = (TextView)findViewById(R.id.realisasi_nama);
        realisasi_foto_1 = (Button)findViewById(R.id.realisasi_foto_1);
        realisasi_foto_2 = (Button)findViewById(R.id.realisasi_foto_2);
        realisasi_foto_3 = (Button)findViewById(R.id.realisasi_foto_3);
        realisasi_foto_4 = (Button)findViewById(R.id.realisasi_foto_4);
        realisasi_foto_1.setOnClickListener(this);
        realisasi_foto_2.setOnClickListener(this);
        realisasi_foto_3.setOnClickListener(this);
        realisasi_foto_4.setOnClickListener(this);
    }

    private void pilihFoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        }else{
            bukaGaleri();
        }
    }

    private void bukaGaleri() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE);
    }

    @Override
    public void onClick(View view) {
        if(view == realisasi_foto_1) {
            pilihFoto();
        } else if(view == realisasi_foto_2) {
            pilihFoto();
        } else if(view == realisasi_foto_3) {
            pilihFoto();
        } else if(view == realisasi_foto_4) {
            pilihFoto();
        } else if(view == realisasi) {
            Bundle extras = getIntent().getExtras();
            id = extras.getString("result_id");
            loading = ProgressDialog.show(mContext,null,"Tolong Tunggu Sebentar"
                    ,true,false);
            updateRealisasi(id);
        }
    }

    private void updateRealisasi(String id) {
        APIServices.realisasiUpload(id,realisasi.getText().toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loading.dismiss();
                    try{
                        JSONObject JSONresult = new JSONObject(response.body().string());
                        if(JSONresult.getString("success").equals("true")) {
                            String pesan_sukses = JSONresult.getString("messages").toString();
                            Toast.makeText(mContext ,pesan_sukses, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug","onFailure: ERROR > " + t.toString());
                loading.dismiss();
            }
        });
    }


}
