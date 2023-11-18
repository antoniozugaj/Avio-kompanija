package org.foi.nwtis.azugaj.aplikacija_5.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.azugaj.aplikacija_4.ws.WsLetovi.endpoint.Letovi;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Antonio Žugaj
 * 
 *         Klasa za upravljanje stranicama dnevnika
 */
@Controller
@Path("/letovi")
@RequestScoped
public class KontrolerLetovi {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/azugaj_aplikacija_4/letovi?wsdl")
  private Letovi service;

  @Inject
  private Models model;

  @Inject
  ServletContext context;

  @Inject
  HttpServletRequest request;

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
  @Path("pocetna")
  @View("letoviGlavni.jsp")
  public void getPocetak() {
    dajZaglavlje();

  }


  @GET
  @Path("letoviIntervalFilter")
  @View("letoviIntervalFilter.jsp")
  public void getLetoviIntervalFilter() {
    dajZaglavlje();

  }


  @POST
  @Path("/letoviInterval")
  @View("letoviInterval.jsp")
  public void getLetoviInterval(@FormParam("icao") String icao,
      @FormParam("datumOd") String datumOd, @FormParam("datumDo") String datumDo,
      @FormParam("odBroja") String odBroja, @FormParam("broj") String broj) {
    dajZaglavlje();
    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var port = service.getWsLetoviPort();

    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var podaci = port.dajPolaskeInterval(korisnik, lozinka, icao, datumOd, datumDo,
        Integer.parseInt(odBroja), Integer.parseInt(broj));
    model.put("podaci", podaci);
  }

  @GET
  @Path("letoviDanFilter")
  @View("letoviDanFilter.jsp")
  public void getLetoviDanFilter() {
    dajZaglavlje();

  }

  @POST
  @Path("/letoviDan")
  @View("letoviDan.jsp")
  public void getLetoviDan(@FormParam("icao") String icao, @FormParam("datum") String datum,
      @FormParam("odBroja") String odBroja, @FormParam("broj") String broj) {
    dajZaglavlje();
    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var port = service.getWsLetoviPort();

    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var podaci = port.dajPolaskeNaDan(korisnik, lozinka, icao, datum, Integer.parseInt(odBroja),
        Integer.parseInt(broj));

    model.put("podaci", podaci);
  }


  @GET
  @Path("letoviDanOSFilter")
  @View("letoviDanOSFilter.jsp")
  public void getLetoviDanOSFilter() {
    dajZaglavlje();

  }


  @POST
  @Path("/letoviDanOS")
  @View("letoviDanOS.jsp")
  public void getLetoviDanOS(@FormParam("icao") String icao, @FormParam("datum") String datum,
      @FormParam("odBroja") String odBroja, @FormParam("broj") String broj) {
    dajZaglavlje();
    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var port = service.getWsLetoviPort();

    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var podaci = port.dajPolaskeNaDanOS(korisnik, lozinka, icao, datum);

    Logger.getGlobal().log(Level.SEVERE, "-----------------" + podaci.size());
    model.put("podaci", podaci);
  }
}
