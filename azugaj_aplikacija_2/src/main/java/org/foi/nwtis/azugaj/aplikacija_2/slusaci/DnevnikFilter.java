package org.foi.nwtis.azugaj.aplikacija_2.slusaci;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 * Klasa za biljezenje zahtjeva u dnevnik
 * 
 * @author Antonio Žugaj
 *
 */
@WebFilter("/api/*")
public class DnevnikFilter implements Filter {

  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Metoda za spremanje podatka u bazu podataka
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest zahtjev = (HttpServletRequest) request;

    if (zahtjev.getMethod().contains("GET")) {

      String query = "INSERT INTO DNEVNIK (DATUM, AKCIJA, VRSTA) VALUES (CURRENT_TIMESTAMP, ?,?)";

      PreparedStatement stmt = null;
      try (var con = ds.getConnection()) {
        stmt = con.prepareStatement(query);
        stmt.setString(1, zahtjev.getPathInfo());
        stmt.setString(2, "AP2");


        stmt.executeUpdate();
      } catch (SQLException e) {
        e.printStackTrace();
        Logger.getGlobal().log(Level.SEVERE, e.getMessage());
      } finally {
        try {
          if (stmt != null && !stmt.isClosed())
            stmt.close();
        } catch (SQLException e) {
          Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }
      }
    }

    chain.doFilter(request, response);
  }

}
