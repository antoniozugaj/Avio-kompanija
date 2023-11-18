package org.foi.nwtis.azugaj.aplikacija_5.slusaci;

import java.io.File;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
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

  static public Konfiguracija konfig = null;

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


    ServletContextListener.super.contextInitialized(contextEvent);

  }

  /**
   * Metoda koja mice upisane xml podatke iz contexta.
   */
  @Override
  public void contextDestroyed(ServletContextEvent contextEvent) {
    ServletContext con = contextEvent.getServletContext();
    con.removeAttribute("konfig");
    ServletContextListener.super.contextDestroyed(contextEvent);
  }



}
