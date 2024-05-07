package weblogic.management.remote.common;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
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
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.NotificationResult;
import javax.management.remote.rmi.RMIConnection;
import javax.security.auth.Subject;
import weblogic.management.context.JMXContext;
import weblogic.management.context.JMXContextHelper;
import weblogic.security.Security;

class RMIConnectionWrapper implements RMIConnection {
   private RMIConnection connection;
   private boolean disconnected = false;
   private Subject subject = null;
   private Locale locale;
   private HashMap localeMap = new HashMap();
   private boolean defaultLocale = false;
   private RMIServerWrapper rmiServerWrapper = null;

   RMIConnectionWrapper(RMIConnection var1, Subject var2, Locale var3, RMIServerWrapper var4) {
      this.subject = var2;
      this.connection = var1;
      this.rmiServerWrapper = var4;
      if (var3 == null) {
         this.locale = Locale.getDefault();
         this.defaultLocale = true;
      } else {
         this.locale = var3;
      }

   }

   public void disconnected() {
      this.disconnected = true;
   }

   public void close() throws IOException {
      if (!this.disconnected) {
         this.connection.close();
      }

      this.rmiServerWrapper.clearClientConnection(this);
   }

   public void setLocale(Locale var1) {
      this.setLocale((Subject)null, var1);
   }

   public void setLocale(Subject var1, Locale var2) {
      this.localeMap.put(var1, var2);
   }

