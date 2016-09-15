    package com.jocusinteractive.kontaktiobeaconlocatordemo;



    import android.Manifest;
    import android.annotation.TargetApi;
    import android.content.DialogInterface;
    import android.content.pm.PackageManager;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatActivity;

    import com.kontakt.sdk.android.common.KontaktSDK;

    public class MainActivity extends AppCompatActivity {

        private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            KontaktSDK.initialize("BmhPDrHhfAGhidiMkwQTVNpqNkJDlLuv"); //from web portal



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






        }


    }
