    package com.jocusinteractive.kontaktiobeaconlocatordemo;



    import android.Manifest;
    import android.annotation.TargetApi;
    import android.content.DialogInterface;
    import android.content.pm.PackageManager;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.v4.app.ActivityCompat;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;
    import android.util.Log;
    import android.widget.TextView;

    import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
    import com.kontakt.sdk.android.ble.manager.ProximityManager;
    import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
    import com.kontakt.sdk.android.ble.manager.listeners.IBeaconListener;
    import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleIBeaconListener;
    import com.kontakt.sdk.android.common.KontaktSDK;
    import com.kontakt.sdk.android.common.profile.IBeaconDevice;
    import com.kontakt.sdk.android.common.profile.IBeaconRegion;

    import java.util.List;

    public class MainActivity extends AppCompatActivity {

        private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
        private ProximityManagerContract proximityManager;

        private TextView textView;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



            textView = (TextView) findViewById(R.id.textView);





            KontaktSDK.initialize("BmhPDrHhfAGhidiMkwQTVNpqNkJDlLuv"); //from web portal


            proximityManager = new ProximityManager(this);
            proximityManager.setIBeaconListener(createIBeaconListener());




        }

        private void checkPermissionAndStart() {
            int checkSelfPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (PackageManager.PERMISSION_GRANTED == checkSelfPermissionResult) {
                //already granted
                startScanning();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //we should show some explanation for user here
                    showExplanationDialog();
                } else {
                    //request permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            }
        }



        private void showExplanationDialog(){
            new AlertDialog.Builder(this.getApplicationContext())
                    .setTitle("Allow Location Services")
                    .setMessage("Android requires location services to be enabled for the BLE Beacon tracking to work correctly")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        private void showDisableDialog(){
            new AlertDialog.Builder(this.getApplicationContext())
                    .setTitle("No Location Services")
                    .setMessage("Location services have not been allowed therefore the BLE becons cannon be detected. This app will NOT work correctly.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (100 == requestCode) {
                    //same request code as was in request permission
                    startScanning();
                }

            } else {
                //not granted permission
                //show some explanation dialog that some features will not work
                showDisableDialog();
            }
        }



        @Override
        protected void onStart() {
            super.onStart();
            checkPermissionAndStart();
        }

        @Override
        protected void onStop() {
            proximityManager.stopScanning();
            super.onStop();
        }

        @Override
        protected void onDestroy() {
            proximityManager.disconnect();
            proximityManager = null;
            super.onDestroy();
        }

        private void startScanning() {
            proximityManager.connect(new OnServiceReadyListener() {
                @Override
                public void onServiceReady() {
                    proximityManager.startScanning();
                }
            });
        }

        private IBeaconListener createIBeaconListener() {
            return new SimpleIBeaconListener() {

                @Override
                public void onIBeaconsUpdated(List<IBeaconDevice> ibeacons, IBeaconRegion region) {
                    super.onIBeaconsUpdated(ibeacons, region);
                }

                @Override
                public void onIBeaconDiscovered(IBeaconDevice ibeacon, IBeaconRegion region) {
                    CharSequence text = textView.getText();
                    textView.setText(text + "\n" + "Beacon: "+ibeacon.getName()+ ", Distance: " + ibeacon.getDistance());
                    Log.i("Sample", "IBeacon discovered: " + ibeacon.getUniqueId());
                }
            };
        }


    }
