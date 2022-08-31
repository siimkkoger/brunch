# brunch
I can't eat brunch alone!

# Some spring security notes

1) https://stackoverflow.com/questions/984644/spring-authentication-provider-vs-authentication-processing-filter
According to Spring Security Architecture the process is:

- Filters are used to intercept the http request and do some checks
- Some filters are doing the check for authorization information in the request headers, body, cookies, etc. You can call them Authentication Processing Filter
- The actual job for authentication is done by another partie called Authentication Provider, because a filter will call a provider if the implementation needs it.
- It can happen that between the filter and provider can stay a Provider Manager, that can call all providers one by one and see if some of them can handle it, if so: then do so.


2) OncePerRequestFilter VS AbstractAuthenticationProcessingFilter
- https://stackoverflow.com/questions/51789109/to-support-a-custom-authentication-flow-is-it-best-to-extend-from-onceperreques

3) DaoAuthenticationProvider
- https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html

4) 