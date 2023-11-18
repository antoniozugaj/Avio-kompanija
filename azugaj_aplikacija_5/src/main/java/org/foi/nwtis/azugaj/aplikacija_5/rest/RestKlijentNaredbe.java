package org.foi.nwtis.azugaj.aplikacija_5.rest;

import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.StatusUdaljenosti;
import com.google.gson.Gson;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

public class RestKlijentNaredbe {

  public static StatusUdaljenosti posaljiKomandu(Konfiguracija konfig, String komanda) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    StatusUdaljenosti json_status;
    WebTarget resource = client.target(url + "nadzor/" + komanda);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    var odgovor = request.get(String.class);
    if (odgovor.isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_status = gson.fromJson(odgovor, StatusUdaljenosti.class);

    client.close();
    return json_status;
  }

  public static StatusUdaljenosti posaljiInfo(Konfiguracija konfig, String komanda) {

    Client client = ClientBuilder.newClient();

    String url = konfig.dajPostavku("adresa.ap2");
    StatusUdaljenosti json_status;
    WebTarget resource = client.target(url + "nadzor/INFO/" + komanda);
    Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

    var odgovor = request.get(String.class);
    if (odgovor.isEmpty()) {
      return null;
    }
    Gson gson = new Gson();
    json_status = gson.fromJson(odgovor, StatusUdaljenosti.class);

    client.close();
    return json_status;
  }
}
