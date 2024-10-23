import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Get the ServletContext from the event
        ServletContext context = event.getServletContext();

        // Read context parameters
        String prelude = context.getInitParameter("prelude");
        String coda = context.getInitParameter("coda");

        // Store the parameters as attributes with application scope
        context.setAttribute("prelude", prelude);
        context.setAttribute("coda", coda);

        // Optional: Log that the attributes have been set
        context.log("Application context initialized. Prelude and Coda set.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Optional: Clean up resources here when the application is stopped
        event.getServletContext().log("Application context destroyed.");
    }
}
