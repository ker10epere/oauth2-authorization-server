# Spring Oauth2 Authorization Server (Spring Web)

The following are working setup for **Oauth2 Authorization Server**.  
Working with `org.springframework.security:spring-security-oauth2-authorization-server:1.2.0-SNAPSHOT`

# Setup

1. Setup default spring security
    - [WebSecurityConfig.java](src%2Fmain%2Fjava%2Fcom%2Fkerdagreat%2Fauthserverspringweb%2Fconfig%2FWebSecurityConfig.java)
2. Setup oauth2 authorization security
    - [AuthorizationServerConfig.java](src%2Fmain%2Fjava%2Fcom%2Fkerdagreat%2Fauthserverspringweb%2Fconfig%2FAuthorizationServerConfig.java)
3. Generate RSA Keys
    - Notice you must create type of `com.nimbusds.jose.jwk.RSAKey`

# Troubleshooting

`Invalid request: code_verifier is missing or invalid for registered client`
- Debug this method org.springframework.security.oauth2.server.authorization.authentication.CodeVerifierAuthenticator.authenticate
- org.springframework.security.oauth2.server.authorization.authentication.CodeVerifierAuthenticator.codeVerifierValid

# Execution

1. Fetch Authorization Code

   Oauth requires code_challenge to be generated. PKCE does that for us.  
   
   ```
   I already tried generating code_challenge manually by 
   1. Hash code_verifier by SHA-256, returns digest.
   2. Encrypt digest by Base64.
   The resulting process doesn't work, we really need to use PKCE Generator.
   ```
   
   Generate code_challenge from code_verifier from this link [PKCE Generator](https://tonyxu-io.github.io/pkce-generator/)
   
   Curl below doesn't work in postman. It should be pasted on the Web Browser.
   ```curl
   curl --location 'localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid&redirect_uri=http%3A%2F%2Fspring.io%2Fauth&code_challenge=S2WHg6Cj7LQO5DL9NMqSwkeaMMRJZ8g7JtB1yTjaCMo&code_challenge_method=S256' \
   --header 'Authorization: Basic dXNlcjoxMjM0NTY='
   ```
   You will be redirected to `login page`, after successful login, you will be redirected to redirect_uri you specified with query parameter of `code`. We will use this `code` so copy it.

2. Fetch JWT Token

   You will do this in postman.  
   Paste on header `code` the one we copied on previous step.  
   Paste on header `code_challenge` from the previous step.  
   Paste on header `code_verifier` the one we used for generating `code_challenge`.  
   ``` curl
   curl --location --request POST 'http://localhost:8080/oauth2/token?client_id=client&redirect_uri=http%3A%2F%2Fspring.io%2Fauth&grant_type=authorization_code&code=X3TMZGXsueF3fRLPWRYgLD5CkwHlJsJ7E-1HWSJIgAK1MhwV21z4i4LDioWKcC7E8RxFeG67p5slWaPjHv860LCTOVXdr8VLdtS5t00ws1-OmjWKdGZ7R0XDdSW2vI7x&code_verifier=8572581f527060f4de5f67be1a98c1e3cc05d434407c541765639f3a75d37672&code_challenge_method=S256&code_challenge=4b658783a0a3ecb40ee432fd34ca92c2479a30c44967c83b26d075c938da08ca' \
   --header 'Authorization: Basic Y2xpZW50OnNlY3JldA=='
   ```
   
   It will respond an object with `access_token`
   ex.
   ```json
   {
    "access_token": "eyJraWQiOiJmNTdiNGYyOS04YjJkLTQ2YzktOGMzOC1hZmEyYmMxNzcwZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNzAwMTI4MTA4LCJzY29wZSI6WyJvcGVuaWQiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwIiwiZXhwIjoxNzAwMTI4NDA4LCJpYXQiOjE3MDAxMjgxMDgsImp0aSI6IjkwN2U0Mjg5LTU0YzQtNGUxYi1hM2JhLTVjNmNhZWZjM2E2ZSJ9.xbeXXkrIjsF7n3GEoJGyaRUvJTLoCyWBczp7wUEWDMRIa6duol5EcwxXWgUmndbwYNgevk0vXq13F6EcmQb2935E07GzEnMZCWa2KJFmYuRu8kOsjsVltwY13Nh98P0SiNCJRRaV71sjJDDW8EJywE9iWwX_eaRcjwNkxv7hNqJPPszxV-0DrFQ7B3IEGEkcBoBCiwO9EFxTWNtpPheIJreN_0s15ZF0xOVFtUXb165n2HsP7b6PnC-Yi0iapjBgO2KfgaJ7y5GSEMTB5jTcJzTQeVqPAx60UNg2j40avv7KVi4JjPnyrVo5gW4rm_tLBbRUqM_sl0N0FwRfc6ZiKg",
    "scope": "openid",
    "id_token": "eyJraWQiOiJmNTdiNGYyOS04YjJkLTQ2YzktOGMzOC1hZmEyYmMxNzcwZWMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoiY2xpZW50IiwiYXpwIjoiY2xpZW50IiwiYXV0aF90aW1lIjoxNzAwMTI4MDk5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJleHAiOjE3MDAxMjk5MDgsImlhdCI6MTcwMDEyODEwOCwianRpIjoiMmNmYWVjNTQtYjkyNy00YjNiLTk1YzItYTg5NTAyOWQ1MjkzIiwic2lkIjoic0dGTElFaXZqRG9sb0ZteUdHc3VsbjFPUXhiYW5jQkpmdUViMzg2SEl1cyJ9.4ew75_I8GQYdkPocvh1x4N66H7KZ7XTcmfXzBD2D1RSjKZ-QxuxUkJshs82xOWv_f5cxAiECL6FRtvR1k1XBdlXtl39STUdFedn7TtUxNNbYcvrah38Mj5DVT_VD5jLjhizbUzAkMB2Cm2uZOvlHw4K7tENIL9hDceEdhDGUbGGkiH6OhGcSyw_2mvfp0bVJes2MqPfg4wvKUDS_m6vtv4MHGDbrXgmlI2iHJTDlY-5Yd27WRWI-LOzJ8Dt8rGZrNj_8YeyduiZWtb77i2HbVGO1N0DkGmKZM0YZi5AAtP6GOqMyf8TwUVbOHVkCMwBNUVWN_-dK7tzh2mySNYbhpg",
    "token_type": "Bearer",
    "expires_in": 300
   }
   ```