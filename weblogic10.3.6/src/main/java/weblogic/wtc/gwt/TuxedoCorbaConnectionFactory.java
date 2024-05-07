package weblogic.wtc.gwt;

import java.io.Serializable;
import weblogic.wtc.jatmi.TPException;

public final class TuxedoCorbaConnectionFactory implements Serializable {
   private static final long serialVersionUID = -1214596436048576404L;
   public static final String JNDI_NAME = "tuxedo.services.TuxedoCorbaConnection";

   public TuxedoCorbaConnection getTuxedoCorbaConnection() throws TPException {
      return new TuxedoCorbaConnection();
   }
}
