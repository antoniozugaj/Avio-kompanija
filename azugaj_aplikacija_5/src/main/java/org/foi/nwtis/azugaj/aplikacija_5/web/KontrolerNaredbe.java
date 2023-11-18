package org.foi.nwtis.azugaj.aplikacija_5.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.azugaj.aplikacija_5.rest.RestKlijentNaredbe;
import org.foi.nwtis.podaci.StatusUdaljenosti;
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
 *         Klasa ua upravljanje stranicama /meteo/
 */
@Controller
@Path("/naredbe")
@RequestScoped
public class KontrolerNaredbe {

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

  /**
   * Metoda koja prikazuje stranicu index
   */
  @GET
  @Path("pocetak")
  @View("naredbeServera.jsp")
  public void pocetak() {
    dajZaglavlje();
  }

  /**
   * Metoda oja prikazuje meteo podatke aerodroma
   * 
   * @param icao - icao aerodroma
   */
  @POST
  @Path("/naredba")
  @View("naredba.jsp")
  public void komanda(@FormParam("naredba") String naredba) {
    dajZaglavlje();

    Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////

    var konfig = (Konfiguracija) context.getAttribute("konfig");
    switch (naredba) {
      case "INIT": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiKomandu(konfig, "INIT");
        model.put("podaci", podaci);
        break;
      }
      case "KRAJ": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiKomandu(konfig, "KRAJ");
        model.put("podaci", podaci);
        break;
      }
      case "STATUS": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiKomandu(konfig, "");
        model.put("podaci", podaci);
        break;
      }
      case "PAUZA": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiKomandu(konfig, "PAUZA");
        model.put("podaci", podaci);
        break;
      }
      case "INFO DA": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiInfo(konfig, "DA");
        model.put("podaci", podaci);
        break;
      }
      case "INFO NE": {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        var podaci = RestKlijentNaredbe.posaljiInfo(konfig, "NE");
        model.put("podaci", podaci);
        break;
      }
      default: {
        Logger.getGlobal().log(Level.SEVERE, "+++++++++++" + naredba);////////////////////
        StatusUdaljenosti podaci = new StatusUdaljenosti(400, "Pogreska u zahtjevu");
        model.put("podaci", podaci);
      }
    }
  }
}
