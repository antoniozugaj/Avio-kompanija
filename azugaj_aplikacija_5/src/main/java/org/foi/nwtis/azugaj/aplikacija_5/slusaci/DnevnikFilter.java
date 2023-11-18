package org.foi.nwtis.azugaj.aplikacija_5.slusaci;

import java.io.IOException;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Dnevnik;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

@WebFilter("/*")
public class DnevnikFilter implements Filter {

  @Inject
  ServletContext context;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest zahtjev = (HttpServletRequest) request;

    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    String url = konfig.dajPostavku("adresa.ap2");
    String akcija = zahtjev.getPathInfo();
    Dnevnik novi = new Dnevnik(null, akcija, "AP5");

    Client client = ClientBuilder.newClient();

    WebTarget resource = client.target(url + "dnevnik/");

    Invocation.Builder req = resource.request(MediaType.APPLICATION_JSON);

    req.post(Entity.entity(novi, MediaType.APPLICATION_JSON));

    chain.doFilter(request, response);
  }

}
