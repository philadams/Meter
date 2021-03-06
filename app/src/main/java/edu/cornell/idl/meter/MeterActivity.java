package edu.cornell.idl.meter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * TODO: SuperNRS???
 */
public class MeterActivity extends Activity {

  static final String TAG = "MeterActivity";

  static final int LAUNCH_METER_ONE = 1;
  static final int LAUNCH_METER_TWO = 2;
  static final int LAUNCH_VAS = 11;
  static final int LAUNCH_NRS = 12;
  static final int LAUNCH_SUURETA = 13;
  static final int LAUNCH_SUPER_VAS = 14;
  static final int LAUNCH_SUPER_VAS_PLUS = 18;
  static final int LAUNCH_PAM = 15;
  static final int LAUNCH_MANY_FINGERS = 16;
  static final int LAUNCH_TAP_TAP = 17;
  static final int LAUNCH_SAFE = 18;
  static final int LAUNCH_PHOTOS_PEOPLE = 19;
  static final int LAUNCH_PHOTOS_LANDSCAPES = 20;
  static final int LAUNCH_NUMBER_PICKER_PLUS = 21;
  static final int LAUNCH_SUPERVAS_NUMBERED = 22;
  static final int LAUNCH_SAFE_SLIDER = 23;

  private NotificationManager mNotificationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.meter, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_meter_one) {
      startActivityForResult(new Intent(this, MeterOneActivity.class),
          MeterActivity.LAUNCH_METER_ONE);
    }
    if (id == R.id.action_vas) {
      startActivityForResult(new Intent(this, VASActivity.class), MeterActivity.LAUNCH_VAS);
    }
    if (id == R.id.action_nrs) {
      startActivityForResult(new Intent(this, NRSActivity.class), MeterActivity.LAUNCH_NRS);
    }
    if (id == R.id.action_suureta) {
      startActivityForResult(new Intent(this, SuuretaActivity.class), MeterActivity.LAUNCH_SUURETA);
    }
    if (id == R.id.action_super_vas) {
      startActivityForResult(new Intent(this, SuperVASActivity.class),
          MeterActivity.LAUNCH_SUPER_VAS);
    }
    if (id == R.id.action_super_vas_plus) {
      startActivityForResult(new Intent(this, SuperVASPlusActivity.class),
          MeterActivity.LAUNCH_SUPER_VAS_PLUS);
    }
    //if (id == R.id.action_pam) {
    //  startActivityForResult(new Intent(this, PAMActivity.class), MeterActivity.LAUNCH_PAM);
    //}
    if (id == R.id.action_many_fingers) {
      startActivityForResult(new Intent(this, ManyFingersActivity.class),
          MeterActivity.LAUNCH_MANY_FINGERS);
    }
    if (id == R.id.action_tap_tap) {
      startActivityForResult(new Intent(this, TapTapActivity.class), MeterActivity.LAUNCH_TAP_TAP);
    }
    if (id == R.id.action_safe) {
      startActivityForResult(new Intent(this, SAFEActivity.class), MeterActivity.LAUNCH_SAFE);
    }
    if (id == R.id.action_photos_people) {
      startActivityForResult(new Intent(this, PhotosPeopleActivity.class),
          MeterActivity.LAUNCH_PHOTOS_PEOPLE);
    }
    if (id == R.id.action_photos_landscape) {
      startActivityForResult(new Intent(this, PhotosLandscapesActivity.class),
          MeterActivity.LAUNCH_PHOTOS_LANDSCAPES);
    }
    if (id == R.id.action_number_picker_plus) {
      startActivityForResult(new Intent(this, NumberPickerPlusActivity.class),
          MeterActivity.LAUNCH_NUMBER_PICKER_PLUS);
    }
    if (id == R.id.action_supervas_numbered) {
      startActivityForResult(new Intent(this, SuperVASNumberedActivity.class),
          MeterActivity.LAUNCH_SUPERVAS_NUMBERED);
    }
    if (id == R.id.action_safe_slider) {
      startActivityForResult(new Intent(this, SAFESliderActivity.class),
          MeterActivity.LAUNCH_SAFE_SLIDER);
    }
    if (id == R.id.action_meter_two) {
      startActivityForResult(new Intent(this, MeterTwoActivity.class),
          MeterActivity.LAUNCH_METER_TWO);
    }
    if (id == R.id.action_notification_vrs) {
      actionNotificationVRS();
    }
    if (id == R.id.action_notification_nrs) {
      actionNotificationNRS();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      float reportedPainLevel = data.getFloatExtra(Constants.OHMAGE_SCORE_KEY, -1);
      double secondsToComplete = data.getDoubleExtra("secondsToComplete", -1);
      String meterNameAndVersion = data.getStringExtra("meterNameAndVersion");
      //Toast.makeText(this, "Pain level submitted", Toast.LENGTH_SHORT).show();
      Toast.makeText(this, String.format("Reported pain level: %.0f", reportedPainLevel),
          Toast.LENGTH_SHORT).show();
      Toast.makeText(this, String.format("meter name/version: %s", meterNameAndVersion),
          Toast.LENGTH_SHORT).show();
      Toast.makeText(this, String.format("Seconds to complete: %.2f", secondsToComplete),
          Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * Shows a simple 3-category VRS in the big notification.
   * If the smaller notification is shown, or the actions aren't clicked in the big one,
   * the NotificationVRS simply calls through to another meter.
   */
  private void actionNotificationVRS() {
    if (mNotificationManager == null) {
      mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    Intent resultIntent = new Intent(this, SAFESliderActivity.class);
    PendingIntent resultPendingIntent =
        PendingIntent.getActivity(this, 100, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    Intent noPainIntent = new Intent(this, NotificationVrsReceiver.class);
    noPainIntent.putExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, 0);
    PendingIntent noPainPendingIntent =
        PendingIntent.getBroadcast(this, 101, noPainIntent, PendingIntent.FLAG_ONE_SHOT);

    Intent somePainIntent = new Intent(this, NotificationVrsReceiver.class);
    somePainIntent.putExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, 5);
    PendingIntent somePainPendingIntent =
        PendingIntent.getBroadcast(this, 102, somePainIntent, PendingIntent.FLAG_ONE_SHOT);

    Intent muchPainIntent = new Intent(this, NotificationVrsReceiver.class);
    muchPainIntent.putExtra(Constants.NOTIFY_ACTION_PAIN_VALUE, 10);
    PendingIntent muchPainPendingIntent =
        PendingIntent.getBroadcast(this, 103, muchPainIntent, PendingIntent.FLAG_ONE_SHOT);

    Notification.Builder mBuilder =
        new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Time to report!")
            .setContentText("Right now, how much pain are you in?")
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .addAction(0, getString(R.string.notify_action_no_pain), noPainPendingIntent)
            .addAction(0, getString(R.string.notify_action_some_pain), somePainPendingIntent)
            .addAction(0, getString(R.string.notify_action_much_pain), muchPainPendingIntent);
    mNotificationManager.notify(Constants.NOTIFY_VRS, mBuilder.build());
  }

  /**
   * Shows a simple NRS-n in the big notification.
   * If the smaller notification is shown, or the actions aren't clicked in the big one,
   * the NotificationVRS simply calls through to another meter.
   */
  private void actionNotificationNRS() {
    if (mNotificationManager == null) {
      mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    Notification.Builder mBuilder = NotificationNrsReceiver.getNrsBuilder(this, -1);
    mNotificationManager.notify(Constants.NOTIFY_NRS, mBuilder.build());
  }
}