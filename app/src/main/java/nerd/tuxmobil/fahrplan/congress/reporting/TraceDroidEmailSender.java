package nerd.tuxmobil.fahrplan.congress.reporting;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import org.ligi.tracedroid.TraceDroid;
import org.ligi.tracedroid.collecting.TraceDroidMetaInfo;

import java.io.File;

import de.cketti.mailto.EmailIntentBuilder;
import nerd.tuxmobil.fahrplan.congress.BuildConfig;
import nerd.tuxmobil.fahrplan.congress.R;

// This class supports translation and configuration via XML files.
// The original TraceDroidEmailSender class is available here:
// https://github.com/ligi/tracedroid
public abstract class TraceDroidEmailSender {

    public static void sendStackTraces(@NonNull final Activity context) {
        File[] stackTraceFiles = TraceDroid.getStackTraceFiles();
        if (stackTraceFiles == null || stackTraceFiles.length < 1) {
            return;
        }

        final String appName = context.getString(R.string.app_name);
        final String emailAddress = BuildConfig.TRACE_DROID_EMAIL_ADDRESS;
        final String dialogTitle = context.getString(
                R.string.trace_droid_dialog_title, appName);
        final String dialogMessage = context.getString(
                R.string.trace_droid_dialog_message, appName);
        final String buttonTitleSend = context.getString(
                R.string.trace_droid_button_title_send);
        final String buttonTitleLater = context.getString(
                R.string.trace_droid_button_title_later);
        final String buttonTitleNo = context.getString(
                R.string.trace_droid_button_title_no);
        final int maximumStackTracesCount = context.getResources().getInteger(
                R.integer.config_trace_droid_maximum_stack_traces_count);

        new AlertDialog.Builder(context)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setPositiveButton(buttonTitleSend, (dialog, whichButton) -> {
                    Intent emailIntent = getEmailIntent(
                            context, emailAddress, maximumStackTracesCount);
                    try {
                        context.startActivity(emailIntent);
                        TraceDroid.deleteStacktraceFiles();
                    } catch (ActivityNotFoundException e) {
                        String message = context.getString(R.string.trace_droid_no_email_app);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(buttonTitleNo, (dialog, whichButton) ->
                        TraceDroid.deleteStacktraceFiles())
                .setNeutralButton(buttonTitleLater, (dialog, whichButton) -> {
                    // Nothing to do here
                })
                .show();
    }

    @NonNull
    private static Intent getEmailIntent(@NonNull final Activity activity,
                                         @SuppressWarnings("SameParameterValue") @NonNull final String recipient,
                                         int maximumStackTracesCount) {
        return EmailIntentBuilder.from(activity)
                .to(recipient)
                .subject("[TraceDroid Report] " + TraceDroidMetaInfo.getAppPackageName())
                .body(TraceDroid.getStackTraceText(maximumStackTracesCount))
                .build();
    }

}
