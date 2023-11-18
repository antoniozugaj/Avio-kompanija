package org.foi.nwtis.azugaj.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Udaljenost;
import org.foi.nwtis.podaci.UdaljenostDoAerodroma;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja obraduje pozive za endpoint
 *
 */
public class RestKlijentAerodroma {

  public static List<Aerodrom> dohvatiAerodromeFilteri(Konfiguracija konfig, String traziDrzavu,
      String traziNaziv, String odBroja, String broj) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    Aerodrom[] json_aerodromi;
    List<Aerodrom> aerodromi;

    WebTarget resource = null;

    if (odBroja.length() >= 1 && broj.length() >= 1 && traziDrzavu.length() >= 1
        && traziNaziv.length() >= 1) {
      resource =
          client.target(url + "aerodromi/").queryParam("odBroja", odBroja).queryParam("broj", broj)
              .queryParam("traziNaziv", traziNaziv).queryParam("traziDrzavu", traziDrzavu);
    } else if (odBroja.length() >= 1 && traziDrzavu.length() >= 1 && traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja)
          .queryParam("traziNaziv", traziNaziv).queryParam("traziDrzavu", traziDrzavu);
    } else if (broj.length() >= 1 && traziDrzavu.length() >= 1 && traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("broj", broj)
          .queryParam("traziNaziv", traziNaziv).queryParam("traziDrzavu", traziDrzavu);
    } else if (odBroja.length() >= 1 && broj.length() >= 1 && traziDrzavu.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja)
          .queryParam("broj", broj).queryParam("traziDrzavu", traziDrzavu);
    } else if (odBroja.length() >= 1 && traziDrzavu.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja)
          .queryParam("traziDrzavu", traziDrzavu);
    } else if (broj.length() >= 1 && traziDrzavu.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("broj", broj)
          .queryParam("traziDrzavu", traziDrzavu);
    } else if (odBroja.length() >= 1 && broj.length() >= 1 && traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja)
          .queryParam("broj", broj).queryParam("traziNaziv", traziNaziv);
    } else if (odBroja.length() >= 1 && traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja)
          .queryParam("traziNaziv", traziNaziv);
    } else if (broj.length() >= 1 && traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("broj", broj).queryParam("traziNaziv",
          traziNaziv);
    } else if (broj.length() >= 1 && odBroja.length() >= 1) {
      resource =
          client.target(url + "aerodromi/").queryParam("broj", broj).queryParam("odBroja", odBroja);
    } else if (broj.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("broj", broj);
    } else if (odBroja.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("odBroja", odBroja);
    } else if (broj.length() >= 1 && odBroja.length() >= 1) {
      resource =
          client.target(url + "aerodromi/").queryParam("broj", broj).queryParam("odBroja", odBroja);
    } else if (traziDrzavu.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("traziDrzavu", traziDrzavu);
    } else if (traziNaziv.length() >= 1) {
      resource = client.target(url + "aerodromi/").queryParam("traziNaziv", traziNaziv);
    } else {
      resource = client.target(url + "aerodromi/");
    }

    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return new ArrayList<>();
    }
    Gson gson = new Gson();
    json_aerodromi = gson.fromJson(request.get(String.class), Aerodrom[].class);

    if (json_aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_aerodromi);
    }
    client.close();
    return aerodromi;
  }

  public static Aerodrom dohvatiAerodrom(Konfiguracija konfig, String icao) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    Aerodrom json_aerodrom;
    WebTarget resource = client.target(url + "aerodromi/" + icao);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_aerodrom = gson.fromJson(request.get(String.class), Aerodrom.class);

    if (json_aerodrom == null) {
      client.close();
      return null;
    } else {
      client.close();
      return json_aerodrom;
    }
  }

  public static List<Udaljenost> dohvatiUdaljenosti(Konfiguracija konfig, String icaoOd,
      String icaoDo) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    Udaljenost[] json_udaljenosti;
    List<Udaljenost> udaljenost;
    WebTarget resource = client.target(url + "aerodromi/" + icaoOd + "/" + icaoDo);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_udaljenosti = gson.fromJson(request.get(String.class), Udaljenost[].class);

    if (json_udaljenosti == null) {
      udaljenost = new ArrayList<>();
    } else {
      udaljenost = Arrays.asList(json_udaljenosti);
    }
    client.close();
    return udaljenost;

  }

  public static String dohvatiUdaljenostiIzracun(Konfiguracija konfig, String icaoOd,
      String icaoDo) {
    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    String json_udaljenost;
    WebTarget resource = client.target(url + "aerodromi/" + icaoOd + "/izracunaj/" + icaoDo);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      client.close();
      return null;
    }

    Gson gson = new Gson();
    json_udaljenost = gson.fromJson(request.get(String.class), String.class);

    if (json_udaljenost == null) {
      client.close();
      return null;
    } else {
      client.close();
      return json_udaljenost;
    }
  }

  public static List<UdaljenostDoAerodroma> dohvatiUdaljenostiIcao(Konfiguracija konfig,
      String icaoOd, String icaoDo) {
    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    UdaljenostDoAerodroma[] json_udaljenosti;
    List<UdaljenostDoAerodroma> udaljenost;
    WebTarget resource = client.target(url + "aerodromi/" + icaoOd + "/udaljenost1/" + icaoDo);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_udaljenosti = gson.fromJson(request.get(String.class), UdaljenostDoAerodroma[].class);

    if (json_udaljenosti == null) {
      udaljenost = new ArrayList<>();
    } else {
      udaljenost = Arrays.asList(json_udaljenosti);
    }
    client.close();
    return udaljenost;
  }

  public static List<UdaljenostDoAerodroma> dohvatiUdaljenostiKm(Konfiguracija konfig, String icao,
      String km, String drzava) {
    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    UdaljenostDoAerodroma[] json_udaljenosti;
    List<UdaljenostDoAerodroma> udaljenost;
    WebTarget resource = client.target(url + "aerodromi/" + icao + "/udaljenost2")
        .queryParam("km", km).queryParam("drzava", drzava);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_udaljenosti = gson.fromJson(request.get(String.class), UdaljenostDoAerodroma[].class);

    if (json_udaljenosti == null) {
      udaljenost = new ArrayList<>();
    } else {
      udaljenost = Arrays.asList(json_udaljenosti);
    }
    client.close();
    return udaljenost;
  }

}
