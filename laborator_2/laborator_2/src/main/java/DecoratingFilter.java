import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*") // Apply this filter to all requests
public class DecoratingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the requested URI to check if it's /ShuffledFile
        String requestURI = httpRequest.getRequestURI();

        // Bypass the filter for /ShuffledFile
        if (requestURI.endsWith("/ShuffledFile")) {
            // Proceed with the request chain without modifying the response
            chain.doFilter(request, response);
            return; // Exit early, no further response modifications
        }

        // Wrap the response to capture the output
        ResponseWrapper responseWrapper = new ResponseWrapper(httpResponse);

        // Proceed with the filter chain first
        chain.doFilter(request, responseWrapper);

        // Now, check if the content type is text-based (after the chain has processed the request)
        String contentType = responseWrapper.getContentType();
        if (contentType != null && (contentType.startsWith("text") || contentType.contains("json"))) {
            String originalContent = responseWrapper.getOutput();

            System.out.println("I'M HERE");
            ServletContext context = httpRequest.getServletContext();
            String prelude = (String) context.getAttribute("prelude");
            String coda = (String) context.getAttribute("coda");

            String modifiedContent = prelude + originalContent + coda;

            httpResponse.getWriter().write(modifiedContent);
            System.out.println("I'M HERE x2");
        } else {
            httpResponse.getWriter().write(responseWrapper.getOutput());
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization code, if needed
    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
