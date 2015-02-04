package com.app.mehdichouag.epitechintranet.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.app.mehdichouag.epitechintranet.R;
import com.app.mehdichouag.epitechintranet.model.Trombi;

/**
 * Created by mehdichouag on 01/02/15.
 */
public class DialogActionTrombi extends DialogFragment {

    public static final String TAG = DialogActionTrombi.class.getSimpleName();

    private static final String KEY_USER_TROMBI = "com.app.mehdichouag.epitechintranet.fragment.dialog.KEY_USER_TROMBI";

    public DialogActionTrombi() {
    }

    public static DialogActionTrombi newInstance(Trombi trombi) {
        DialogActionTrombi fragment = new DialogActionTrombi();
        Bundle b = new Bundle();

        b.putSerializable(KEY_USER_TROMBI, trombi);
        fragment.setArguments(b);
        return (fragment);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Trombi trombi = (Trombi)getArguments().getSerializable(KEY_USER_TROMBI);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.action_trombi, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_trombi, trombi.getLogin())});
                    email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_email_trombi, trombi.getPrenom()));
                    email.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_email_trombi));
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                }
            }
        });
        return builder.create();
    }
}
