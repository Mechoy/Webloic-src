package weblogic.jndi.internal;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.jndi.OpaqueReference;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;

public class ForeignOpaqueReference implements OpaqueReference, Serializable {
   static final long serialVersionUID = 4404892619941441265L;
   private Hashtable jndiEnvironment;
   private String remoteJNDIName;
   private static ClearOrEncryptedService ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());

   public ForeignOpaqueReference() {
   }

   public ForeignOpaqueReference(String var1, Hashtable var2) {
      this.remoteJNDIName = var1;
      this.jndiEnvironment = var2;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      InitialContext var3;
      if (this.jndiEnvironment == null) {
         var3 = new InitialContext();
      } else {
         Hashtable var4 = this.decrypt();
         var3 = new InitialContext(var4);
      }

      Object var8;
      try {
         var8 = var3.lookup(this.remoteJNDIName);
      } finally {
         var3.close();
      }

      return var8;
   }

   private Hashtable decrypt() {
      Hashtable var1 = (Hashtable)this.jndiEnvironment.clone();
      String var2 = (String)var1.get("java.naming.security.principal");
      if (var2 != null && var2.trim().length() != 0) {
         var1.put("java.naming.security.principal", ces.decrypt(var2));
      }

      String var3 = (String)var1.get("java.naming.security.credentials");
      if (var3 != null && var3.trim().length() != 0) {
         var1.put("java.naming.security.credentials", ces.decrypt(var3));
      }

      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ForeignOpaqueReference: target=\"");
      var1.append(this.remoteJNDIName);
      var1.append('"');
      if (this.jndiEnvironment != null) {
         Enumeration var2 = this.jndiEnvironment.keys();

         while(var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            String var4 = (String)this.jndiEnvironment.get(var3);
            var1.append(' ');
            var1.append(var3);
            var1.append('=');
            var1.append(var4);
         }
      }

      return var1.toString();
   }
}
