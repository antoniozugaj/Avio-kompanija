package org.foi.nwtis.azugaj.aplikacija_2.slusaci;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja onemogucuje pristup ako AP1 nije aktivan
 *
 */
@WebFilter("/api/*")
public class StatusFilter implements Filter {

  @Inject
  ServletContext context;

  /**
   * Metoda za filtriranje zahtjeva
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest zahtjev = (HttpServletRequest) request;

    if (!zahtjev.getMethod().contains("POST")) {
      if (!zahtjev.getPathInfo().contains("nadzor")) {
        String poruka = posaljiZahtjev("STATUS");
        var odvojeno = poruka.split(" ");

        if (odvojeno.length == 2) {
          if (odvojeno[1].equals("1"))
            chain.doFilter(request, response);
        }
      } else {
        chain.doFilter(request, response);
      }
    } else {
      chain.doFilter(request, response);
    }

  }


  private String posaljiZahtjev(String zahtjev) {
    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var posluzitelj = konfig.dajPostavku("posluziteljUdaljenosti");
    var mreznaVrata = konfig.dajPostavku("mreznaVrata");
    var poruka = new StringBuilder();

    try {
      var mreznaUticnica = new Socket(posluzitelj, Integer.parseInt(mreznaVrata));
      mreznaUticnica.setSoTimeout(3000);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));


      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();


      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Pogreska Klijent: " + e.getMessage());
    }
    return poruka.toString();
  }

}
