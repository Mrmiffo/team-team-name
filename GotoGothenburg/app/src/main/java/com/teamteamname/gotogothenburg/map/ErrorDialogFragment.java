package com.teamteamname.gotogothenburg.map;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.teamteamname.gotogothenburg.R;

/**
 * Created by patrick on 29/09/2015.
 * Dialog which displays the errors from mapfragment to the user.
 */
public class ErrorDialogFragment extends DialogFragment {
    public ErrorDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int errorCode = this.getArguments().getInt(MapFragment.DIALOG_ERROR);
        return GoogleApiAvailability.getInstance().getErrorDialog(
                this.getActivity(), errorCode, MapFragment.REQUEST_RESOLVE_ERROR);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Fragment mapFragment = getFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            Log.e("REWRITE", "Erroneous code");
            return;
        }
        ((MapFragment) mapFragment).onDialogDismissed();
    }
}
