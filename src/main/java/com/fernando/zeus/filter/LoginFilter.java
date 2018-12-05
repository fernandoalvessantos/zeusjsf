package com.fernando.zeus.filter;

import com.fernando.zeus.control.LoginControl;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class LoginFilter implements Filter {

    @Inject
    private LoginControl loginControl;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(loginControl == null || loginControl.getUsuario() == null){
            String contextPath = ((HttpServletRequest) servletRequest)
                    .getContextPath();
            //Redirecionamos o usuário imediatamente
            //para a página de login.xhtml
            ((HttpServletResponse) servletResponse).sendRedirect
                    (contextPath + "/login.xhtml");
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
