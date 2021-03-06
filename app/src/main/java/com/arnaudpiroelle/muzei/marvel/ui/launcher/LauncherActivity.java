package com.arnaudpiroelle.muzei.marvel.ui.launcher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.arnaudpiroelle.muzei.marvel.R;
import com.arnaudpiroelle.muzei.marvel.core.inject.Injector;
import com.arnaudpiroelle.muzei.marvel.core.utils.AppCheckerUtils;
import com.arnaudpiroelle.muzei.marvel.core.utils.TrackerUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Intent.ACTION_VIEW;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public class LauncherActivity extends AppCompatActivity {

    @InjectView(R.id.install_button)
    Button installButton;

    @InjectView(R.id.remove_button)
    Button removeButton;

    @Inject
    PackageManager packageManager;

    @Inject
    AppCheckerUtils appCheckerUtils;

    @Inject
    TrackerUtils trackerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Injector.inject(this);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        trackerUtils.sendScreen("LauncherActivity");

        if (appCheckerUtils.checkApplicationInstalled("net.nurik.roman.muzei")) {
            installButton.setVisibility(GONE);
            removeButton.setVisibility(VISIBLE);
        } else {
            installButton.setVisibility(VISIBLE);
            removeButton.setVisibility(GONE);
        }
    }

    @OnClick(R.id.install_button)
    void onInstallClick() {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=net.nurik.roman.muzei"));
        startActivity(intent);
    }

    @OnClick(R.id.remove_button)
    void onRemoveClick() {
        packageManager.setComponentEnabledSetting(getComponentName(),
                COMPONENT_ENABLED_STATE_DISABLED,
                DONT_KILL_APP);

        Toast.makeText(this, R.string.already_installed, LENGTH_SHORT).show();

        this.finish();
    }
}
