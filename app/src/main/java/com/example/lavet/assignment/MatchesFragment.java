package com.example.lavet.assignment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lavet.assignment.models.Matches;
import com.example.lavet.assignment.viewmodels.FirebaseViewModel;

import java.util.ArrayList;
import java.util.List;



/**
 * Provides UI for the view with List.
 */
public class MatchesFragment extends Fragment {

    private FirebaseViewModel viewModel;
    private List<Matches> mMatchList;
    private MatchesViewAdapter adapter;
    public static final String ARG_DATA_SET = "data-set";
    private OnListFragmentInteractionListener mListener;
    private LocationManager locationManager;
    public double longitudeGPS, latitudeGPS;
    public RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMatchList = getArguments().getParcelableArrayList(ARG_DATA_SET);
        }
    }

    @Override
    @SuppressWarnings("unused")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);

        viewModel = new FirebaseViewModel();

        adapter = new MatchesViewAdapter(mMatchList, mListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getMatches((response) -> {
            adapter.updateMatchesList(response);
        });

        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        getGPSUpdates();

        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        locationManager.removeUpdates((locationListenerGPS));
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Matches match);
    }

    //Location Methods

    //Returns whether location is enable
    private boolean isLocationEnabled(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //Checks whether location service is enable on phone or not
    private boolean checkLocation() {
        if(!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();
    }

    //If location is not enabled, brings up a dialog to enable it
    private void showAlert(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
        dialog.setTitle(R.string.enable_location)
                .setMessage(getString(R.string.location_message))
                .setPositiveButton(R.string.location_settings, (paramDialogInterface, paramInt)->{
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton(R.string.location_cancel, (paramDialogInterface, paramInt)-> {});
        dialog.show();
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeGPS = location.getLongitude();
            latitudeGPS = location.getLatitude();
            adapter.setLatLong(latitudeGPS, longitudeGPS);

            getActivity().runOnUiThread(()-> {
                viewModel.getMatches((response) -> {
                    float [] distanceToMatch = new float[1];
                    ArrayList<Matches> tempList = new ArrayList<>();
                    for(Matches match:response){

                        Location.distanceBetween(latitudeGPS, longitudeGPS,
                                Double.parseDouble(match.lat), Double.parseDouble(match.longitude), distanceToMatch);

                        if(distanceToMatch[0]/1609.34 <= 10){
                            tempList.add(match);
                        }
                    }
                    //Check if tempList is empty, add a blank match if it is
                    if(tempList.size() == 0){
                        Matches tempMatch = new Matches();
                        tempList.add(tempMatch);
                    }
                    adapter.updateMatchesList(tempList);
                });
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    public void getGPSUpdates() {
        if(!checkLocation()) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6 * 1000, 10, locationListenerGPS);
        }
    }
}