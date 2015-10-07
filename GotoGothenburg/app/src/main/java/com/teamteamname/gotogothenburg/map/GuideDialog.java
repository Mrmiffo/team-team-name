package com.teamteamname.gotogothenburg.map;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import com.teamteamname.gotogothenburg.PointOfInterest;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.utils.AndroidConverter;

/**
 * Created by kakan on 2015-10-05.
 */
public class GuideDialog extends DialogFragment {

    private PointOfInterest pointToGuide;

    public static GuideDialog createInstance(PointOfInterest pointToGuide) { //Android does not want constructors
        final GuideDialog guideDialog = new GuideDialog();
        guideDialog.pointToGuide = pointToGuide;
        guideDialog.createGUI();
        return guideDialog;
    }

    private void createGUI() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String title = pointToGuide.toString();
        builder.setTitle(title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase()); //Capitalise first letter
        builder.setIcon(AndroidConverter.fileToRawResourceID(getActivity(), pointToGuide.getPicture()));
        builder.setMessage(AndroidConverter.fileToRawResourceID(getActivity(), pointToGuide.getTextGuide()))
            .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }
}
