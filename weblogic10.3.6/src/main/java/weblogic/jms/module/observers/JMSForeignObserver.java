package weblogic.jms.module.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.ForeignDestinationBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ForeignJMSConnectionFactoryMBean;
import weblogic.management.configuration.ForeignJMSDestinationMBean;
import weblogic.management.configuration.ForeignJMSServerMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.utils.ArrayUtils;

public class JMSForeignObserver implements PropertyChangeListener, ArrayUtils.DiffHandler {
   private static final String DESTINATION_STRING = "ForeignJMSDestinations";
   private static final String CONNECTION_FACTORY_STRING = "ForeignJMSConnectionFactories";
   private static final String[] handledProperties = new String[]{"ForeignJMSDestinations", "ForeignJMSConnectionFactories"};
   private static final int UNHANDLED = -1;
   private static final int DESTINATION = 0;
   private static final int CONNECTION_FACTORY = 1;
   private static final int MAX_PROPERTIES = 2;
   private JMSObserver domainObserver;
   private ForeignJMSServerMBean foreignJMSServer;
   private int currentType = -1;

   public JMSForeignObserver(JMSObserver var1, ForeignJMSServerMBean var2) {
      this.domainObserver = var1;
      this.foreignJMSServer = var2;
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      this.currentType = this.getType(var1.getPropertyName());
      if (this.currentType != -1) {
         Object[] var2 = (Object[])((Object[])var1.getOldValue());
         Object[] var3 = (Object[])((Object[])var1.getNewValue());
         ArrayUtils.computeDiff(var2, var3, this, this.domainObserver);
         this.currentType = -1;
      }
   }

   public ForeignJMSServerMBean getForeignJMSServer() {
      return this.foreignJMSServer;
   }

   private int getType(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < 2; ++var2) {
            String var3 = handledProperties[var2];
            if (var3.equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   private void addDestination(ForeignJMSDestinationMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSSystemResourceMBean var3 = JMSBeanHelper.addInteropApplication(var2);
      JMSBean var4 = var3.getJMSResource();
      ForeignServerBean var5 = var4.lookupForeignServer(this.foreignJMSServer.getName());
      if (var5 != null) {
         ForeignDestinationBean var6 = MBeanConverter.addForeignDestination(var5, var1);
         var1.useDelegates(var6);
      }
   }

   private void removeDestination(ForeignJMSDestinationMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSInteropModuleMBean var3 = JMSBeanHelper.getJMSInteropModule(var2);
      if (var3 != null) {
         JMSBean var4 = var3.getJMSResource();
         ForeignServerBean var5 = var4.lookupForeignServer(this.foreignJMSServer.getName());
         if (var5 != null) {
            ForeignDestinationBean var6 = var5.lookupForeignDestination(var1.getName());
            var5.destroyForeignDestination(var6);
         }
      }
   }

   private void addConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSSystemResourceMBean var3 = JMSBeanHelper.addInteropApplication(var2);
      JMSBean var4 = var3.getJMSResource();
      ForeignServerBean var5 = var4.lookupForeignServer(this.foreignJMSServer.getName());
      if (var5 != null) {
         ForeignConnectionFactoryBean var6 = MBeanConverter.addForeignConnectionFactory(var5, var1);
         var1.useDelegates(var6);
      }
   }

   private void removeConnectionFactory(ForeignJMSConnectionFactoryMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSInteropModuleMBean var3 = JMSBeanHelper.getJMSInteropModule(var2);
      if (var3 != null) {
         JMSBean var4 = var3.getJMSResource();
         ForeignServerBean var5 = var4.lookupForeignServer(this.foreignJMSServer.getName());
         if (var5 != null) {
            ForeignConnectionFactoryBean var6 = var5.lookupForeignConnectionFactory(var1.getName());
            var5.destroyForeignConnectionFactory(var6);
         }
      }
   }

   public void addObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.addDestination((ForeignJMSDestinationMBean)var1);
            break;
         case 1:
            this.addConnectionFactory((ForeignJMSConnectionFactoryMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   public void removeObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.removeDestination((ForeignJMSDestinationMBean)var1);
            break;
         case 1:
            this.removeConnectionFactory((ForeignJMSConnectionFactoryMBean)var1);
            break;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }

   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof JMSForeignObserver) {
         JMSForeignObserver var2 = (JMSForeignObserver)var1;
         return this.foreignJMSServer == var2.foreignJMSServer;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.foreignJMSServer.hashCode();
   }
}
