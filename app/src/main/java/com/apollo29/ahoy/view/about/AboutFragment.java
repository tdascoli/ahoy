package com.apollo29.ahoy.view.about;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.apollo29.ahoy.R;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class AboutFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView version = view.findViewById(R.id.version_title);
        version.setText(getString(R.string.main_about_version, R.string.versionName));

        TextView resources1 = view.findViewById(R.id.about_resources_content_1);
        resources1.setText(Html.fromHtml(getResources().getString(R.string.main_about_resources_icons1), FROM_HTML_MODE_LEGACY));

        TextView resources2 = view.findViewById(R.id.about_resources_content_2);
        resources2.setText(Html.fromHtml(getResources().getString(R.string.main_about_resources_icons2), FROM_HTML_MODE_LEGACY));
    }
}