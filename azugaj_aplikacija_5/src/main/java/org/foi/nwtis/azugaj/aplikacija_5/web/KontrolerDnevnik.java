package org.foi.nwtis.azugaj.aplikacija_5.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.azugaj.aplikacija_5.rest.RestKlijentDnevnik;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 *
 * @author Antonio Žugaj
 * 
 *         Klasa za upravljanje stranicama dnevnika
 */
@Controller
@Path("/dnevnik")
@RequestScoped
public class KontrolerDnevnik {

  @Inject
  private Models model;

  @Inject
  ServletContext context;

  /**
   * Metoda koja stvara podatke za zaglavlje
   */
  private void dajZaglavlje() {
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    String autorImeString = konfig.dajPostavku("autor.ime");
    String autorPrezimeString = konfig.dajPostavku("autor.prezime");
    model.put("autor", autorImeString + " " + autorPrezimeString);
    String stranice = konfig.dajPostavku("stranica.brojRedova");
    model.put("broj", stranice);
    String predmet = konfig.dajPostavku("autor.predmet");
    String godina = konfig.dajPostavku("aplikacija.godina");
    String verzija = konfig.dajPostavku("aplikacija.verzija");
    model.put("opis", predmet + " " + godina + " " + verzija);

  }


  @GET
  @Path("/filter")
  @View("dnevnikFilter.jsp")
  public void getDnevnikFilter() {
    dajZaglavlje();
  }

  @POST
  @Path("/ispis")
  @View("dnevnik.jsp")
  public void getDnevnik(@FormParam("vrsta") String vrsta, @FormParam("odBroja") String odBroja,
      @FormParam("broj") String broj) {
    dajZaglavlje();


    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var podaci = RestKlijentDnevnik.dohvatiDnevnik(konfig, odBroja, broj, vrsta);
    model.put("podaci", podaci);
  }
}
