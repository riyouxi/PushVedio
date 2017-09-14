package com.test.push.media;

/**
 * Created by blueberry on 1/5/2017.
 */

public class Yuv420Util {
    /**
     * Nv21:
     * YYYYYYYY
     * YYYYYYYY
     * YYYYYYYY
     * YYYYYYYY
     * VUVU
     * VUVU
     * VUVU
     * VUVU
     * <p>
     * I420:
     * YYYYYYYY
     * YYYYYYYY
     * YYYYYYYY
     * YYYYYYYY
     * UUUU
     * UUUU
     * VVVV
     * VVVV
     *
     * @param data Nv21数据
     * @param dstData I420(YUV420)数据
     * @param w 宽度
     * @param h 长度
     */
    public static void Nv21ToI420(byte[] data, byte[] dstData, int w, int h) {

        int size = w * h;
        // Y
        System.arraycopy(data, 0, dstData, 0, size);
        for (int i = 0; i < size / 4; i++) {
            dstData[size + i] = data[size + i * 2 + 1]; //U
            dstData[size + size / 4 + i] = data[size + i * 2]; //V
        }
    }

    /**
     * 将Nv21数据转换为Yuv420SP数据
     * @param data Nv21数据
     * @param dstData Yuv420sp数据
     * @param w 宽度
     * @param h 高度
     */
    public static void Nv21ToYuv420SP(byte[] data, byte[] dstData, int w, int h) {
        int size = w * h;
        // Y
        System.arraycopy(data, 0, dstData, 0, size);

        for (int i = 0; i < size / 4; i++) {
            dstData[size + i * 2] = data[size + i * 2 + 1]; //U
            dstData[size + i * 2 + 1] = data[size + i * 2]; //V
        }
    }

    //顺时针旋转90
    public static void YUV420spRotate90Clockwise(byte[] src, byte[] dst, int srcWidth, int srcHeight) {
//        int wh = width * height;
//        int k = 0;
//        for (int i = 0; i < width; i++) {
//            for (int j = height - 1; j >= 0; j--) {
//                des[k] = src[width * j + i];
//                k++;
//            }
//        }
//        for (int i = 0; i < width; i += 2) {
//            for (int j = height / 2 - 1; j >= 0; j--) {
//                des[k] = src[wh + width * j + i];
//                des[k + 1] = src[wh + width * j + i + 1];
//                k += 2;
//            }
//        }

        int wh = srcWidth * srcHeight;
        int uvHeight = srcHeight >> 1;

        //旋转Y
        int k = 0;
        for (int i = 0; i < srcWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < srcHeight; j++) {
                dst[k] = src[nPos + i];
                k++;
                nPos += srcWidth;
            }
        }

        for (int i = 0; i < srcWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                dst[k] = src[nPos + i];
                dst[k + 1] = src[nPos + i + 1];
                k += 2;
                nPos += srcWidth;
            }
        }

    }

    private static void yuv420spRotate90(byte[] des, byte[] src,
                                         int width, int height) {
        int wh = width * height;
        int k = 0;
        for(int i = 0; i < width; i++) {
            for(int j = height - 1; j >= 0; j--)
            {
                des[k] = src[width * j + i];
                k++;
            }
        }
        for(int i = 0; i < width; i += 2) {
            for(int j = height / 2 - 1; j >= 0; j--)
            {
                des[k] = src[wh+ width * j + i];
                des[k+1] = src[wh + width * j + i + 1];
                k+=2;
            }
        }
    }

    public static byte[] rotateYUV240SP(byte[] src,byte[] des,int width,int height)
    {

        int wh = width * height;
        //旋转Y
        int k = 0;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++)
            {
                des[k] = src[width*j + i];
                k++;
            }
        }

        for(int i=0;i<width/2;i++) {
            for(int j=0;j<height/2;j++)
            {
                des[k] = src[wh+ width/2*j + i];
                des[k+width*height/4]=src[wh*5/4 + width/2*j + i];
                k++;
            }
        }
        return des;

    }


    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight){
        byte[] yuv =new byte[imageWidth*imageHeight*3/2];
// Rotate the Y luma
        int i =0;
        for(int x =0;x < imageWidth;x++){
            for(int y = imageHeight-1;y >=0;y--){
                yuv[i]= data[y*imageWidth+x];
                i++;}

        }
// Rotate the U and V color components
        i = imageWidth*imageHeight*3/2-1;for(int x = imageWidth-1;x >0;x=x-2){for(int y =0;y < imageHeight/2;y++){
            yuv[i]= data[(imageWidth*imageHeight)+(y*imageWidth)+x];
            i--;
            yuv[i]= data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
            i--;}}return yuv;}

}
