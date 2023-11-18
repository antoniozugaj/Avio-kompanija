package org.foi.nwtis.azugaj.aplikacija_5.web;


import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.azugaj.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.azugaj.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.azugaj.aplikacija_5.rest.RestKlijentAerodroma;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
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
import jakarta.ws.rs.PathParam;
import jakarta.xml.ws.WebServiceRef;

/**
 *
 * @author Antonio Žugaj
 * 
 *         Klasa ua upravljanje stranicama /aerodromi/
 */
@Controller
@Path("/aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/azugaj_aplikacija_4/aerodromi?wsdl")
  private Aerodromi service;

  @WebServiceRef(wsdlLocation = "http://localhost:8080/azugaj_aplikacija_4/meteo?wsdl")
  private Meteo service2;

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

  /**
   * Metoda koja prikazuje stranicu index
   */
  @GET
  @Path("/")
  @View("index.jsp")
  public void index() {
    dajZaglavlje();
  }

  /**
   * Metoda koja prikazuje stranicu index
   */
  @GET
  @Path("pocetak")
  @View("aerodromiGlavni.jsp")
  public void pocetak() {
    dajZaglavlje();
  }

  @GET
  @Path("aerodromiFilter")
  @View("aerodromiFilter.jsp")
  public void aerodromiFilter() {
    dajZaglavlje();
  }

  /**
   * Metoda koja prikazuje sve aerodrome
   * 
   * @param odBroja - indeks od kojeg se redovi prikazuju
   * @param broj - broj redova po stranici
   */
  @POST
  @Path("svi")
  @View("aerodromi.jsp")
  public void getAerodromi(@FormParam("aerodrom") String aerodrom,
      @FormParam("drzava") String drzava, @FormParam("naziv") String naziv,
      @FormParam("odBroja") String odBroja, @FormParam("broj") String broj) {
    dajZaglavlje();

    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    var aerodromi =
        RestKlijentAerodroma.dohvatiAerodromeFilteri(konfig, drzava, naziv, odBroja, broj);
    model.put("aerodromi", aerodromi);
    // TODO WebSocket
  }

  /**
   * Metoda koja prikazuje podatke o aerodromu
   * 
   * @param icao - icao aerodroma
   */
  @GET
  @Path("/{icao}")
  @View("aerodrom.jsp")
  public void getAerodrom(@PathParam("icao") String icao) {
    dajZaglavlje();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");

    var port = service2.getWsMeteoPort();

    Aerodrom aerodrom = RestKlijentAerodroma.dohvatiAerodrom(konfig, icao);
    model.put("aerodrom", aerodrom);

    var meteo = port.dajMeteo(icao);
    model.put("meteo", meteo);
  }

  @GET
  @Path("aerodromiSpremanje")
  @View("aerodromSpremanje.jsp")
  public void getZaSpremanje() {
    dajZaglavlje();
    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var port = service.getWsAerodromiPort();
    var aerodromi = port.dajAerodromeZaLetove(korisnik, lozinka);

    var podaci = port.dohvatiStatus(aerodromi);
    model.put("podaci", podaci);
  }

  @GET
  @Path("aerodromiSpremanje/aktiviraj/{icao}")
  @View("aerdromiAktiviraj.jsp")
  public void getAktiviraj(@PathParam("icao") String icao) {
    dajZaglavlje();
    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var port = service.getWsAerodromiPort();
    var podaci = port.aktivirajAerodromZaLetov(korisnik, lozinka, icao);

    if (podaci) {
      model.put("podaci", "Aerodrom aktiviran");
    } else {
      model.put("podaci", "Dogodila se greška pri aktivaciji");
    }

  }

  @GET
  @Path("aerodromiSpremanje/pauziraj/{icao}")
  @View("aerdromiPauziraj.jsp")
  public void getPauziraj(@PathParam("icao") String icao) {
    dajZaglavlje();
    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");

    var port = service.getWsAerodromiPort();
    var podaci = port.pauzirajAerodromZaLetov(korisnik, lozinka, icao);

    if (podaci) {
      model.put("podaci", "Aerodrom Pauziran");
    } else {
      model.put("podaci", "Dogodila se greška pri pauziranju");
    }

  }

  @GET
  @Path("aerodromiUdaljenostFilter")
  @View("aerdromiUdaljenostFilter.jsp")
  public void getUdaljenostFilter() {
    dajZaglavlje();
  }

  /**
   * Metoda koja prikazuje udaljenosti izmedu dva aerodroma
   * 
   * @param icaoOd - pocetni aerodorm
   * @param icaoDo - krajnji aerodrom
   */
  @POST
  @Path("aerodromiUdaljenosti")
  @View("aerdromiUdaljenosti.jsp")
  public void getAerodromiUdaljenost(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    dajZaglavlje();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    var udaljenosti = RestKlijentAerodroma.dohvatiUdaljenosti(konfig, icaoOd, icaoDo);
    model.put("udaljenosti", udaljenosti);
    float ukupna = 0;

    for (Udaljenost ud : udaljenosti) {
      ukupna += ud.getKm();
    }
    model.put("ukupna", ukupna);
  }

  @GET
  @Path("aerodromiIzracunFilter")
  @View("aerodromIzracunFilter.jsp")
  public void getIzracunFilter() {
    dajZaglavlje();
  }

  /**
   * Metoda koja prikazuje udaljenosti izmedu dva aerodroma
   * 
   * @param icaoOd - pocetni aerodorm
   * @param icaoDo - krajnji aerodrom
   */
  @POST
  @Path("aerodromiIzracun")
  @View("aerodromIzracun.jsp")
  public void getAerodromiUdaljenostIzracun(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    dajZaglavlje();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    var udaljenost = RestKlijentAerodroma.dohvatiUdaljenostiIzracun(konfig, icaoOd, icaoDo);
    model.put("udaljenost", udaljenost);
  }

  @GET
  @Path("aerodromiUdaljenostiIcaoFilter")
  @View("aerdromiUdaljenostiIcaoFilter.jsp")
  public void getIzracunIcaoFilter() {
    dajZaglavlje();
  }

  @POST
  @Path("aerodromiUdaljenostiIcao")
  @View("aerdromiUdaljenostiIcaojsp.jsp")
  public void getAerodromiUdaljenostIcao(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    dajZaglavlje();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    var udaljenost = RestKlijentAerodroma.dohvatiUdaljenostiIcao(konfig, icaoOd, icaoDo);
    model.put("udaljenost", udaljenost);
  }

  @GET
  @Path("aerodromiUdaljenostiKmFilter")
  @View("aerdromiUdaljenostiKmFilter.jsp")
  public void getIzracunKmFilter() {
    dajZaglavlje();
  }

  @POST
  @Path("aerodromiUdaljenostiKm")
  @View("aerdromiUdaljenostiKm.jsp")
  public void getAerodromiUdaljenostKm(@FormParam("icao") String icao,
      @FormParam("drzava") String drzava, @FormParam("km") String km) {
    dajZaglavlje();
    Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfig");
    var udaljenost = RestKlijentAerodroma.dohvatiUdaljenostiKm(konfig, icao, km, drzava);
    model.put("udaljenost", udaljenost);
  }



}
