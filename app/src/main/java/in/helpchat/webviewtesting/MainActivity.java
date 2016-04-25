package in.helpchat.webviewtesting;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdvancedWebView webView = (AdvancedWebView)findViewById(R.id.web_view);
        webView.loadUrl("http://54.169.163.112/news_view/Humor");
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
        public String name() {
            return "{\"name\":\"Adarsh\"}";
        }

        public String topAppUsages() {

        }
    }
}
