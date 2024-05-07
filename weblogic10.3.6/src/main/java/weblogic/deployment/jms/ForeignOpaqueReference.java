package weblogic.deployment.jms;

import java.io.IOException;
import java.io.Serializable;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.PropertyBean;
import weblogic.jndi.OpaqueReference;
import weblogic.kernel.KernelStatus;
import weblogic.management.EncryptionHelper;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;

public class ForeignOpaqueReference implements ForeignOpaqueTag, OpaqueReference, Serializable {
   static final long serialVersionUID = 4404892619941441265L;
   private static final transient AbstractSubject KERNEL_ID = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private boolean isFactory = false;
   private String username;
   private String password;
   private String connectionHealthChecking = "enabled";
   private Hashtable jndiEnvironment;
   private String remoteJNDIName;
   private static String AQJMS_ICF = "oracle.jms.AQjmsInitialContextFactory";
   private static String AQJMS_QPREFIX = "Queues/";
   private static String AQJMS_TPREFIX = "Topics/";
   private transient Object cachedReferent;

   public ForeignOpaqueReference() {
   }

   public ForeignOpaqueReference(ForeignServerBean var1, ForeignJNDIObjectBean var2) {
      this.remoteJNDIName = var2.getRemoteJNDIName();
      if (var2 instanceof ForeignConnectionFactoryBean) {
         this.isFactory = true;
         this.username = ((ForeignConnectionFactoryBean)var2).getUsername();
         this.password = ((ForeignConnectionFactoryBean)var2).getPassword();
         this.connectionHealthChecking = ((ForeignConnectionFactoryBean)var2).getConnectionHealthChecking();
      }

      PropertyBean[] var3 = var1.getJNDIProperties();
      if (var3 != null && var3.length != 0) {
         this.jndiEnvironment = new Hashtable(var3.length);

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.jndiEnvironment.put(var3[var4].getKey(), var3[var4].getValue());
         }
      }

      if (this.jndiEnvironment == null) {
         this.jndiEnvironment = new Hashtable(3);
      }

      String var9 = var1.getInitialContextFactory();
      if (var9 != null && var9.trim().length() != 0 && !var9.equals("weblogic.jndi.WLInitialContextFactory")) {
         this.jndiEnvironment.put("java.naming.factory.initial", var9);
      }

      String var5 = var1.getConnectionURL();
      if (var5 != null && var5.trim().length() != 0) {
         this.jndiEnvironment.put("java.naming.provider.url", var5);
      }

      byte[] var6 = var1.getJNDIPropertiesCredentialEncrypted();
      if (var6 != null) {
         AbstractSubject var7 = (AbstractSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
         String var8 = new String(EncryptionHelper.decrypt(var6, (AuthenticatedSubject)var7));
         EncryptionHelper.clear(var6);
         if (var8.trim().length() > 0) {
            this.jndiEnvironment.put("java.naming.security.credentials", var8);
         }
      }

      if (this.jndiEnvironment.size() == 0) {
         this.jndiEnvironment = null;
      }

   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      AbstractSubject var3 = SubjectManager.getSubjectManager().getCurrentSubject(KERNEL_ID);
      InitialContext var4;
      if (this.jndiEnvironment == null) {
         var4 = new InitialContext();
      } else {
         if (this.jndiEnvironment.get("java.naming.factory.initial") == null) {
            this.jndiEnvironment.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
         }

         var4 = new InitialContext(this.jndiEnvironment);
      }

      AbstractSubject var6 = SubjectManager.getSubjectManager().getCurrentSubject(KERNEL_ID);
      if (KernelStatus.isServer()) {
         String var7 = SubjectUtils.getUsername(var6.getSubject());
         String var8 = SubjectUtils.getUsername(var3.getSubject());
         if (WLSPrincipals.isKernelUsername(var7)) {
            if (WLSPrincipals.isKernelUsername(var8)) {
               var6 = SubjectManager.getSubjectManager().getAnonymousSubject();
            } else {
               var6 = var3;
            }
         }

         String var9 = null;
         if (this.jndiEnvironment != null && this.jndiEnvironment.get("java.naming.security.principal") == null) {
            var9 = (String)this.jndiEnvironment.get("java.naming.provider.url");
            if (var9 != null && this.jndiEnvironment.get("java.naming.factory.initial") != null && ((String)this.jndiEnvironment.get("java.naming.factory.initial")).indexOf("weblogic") != -1) {
               try {
                  if (RemoteDomainSecurityHelper.isRemoteDomain(var9)) {
                     var6 = SubjectManager.getSubjectManager().getAnonymousSubject();
                  }
               } catch (IOException var17) {
                  throw new NamingException(var17.getMessage());
               }
            }
         }
      }

      SubjectManager.getSubjectManager().pushSubject(KERNEL_ID, var6);

      Object var5;
      try {
         if (this.jndiEnvironment == null || !AQJMS_ICF.equals(this.jndiEnvironment.get("java.naming.factory.initial")) || this.remoteJNDIName == null || !this.remoteJNDIName.startsWith(AQJMS_QPREFIX) && !this.remoteJNDIName.startsWith(AQJMS_TPREFIX)) {
            var5 = var4.lookup(this.remoteJNDIName);
         } else {
            synchronized(this) {
               if (this.cachedReferent == null) {
                  this.cachedReferent = var4.lookup(this.remoteJNDIName);
               }
            }

            var5 = this.cachedReferent;
         }
      } finally {
         SubjectManager.getSubjectManager().popSubject(KERNEL_ID);
         var4.close();
      }

      return var5;
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

   public boolean isFactory() {
      return this.isFactory;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public String getConnectionHealthChecking() {
      return this.connectionHealthChecking;
   }

   public Hashtable getJNDIEnvironment() {
      return this.jndiEnvironment;
   }

   public String getRemoteJNDIName() {
      return this.remoteJNDIName;
   }
}
