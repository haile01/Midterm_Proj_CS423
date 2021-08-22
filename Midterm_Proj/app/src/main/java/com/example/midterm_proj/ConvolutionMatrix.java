package com.example.midterm_proj;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ConvolutionMatrix {
    public static final int SIZE = 3;

    public static double[][] Matrix;

    public static void setFactor(double factor) {
        Factor = factor;
    }

    public static void setOffset(double offset) {
        Offset = offset;
    }

    public static double Factor = 1;
    public static double Offset = 1;

    public static void setSize(int size) {
        Matrix = new double[size][size];
    }

    private static ConvolutionMatrix single_instance = null;
    public static ConvolutionMatrix getInstance()
    {
        if (single_instance == null)
            single_instance = new ConvolutionMatrix();

        return single_instance;
    }

    public void setAll(double value) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = value;
            }
        }
    }

    public static void applyConfig(double[][] config) {
        for(int x = 0; x < SIZE; ++x) {
            for(int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = config[x][y];
            }
        }
    }

    public static Bitmap computeConvolution3x3(Bitmap src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[SIZE][SIZE];

        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {

                // get pixel matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;

                // get sum of RGB on matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        sumR += (Color.red(pixels[i][j]) * Matrix[i][j]);
                        sumG += (Color.green(pixels[i][j]) * Matrix[i][j]);
                        sumB += (Color.blue(pixels[i][j]) * Matrix[i][j]);
                    }
                }

                // get final Red
                R = (int)(sumR / Factor + Offset);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG / Factor + Offset);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB / Factor + Offset);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // apply new pixel
                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }

        src.recycle();
        src = null;

        // final image
        return result;
    }
}