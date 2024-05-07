package weblogic.deploy.api.spi.deploy.mbeans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeChangeNotificationFilter;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.spi.exceptions.ServerConnectionException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;

public abstract class MBeanCache implements Serializable {
   private static final long serialVersionUID = 1L;
   private static final boolean ddebug = Debug.isDebug("internal");
   private static final boolean debug = Debug.isDebug("deploy");
   private boolean cachingEnabled = true;
   private transient List mbeans = null;
   private boolean stale = true;
   protected transient WebLogicDeploymentManager dm;
   protected transient DomainMBean currDomain;
   protected Object listener;
   protected String[] listenType = null;

   MBeanCache(WebLogicDeploymentManager var1) {
      this.dm = var1;
   }

   public synchronized List getMBeans() throws ServerConnectionException {
      if (this.stale) {
         try {
            ConfigurationMBean[] var1 = this.getTypedMBeans();
            this.mbeans = new ArrayList();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               if (var1[var2] != null) {
                  this.mbeans.add(var1[var2]);
               }
            }

            this.stale = !this.cachingEnabled;
            if (ddebug) {
               Debug.say("Returning " + this.mbeans.size() + " mbeans: ");
            }
         } catch (Throwable var3) {
            throw new ServerConnectionException(SPIDeployerLogger.connectionError(), var3);
         }
      }

      return this.mbeans;
   }

   protected void addNotificationListener() {
      if (this.dm.getServerConnection().getMBeanServerConnection() == null) {
         DomainMBean var1 = this.currDomain;
         this.listener = new MyListener(false);
         var1.addPropertyChangeListener((PropertyChangeListener)this.listener);
      } else {
         this.listener = new MyListener(true);

         try {
            MBeanServerConnection var3 = this.dm.getServerConnection().getRuntimeServerConnection();
            if (var3 == null) {
               if (debug || ddebug) {
                  Debug.say("Disabling mbean caching since we do not runtime mbean server connection");
               }

               this.cachingEnabled = false;
               return;
            }

            if (debug || ddebug) {
               Debug.say("Adding notification listener for " + this);
            }

            var3.addNotificationListener(this.currDomain.getObjectName(), (NotificationListener)this.listener, new MyJMXFilter(), (Object)null);
            if (debug || ddebug) {
               Debug.say("Added notification listener for " + this);
            }
         } catch (Exception var2) {
            if (debug || ddebug) {
               Debug.say("Disabling mbean caching due to: " + var2.toString());
            }

            this.cachingEnabled = false;
         }
      }

   }

   protected void removeNotificationListener() {
      if (this.dm.getServerConnection().getMBeanServerConnection() == null) {
         DomainMBean var1 = this.currDomain;
         if (this.listener != null) {
            var1.removePropertyChangeListener((PropertyChangeListener)this.listener);
         }
      } else {
         try {
            this.currDomain.removeNotificationListener((NotificationListener)this.listener);
         } catch (Exception var2) {
         }
      }

   }

   public abstract ConfigurationMBean[] getTypedMBeans();

   public ConfigurationMBean getMBean(String var1) throws ServerConnectionException {
      Iterator var2 = this.getMBeans().iterator();

      ConfigurationMBean var3;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         var3 = (ConfigurationMBean)var2.next();
      } while(!var3.getName().equals(var1));

      return var3;
   }

   public synchronized void reset() {
      if (ddebug) {
         Debug.say("Resetting cache: " + this.getClass().getName());
      }

      this.mbeans = null;
      this.stale = true;
   }

   public void close() {
      this.reset();
      this.removeNotificationListener();
   }

   public boolean isStale() {
      return this.stale;
   }

   public DeploymentManager getDM() {
      return this.dm;
   }

   public class MyJMXFilter extends AttributeChangeNotificationFilter implements Serializable {
      private static final long serialVersionUID = 7201439898187309867L;

      public MyJMXFilter() {
         if (MBeanCache.this.listenType != null) {
            this.disableAllAttributes();

            for(int var2 = 0; var2 < MBeanCache.this.listenType.length; ++var2) {
               this.enableAttribute(MBeanCache.this.listenType[var2]);
            }
         }

      }
   }

   public class MyListener implements PropertyChangeListener, NotificationListener, Serializable {
      private static final long serialVersionUID = 1L;
      private Vector props = new Vector();
      private boolean jmx;

      public MyListener(boolean var2) {
         this.jmx = var2;
         if (MBeanCache.this.listenType != null) {
            for(int var3 = 0; var3 < MBeanCache.this.listenType.length; ++var3) {
               this.props.add(MBeanCache.this.listenType[var3]);
            }

         }
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (MBeanCache.ddebug) {
            Debug.say("Got change event for " + var1.getPropertyName());
         }

         if (this.props.contains(var1.getPropertyName())) {
            MBeanCache.this.reset();
         }

      }

      public void handleNotification(Notification var1, Object var2) {
         if (var1 instanceof AttributeChangeNotification) {
            if (MBeanCache.ddebug) {
               Debug.say("Got change event for prop: " + ((AttributeChangeNotification)var1).getAttributeName());
            }

            if (this.props.contains(((AttributeChangeNotification)var1).getAttributeName())) {
               MBeanCache.this.reset();
            }

         }
      }
   }
}
