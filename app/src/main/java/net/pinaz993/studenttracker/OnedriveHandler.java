package net.pinaz993.studenttracker;

import com.mashape.unirest.http.Unirest;

import java.util.Formatter;

/**
 * Created by Alexander Snedden on 9/19/17.
 */

public class OnedriveHandler {
    /*
    Class to handle Onedrive functionality.
     */
    private final String authUrlFmtString = "https://login.microsoftonline.com/common/oauth2/v2.0/authorize?" +
                                            "client_id=%s&redirect_uri=%s&scope=%s";
    private final String applicationID = "76141700-9cbb-441f-86ca-30a48a73ec9a";
    private final String redirectUri = "msal76141700-9cbb-441f-86ca-30a48a73ec9a://auth";
    private final String scope = "user.read";


    public String getUserAuthenticationUrl() {
        /* construct url to direct user to sign themselves into with */
        StringBuilder authUrlBuilder = new StringBuilder();
        Formatter authUrlFormatter = new Formatter();
        authUrlFormatter.format(authUrlFmtString, applicationID, redirectUri, scope);
        return authUrlBuilder.toString();
    }
}
