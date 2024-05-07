package weblogic.management.remote.wlx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.rmi.RMIServer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import weblogic.kernel.KernelStatus;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.management.remote.common.WLSJMXConnector;
import weblogic.rmi.extensions.PortableRemoteObject;

public class ClientProvider implements JMXConnectorProvider {
   private static final String WEBLOGIC_CONTEXT = "weblogic.context";

   public JMXConnector newJMXConnector(JMXServiceURL var1, Map var2) throws IOException {
      HashMap var3 = new HashMap();
      var3.putAll(var2);
      var3.put("jmx.remote.x.notification.fetch.timeout", new Long(1000L));
      var3.put("jmx.remote.x.client.connection.check.period", new Long(0L));
      RMIServer var4 = this.findRMIServer(var1, var3);
      return new WLXRMIConnectorWrapper(var4, var3);
   }

   private RMIServer findRMIServer(JMXServiceURL var1, Map var2) throws IOException {
      String var3 = var1.getURLPath();
      if (!var3.startsWith("/jndi/")) {
         throw new MalformedURLException("URL path must begin with /jndi/");
      } else {
         Object var4 = (Context)var2.get("weblogic.context");
         if (var4 == null) {
            if (!KernelStatus.isServer()) {
               throw new IOException("weblogic.context is not defined in the environment");
            }

            try {
               var4 = new InitialContext();
            } catch (NamingException var9) {
               IOException var6 = new IOException("Unable to create InitialContext: " + var9.toString());
               var6.initCause(var9);
               throw var6;
            }
         }

         Object var5;
         try {
            var5 = ((Context)var4).lookup(var3.substring(6));
         } catch (NamingException var8) {
            IOException var7 = new IOException(var8.getMessage());
            var7.initCause(var8);
            throw var7;
         }

         return narrowServer(var5);
      }
   }

   private static RMIServer narrowServer(Object var0) {
      try {
         return (RMIServer)PortableRemoteObject.narrow(var0, RMIServer.class);
      } catch (ClassCastException var2) {
         return null;
      }
   }

   class WLXRMIConnectorWrapper extends RMIConnector implements WLSJMXConnector {
      private Locale locale_ = null;

      public WLXRMIConnectorWrapper(RMIServer var2, Map var3) {
         super(var2, var3);
         this.locale_ = (Locale)var3.get("weblogic.management.remote.locale");
      }

      public synchronized MBeanServerConnection getMBeanServerConnection() throws IOException {
         Object var1 = super.getMBeanServerConnection();
         if (this.locale_ != null) {
            var1 = ClientProvider.this.new WLXMBeanServerConnectionWrapper((MBeanServerConnection)var1, this.locale_);
         }

         return (MBeanServerConnection)var1;
      }

      public synchronized MBeanServerConnection getMBeanServerConnection(Subject var1) throws IOException {
         Object var2 = super.getMBeanServerConnection(var1);
         if (this.locale_ != null) {
            var2 = ClientProvider.this.new WLXMBeanServerConnectionWrapper((MBeanServerConnection)var2, this.locale_);
         }

         return (MBeanServerConnection)var2;
      }

      public synchronized MBeanServerConnection getMBeanServerConnection(Locale var1) throws IOException {
         MBeanServerConnection var2 = super.getMBeanServerConnection();
         return ClientProvider.this.new WLXMBeanServerConnectionWrapper(var2, var1);
      }

      public synchronized MBeanServerConnection getMBeanServerConnection(Subject var1, Locale var2) throws IOException {
         MBeanServerConnection var3 = super.getMBeanServerConnection(var1);
         return ClientProvider.this.new WLXMBeanServerConnectionWrapper(var3, var2);
      }
   }

   class WLXMBeanServerConnectionWrapper implements MBeanServerConnection {
      private MBeanServerConnection connection_;
      private Locale locale_;

      WLXMBeanServerConnectionWrapper(MBeanServerConnection var2, Locale var3) {
         this.connection_ = var2;
         this.locale_ = var3;
      }

