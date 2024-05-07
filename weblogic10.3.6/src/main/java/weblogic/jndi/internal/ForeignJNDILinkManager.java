package weblogic.jndi.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.jndi.JNDILogger;
import weblogic.management.configuration.ForeignJNDILinkMBean;
import weblogic.management.configuration.ForeignJNDIProviderMBean;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.utils.StackTraceUtils;

public class ForeignJNDILinkManager implements BeanUpdateListener {
   private Hashtable jndiEnvironment;
   private Map local2remote = new HashMap();
   private Hashtable pendingChanges = new Hashtable();
   private InitialContext ic;
   private static ClearOrEncryptedService ces = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());

   public ForeignJNDILinkManager(ForeignJNDIProviderMBean var1, ForeignJNDILinkMBean[] var2, InitialContext var3) {
      this.ic = var3;
      Properties var4 = var1.getProperties();
      if (var4 != null && var4.size() != 0) {
         this.jndiEnvironment = new Hashtable(var4);
      }

      if (this.jndiEnvironment == null) {
         this.jndiEnvironment = new Hashtable(4);
      }

      String var5 = var1.getInitialContextFactory();
      if (var5 != null && var5.trim().length() != 0 && !var5.equals("weblogic.jndi.WLInitialContextFactory")) {
         this.jndiEnvironment.put("java.naming.factory.initial", var5);
      }

      String var6 = var1.getProviderURL();
      if (var6 != null && var6.trim().length() != 0) {
         this.jndiEnvironment.put("java.naming.provider.url", var6);
      }

      String var7 = var1.getUser();
      if (var7 != null && var7.trim().length() != 0) {
         this.jndiEnvironment.put("java.naming.security.principal", ces.encrypt(var7));
      }

      String var8 = var1.getPassword();
      if (var8 != null && var8.trim().length() != 0) {
         this.jndiEnvironment.put("java.naming.security.credentials", ces.encrypt(var8));
      }

      if (this.jndiEnvironment.size() == 0) {
         this.jndiEnvironment = null;
      }

      var1.addBeanUpdateListener(this);
      if (var2 != null) {
         for(int var9 = 0; var9 < var2.length; ++var9) {
            this.local2remote.put(var2[var9].getLocalJNDIName(), var2[var9].getRemoteJNDIName());
            var2[var9].addBeanUpdateListener(this);
         }

         this.bindAll(false);
      }

   }

   public void bindAll(boolean var1) {
      Iterator var2 = this.local2remote.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.bind(var1, (String)var3.getKey(), (String)var3.getValue());
      }

   }

   public void unbindAll() {
      Iterator var1 = this.local2remote.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         this.unbind(var2);
         var1.remove();
      }

   }

   private void bind(boolean var1, String var2, String var3) {
      ForeignOpaqueReference var4 = new ForeignOpaqueReference(var3, this.jndiEnvironment);

      try {
         if (!var1) {
            this.ic.bind(var2, var4);
         } else {
            this.ic.rebind(var2, var4);
         }
      } catch (NamingException var6) {
         JNDILogger.logUnableToBind(StackTraceUtils.throwable2StackTrace(var6));
      }

   }

   private void unbind(String var1) {
      try {
         this.ic.unbind(var1);
      } catch (NamingException var3) {
         JNDILogger.logUnableToUnBind(StackTraceUtils.throwable2StackTrace(var3));
      }

   }

   private void checkDuplicate(String var1) throws BeanUpdateRejectedException {
      if (this.local2remote.get(var1) != null) {
         throw new BeanUpdateRejectedException("A JNDI name already Exists");
      } else {
         try {
            if (this.ic.lookup(var1) != null) {
               throw new BeanUpdateRejectedException("A JNDI name already exists");
            }
         } catch (NamingException var3) {
            if (!(var3 instanceof NameNotFoundException)) {
               throw new BeanUpdateRejectedException("A JNDI name already exists");
            }
         }
      }
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
      if (var1.getSourceBean() instanceof ForeignJNDIProviderMBean) {
         ForeignJNDIProviderMBean var2 = (ForeignJNDIProviderMBean)var1.getSourceBean();
         BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getAddedObject() instanceof ForeignJNDILinkMBean) {
               ForeignJNDILinkMBean var5 = (ForeignJNDILinkMBean)var3[var4].getAddedObject();
               this.checkDuplicate(var5.getLocalJNDIName());
            }
         }
      }

      if (var1.getSourceBean() instanceof ForeignJNDILinkMBean) {
         ForeignJNDILinkMBean var6 = (ForeignJNDILinkMBean)var1.getSourceBean();
         ForeignJNDILinkMBean var7 = (ForeignJNDILinkMBean)var1.getProposedBean();
         String var8 = var7.getLocalJNDIName();
         if (!var6.getLocalJNDIName().equals(var8)) {
            this.checkDuplicate(var8);
            this.pendingChanges.put(var8, var6.getLocalJNDIName());
         }
      }

   }

   public void activateUpdate(BeanUpdateEvent var1) {
      BeanUpdateEvent.PropertyUpdate[] var3;
      int var4;
      if (var1.getSourceBean() instanceof ForeignJNDIProviderMBean) {
         ForeignJNDIProviderMBean var2 = (ForeignJNDIProviderMBean)var1.getSourceBean();
         var3 = var1.getUpdateList();

         for(var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getAddedObject() instanceof ForeignJNDILinkMBean) {
               this.addLink(var3[var4]);
            } else if (var3[var4].getRemovedObject() instanceof ForeignJNDILinkMBean) {
               this.removeLink(var3[var4]);
            } else {
               this.updateJndiProvider(var2, var3[var4]);
            }
         }
      } else if (var1.getSourceBean() instanceof ForeignJNDILinkMBean) {
         ForeignJNDILinkMBean var8 = (ForeignJNDILinkMBean)var1.getSourceBean();
         var3 = var1.getUpdateList();

         for(var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var3[var4].getPropertyName();
            if (var5.equals("LocalJNDIName")) {
               String var6 = var8.getLocalJNDIName();
               String var7 = (String)this.pendingChanges.get(var6);
               this.unbindOldJndi(var7);
               this.bind(false, var6, var8.getRemoteJNDIName());
            } else if (var5.equals("RemoteJNDIName")) {
               this.bind(true, var8.getLocalJNDIName(), var8.getRemoteJNDIName());
            }
         }
      }

   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   private void updateJndiProvider(ForeignJNDIProviderMBean var1, BeanUpdateEvent.PropertyUpdate var2) {
      if (this.jndiEnvironment == null) {
         this.jndiEnvironment = new Hashtable(5);
      }

      String var3 = var2.getPropertyName();
      if (var3.equals("InitialContextFactory")) {
         String var4 = var1.getInitialContextFactory();
         if (var4 != null && var4.trim().length() != 0 && !var4.equals("weblogic.jndi.WLInitialContextFactory")) {
            this.jndiEnvironment.put("java.naming.factory.initial", var4);
            this.bindAll(true);
         } else {
            String var5;
            if (var3.equals("ProviderURL")) {
               var5 = var1.getProviderURL();
               if (var5 != null && var5.trim().length() != 0) {
                  this.jndiEnvironment.put("java.naming.provider.url", var5);
                  this.bindAll(true);
               }
            } else if (var3.equals("Password")) {
               var5 = var1.getPassword();
               if (var5 != null && var5.trim().length() != 0) {
                  this.jndiEnvironment.put("java.naming.security.principal", ces.encrypt(var5));
                  this.bindAll(true);
               }
            } else if (var3.equals("User")) {
               var5 = var1.getUser();
               if (var5 != null && var5.trim().length() != 0) {
                  this.jndiEnvironment.put("java.naming.security.credentials", ces.encrypt(var5));
               }

               this.bindAll(true);
            }
         }
      } else if (var3.equals("Properties")) {
         Properties var6 = var1.getProperties();
         if (var6 != null && var6.size() != 0) {
            this.jndiEnvironment.putAll(var6);
            this.bindAll(true);
         }
      }

   }

   private void unbindOldJndi(String var1) {
      this.unbind(var1);
      this.local2remote.remove(var1);
   }

   private void addLink(BeanUpdateEvent.PropertyUpdate var1) {
      ForeignJNDILinkMBean var2 = (ForeignJNDILinkMBean)var1.getAddedObject();
      String var3 = var2.getRemoteJNDIName();
      String var4 = var2.getLocalJNDIName();
      this.local2remote.put(var4, var3);
      this.bind(false, var4, var3);
      var2.addBeanUpdateListener(this);
   }

   private void removeLink(BeanUpdateEvent.PropertyUpdate var1) {
      ForeignJNDILinkMBean var2 = (ForeignJNDILinkMBean)var1.getRemovedObject();
      String var3 = var2.getLocalJNDIName();
      this.unbind(var3);
      this.local2remote.remove(var3);
   }
}
