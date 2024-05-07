package weblogic.wsee.jaxrpc;

import javax.xml.soap.SOAPElement;
import weblogic.wsee.util.Verbose;

public class NoOpProvider implements Provider {
   private static final boolean verbose = Verbose.isVerbose(NoOpProvider.class);

   public SOAPElement invoke(SOAPElement var1) {
      throw new Error("This method should not be called");
   }
}
