package com.arnaudpiroelle.muzei.marvel.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.arnaudpiroelle.muzei.marvel.R;
import com.arnaudpiroelle.muzei.marvel.core.inject.Injector;
import com.arnaudpiroelle.muzei.marvel.source.MarvelMuzeiArtSource;
import com.arnaudpiroelle.muzei.marvel.ui.settings.fragment.PrefsFragment;

import static com.arnaudpiroelle.muzei.marvel.core.utils.Constants.ACTION_RESCHEDULE;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Injector.inject(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.pref_content, new PrefsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Intent localIntent = new Intent(ACTION_RESCHEDULE);
            localIntent.setClass(Injector.getContext(), MarvelMuzeiArtSource.class);
            startService(localIntent);
            
            super.onBackPressed();
        }
    }
}
