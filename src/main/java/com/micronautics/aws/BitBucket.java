package com.micronautics.aws;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BitBucket {
    public Exception exception;
    String accessKey;
    String secretKey;
    OAuthService oAuthService;
    Token accessToken;

    /**@see https://github.com/fernandezpablo85/scribe-java/wiki/getting-started */
    public BitBucket() throws IOException {
        accessKey = System.getenv("bbAccessKey");
        secretKey = System.getenv("bbSecretKey");
        try {
            if (accessKey==null && secretKey==null) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("BBCredentials.properties");
                Properties prop = new Properties();
                prop.load(inputStream);
                accessKey = prop.getProperty("accessKey");
                secretKey = prop.getProperty("secretKey");
            }

            oAuthService = new ServiceBuilder()
                           .provider(BitBucketApi.class)
                           .apiKey(accessKey)
                           .apiSecret(secretKey)
                           .debug() // comment this out when it works
                           .build();

            Token requestToken = oAuthService.getRequestToken();
            String authUrl = oAuthService.getAuthorizationUrl(requestToken);
            Verifier verifier = new Verifier(authUrl); // "verifier you got from the user"
            accessToken = oAuthService.getAccessToken(requestToken, verifier);
        } catch (Exception ex) {
            exception = ex;
        }
    }

    public Response request(String urlStr) {
        OAuthRequest request = new OAuthRequest(Verb.GET, urlStr);
        oAuthService.signRequest(accessToken, request);
        Response response = request.send();
        System.out.println(response.getBody());
        return response;
    }
}
