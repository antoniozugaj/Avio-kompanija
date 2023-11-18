package org.foi.nwtis.azugaj.aplikacija_3.slusaci;

import java.io.File;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.foi.nwtis.azugaj.aplikacija_3.dretve.DretvaPreuzimanja;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja pri pokretanju servleta ucitava podatke iz xml datoteke u context aplikacije.
 *
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  static public Konfiguracija konfig = null;
  public DretvaPreuzimanja dretva = null;

  /**
   * Metoda koja ucitava podatke iz xml-a i postavlja ih u context.
   */
  @Override
  public void contextInitialized(ServletContextEvent contextEvent) {

    ServletContext con = contextEvent.getServletContext();
    String putanjaDatoteka =
        con.getRealPath("/WEB-INF") + File.separator + con.getInitParameter("konfiguracija");

    try {
      konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanjaDatoteka);
    } catch (NeispravnaKonfiguracija e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    con.setAttribute("konfig", konfig);



    dretva = new DretvaPreuzimanja(konfig, ds);
    dretva.start();

    ServletContextListener.super.contextInitialized(contextEvent);
  }

  /**
   * Metoda koja mice upisane xml podatke iz contexta.
   */
  @Override
  public void contextDestroyed(ServletContextEvent contextEvent) {
    ServletContext con = contextEvent.getServletContext();
    con.removeAttribute("konfig");
    dretva.interrupt();
    ServletContextListener.super.contextDestroyed(contextEvent);
  }


}
