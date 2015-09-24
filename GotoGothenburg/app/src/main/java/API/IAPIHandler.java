package API;
import com.teamteamname.gotogothenburg.GPSCoordReceiver;

/**
 * Created by Olof on 21/09/2015.
 */
public interface IAPIHandler extends IDeviceAPI{

    // Needs a callback and the dgw for the buss you want.
    public void getGPSPosition(GPSCoordReceiver callback, String dgw);

}
