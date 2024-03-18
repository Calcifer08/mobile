package ru.mirea.lozhnichenkoas.mireaproject.ui.WebView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import ru.mirea.lozhnichenkoas.mireaproject.R;

public class WebViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        WebView webView = view.findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.youtube.com");

        return view;
    }

}