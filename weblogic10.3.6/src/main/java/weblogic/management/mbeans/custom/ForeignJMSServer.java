package weblogic.management.mbeans.custom;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.PropertyBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.EncryptionHelper;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSConnectionFactoryMBean;
import weblogic.management.configuration.ForeignJMSDestinationMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.utils.ArrayUtils;

public class ForeignJMSServer extends ConfigurationMBeanCustomizer {
   private static final String TARGETS = "Targets";
   private transient ForeignServerBean delegate;
   private transient SubDeploymentMBean subDeployment;

   public ForeignJMSServer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(ForeignServerBean var1, SubDeploymentMBean var2) {
      this.delegate = var1;
      this.subDeployment = var2;
   }

   public TargetMBean[] getTargets() {
      if (this.subDeployment == null) {
         Object var1 = this.getValue("Targets");
         if (var1 == null) {
            return new TargetMBean[0];
         } else {
            if (!(var1 instanceof TargetMBean)) {
               if (!(var1 instanceof WebLogicMBean[])) {
                  return new TargetMBean[0];
               }

               WebLogicMBean[] var2 = (WebLogicMBean[])((WebLogicMBean[])var1);
               TargetMBean[] var3 = new TargetMBean[var2.length];

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  WebLogicMBean var5 = var2[var4];
                  if (!(var5 instanceof TargetMBean)) {
                     return new TargetMBean[0];
                  }

                  var3[var4] = (TargetMBean)var5;
               }

               var1 = var3;
            }

            return (TargetMBean[])((TargetMBean[])var1);
         }
      } else {
         return this.subDeployment.getTargets();
      }
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.subDeployment == null) {
         this.putValueNotify("Targets", var1);
      } else {
         this.subDeployment.setTargets(var1);
      }

   }

   public String getInitialContextFactory() {
      if (this.delegate == null) {
         Object var1 = this.getValue("InitialContextFactory");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getInitialContextFactory();
      }
   }

   public void setInitialContextFactory(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("InitialContextFactory", var1);
      } else {
         this.delegate.setInitialContextFactory(var1);
      }

   }

   public String getConnectionURL() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ConnectionURL");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getConnectionURL();
      }
   }

   public void setConnectionURL(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("ConnectionURL", var1);
      } else {
         this.delegate.setConnectionURL(var1);
      }

   }

   public Properties getJNDIProperties() {
      if (this.delegate == null) {
         Object var5 = this.getValue("JNDIProperties");
         return var5 != null && var5 instanceof Properties ? (Properties)var5 : null;
      } else {
         Properties var1 = new Properties();
         PropertyBean[] var2 = this.delegate.getJNDIProperties();
         if (var2 != null) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               PropertyBean var4 = var2[var3];
               var1.setProperty(var4.getKey(), var4.getValue());
            }
         }

         return var1;
      }
   }

   public void setJNDIProperties(Properties var1) throws InvalidAttributeValueException {
      String var3;
      PropertyBean var4;
      if (this.delegate == null) {
         if (var1 == null) {
            return;
         }

         Properties var2 = new Properties();
         var2.putAll(var1);
         var3 = (String)var2.remove("java.naming.security.credentials");
         this.putValue("JNDIProperties", var2);
         var4 = null;
         if (var3 != null) {
            byte[] var14 = EncryptionHelper.encryptString(var3);
            this.putValue("JNDIPropertiesCredentialEncrypted", var14);
         }
      } else {
         PropertyBean[] var12 = this.delegate.getJNDIProperties();

         for(int var13 = 0; var13 < var12.length; ++var13) {
            var4 = var12[var13];
            this.delegate.destroyJNDIProperty(var4);
         }

         if (var1 == null) {
            return;
         }

         var3 = (String)var1.remove("java.naming.security.credentials");
         Set var15 = var1.entrySet();
         Iterator var5 = var15.iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            Object var7 = var6.getKey();
            String var8 = var7 == null ? "" : (!(var7 instanceof String) ? var7.toString() : (String)var7);
            Object var9 = var6.getValue();
            String var10 = var9 == null ? null : (!(var9 instanceof String) ? var9.toString() : (String)var9);
            PropertyBean var11 = this.delegate.createJNDIProperty(var8);
            var11.setValue(var10);
         }

         if (var3 != null) {
            this.delegate.setJNDIPropertiesCredential(var3);
         }
      }

   }

   public ForeignJMSDestinationMBean[] getDestinations() {
      return ((ForeignJMSServerMBean)this.getMbean()).getForeignJMSDestinations();
   }

   /** @deprecated */
   void setDestinations(ForeignJMSDestinationMBean[] var1) {
      ArrayUtils.computeDiff(this.getDestinations(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            ForeignJMSServer.this.addDestination((ForeignJMSDestinationMBean)var1);
         }

         public void removeObject(Object var1) {
            ForeignJMSServer.this.removeDestination((ForeignJMSDestinationMBean)var1);
         }
      });
   }

   /** @deprecated */
   public boolean addDestination(ForeignJMSDestinationMBean var1) {
      ForeignJMSServerMBean var2 = (ForeignJMSServerMBean)this.getMbean();
      if (var2.lookupForeignJMSDestination(var1.getName()) != null) {
         return true;
      } else {
         ForeignJMSDestination var10000 = (ForeignJMSDestination)this.getMbean().createChildCopyIncludingObsolete("ForeignJMSDestination", var1);
         DomainMBean var4 = (DomainMBean)var2.getParentBean();
         var4.destroyForeignJMSDestination(var1);
         return true;
      }
   }

   /** @deprecated */
   boolean removeDestination(ForeignJMSDestinationMBean var1) {
      ForeignJMSServerMBean var2 = (ForeignJMSServerMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var2.lookupForeignJMSDestination(var1.getName()) == null) {
         return true;
      } else {
         var3.createForeignJMSDestination(var1.getName(), var1);
         var2.destroyForeignJMSDestination(var1);
         return true;
      }
   }

   ForeignJMSConnectionFactoryMBean[] getConnectionFactories() {
      return ((ForeignJMSServerMBean)this.getMbean()).getForeignJMSConnectionFactories();
   }

   void setConnectionFactories(ForeignJMSConnectionFactoryMBean[] var1) {
      ArrayUtils.computeDiff(this.getDestinations(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            ForeignJMSServer.this.addConnectionFactory((ForeignJMSConnectionFactoryMBean)var1);
         }

         public void removeObject(Object var1) {
            ForeignJMSServer.this.removeConnectionFactory((ForeignJMSConnectionFactoryMBean)var1);
         }
      });
   }

   /** @deprecated */
   public boolean addConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      ForeignJMSServerMBean var2 = (ForeignJMSServerMBean)this.getMbean();
      if (var2.lookupForeignJMSConnectionFactory(var1.getName()) != null) {
         return true;
      } else {
         ForeignJMSDestination var10000 = (ForeignJMSDestination)this.getMbean().createChildCopyIncludingObsolete("ForeignConnectionFactory", var1);
         DomainMBean var4 = (DomainMBean)var2.getParentBean();
         var4.destroyForeignJMSConnectionFactory(var1);
         return true;
      }
   }

   /** @deprecated */
   boolean removeConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      ForeignJMSServerMBean var2 = (ForeignJMSServerMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var2.lookupForeignJMSDestination(var1.getName()) == null) {
         return true;
      } else {
         var3.createForeignJMSConnectionFactory(var1.getName(), var1);
         var2.destroyForeignJMSConnectionFactory(var1);
         return true;
      }
   }

   public ForeignJMSConnectionFactoryMBean createForeignJMSConnectionFactory(String var1, ForeignJMSConnectionFactoryMBean var2) {
      return (ForeignJMSConnectionFactoryMBean)this.getMbean().createChildCopyIncludingObsolete("ForeignJMSConnectionFactory", var2);
   }

   public ForeignJMSDestinationMBean createForeignJMSDestination(String var1, ForeignJMSDestinationMBean var2) {
      return (ForeignJMSDestinationMBean)this.getMbean().createChildCopyIncludingObsolete("ForeignJMSDestination", var2);
   }
}
