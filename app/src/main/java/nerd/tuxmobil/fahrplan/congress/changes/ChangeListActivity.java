package nerd.tuxmobil.fahrplan.congress.changes;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import nerd.tuxmobil.fahrplan.congress.MyApp;
import nerd.tuxmobil.fahrplan.congress.R;
import nerd.tuxmobil.fahrplan.congress.base.AbstractListFragment;
import nerd.tuxmobil.fahrplan.congress.base.BaseActivity;
import nerd.tuxmobil.fahrplan.congress.details.SessionDetailsActivity;

public class ChangeListActivity extends BaseActivity implements
        AbstractListFragment.OnSessionListClick {

    private static final String LOG_TAG = "ChangeListActivity";

    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, ChangeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_list);
        Toolbar toolbar = requireViewByIdCompat(R.id.toolbar);
        setSupportActionBar(toolbar);
        int actionBarColor = ContextCompat.getColor(this, R.color.colorActionBar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));

        if (savedInstanceState == null) {
            ChangeListFragment fragment = ChangeListFragment.newInstance(false);
            addFragment(R.id.container, fragment, ChangeListFragment.FRAGMENT_TAG);
            MyApp.LogDebug(LOG_TAG, "onCreate fragment created");
        }
    }

    @Override
    public void onSessionListClick(@NonNull String sessionId) {
        SessionDetailsActivity.startForResult(this, sessionId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MyApp.SESSION_VIEW && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
        }
    }
}
