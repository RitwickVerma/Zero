package com.lucasasselli.zero.wallpaper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.lucasasselli.zero.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lucasasselli.zero.Constants.BG_CUSTOM_ID;
import static com.lucasasselli.zero.Constants.BG_FORMAT;
import static com.lucasasselli.zero.utils.StorageHelper.getBackgroundFolder;
import static com.lucasasselli.zero.utils.StorageHelper.getCustomWallpaper;


class BackgroundHelper {

    private static final String TAG = "BackgroundHelper";

    static List<Layer> loadFromFile(String id, Context context) {
        // Create final list
        List<Layer> output = new ArrayList<>();

        if (id.equals(BG_CUSTOM_ID)) {
            // Custom wallpaper (one file)
            File customFile = getCustomWallpaper(context);

            if (customFile != null) {
                if (customFile.exists()) {
                    Layer customLayer = new Layer(customFile, 50);
                    output.add(customLayer);
                }
            }
        } else {
            // Get background folder
            File file = getBackgroundFolder(id, context);

            if (file != null) {
                // Directory found
                Log.d(TAG, "Directory " + id + " found in root!");

                File[] layers = file.listFiles();
                if (layers != null) {
                    if (layers.length > 0) {

                        // Sort array by name
                        Arrays.sort(layers);

                        for (File layerFile : layers) {
                            String zString = Utils.getBetweenStrings(layerFile.getPath(), id + "_", BG_FORMAT);
                            int layerZ = Integer.valueOf(zString);

                            Layer layer = new Layer(layerFile, layerZ);
                            output.add(layer);

                            Log.d(TAG, "Layer with name " + layerFile.getName() + " loaded with z=" + layerZ);
                        }
                    } else {
                        Log.e(TAG, "Directory " + id + " is empty!");
                        return null;
                    }
                }
            } else {
                // Directory not found
                Log.e(TAG, "Directory " + id + " not found in root!");
                return null;
            }
        }

        return output;
    }

    static Bitmap decodeScaledFromFile(File file) {
        // Get the size
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(file.getPath(), options);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    static Bitmap decodeScaledFromRes(Resources res, int id) {
        // Get the size
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, id, options);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, id, options);
    }

    static class Layer {

        private File file;
        private int z;

        Layer(File file, int z) {
            this.file = file;
            this.z = z;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }
}
