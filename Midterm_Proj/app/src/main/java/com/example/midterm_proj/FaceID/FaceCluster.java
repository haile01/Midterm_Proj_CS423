package com.example.midterm_proj.FaceID;

import java.util.ArrayList;
import java.util.Arrays;

public class FaceCluster {
    static int count;
    private final int id;
    private final float[] vector;
    public ArrayList<Integer> faceId;

    public FaceCluster (float[] features) {
        vector = Arrays.copyOf(features, features.length);
        id = count++;
    }

    public float distance(float[] features) {
        float sum = 0;
        for(int i = 0; i < features.length; i++) {
            sum += Math.pow(features[i]  - vector[i], 2);
        }
        return (float)Math.sqrt(sum);
    }

    public void addFace (int id) {
        faceId.add(id);
    }

    public int getID() {
        return id;
    }
}
