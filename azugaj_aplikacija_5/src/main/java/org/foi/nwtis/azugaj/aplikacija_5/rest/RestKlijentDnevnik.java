package org.foi.nwtis.azugaj.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.Dnevnik;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentDnevnik {

  public static List<Dnevnik> dohvatiDnevnik(Konfiguracija konfig, String odBroja, String broj,
      String vrsta) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    Dnevnik[] json_dnevnik;
    List<Dnevnik> dnevnik;


    WebTarget resource = null;

    if (odBroja.length() >= 1 && broj.length() >= 1 && vrsta.length() >= 1) {

      resource = client.target(url + "dnevnik").queryParam("vrsta", vrsta)
          .queryParam("odBroja", odBroja).queryParam("broj", broj);

    } else if (odBroja.length() >= 1 && broj.length() >= 1) {

      resource =
          client.target(url + "dnevnik").queryParam("broj", broj).queryParam("odBroja", odBroja);

    } else if (vrsta.length() >= 1 && broj.length() >= 1) {

      resource = client.target(url + "dnevnik").queryParam("vrsta", vrsta).queryParam("broj", broj);

    } else if (vrsta.length() >= 1 && odBroja.length() >= 1) {

      resource =
          client.target(url + "dnevnik").queryParam("vrsta", vrsta).queryParam("odBroja", odBroja);

    } else if (vrsta.length() >= 1) {

      resource = client.target(url + "dnevnik").queryParam("vrsta", vrsta);

    } else if (broj.length() >= 1) {

      resource = client.target(url + "dnevnik").queryParam("broj", broj);
    } else if (odBroja.length() >= 1) {

      resource = client.target(url + "dnevnik").queryParam("odBroja", odBroja);
    } else {
      resource = client.target(url + "dnevnik");
    }
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    if (request.get(String.class).isEmpty()) {
      return new ArrayList<>();
    }
    Gson gson = new Gson();
    json_dnevnik = gson.fromJson(request.get(String.class), Dnevnik[].class);

    if (json_dnevnik == null) {
      dnevnik = new ArrayList<>();
    } else {
      dnevnik = Arrays.asList(json_dnevnik);
    }
    client.close();
    return dnevnik;
  }

}
