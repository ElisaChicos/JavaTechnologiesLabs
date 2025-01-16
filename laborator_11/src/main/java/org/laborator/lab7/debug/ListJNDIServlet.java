package org.laborator.lab7.debug;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

@WebServlet("/listJNDI")
public class ListJNDIServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        try {
            // Initialize JNDI Context
            Context context = new InitialContext();

            // Start from java:comp/env to list all resources
            Context envContext = (Context) context.lookup("java:comp/env");
            resp.getWriter().println("Available JNDI Resources in java:comp/env:");
            listJNDIResources(envContext, resp);
        } catch (Exception e) {
            resp.getWriter().println("Error while listing JNDI resources: " + e.getMessage());
            e.printStackTrace(resp.getWriter());
        }
    }

    private void listJNDIResources(Context context, HttpServletResponse resp) throws Exception {
        NamingEnumeration<NameClassPair> list = context.list("");
        while (list.hasMore()) {
            NameClassPair nc = list.next();
            resp.getWriter().println("Name: " + nc.getName() + " - Type: " + nc.getClassName());

            // Recursively list subcontexts
            Object obj = context.lookup(nc.getName());
            if (obj instanceof Context) {
                listJNDIResources((Context) obj, resp);
            }
        }
    }
}