      public ObjectInstance createMBean(String var1, ObjectName var2) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
         return this.connection_.createMBean(var1, var2);
      }

      public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
         return this.connection_.createMBean(var1, var2, var3);
      }

      public ObjectInstance createMBean(String var1, ObjectName var2, Object[] var3, String[] var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
         return this.connection_.createMBean(var1, var2, var3, var4);
      }

      public ObjectInstance createMBean(String var1, ObjectName var2, ObjectName var3, Object[] var4, String[] var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
         return this.connection_.createMBean(var1, var2, var3, var4, var5);
      }

      public void unregisterMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
         this.connection_.unregisterMBean(var1);
      }

      public ObjectInstance getObjectInstance(ObjectName var1) throws InstanceNotFoundException, IOException {
         return this.connection_.getObjectInstance(var1);
      }

      public Set queryMBeans(ObjectName var1, QueryExp var2) throws IOException {
         return this.connection_.queryMBeans(var1, var2);
      }

      public Set queryNames(ObjectName var1, QueryExp var2) throws IOException {
         return this.connection_.queryNames(var1, var2);
      }

      public boolean isRegistered(ObjectName var1) throws IOException {
         return this.connection_.isRegistered(var1);
      }

      public Integer getMBeanCount() throws IOException {
         return this.connection_.getMBeanCount();
      }

      public Object getAttribute(ObjectName var1, String var2) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
         Object var3;
         try {
            this.initializeJMXContext();
            var3 = this.connection_.getAttribute(var1, var2);
         } finally {
            this.removeJMXContext();
         }

         return var3;
      }

      public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException, IOException {
         AttributeList var3;
         try {
            this.initializeJMXContext();
            var3 = this.connection_.getAttributes(var1, var2);
         } finally {
            this.removeJMXContext();
         }

         return var3;
      }

      public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
         try {
            this.initializeJMXContext();
            this.connection_.setAttribute(var1, var2);
         } finally {
            this.removeJMXContext();
         }

      }

      public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException, IOException {
         AttributeList var3;
         try {
            this.initializeJMXContext();
            var3 = this.connection_.setAttributes(var1, var2);
         } finally {
            this.removeJMXContext();
         }

         return var3;
      }

      public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
         Object var5;
         try {
            this.initializeJMXContext();
            var5 = this.connection_.invoke(var1, var2, var3, var4);
         } finally {
            this.removeJMXContext();
         }

         return var5;
      }

      public String getDefaultDomain() throws IOException {
         return this.connection_.getDefaultDomain();
      }

      public String[] getDomains() throws IOException {
         return this.connection_.getDomains();
      }

      public MBeanInfo getMBeanInfo(ObjectName var1) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
         MBeanInfo var2;
         try {
            this.initializeJMXContext();
            var2 = this.connection_.getMBeanInfo(var1);
         } finally {
            this.removeJMXContext();
         }

         return var2;
      }

      public boolean isInstanceOf(ObjectName var1, String var2) throws InstanceNotFoundException, IOException {
         return this.connection_.isInstanceOf(var1, var2);
      }

      public void addNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
         this.connection_.addNotificationListener(var1, var2, var3, var4);
      }

      public void addNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, IOException {
         this.connection_.addNotificationListener(var1, var2, var3, var4);
      }

      public void removeNotificationListener(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
         this.connection_.removeNotificationListener(var1, var2);
      }

      public void removeNotificationListener(ObjectName var1, ObjectName var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
         this.connection_.removeNotificationListener(var1, var2, var3, var4);
      }

      public void removeNotificationListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
         this.connection_.removeNotificationListener(var1, var2);
      }

      public void removeNotificationListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
         this.connection_.removeNotificationListener(var1, var2, var3, var4);
      }

      private void initializeJMXContext() {
         JMXContext var1 = JMXContextHelper.getJMXContext(true);
         var1.setLocale(this.locale_);
         JMXContextHelper.putJMXContext(var1);
      }

      private void removeJMXContext() {
         JMXContextHelper.removeJMXContext();
      }
   }
}
