package com.example.rajwaliandroiddemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.TranslateAnimation3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.MaterialManager;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

public class DoubleBedRenderer extends Renderer
        implements IAsyncLoaderCallback {

    private static final String TAG = DoubleBedRenderer.class.getCanonicalName();

    private MaterialManager materialManager;
    private Material materialMainDesign, materialPillow, materialMatressCube,
            materialSeExtensibleFrame, materialSeSlatePlane, materialSeBedFramePlane,
            materialSeDrawer1, materialseDrawer2;
    private Texture wood;
    private Texture clothe;
    private Texture fabric;
    private Texture jeans;
    private Texture rustic_beech;

    private OnModelInteractionListener modelInteractionListener;

    double topLength = 1, topBreadth = 1, topThickness = 1;

    private Object3D sedDrawer1, sedDrawer2;

    private Animation3D openSedDrawer1, openSedDrawer2,
            closeSedDrawer1, closeSedDrawer2;

    public DoubleBedRenderer(Context context) {
        super(context);
    }

    @Override
    protected void initScene() {
        modelInteractionListener.onModelLoading();

        materialManager = MaterialManager.getInstance();
        DirectionalLight directionalLight =
                new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(2);

        wood = new Texture("wood",
                R.drawable.natural_red);

        clothe = new Texture("clothe",
                R.drawable.cloth);

        fabric = new Texture("fabric",
                R.drawable.fabric);

        jeans = new Texture("jeans",
                R.drawable.jeans);

        rustic_beech = new Texture("rustic_beech",
                R.drawable.beech_rustic);

        materialMainDesign = new Material();
        materialPillow = new Material();
        materialMatressCube = new Material();
        materialSeExtensibleFrame = new Material();
        materialSeSlatePlane = new Material();
        materialSeBedFramePlane = new Material();
        materialSeDrawer1 = new Material();
        materialseDrawer2 = new Material();

        LoaderOBJ loaderOBJ = new LoaderOBJ(mContext.getResources(),
                mTextureManager, R.raw.double_size_bed_obj);
        loadModel(loaderOBJ, this, R.raw.double_size_bed_obj);

    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset,
                                 float xOffsetStep, float yOffsetStep,
                                 int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    @Override
    public void onModelLoadComplete(ALoader loader) {
        modelInteractionListener.onModelLoadingComplete();
        Log.d(TAG, "onModelLoadComplete: model loaded.");
        final LoaderOBJ obj = (LoaderOBJ) loader;
        Object3D parsedObject = obj.getParsedObject();
        parsedObject.setPosition(Vector3.ZERO);
        parsedObject.setScale(1f);
        parsedObject.setDoubleSided(true);

        ArcballCamera arcballCamera = new ArcballCamera(mContext,
                ((Activity) mContext).findViewById(R.id.surface_view),
                parsedObject);



        for (int i = 0; i < parsedObject.getNumChildren(); i++) {
            Object3D childObj = parsedObject.getChildAt(i);
            String childName = childObj.getName();

            try {
                if (childName.equalsIgnoreCase("main_design.015_Plane.031")) {

                    materialMainDesign.removeTexture(wood);
                    materialMainDesign.addTexture(mTextureManager.addTexture(jeans));
                    materialMainDesign.setColorInfluence(0);
                    materialMainDesign.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialMainDesign.setAmbientColor(Color.BLUE);
                    materialMainDesign.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialMainDesign);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("Pillow_02.002_Cube.007")) {
                    materialPillow.removeTexture(wood);
                    materialPillow.addTexture(mTextureManager.addTexture(fabric));
                    materialPillow.setColorInfluence(0);
                    materialPillow.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialPillow.setAmbientColor(Color.BLUE);
                    materialPillow.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialPillow);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);

                }
                else if (childName.equalsIgnoreCase("Sedrawer2_Plane.007")) {

                    this.sedDrawer2 = childObj;

                    materialseDrawer2.addTexture(mTextureManager.addTexture(rustic_beech));
                    materialseDrawer2.setColorInfluence(0);
                    materialseDrawer2.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialseDrawer2.setAmbientColor(Color.BLACK);
                    materialseDrawer2.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialseDrawer2);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("Sebedframe_Plane.009")) {
                    materialSeBedFramePlane.addTexture(mTextureManager.addTexture(wood));
                    materialSeBedFramePlane.setColorInfluence(0);
                    materialSeBedFramePlane.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialSeBedFramePlane.setAmbientColor(Color.BLACK);
                    materialSeBedFramePlane.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialSeBedFramePlane);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("SeExtensibleframe_Plane.006")) {
                    materialSeExtensibleFrame.addTexture(mTextureManager.addTexture(wood));
                    materialSeExtensibleFrame.setColorInfluence(0);
                    materialSeExtensibleFrame.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialSeExtensibleFrame.setAmbientColor(Color.BLACK);
                    materialSeExtensibleFrame.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialSeExtensibleFrame);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("mattress_Cube.002")) {

                    materialMatressCube.removeTexture(wood);
                    materialMatressCube.addTexture(mTextureManager.addTexture(clothe));
                    materialMatressCube.setColorInfluence(0);
                    materialMatressCube.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialMatressCube.setAmbientColor(Color.BLACK);
                    materialMatressCube.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialMatressCube);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("SeSlat_Plane.005")) {
                    materialSeSlatePlane.addTexture(mTextureManager.addTexture(wood));
                    materialSeSlatePlane.setColorInfluence(0);
                    materialSeSlatePlane.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialSeSlatePlane.setAmbientColor(Color.BLACK);
                    materialSeSlatePlane.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialSeSlatePlane);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
                else if (childName.equalsIgnoreCase("Sedrawer1_Plane.008")) {

                    this.sedDrawer1 = childObj;

                    materialSeDrawer1.addTexture(mTextureManager.addTexture(rustic_beech));
                    materialSeDrawer1.setColorInfluence(0);
                    materialSeDrawer1.setDiffuseMethod(new DiffuseMethod.Lambert());
                    materialSeDrawer1.setAmbientColor(Color.BLACK);
                    materialSeDrawer1.enableLighting(true);

                    materialManager.removeMaterial(childObj.getMaterial());
                    childObj.setMaterial(materialSeDrawer1);
                    childObj.setVisible(true);
                    childObj.setScaleX(topLength);
                    childObj.setScaleZ(topBreadth);
                    childObj.setScaleY(topThickness);
                }
            } catch (Texture.TextureException e) {
                e.printStackTrace();
            }

            getCurrentScene().addChild(childObj);
        }

        arcballCamera.setTarget(parsedObject);
        arcballCamera.setPosition(0, 0, 2);
        arcballCamera.setNearPlane(0.1);
        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(),
                arcballCamera);
        parsedObject.setLookAt(getCurrentCamera().getPosition());

        getCurrentScene().addChild(parsedObject);
    }

    @Override
    public void onModelLoadFailed(ALoader loader) {
        modelInteractionListener.onModelLoadingComplete();
        Log.d(TAG, "onModelLoadFailed: ");
    }

    public void openDrawer() {

        if ((closeSedDrawer1 != null) && (closeSedDrawer2 != null)) {
            closeSedDrawer1.reset();
            closeSedDrawer2.reset();
        }

        openSedDrawer1 = new TranslateAnimation3D(Vector3.ZERO,
                Vector3.X.multiply(3 / 4f));
        openSedDrawer1.setTransformable3D(sedDrawer1);
        openSedDrawer1.setDurationDelta(3);
        openSedDrawer1.setInterpolator(new AccelerateDecelerateInterpolator());
        openSedDrawer1.play();
        getCurrentScene().registerAnimation(openSedDrawer1);

        openSedDrawer2 = new TranslateAnimation3D(Vector3.ZERO,
                Vector3.X.multiply(3 / 4f));
        openSedDrawer2.setTransformable3D(sedDrawer2);
        openSedDrawer2.setDurationDelta(3);
        openSedDrawer2.setInterpolator(new AccelerateDecelerateInterpolator());
        openSedDrawer2.play();
        getCurrentScene().registerAnimation(openSedDrawer2);

    }

    public void closeDrawer() {

        if ((closeSedDrawer1 != null) && (closeSedDrawer2 != null)) {
            openSedDrawer1.reset();
            openSedDrawer2.reset();
        }

        closeSedDrawer1 = new TranslateAnimation3D(Vector3.X.multiply(3 / 4f),
                Vector3.ZERO);
        closeSedDrawer1.setTransformable3D(sedDrawer1);
        closeSedDrawer1.setDurationDelta(3);
        closeSedDrawer1.setInterpolator(new AccelerateDecelerateInterpolator());
        closeSedDrawer1.play();
        getCurrentScene().registerAnimation(closeSedDrawer1);

        closeSedDrawer2 = new TranslateAnimation3D(Vector3.X.multiply(3 / 4f),
                Vector3.ZERO);
        closeSedDrawer2.setTransformable3D(sedDrawer2);
        closeSedDrawer2.setDurationDelta(3);
        closeSedDrawer2.setInterpolator(new AccelerateDecelerateInterpolator());
        closeSedDrawer2.play();
        getCurrentScene().registerAnimation(closeSedDrawer2);

    }

    public void setModelInteractionListener(OnModelInteractionListener modelInteractionListener) {
        this.modelInteractionListener = modelInteractionListener;
    }
}
