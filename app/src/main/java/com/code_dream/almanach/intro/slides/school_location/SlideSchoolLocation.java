package com.code_dream.almanach.intro.slides.school_location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.code_dream.almanach.R;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SlideSchoolLocation extends SlideFragment implements SchoolLocationContract.View, OnMapReadyCallback{

    @BindView(R.id.slide_new_school_view)
    LinearLayout newSchoolLayout;

    @BindView(R.id.slide_existing_school_view)
    LinearLayout existingSchoolLayout;

    @BindView(R.id.slide_school_map_view)
    MapView mapView;

    private SchoolLocationPresenter presenter;

    GoogleMap mGoogleMap;

    ArrayList<MarkerOptions> markers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_intro_slide_school_location, container, false);
        ButterKnife.bind(this, view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new SchoolLocationPresenter(this);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Registry.userFinishedChoosing) {
                    setProperView(Registry.addingNewSchool);
                    presenter.getSchoolLocation(Registry.schoolName);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.787197, 20.457273), 10));

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                presenter.onMapClick(latLng);
            }
        });

    }

    @Override
    public void setProperView(Boolean addingNewSchool) {
        if (addingNewSchool){
            existingSchoolLayout.setVisibility(View.GONE);
            newSchoolLayout.setVisibility(View.VISIBLE);
        } else {
            existingSchoolLayout.setVisibility(View.VISIBLE);
            newSchoolLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void moveAndMarkLocationOnMap(LatLng location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);

        markers.clear();
        markers.add(markerOptions);

        mGoogleMap.clear();
        mGoogleMap.addMarker(markerOptions);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }

    @Override
    public String getGeocodingApiKey() {
        return getString(R.string.google_maps_key);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public Boolean markerSet() {
        return !markers.isEmpty();
    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.transparent;
    }

    @Override
    public boolean canMoveFurther() {
        return presenter.canMoveFurther();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return presenter.cantMoveFurtherMessage();
    }
}
