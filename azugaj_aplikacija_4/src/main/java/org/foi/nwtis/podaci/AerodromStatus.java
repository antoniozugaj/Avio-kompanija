package org.foi.nwtis.podaci;

import lombok.Getter;
import lombok.Setter;

public class AerodromStatus {

  @Getter
  @Setter
  private Aerodrom aerodrom;
  @Getter
  @Setter
  private String status;

  public AerodromStatus(Aerodrom aer, String stat) {
    aerodrom = aer;
    status = stat;
  }
}
