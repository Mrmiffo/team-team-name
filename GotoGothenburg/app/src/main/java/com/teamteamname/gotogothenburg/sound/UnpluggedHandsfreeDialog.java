package com.teamteamname.gotogothenburg.sound;

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

    private IDialogListener callback;

    public interface IDialogListener
    {
        void okPressed(boolean value);
    }


    public void registerListener (IDialogListener callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.unplugged_handsfree)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callback.okPressed(true);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callback.okPressed(false);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
