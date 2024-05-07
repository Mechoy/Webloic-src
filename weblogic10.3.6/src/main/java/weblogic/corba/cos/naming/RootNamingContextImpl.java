package weblogic.corba.cos.naming;

import java.io.IOException;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.rmi.internal.OIDManager;

public class RootNamingContextImpl extends NamingContextImpl {
   private static final RootNamingContextImpl root = new RootNamingContextImpl();

   public static final NamingContextImpl getRootNamingContext() {
      return root;
   }

   public IOR getIOR() throws IOException {
      return IIOPReplacer.getIIOPReplacer().replaceRemote(OIDManager.getInstance().getServerReference(getRootNamingContext()).getStubReference());
   }

   private RootNamingContextImpl() {
   }
}
