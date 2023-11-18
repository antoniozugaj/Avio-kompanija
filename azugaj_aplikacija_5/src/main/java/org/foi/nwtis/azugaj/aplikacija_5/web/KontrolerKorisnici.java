package org.foi.nwtis.azugaj.aplikacija_5.web;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.azugaj.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
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
@Path("/korisnici")
@RequestScoped
public class KontrolerKorisnici {

  @WebServiceRef(wsdlLocation = "http://localhost:8080/azugaj_aplikacija_4/korisnici?wsdl")
  private Korisnici service;

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
  @Path("/pocetna")
  @View("pocetnaKorisnici.jsp")
  public void pocetna() {
    dajZaglavlje();
  }

  @GET
  @Path("/registracija")
  @View("registracija.jsp")
  public void registracijaFilter() {
    dajZaglavlje();
  }

  @POST
  @Path("/registriraj")
  @View("registriraj.jsp")
  public void registracija(@FormParam("korime") String korime, @FormParam("lozinka") String lozinka,
      @FormParam("ime") String ime, @FormParam("prezime") String prezime,
      @FormParam("email") String email) {
    dajZaglavlje();

    org.foi.nwtis.azugaj.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik novi =
        new org.foi.nwtis.azugaj.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik();
    novi.setKorIme(korime);
    novi.setLozinka(lozinka);
    novi.setIme(ime);
    novi.setPrezime(prezime);
    novi.setEmail(email);

    var port = service.getWsKorisniciPort();
    var podaci = port.dodajKorisnika(novi);

    if (podaci) {
      model.put("podaci", "Hvala na registraciji");
    } else {
      model.put("podaci", "Pogreška kod registracije");
    }
  }

  @GET
  @Path("/prijava")
  @View("prijava.jsp")
  public void prijava() {
    dajZaglavlje();
  }

  @POST
  @Path("/login")
  @View("login.jsp")
  public void login(@FormParam("korime") String korime, @FormParam("lozinka") String lozinka) {
    dajZaglavlje();

    var port = service.getWsKorisniciPort();
    var podaci = port.dajKorisnik(korime, lozinka, korime);

    if (podaci != null) {
      request.getSession().setAttribute("korisnik", korime);
      request.getSession().setAttribute("lozinka", lozinka);
      model.put("podaci", "Hvala na prijavi");
    } else {
      model.put("podaci", "Pogrešna prijava! Provjerite ispravnost lozinke i korisničko ime.");
    }
  }

  @GET
  @Path("/korisniciFilter")
  @View("korisniciFilter.jsp")
  public void sviKorisnici() {
    dajZaglavlje();
  }

  @POST
  @Path("/korisnici")
  @View("korisnici.jsp")
  public void getKorisnici(@FormParam("ime") String traziImeKorisnika,
      @FormParam("prezime") String traziPrezimeKorisnika) {
    dajZaglavlje();

    var korisnik = (String) request.getSession().getAttribute("korisnik");
    var lozinka = (String) request.getSession().getAttribute("lozinka");
    var port = service.getWsKorisniciPort();
    var podaci = port.dajKorisnike(korisnik, lozinka, traziImeKorisnika, traziPrezimeKorisnika);
    model.put("podaci", podaci);
  }

}
