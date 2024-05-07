package weblogic.wsee.mc.api;

import javax.xml.ws.WebServiceException;

public class McPollingTimeoutException extends WebServiceException {
   public McPollingTimeoutException() {
   }

   public McPollingTimeoutException(String var1) {
      super(var1);
   }
}
