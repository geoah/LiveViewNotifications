package nl.frankkie.livenotifications;

import android.accessibilityservice.AccessibilityService;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.sonyericsson.extras.liveview.plugins.livenotifications.LiveNotificationsService;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.wotuu.database.DatabaseOpenHelper;

/**
 *
 * @author FrankkieNL
 */
public class MyAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        //API16+ !!!!
        //getServiceInfo().eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
        DatabaseOpenHelper.createInstance(this);
        if (!IgnoreFilterUtil.allowNotification(this, arg0)){
            return;
        }

        Log.e("LiveNotifications_Accessibilty", arg0.toString());
        //Toast.makeText(this, "Notification from: " + arg0.getPackageName(), Toast.LENGTH_SHORT).show();

        //show on LiveView
        LiveNotificationsService liveViewService = LiveNotificationsService.getInstance();
        if (liveViewService != null) {
            String applicationName = "Unknown Application";
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(arg0.getPackageName().toString(), 0);
                applicationName = getPackageManager().getApplicationLabel(packageInfo.applicationInfo).toString();
            } catch (PackageManager.NameNotFoundException ex) {
                Logger.getLogger(MyAccessibilityService.class.getName()).log(Level.SEVERE, null, ex);
            }
            liveViewService.sendAnnounce(getString(R.string.notification), applicationName + " " + getString(R.string.send_a_notification));
        }
    }

    @Override
    public void onInterrupt() {
        //do nothin'
    }
}
