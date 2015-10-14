package com.teamteamname.gotogothenburg.guide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.teamteamname.gotogothenburg.R;

/**
 * Created by kakan on 2015-09-23.
 */
public class UnpluggedHandsfreeDialog extends DialogFragment {

    public UnpluggedHandsfreeDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.unplugged_handsfree)
                .setMessage(R.string.unplugged_handsfree_message)
                .setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
                .create();
    }
}
