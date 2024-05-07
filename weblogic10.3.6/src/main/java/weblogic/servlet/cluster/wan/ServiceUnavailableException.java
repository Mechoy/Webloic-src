package weblogic.servlet.cluster.wan;

import java.rmi.NoSuchObjectException;

public class ServiceUnavailableException extends NoSuchObjectException {
   public ServiceUnavailableException(String var1) {
      super(var1);
   }
}
