package com.andack.croputils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andack.croputils.Model.ImagePiece;
import com.andack.croputils.Utils.ImageSave;
import com.andack.croputils.Utils.ImageSplitter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int IMAGE=1;
    private Button ChoosePic;
    private Button CutPic;
    private ImageView Pic;
    private Toolbar toolbar;
    private static int chooseId=0;
    private Bitmap bm;
    private AlertDialog dialog;
    private EditText xValue;
    private EditText yValue;
    private static int xCut;
    private static int yCut;
//    private View view;
    private List<ImagePiece> pieces=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        toolbar= (Toolbar) findViewById(R.id.ToolBar);
        toolbar.inflateMenu(R.menu.choose_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_nine:
                        Toast.makeText(MainActivity.this, "已经设置为9图裁剪", Toast.LENGTH_SHORT).show();
                        chooseId=9;
                        break;
                    case R.id.item_six:
                        Toast.makeText(MainActivity.this, "已经设置为6图裁剪", Toast.LENGTH_SHORT).show();
                        chooseId=6;
                        break;
                    case R.id.item_qq:
                        Toast.makeText(MainActivity.this, "qq类型", Toast.LENGTH_SHORT).show();
                        chooseId=3;
                        break;
                    case R.id.item_diy:
                        showDiyDialog();
                        chooseId=4;
                        break;
                }
                return true;
            }
        });
    }
    private void showDiyDialog()
    {
        LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.diy_dialog,null);
        xValue= (EditText) view.findViewById(R.id.xValueEt);
        yValue= (EditText) view.findViewById(R.id.yValueEt);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("请填写所需要的裁剪比例（3x3）");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String x=xValue.getText().toString().trim();
                String y=yValue.getText().toString().trim();
                if (!x.equals(null)&&!y.equals(null)&&!x.equals("")&&!y.equals("")) {
                    xCut=Integer.parseInt(x);
                    yCut=Integer.parseInt(y);
                }else {
                    Toast.makeText(MainActivity.this, "请填写相关参数", Toast.LENGTH_SHORT).show();
                }
            }
        });
            builder.setView(view);
        dialog=builder.create();
        dialog.show();

    }
    private  void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("请稍等...");
        builder.setView(new ProgressBar(this));
//        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        Window dialogWindow=dialog.getWindow();
        WindowManager.LayoutParams p=dialogWindow.getAttributes();
        p.width= (int) (d.getWidth()*0.4);
        p.height=(int) (d.getWidth()*0.4);
        p.gravity= Gravity.CENTER;
        dialogWindow.setAttributes(p);
    }
    private void dismissDialog()
    {
       dialog.dismiss();
    }

    private void initView() {
        ChoosePic= (Button) findViewById(R.id.choosePicture);
        CutPic= (Button) findViewById(R.id.cutPicBtn);
        Pic= (ImageView) findViewById(R.id.ImageView);

        ChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,IMAGE);
                ChoosePic.setVisibility(View.GONE);
                CutPic.setVisibility(View.VISIBLE);
            }
        });
        CutPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                switch (chooseId) {
                    case 0:
                        Toast.makeText(MainActivity.this, "没有选择裁剪的数量，请点击右上角的设置按钮，进行设置", Toast.LENGTH_SHORT).show();
                        break;
                    case 9:
                        pieces= ImageSplitter.split(bm,3,3);
                        break;
                    case 6:
                        pieces= ImageSplitter.split(bm,3,2);
                        break;
                    case 3:
                        pieces=ImageSplitter.splitForQQ(bm);
                        break;
                    case 4:
                        pieces=ImageSplitter.split(bm,xCut,yCut);
                        dialog.dismiss();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                        break;
                }
//                pieces= ImageSplitter.split(bm,3,2);

                for (ImagePiece imagePiece : pieces) {
                    ImageSave.saveImage(imagePiece.bitmap,MainActivity.this);
                }

                ChoosePic.setVisibility(View.VISIBLE);
//                CutPic.setVisibility(View.GONE);
                dismissDialog();
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE && resultCode== Activity.RESULT_OK && data!=null)
        {
            Uri selectedImage=data.getData();
            String[] filePathColumns={MediaStore.Images.Media.DATA};
            Cursor c=getContentResolver().query(selectedImage,filePathColumns,null,null,null);
            c.moveToFirst();
            int columnIndex=c.getColumnIndex(filePathColumns[0]);
            String imagePath=c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    private void showImage(String imagePath) {
        bm= BitmapFactory.decodeFile(imagePath);
        Pic.setImageBitmap(bm);
    }
}
