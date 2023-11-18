package org.foi.nwtis.podaci;

import lombok.Getter;
import lombok.Setter;

public class UdaljenostDoAerodroma {


  @Getter
  @Setter
  private String icao;
  @Getter
  @Setter
  private Udaljenost udaljenost;

  public UdaljenostDoAerodroma(String aerodrom, Udaljenost u) {
    this.icao = aerodrom;
    this.udaljenost = u;
  }

}
