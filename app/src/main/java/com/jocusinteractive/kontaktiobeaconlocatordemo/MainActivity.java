    package com.jocusinteractive.kontaktiobeaconlocatordemo;



    import android.Manifest;
    import android.annotation.TargetApi;
    import android.content.DialogInterface;
    import android.content.pm.PackageManager;
    import android.os.Build;
    import android.os.Bundle;
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

            KontaktSDK.initialize("BmhPDrHhfAGhidiMkwQTVNpqNkJDlLuv"); //from web portal

            textView = (TextView) findViewById(R.id.textView);



            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("This app need location access");
                    builder.setMessage("Please grant these permissions so the beacons can be identified");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    });
                    builder.show();

                }

            }




            proximityManager = new ProximityManager(this);
            proximityManager.setIBeaconListener(createIBeaconListener());




        }



        @Override
        protected void onStart() {
            super.onStart();
            startScanning();
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
