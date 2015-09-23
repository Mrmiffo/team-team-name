package API;
import com.teamteamname.gotogothenburg.GPSCoordReceiver;

/**
 * Created by Olof on 21/09/2015.
 */
public interface IAPIHandler {

    public void getGPSPosition(GPSCoordReceiver callback);

}
