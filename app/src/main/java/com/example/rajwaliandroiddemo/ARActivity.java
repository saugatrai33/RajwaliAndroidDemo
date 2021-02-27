package com.example.rajwaliandroiddemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.PlaneRenderer;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ARActivity extends AppCompatActivity {

    private static final String TAG =
            ARActivity.class.getCanonicalName();
    private ModelRenderable modelRenderable;
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ArFragment arFragment;

    private static final String GLTF_ASSET =
            Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/CNCDATA/double_sized_bed/testbed.gltf";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_r);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        arFragment = (ArFragment) getSupportFragmentManager()
                .findFragmentById(R.id.ux_fragment);

        if (arFragment != null) {
            Texture.Sampler sampler = Texture.Sampler.builder()
                    .setMinFilter(Texture.Sampler.MinFilter.LINEAR)
                    .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                    .setWrapMode(Texture.Sampler.WrapMode.REPEAT).build();

            CompletableFuture<Texture> trigrid = Texture.builder()
                    .setSource(this, R.drawable.trigrid)
                    .setSampler(sampler).build();

            this.arFragment.getArSceneView()
                    .getPlaneRenderer()
                    .getMaterial()
                    .thenAcceptBoth(trigrid, (material, texture) -> {
                        material.setTexture(PlaneRenderer.MATERIAL_TEXTURE, texture);
                        material.setFloat(PlaneRenderer.MATERIAL_SPOTLIGHT_RADIUS, Float.MAX_VALUE);
                    });
        }

        if (checkIsSupportedDeviceOrFinish(this)) {
            loadModel();
            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane,
                     MotionEvent motionEvent) -> {
                        if (modelRenderable != null) {
                            // Create the Anchor.
                            modelRenderable.setShadowCaster(false);
                            modelRenderable.setShadowReceiver(false);
                            Anchor anchor = hitResult.createAnchor();
                            AnchorNode anchorNode = new AnchorNode(anchor);
                            anchorNode.setParent(arFragment.getArSceneView().getScene());
                            // Create the transformable andy and add it to the anchor.
                            TransformableNode transformableNode =
                                    new TransformableNode(arFragment.getTransformationSystem());
                            transformableNode.setParent(anchorNode);
                            /*blocking zoom functionality*/
                            transformableNode.getScaleController().setEnabled(false);
                            transformableNode.getTranslationController().setEnabled(true);

                            transformableNode.setRenderable(modelRenderable);
                            transformableNode.select();
                        } else {
                            Log.d(TAG, "onCreate: " + "model renderable is null");
                        }
                    });
        }

    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadModel() {
        ModelRenderable.builder()
                .setSource(this, RenderableSource.builder().setSource(
                        this,
                        Uri.parse(GLTF_ASSET),
                        RenderableSource.SourceType.GLTF2)
                        .setScale(0.5f)  // Scale the original model to 50%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId(GLTF_ASSET)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load renderable " +
                                            GLTF_ASSET, Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onClear();
    }

    private void onClear() {
        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        for (com.google.ar.sceneform.Node node : children) {
            if (node instanceof AnchorNode) {
                if (((AnchorNode) node).getAnchor() != null) {
                    Objects.requireNonNull(((AnchorNode) node).getAnchor()).detach();
                }
            }

        }
    }

}