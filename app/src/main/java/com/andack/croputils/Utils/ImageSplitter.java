package com.andack.croputils.Utils;

import android.graphics.Bitmap;

import com.andack.croputils.Model.ImagePiece;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CropUtils
 * 项目作者：anDack
 * 项目时间：2017/1/28
 * 邮箱：    1160083806@qq.com
 * 描述：    切割工具类
 */

public class ImageSplitter {
    public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {
        List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / xPiece;
        int pieceHeight = height / yPiece;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                ImagePiece piece = new ImagePiece();
                piece.index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
                        pieceWidth, pieceHeight);
                pieces.add(piece);
            }
        }

        return pieces;
    }
    public static List<ImagePiece> splitForQQ(Bitmap bitmap)
    {
        List<ImagePiece> pieces = new ArrayList<ImagePiece>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceBigWidth = (width / 3 )* 2;
        int pieceBigHeight = (height / 3)* 2;
        int pieceHeight=height/3;
        int pieceWidth=width/3;
        ImagePiece pieceBig=new ImagePiece();
        pieceBig.index=0;
        pieceBig.bitmap= Bitmap.createBitmap(bitmap, 0, 0,
                pieceBigWidth, pieceBigHeight);
        pieces.add(pieceBig);

        for (int i = 0; i <3; i++) {
            ImagePiece piece=new ImagePiece();
            piece.index=i+1;
            int yValue=i*pieceHeight;
            piece.bitmap=Bitmap.createBitmap(bitmap, pieceBigWidth, yValue,
                    pieceWidth, pieceHeight);
            pieces.add(piece);
        }
        for (int i = 0; i <2; i++) {
            ImagePiece piece=new ImagePiece();
            piece.index=i+4;
            int xValue=i*pieceWidth;
            piece.bitmap=Bitmap.createBitmap(bitmap, xValue, pieceBigHeight,
                    pieceWidth, pieceHeight);
            pieces.add(piece);
        }
        return pieces;
    }
}
