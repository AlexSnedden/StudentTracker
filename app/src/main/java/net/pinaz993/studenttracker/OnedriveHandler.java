package net.pinaz993.studenttracker;

import com.mashape.unirest.http.Unirest;

import java.util.Formatter;

/**
 * Created by Alexander Snedden on 9/19/17.
 */

public class OnedriveHandler {
    private final String userAuthenticationUrl = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize";
    private final String applicationID = "76141700-9cbb-441f-86ca-30a48a73ec9a";
    private final String redirectUri = "msal76141700-9cbb-441f-86ca-30a48a73ec9a://auth";
    private final String scope = "user.read";

    public String getUserAuthenticationUrl() {
        StringBuilder authUrlBuilder = new StringBuilder();
        Formatter authUrlFormatter = new Formatter();
        authUrlFormatter.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                                                                            userAuthenticationUrl,
                                                                            applicationID,
                                                                            redirectUri, scope);
        return authUrlBuilder.toString();
    }
}
