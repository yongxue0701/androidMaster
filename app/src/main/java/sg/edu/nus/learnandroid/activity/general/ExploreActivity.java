package sg.edu.nus.learnandroid.activity.general;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import sg.edu.nus.learnandroid.R;
import sg.edu.nus.learnandroid.activity.course.CourseMapActivity;

/**
 * Created by Yongxue
 */

public class ExploreActivity extends AppCompatActivity {

    private ShareDialog shareDialog;
    private BottomNavigationView bottomNavigationView;
    private BottomNavigationMenuView bottomNavigationMenuView;
    private ImageView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WebView exploreWebView = (WebView) findViewById(R.id.explore_webview);
                exploreWebView.setVisibility(View.VISIBLE);
            }
        }, 2000);

        loadingAnim = (ImageView) findViewById(R.id.explore_loading_anim);
        loadingAnim.setBackgroundResource(R.drawable.spinning_globe_grey);
        AnimationDrawable frameAnimation = (AnimationDrawable) loadingAnim.getBackground();
        frameAnimation.start();

        shareDialog = new ShareDialog(this);  // intialize facebook shareDialog.

        // Set up bottom navigation view
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.explore_bottom_navigation);

        // Change icon size of bottom navigation bar
        bottomNavigationMenuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < bottomNavigationMenuView.getChildCount(); i++) {
            final View iconView = bottomNavigationMenuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent myIntent;

                switch (item.getItemId()) {
                    case R.id.navigation_title_account:
                        myIntent = new Intent(getApplicationContext(), UserAccountActivity.class);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                        break;
                    case R.id.navigation_title_course:
                        myIntent = new Intent(getApplicationContext(), CourseMapActivity.class);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                        break;
                }
                return false;
            }
        });

        // Set up custom action bar with back button
        getSupportActionBar().setDisplayOptions(getActionBar().DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar_only_has_title);

        // Set up the back button and title on action bar
        View view = getSupportActionBar().getCustomView();

        TextView actionBarTitleTV = (TextView) view.findViewById(R.id.action_bar_only_has_title_title);
        actionBarTitleTV.setText(R.string.action_bar_title_explore);

        WebView webView = (WebView) findViewById(R.id.explore_webview);
        ExploreActivity.JavaScriptInterface myJavaScriptInterface =
                new ExploreActivity.JavaScriptInterface(this, webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(myJavaScriptInterface, "Explore");
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/www/explore.html");
    }

    @Override
    public void onBackPressed() {

    }

    public class JavaScriptInterface {

        private ExploreActivity parentActivity;
        private WebView webView;

        public JavaScriptInterface(ExploreActivity exploreActivity, WebView mWebView) {
            parentActivity = exploreActivity;
            webView = mWebView;
        }

        @JavascriptInterface
        public void shareToPrivateAccounts() {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://www.facebook.com/Learnandroidwithus-854183284756525/"))
                        .build();

                shareDialog.show(linkContent);
            }
        }

        @JavascriptInterface
        public void joinFBGroup() {
            Intent myIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/Learnandroidwithus-854183284756525/"));
            parentActivity.startActivity(myIntent);
        }
    }
}
