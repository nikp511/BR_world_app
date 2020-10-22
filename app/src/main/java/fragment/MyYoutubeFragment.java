package fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.movie.brworld.MainActivity;
import com.movie.brworld.R;

import java.util.ArrayList;
import java.util.List;

import adapter.NotificationAdapter;
import application.MyApplication;
import constants.ConstantCodes;
import pojo.PojoNotification;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utils;

public class MyYoutubeFragment extends Fragment {

    private View mParentView;
    MyApplication mApplication;
    private Context mContext;
    private SharedPreferences mSharedPreference;
    private RelativeLayout mRelativeMain;
    private ProgressBar mProgressBar;

    private RecyclerView rvNotification;

    NotificationAdapter notificationAdapter;

    List<PojoNotification.Datum> mArraryNotifications = new ArrayList<>();


    LinearLayout llBottombar;

    WebView webView;

    String video_url = "wSXGpoDGDbE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.my_youtube_fragment, container, false);
        mApplication = (MyApplication) getActivity().getApplicationContext();
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        initailise();
        listners();

        mProgressBar = mParentView.findViewById(R.id.mProgressBar);

        webView = mParentView.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient());

        /*String summary = "<html><body><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/wSXGpoDGDbE\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe></body></html>";
        webView.loadDataWithBaseURL(null, summary, "text/html", "UTF-8", null);
*/

        webView.loadData("", "text/html", "UTF-8");


        WebSettings ws = webView.getSettings();
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setPluginState(WebSettings.PluginState.ON);
        ws.setJavaScriptEnabled(true);
        ws.setDisplayZoomControls(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.reload();


        String html = getHTML(video_url);

        webView.loadData(html, "text/html", "UTF-8");

        WebClientClass webViewClient = new WebClientClass(mProgressBar);
        webView.setWebViewClient(webViewClient);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);


        return mParentView;

    }

    void initailise() {

        Toolbar toolbar;
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        toolbar.setVisibility(View.GONE);

        llBottombar = getActivity().findViewById(R.id.llBottombar);
        llBottombar.setVisibility(View.GONE);


    }

    void listners() {


    }


    public class WebClientClass extends WebViewClient {
        ProgressBar ProgressBar = null;

        WebClientClass(ProgressBar progressBar) {
            ProgressBar = progressBar;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            ProgressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(getHTML(video_url));
            return true;
        }

    }

    public String getHTML(String videoId) {
        String html = "<iframe class=\"youtube-player\" " + "style=\"border: 0; width: 100%; height: 96%;"
                + "padding:0px; margin:0px\" " + "id=\"ytplayer\" type=\"text/html\" "
                + "src=\"http://www.youtube.com/embed/" + videoId
                + "?&theme=dark&autohide=2&modestbranding=1&showinfo=0&autoplay=1\fs=0\" frameborder=\"0\" "
                + "allowfullscreen autobuffer " + "controls onclick=\"this.play()\">\n" + "</iframe>\n";
        return html;
    }


}
