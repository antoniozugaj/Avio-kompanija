package org.foi.nwtis.azugaj.aplikacija_2.rest;

import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * 
 * @author Antonio Žugaj
 * 
 *         Klasa koja definira /api/putanju
 */
@ApplicationPath("api")
public class RestServis extends Application {

  private void addRestResourceClasses(Set<Class<?>> res) {
    res.add(org.foi.nwtis.azugaj.aplikacija_2.rest.RestAerodromi.class);
    res.add(org.foi.nwtis.azugaj.aplikacija_2.rest.RestNadzor.class);
    res.add(org.foi.nwtis.azugaj.aplikacija_2.rest.RestDnevnik.class);
  }
}
