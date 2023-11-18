package org.foi.nwtis.azugaj.aplikacija_2.rest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.podaci.StatusUdaljenosti;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja sadrzi endpoint-ove za /nadzor zahtjeve
 *
 */

@Path("nadzor")
@RequestScoped
public class RestNadzor {

  @Inject
  ServletContext context;

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /**
   * Metoda koja šalje STATUS zahtjev prema AP1
   * 
   * @return odgovor
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajStatus() {

    String poruka = posaljiZahtjev("STATUS");
    var gson = new Gson();
    var odvojeno = poruka.split(" ");
    if (poruka.contains("ERROR")) {
      var status = new StatusUdaljenosti(400, odvojeno[2]);
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

    } else {
      String opis;
      if (odvojeno[1].equals("0"))
        opis = "Posluzitelj je pauziran";
      else {
        opis = "Posluzitelj je aktivan";
      }
      var status = new StatusUdaljenosti(200, opis);
      var res = gson.toJson(status);
      return Response.ok().entity(res).build();
    }
  }

  /**
   * Metoda koja šalje komandu zahtjev prema AP1
   * 
   * @param komanda
   * @return odgovor
   */
  @GET
  @Path("/{komanda}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajKomanda(@PathParam("komanda") String komanda) {

    String poruka = posaljiZahtjev(komanda);

    var gson = new Gson();
    if (poruka.contains("ERROR")) {
      var status = new StatusUdaljenosti(400, poruka);
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

    } else {
      var status = new StatusUdaljenosti(200, poruka);
      var res = gson.toJson(status);
      return Response.ok().entity(res).build();
    }
  }

  /**
   * Metoda koja šalje INFO zahtjev prema AP1
   * 
   * @param vrsta DA/NE
   * @return odgovor
   */
  @GET
  @Path("/INFO/{vrsta}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajInfo(@PathParam("vrsta") String vrsta) {

    String poruka = posaljiZahtjev("INFO " + vrsta);

    var gson = new Gson();
    var odvojeno = poruka.split(" ");
    if (poruka.contains("ERROR")) {
      var status = new StatusUdaljenosti(400, poruka);
      var res = gson.toJson(status);
      return Response.status(Response.Status.BAD_REQUEST).entity(res).build();

    } else {
      var status = new StatusUdaljenosti(200, poruka);
      var res = gson.toJson(status);
      return Response.ok().entity(res).build();
    }
  }



  /**
   * Metoda za slanje zahtjeva prema AP1
   * 
   * @param zahtjev
   * @return
   */
  private String posaljiZahtjev(String zahtjev) {
    var konfig = (Konfiguracija) context.getAttribute("konfig");
    var posluzitelj = konfig.dajPostavku("posluziteljUdaljenosti");
    var mreznaVrata = konfig.dajPostavku("mreznaVrata");
    var poruka = new StringBuilder();

    try {
      var mreznaUticnica = new Socket(posluzitelj, Integer.parseInt(mreznaVrata));
      mreznaUticnica.setSoTimeout(3000);
      var citac = new BufferedReader(
          new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
      var pisac = new BufferedWriter(
          new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));


      pisac.write(zahtjev);
      pisac.flush();
      mreznaUticnica.shutdownOutput();


      while (true) {
        var red = citac.readLine();
        if (red == null)
          break;
        poruka.append(red);
      }
      mreznaUticnica.shutdownInput();
      mreznaUticnica.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Pogreska Klijent: " + e.getMessage());//////////////////// TODO
    }
    return poruka.toString();
  }


}

