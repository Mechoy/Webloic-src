package weblogic.connector.extensions;

import javax.resource.ResourceException;
import javax.resource.spi.BootstrapContext;

public interface ExtendedBootstrapContext extends BootstrapContext {
   void setDiagnosticContextID(String var1);

   String getDiagnosticContextID();

   void setDyeBits(byte var1) throws ResourceException;

   byte getDyeBits() throws ResourceException;

   void complete();
}
