package layout;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by Qwerasdzxc on 9/12/17.
 */

public class TimetableWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new TimetableWidgetViewFactory(this.getApplicationContext(), intent));
    }
}