   public String getConnectionId() throws IOException {
      String var1 = null;

      try {
         var1 = (String)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getConnectionId();
            }
         });
         return var1;
      } catch (PrivilegedActionException var4) {
         Exception var3 = var4.getException();
         if (var3 instanceof IOException) {
            throw (IOException)var3;
         } else {
            throw new RuntimeException((Throwable)(var3 == null ? var4 : var3));
         }
      }
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final Subject var3) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      ObjectInstance var4 = null;

      try {
         var4 = (ObjectInstance)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.createMBean(var1, var2, var3);
            }
         });
         return var4;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         } else if (var6 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var6;
         } else if (var6 instanceof MBeanRegistrationException) {
            throw (MBeanRegistrationException)var6;
         } else if (var6 instanceof MBeanException) {
            throw (MBeanException)var6;
         } else if (var6 instanceof NotCompliantMBeanException) {
            throw (NotCompliantMBeanException)var6;
         } else if (var6 instanceof IOException) {
            throw (IOException)var6;
         } else {
            throw new RuntimeException((Throwable)(var6 == null ? var7 : var6));
         }
      }
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final ObjectName var3, final Subject var4) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      ObjectInstance var5 = null;

      try {
         var5 = (ObjectInstance)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.createMBean(var1, var2, var3, var4);
            }
         });
         return var5;
      } catch (PrivilegedActionException var8) {
         Exception var7 = var8.getException();
         if (var7 instanceof ReflectionException) {
            throw (ReflectionException)var7;
         } else if (var7 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var7;
         } else if (var7 instanceof MBeanRegistrationException) {
            throw (MBeanRegistrationException)var7;
         } else if (var7 instanceof MBeanException) {
            throw (MBeanException)var7;
         } else if (var7 instanceof NotCompliantMBeanException) {
            throw (NotCompliantMBeanException)var7;
         } else if (var7 instanceof IOException) {
            throw (IOException)var7;
         } else {
            throw new RuntimeException((Throwable)(var7 == null ? var8 : var7));
         }
      }
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final MarshalledObject var3, final String[] var4, final Subject var5) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, IOException {
      ObjectInstance var6 = null;

      try {
         var6 = (ObjectInstance)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.createMBean(var1, var2, var3, var4, var5);
            }
         });
         return var6;
      } catch (PrivilegedActionException var9) {
         Exception var8 = var9.getException();
         if (var8 instanceof ReflectionException) {
            throw (ReflectionException)var8;
         } else if (var8 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var8;
         } else if (var8 instanceof MBeanRegistrationException) {
            throw (MBeanRegistrationException)var8;
         } else if (var8 instanceof MBeanException) {
            throw (MBeanException)var8;
         } else if (var8 instanceof NotCompliantMBeanException) {
            throw (NotCompliantMBeanException)var8;
         } else if (var8 instanceof IOException) {
            throw (IOException)var8;
         } else {
            throw new RuntimeException((Throwable)(var8 == null ? var9 : var8));
         }
      }
   }

   public ObjectInstance createMBean(final String var1, final ObjectName var2, final ObjectName var3, final MarshalledObject var4, final String[] var5, final Subject var6) throws ReflectionException, InstanceAlreadyExistsException, MBeanRegistrationException, MBeanException, NotCompliantMBeanException, InstanceNotFoundException, IOException {
      ObjectInstance var7 = null;

      try {
         var7 = (ObjectInstance)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.createMBean(var1, var2, var3, var4, var5, var6);
            }
         });
         return var7;
      } catch (PrivilegedActionException var10) {
         Exception var9 = var10.getException();
         if (var9 instanceof ReflectionException) {
            throw (ReflectionException)var9;
         } else if (var9 instanceof InstanceAlreadyExistsException) {
            throw (InstanceAlreadyExistsException)var9;
         } else if (var9 instanceof MBeanRegistrationException) {
            throw (MBeanRegistrationException)var9;
         } else if (var9 instanceof MBeanException) {
            throw (MBeanException)var9;
         } else if (var9 instanceof NotCompliantMBeanException) {
            throw (NotCompliantMBeanException)var9;
         } else if (var9 instanceof IOException) {
            throw (IOException)var9;
         } else {
            throw new RuntimeException((Throwable)(var9 == null ? var10 : var9));
         }
      }
   }

   public void unregisterMBean(final ObjectName var1, final Subject var2) throws InstanceNotFoundException, MBeanRegistrationException, IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.unregisterMBean(var1, var2);
               return null;
            }
         });
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var4;
         } else if (var4 instanceof MBeanRegistrationException) {
            throw (MBeanRegistrationException)var4;
         } else if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new RuntimeException((Throwable)(var4 == null ? var5 : var4));
         }
      }
   }

   public ObjectInstance getObjectInstance(final ObjectName var1, final Subject var2) throws InstanceNotFoundException, IOException {
      ObjectInstance var3 = null;

      try {
         var3 = (ObjectInstance)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getObjectInstance(var1, var2);
            }
         });
         return var3;
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         } else if (var5 instanceof IOException) {
            throw (IOException)var5;
         } else {
            throw new RuntimeException((Throwable)(var5 == null ? var6 : var5));
         }
      }
   }

   public Set queryMBeans(final ObjectName var1, final MarshalledObject var2, final Subject var3) throws IOException {
      Set var4 = null;

      try {
         var4 = (Set)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.queryMBeans(var1, var2, var3);
            }
         });
         return var4;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof IOException) {
            throw (IOException)var6;
         } else {
            throw new RuntimeException((Throwable)(var6 == null ? var7 : var6));
         }
      }
   }

   public Set queryNames(final ObjectName var1, final MarshalledObject var2, final Subject var3) throws IOException {
      Set var4 = null;

      try {
         var4 = (Set)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.queryNames(var1, var2, var3);
            }
         });
         return var4;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof IOException) {
            throw (IOException)var6;
         } else {
            throw new RuntimeException((Throwable)(var6 == null ? var7 : var6));
         }
      }
   }

   public boolean isRegistered(final ObjectName var1, final Subject var2) throws IOException {
      Boolean var3 = null;

      try {
         var3 = (Boolean)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.isRegistered(var1, var2);
            }
         });
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof IOException) {
            throw (IOException)var5;
         }

         throw new RuntimeException((Throwable)(var5 == null ? var6 : var5));
      }

      return var3;
   }

   public Integer getMBeanCount(final Subject var1) throws IOException {
      Integer var2 = null;

      try {
         var2 = (Integer)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getMBeanCount(var1);
            }
         });
         return var2;
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new RuntimeException((Throwable)(var4 == null ? var5 : var4));
         }
      }
   }

   public Object getAttribute(final ObjectName var1, final String var2, final Subject var3) throws MBeanException, AttributeNotFoundException, InstanceNotFoundException, ReflectionException, IOException {
      Object var4 = null;

      try {
         this.initializeJMXContext(var3);
         var4 = Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getAttribute(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var10) {
         Exception var6 = var10.getException();
         if (var6 instanceof MBeanException) {
            throw (MBeanException)var6;
         }

         if (var6 instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException)var6;
         }

         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         }

         if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         }

         if (var6 instanceof IOException) {
            throw (IOException)var6;
         }

         throw new RuntimeException((Throwable)(var6 == null ? var10 : var6));
      } finally {
         this.removeJMXContext();
      }

      return var4;
   }

   public AttributeList getAttributes(final ObjectName var1, final String[] var2, final Subject var3) throws InstanceNotFoundException, ReflectionException, IOException {
      AttributeList var4 = null;

      try {
         this.initializeJMXContext(var3);
         var4 = (AttributeList)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getAttributes(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var10) {
         Exception var6 = var10.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         }

         if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         }

         if (var6 instanceof IOException) {
            throw (IOException)var6;
         }

         throw new RuntimeException((Throwable)(var6 == null ? var10 : var6));
      } finally {
         this.removeJMXContext();
      }

      return var4;
   }

   public void setAttribute(final ObjectName var1, final MarshalledObject var2, final Subject var3) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      try {
         this.initializeJMXContext(var3);
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.setAttribute(var1, var2, var3);
               return null;
            }
         });
      } catch (PrivilegedActionException var9) {
         Exception var5 = var9.getException();
         if (var5 instanceof MBeanException) {
            throw (MBeanException)var5;
         }

         if (var5 instanceof AttributeNotFoundException) {
            throw (AttributeNotFoundException)var5;
         }

         if (var5 instanceof InvalidAttributeValueException) {
            throw (InvalidAttributeValueException)var5;
         }

         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         }

         if (var5 instanceof ReflectionException) {
            throw (ReflectionException)var5;
         }

         if (var5 instanceof IOException) {
            throw (IOException)var5;
         }

         throw new RuntimeException((Throwable)(var5 == null ? var9 : var5));
      } finally {
         this.removeJMXContext();
      }

   }

   public AttributeList setAttributes(final ObjectName var1, final MarshalledObject var2, final Subject var3) throws InstanceNotFoundException, ReflectionException, IOException {
      AttributeList var4 = null;

      try {
         this.initializeJMXContext(var3);
         var4 = (AttributeList)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.setAttributes(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var10) {
         Exception var6 = var10.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         }

         if (var6 instanceof ReflectionException) {
            throw (ReflectionException)var6;
         }

         if (var6 instanceof IOException) {
            throw (IOException)var6;
         }

         throw new RuntimeException((Throwable)(var6 == null ? var10 : var6));
      } finally {
         this.removeJMXContext();
      }

      return var4;
   }

   public Object invoke(final ObjectName var1, final String var2, final MarshalledObject var3, final String[] var4, final Subject var5) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      Object var6 = null;

      try {
         this.initializeJMXContext(var5);
         var6 = Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.invoke(var1, var2, var3, var4, var5);
            }
         });
      } catch (PrivilegedActionException var12) {
         Exception var8 = var12.getException();
         if (var8 instanceof MBeanException) {
            throw (MBeanException)var8;
         }

         if (var8 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var8;
         }

         if (var8 instanceof ReflectionException) {
            throw (ReflectionException)var8;
         }

         if (var8 instanceof IOException) {
            throw (IOException)var8;
         }

         throw new RuntimeException((Throwable)(var8 == null ? var12 : var8));
      } finally {
         this.removeJMXContext();
      }

      return var6;
   }

   public String getDefaultDomain(final Subject var1) throws IOException {
      String var2 = null;

      try {
         var2 = (String)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getDefaultDomain(var1);
            }
         });
         return var2;
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new RuntimeException((Throwable)(var4 == null ? var5 : var4));
         }
      }
   }

   public String[] getDomains(final Subject var1) throws IOException {
      String[] var2 = null;

      try {
         var2 = (String[])((String[])Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getDomains(var1);
            }
         }));
         return var2;
      } catch (PrivilegedActionException var5) {
         Exception var4 = var5.getException();
         if (var4 instanceof IOException) {
            throw (IOException)var4;
         } else {
            throw new RuntimeException((Throwable)(var4 == null ? var5 : var4));
         }
      }
   }

   private void initializeJMXContext(Subject var1) {
      Locale var2 = (Locale)this.localeMap.get(var1);
      boolean var3 = !this.defaultLocale || var2 != null;
      if (var2 == null) {
         var2 = this.locale;
      }

      JMXContext var4 = JMXContextHelper.getJMXContext(true);
      if (var4.getLocale() == null || var3) {
         var4.setLocale(var2);
      }

      JMXContextHelper.putJMXContext(var4);
   }

   private void removeJMXContext() {
      JMXContextHelper.removeJMXContext();
   }

   public MBeanInfo getMBeanInfo(final ObjectName var1, final Subject var2) throws InstanceNotFoundException, IntrospectionException, ReflectionException, IOException {
      MBeanInfo var3 = null;

      try {
         this.initializeJMXContext(var2);
         var3 = (MBeanInfo)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.getMBeanInfo(var1, var2);
            }
         });
      } catch (PrivilegedActionException var9) {
         Exception var5 = var9.getException();
         if (var5 instanceof IntrospectionException) {
            throw (IntrospectionException)var5;
         }

         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         }

         if (var5 instanceof ReflectionException) {
            throw (ReflectionException)var5;
         }

         if (var5 instanceof IOException) {
            throw (IOException)var5;
         }

         throw new RuntimeException((Throwable)(var5 == null ? var9 : var5));
      } finally {
         this.removeJMXContext();
      }

      return var3;
   }

   public boolean isInstanceOf(final ObjectName var1, final String var2, final Subject var3) throws InstanceNotFoundException, IOException {
      Boolean var4 = null;

      try {
         var4 = (Boolean)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.isInstanceOf(var1, var2, var3);
            }
         });
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         }

         if (var6 instanceof IOException) {
            throw (IOException)var6;
         }

         throw new RuntimeException((Throwable)(var6 == null ? var7 : var6));
      }

      return var4;
   }

   public void addNotificationListener(final ObjectName var1, final ObjectName var2, final MarshalledObject var3, final MarshalledObject var4, final Subject var5) throws InstanceNotFoundException, IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.addNotificationListener(var1, var2, var3, var4, var5);
               return null;
            }
         });
      } catch (PrivilegedActionException var8) {
         Exception var7 = var8.getException();
         if (var7 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var7;
         } else if (var7 instanceof IOException) {
            throw (IOException)var7;
         } else {
            throw new RuntimeException((Throwable)(var7 == null ? var8 : var7));
         }
      }
   }

   public void removeNotificationListener(final ObjectName var1, final ObjectName var2, final Subject var3) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.removeNotificationListener(var1, var2, var3);
               return null;
            }
         });
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         } else if (var5 instanceof ListenerNotFoundException) {
            throw (ListenerNotFoundException)var5;
         } else if (var5 instanceof IOException) {
            throw (IOException)var5;
         } else {
            throw new RuntimeException((Throwable)(var5 == null ? var6 : var5));
         }
      }
   }

   public void removeNotificationListener(final ObjectName var1, final ObjectName var2, final MarshalledObject var3, final MarshalledObject var4, final Subject var5) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.removeNotificationListener(var1, var2, var3, var4, var5);
               return null;
            }
         });
      } catch (PrivilegedActionException var8) {
         Exception var7 = var8.getException();
         if (var7 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var7;
         } else if (var7 instanceof ListenerNotFoundException) {
            throw (ListenerNotFoundException)var7;
         } else if (var7 instanceof IOException) {
            throw (IOException)var7;
         } else {
            throw new RuntimeException((Throwable)(var7 == null ? var8 : var7));
         }
      }
   }

   public Integer[] addNotificationListeners(final ObjectName[] var1, final MarshalledObject[] var2, final Subject[] var3) throws InstanceNotFoundException, IOException {
      Integer[] var4 = null;

      try {
         var4 = (Integer[])((Integer[])Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.addNotificationListeners(var1, var2, var3);
            }
         }));
         return var4;
      } catch (PrivilegedActionException var7) {
         Exception var6 = var7.getException();
         if (var6 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var6;
         } else if (var6 instanceof IOException) {
            throw (IOException)var6;
         } else {
            throw new RuntimeException((Throwable)(var6 == null ? var7 : var6));
         }
      }
   }

   public void removeNotificationListeners(final ObjectName var1, final Integer[] var2, final Subject var3) throws InstanceNotFoundException, ListenerNotFoundException, IOException {
      try {
         Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               RMIConnectionWrapper.this.connection.removeNotificationListeners(var1, var2, var3);
               return null;
            }
         });
      } catch (PrivilegedActionException var6) {
         Exception var5 = var6.getException();
         if (var5 instanceof InstanceNotFoundException) {
            throw (InstanceNotFoundException)var5;
         } else if (var5 instanceof ListenerNotFoundException) {
            throw (ListenerNotFoundException)var5;
         } else if (var5 instanceof IOException) {
            throw (IOException)var5;
         } else {
            throw new RuntimeException((Throwable)(var5 == null ? var6 : var5));
         }
      }
   }

   public NotificationResult fetchNotifications(final long var1, final int var3, final long var4) throws IOException {
      NotificationResult var6 = null;

      try {
         var6 = (NotificationResult)Security.runAs(this.subject, new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               return RMIConnectionWrapper.this.connection.fetchNotifications(var1, var3, var4);
            }
         });
         return var6;
      } catch (PrivilegedActionException var9) {
         Exception var8 = var9.getException();
         if (var8 instanceof IOException) {
            throw (IOException)var8;
         } else {
            throw new RuntimeException((Throwable)(var8 == null ? var9 : var8));
         }
      }
   }
}
