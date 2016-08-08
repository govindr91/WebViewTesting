package in.helpchat.webviewtesting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.ExecutionException;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    AdvancedWebView webView;

    Handler handler;
    WebMenu webMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (AdvancedWebView)findViewById(R.id.web_view);
//        webView.loadUrl("http://54.169.163.112/news_view/Humor");
//        webView.loadUrl("https://www.flipkart.com");
        webView.loadUrl("file:///android_asset/index.html");
        WebView.setWebContentsDebuggingEnabled(true);


//        webView.getSettings().setJavaScriptEnabled(true);
        webView.setListener(this, this);

//        webView.getSettings().setAllowContentAccess(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setDomStorageEnabled(true);
        /*webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAppCacheMaxSize(1024*1024*8);
        webView.getSettings().setAppCachePath("/data/data/com.akosha.directtalk/cache");
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
*/
       /* webView.setWebViewClient(new WebViewClient() {

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse response = null;
                if(url != null && url.endsWith(".js")) {
                    String assetPath = url.substring(url.lastIndexOf("/")+1, url.length());
                    try {
                        response = new WebResourceResponse(
                                "application/javascript",
                                "UTF8",
                                getAssets().open(assetPath)
                        );
                    } catch (IOException e) {
                        e.printStackTrace(); // Failed to load asset file
                    }
                } else if(url != null && url.endsWith(".css")) {
                    String assetPath = url.substring(url.lastIndexOf("/")+1, url.length());
                    try {
                        response = new WebResourceResponse(
                                "text/css",
                                "UTF8",
                                getAssets().open(assetPath)
                        );
                    } catch (IOException e) {
                        e.printStackTrace(); // Failed to load asset file
                    }
                }
                if (response == null) {
                    response = super.shouldInterceptRequest(view, url);
                }
                return response;
            }
        });*/


//        webView.getSettings().setAllowFileAccessFromFileURLs(true); //Maybe you don't need this rule
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);


//        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new WebViewJavaScriptInterface(this), "app");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("======= Create option menu called =====");

        if (webMenu != null) {
            List<WebMenu.Menu> menus = webMenu.menus;
            for (int i = 0; i < menus.size(); i++) {
                MenuItem newMenu = menu.add(0, menus.get(i).id, 0, menus.get(i).name);
                if (menus.get(i).drawable != null) {
                    newMenu.setIcon(menus.get(i).drawable);
                }
                int overflow = MenuItem.SHOW_AS_ACTION_ALWAYS;
                if (menus.get(i).overflow == 1) {
                    overflow = MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW;
                }
                newMenu.setShowAsAction(overflow);
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        System.out.println("======= Prepare option menu called =====");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<WebMenu.Menu> menus = webMenu.menus;

        for (int i = 0; i < menus.size(); i++) {
            if (item.getItemId() == menus.get(i).id) {
                if (menus.get(i).action.startsWith("javascript")) {
                    webView.loadUrl(menus.get(i).action);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface {

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }

        @JavascriptInterface
        public void addMenus(String menus) {
            System.out.println("==== Menu: " + menus);
            GsonBuilder gsonBuilder = new GsonBuilder();
            webMenu = gsonBuilder.create().fromJson(menus, WebMenu.class);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (webMenu != null && webMenu.menus != null && webMenu.menus.size() > 0) {
                        List<WebMenu.Menu> menuList = webMenu.menus;
                        for (int i = 0; i < menuList.size(); i++) {
                            WebMenu.Menu menu = menuList.get(i);
                            if (!TextUtils.isEmpty(menu.imageUrl)) {
                                try {
                                    Bitmap bitmap = Glide.with(MainActivity.this).load(menu.imageUrl).asBitmap().into(-1, -1).get();
                                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                                    menu.drawable = drawable;

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            supportInvalidateOptionsMenu();
                        }
                    });
                }
            });
            thread.start();
        }

        @JavascriptInterface
        public String name() {
            return "{\"name\":\"Adarsh\"}";
        }

    }
}
