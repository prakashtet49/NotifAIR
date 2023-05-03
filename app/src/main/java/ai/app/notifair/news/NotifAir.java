package ai.app.notifair.news;

import android.app.Application;

/*
** Used for getting the application instance
**/
public class NotifAir extends Application {
    private static NotifAir notifAirInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        notifAirInstance = this;
    }
    public static NotifAir getMyTimesApplicationInstance(){
        return notifAirInstance;
    }
}
