package com.teamteamname.gotogothenburg.guide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.route.PointOfInterest;
import com.teamteamname.gotogothenburg.utils.AndroidConverter;

/**
 * A modal window which contains the guiding information of the given Point of Interest.
 * Created by kakan on 2015-10-05.
 */
public class GuideDialog extends DialogFragment {

    public static GuideDialog newInstance(PointOfInterest poi) {
        final GuideDialog frag = new GuideDialog();
        final Bundle args = new Bundle();
        args.putSerializable("poi", poi);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final PointOfInterest poi = (PointOfInterest) getArguments().getSerializable("poi");

        return new AlertDialog.Builder(getActivity())
                .setTitle(poi.getName())
                .setIcon(AndroidConverter.fileToRawResourceID(getActivity(), poi.getPicture()))
                .setMessage(AndroidConverter.fileToMessageConverter(getActivity(), poi.getTextGuide()))
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
                            }
                        }
                )
                .create();
    }
}
