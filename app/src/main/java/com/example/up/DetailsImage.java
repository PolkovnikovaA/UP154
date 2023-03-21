package com.example.up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Base64;


public class DetailsImage extends AppCompatActivity {
    Connection connection;
    //ImageView imageView;
    MaskaProfile mask;
    AdapterProfile adapter;

    private SubsamplingScaleImageView imageView;

    View v;
    private boolean isImageScaled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_image);

        getSupportActionBar().hide();

        mask = getIntent().getParcelableExtra("Pictures");

        //imageView = findViewById(R.id.Image_Details);


        imageView = findViewById(R.id.Image_Details);
        //imageView.setImage(ImageSource.resource(R.drawable.ku));
        imageView.setImage(ImageSource.bitmap(getImgBitmap(mask.getImage())));

        /*imageView.setOnClickListener(v -> {
            if (!isImageScaled) v.animate().scaleX(1.6f).scaleY(1.6f).setDuration(500);
            if (isImageScaled) v.animate().scaleX(1f).scaleY(1f).setDuration(500);
            isImageScaled = !isImageScaled;
        });*/

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                //onMove - обнаруживает перетаскивания

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //onSwiped - обнаруживает свайпы





                //int pos = viewHolder.getAdapterPosition();
                //adapter.getItemId(pos);
            }
        });
               // .attachToRecyclerView(imageView);

    }

    private Bitmap getImgBitmap(String encodedImg) {
        if(encodedImg!=null&& !encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(DetailsImage.this.getResources(),
                R.drawable.icon);
    }

    public void onClickProfile(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void onClickDelete(View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(DetailsImage.this);
        builder.setTitle("Удалить")
                .setMessage("Вы уверены что хотите удалить фотографию?")
                .setCancelable(false)
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            ConnectionHelper connectionHelper = new ConnectionHelper();
                            connection = connectionHelper.connectionClass();
                            if (connection != null) {
                                String query = "DELETE FROM  Pictures  WHERE ID= "+mask.getID()+"";
                                Statement statement = connection.createStatement();
                                statement.executeQuery(query);
                            }
                        }

                        catch (Exception ex)
                        {

                        }
                        Perehod(v);
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void Perehod(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}