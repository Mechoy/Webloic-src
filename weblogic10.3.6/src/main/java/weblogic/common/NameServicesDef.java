package weblogic.common;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;

/** @deprecated */
public interface NameServicesDef {
   /** @deprecated */
   Context getInitialContext() throws NamingException;

   /** @deprecated */
   Context getInitialContext(Hashtable var1) throws NamingException;

   void private_setT3Client(T3Client var1);
}
