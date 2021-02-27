package com.example.rajwaliandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;

public class MainActivity extends AppCompatActivity
        implements OnModelInteractionListener {

    private ProgressBar progressBar;
    private Button drawer;
    private DoubleBedRenderer doubleBedRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView surfaceView = findViewById(R.id.surface_view);
        progressBar = findViewById(R.id.progress_circular);
        drawer = findViewById(R.id.btn_drawer);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, ARActivity.class)));
        surfaceView.setTransparent(true);
        surfaceView.setFrameRate(60.0);
        surfaceView.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);
        doubleBedRenderer = new DoubleBedRenderer(this);
        doubleBedRenderer.setModelInteractionListener(this);
        surfaceView.setSurfaceRenderer(doubleBedRenderer);
        drawer.setOnClickListener(view -> {
                    String btnText = drawer.getText().toString().trim();
                    if (btnText.equalsIgnoreCase(getString(R.string.btn_label_open_drawer))) {
                        doubleBedRenderer.openDrawer();
                        drawer.setText(getString(R.string.btn_label_close_drawer));
                    } else {
                        doubleBedRenderer.closeDrawer();
                        drawer.setText(getString(R.string.btn_label_open_drawer));
                    }
                }
        );
    }

    @Override
    public void onModelLoading() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    @Override
    public void onModelLoadingComplete() {
        runOnUiThread(() -> progressBar.setVisibility(View.GONE));
    }
}