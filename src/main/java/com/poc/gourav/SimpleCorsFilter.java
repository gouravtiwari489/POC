package com.poc.gourav;

import eu.bitwalker.useragentutils.UserAgent;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Getter
public class SimpleCorsFilter implements Filter {

  private String remoteAddress;
  private Integer remotePort;
  private String remoteBrowser;

  public SimpleCorsFilter() {}

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse response = (HttpServletResponse) res;

    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader(
        "Access-Control-Allow-Headers",
        "x-requested-with, content-type, authorization,responseType");
    HttpServletRequest request = (HttpServletRequest) req;

    remoteAddress = req.getRemoteAddr();
    remotePort = req.getRemotePort();
    UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    remoteBrowser = userAgent.getBrowser().getName();

    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      chain.doFilter(req, res);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void destroy() {}
}
