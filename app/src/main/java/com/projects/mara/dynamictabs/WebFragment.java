package com.projects.mara.dynamictabs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Yashasvi on 23-06-2017.
 */

public class WebFragment extends Fragment {

    WebView webView;
    ProgressBar progressBar;
    String URL;
    SwipeRefreshLayout swipeLayout;
    public WebFragment() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setURL(String url){
        URL = url;
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        webView.saveState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_tabs, container, false);
        webView = (WebView) myView.findViewById(R.id.webView);
        progressBar = (ProgressBar) myView.findViewById(R.id.progressBar);
        swipeLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swipeToRefresh);
        reloadPage(savedInstanceState);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view,int progress){
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                if(progress==100){
                    //progressBar.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
        //webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().supportMultipleWindows();
        webView.setFocusable(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()){
                    webView.goBack();
                }
                else {
                    //getActivity().finish();
                }
                return true;
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Insert your code here
                reloadPage(savedInstanceState); // refreshes the WebView
            }
        });
        return myView;
    }

    private void reloadPage(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            webView.restoreState(savedInstanceState);
            webView.setVisibility(View.GONE);
        } else{
            webView.loadUrl(URL);
            webView.setVisibility(View.GONE);
        }
        if(swipeLayout.isRefreshing()){
            swipeLayout.setRefreshing(false);
        }
    }
}
