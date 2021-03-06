package com.arnaudpiroelle.muzei.marvel.ui.settings.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnaudpiroelle.muzei.marvel.R;
import com.arnaudpiroelle.muzei.marvel.core.inject.Injector;
import com.arnaudpiroelle.muzei.marvel.core.utils.PreferencesUtils;
import com.arnaudpiroelle.muzei.marvel.core.utils.TrackerUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;

public class MyTagsFragment extends Fragment {

    @Inject
    protected PreferencesUtils preferencesUtils;

    @Inject
    TrackerUtils trackerUtils;

    @InjectView(R.id.list)
    ListView mList;

    @InjectView(R.id.tag)
    EditText mTag;

    @InjectView(R.id.empty)
    View mEmpty;

    private TagAdapter mTagAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Injector.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mytags, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onResume() {
        trackerUtils.sendScreen("MyTagsFragment");

        mTagAdapter = new TagAdapter();

        mList.setAdapter(mTagAdapter);
        mList.setEmptyView(mEmpty);

        mTag.setImeActionLabel(getString(R.string.add), EditorInfo.IME_ACTION_DONE);
        
        getActivity().setTitle(R.string.pref_tags);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.empty)
    void onEmptyClick() {
        if (mTag.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mTag, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnItemClick(R.id.list)
    void onTagClick(int i) {
        mTagAdapter.remove(i);
    }

    @OnEditorAction(R.id.tag)
    boolean onTagEditorAction(int i) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            onAddClick();
        }
        return false;
    }

    @OnClick(R.id.add)
    void onAddClick() {
        String tag = mTag.getText().toString();

        if (TextUtils.isEmpty(tag)) {
            Toast.makeText(getActivity(), getString(R.string.bad_tag_format), Toast.LENGTH_SHORT).show();
            return;
        }
        mTagAdapter.add(tag);
        mTag.setText(null);
    }

    private class TagAdapter extends BaseAdapter {

        private List<String> mTags;

        private TagAdapter() {
            mTags = preferencesUtils.getTags();
        }

        private void updateTagsInPref(List<String> tags) {
            preferencesUtils.setTags(tags);
        }

        public void add(String tag) {
            if (mTags.contains(tag)) {
                return;
            }
            mTags.add(0, tag);
            updateTagsInPref(mTags);
            notifyDataSetChanged();
        }

        void remove(int position) {
            mTags.remove(position);
            updateTagsInPref(mTags);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTags.size();
        }

        @Override
        public String getItem(int i) {
            return mTags.get(i);
        }

        @Override
        public long getItemId(int i) {
            return getItem(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.item_tag, viewGroup, false);
            }

            TextView tagTextView = (TextView) view.findViewById(R.id.tag_name);

            String tag = getItem(i);
            tagTextView.setText(tag);

            return view;
        }
    }
}
