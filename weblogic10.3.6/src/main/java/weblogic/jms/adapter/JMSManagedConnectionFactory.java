package weblogic.jms.adapter;

import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import javax.jms.JMSException;
import javax.resource.ResourceException;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.security.PasswordCredential;
import javax.security.auth.Subject;
import weblogic.jms.JMSLogger;
import weblogic.jms.bridge.AdapterConnection;
import weblogic.jms.bridge.AdapterConnectionFactory;
import weblogic.jms.bridge.ConnectionSpec;
import weblogic.jms.bridge.SourceConnection;
import weblogic.jms.bridge.TargetConnection;

public class JMSManagedConnectionFactory implements ManagedConnectionFactory, Serializable {
   static final long serialVersionUID = -8319737096410419555L;
   private static final String JNDI_FACTORY = "weblogic.jndi.WLInitialContextFactory";
   private String clientName;
   private String url;
   private String icFactory;
   private String cfJNDI;
   private String destJNDI;
   private String destType;
   private String adapterType;
   private AdapterConnectionFactory factory;
   private boolean isXA;
   private transient PrintWriter logWriter;
   private static transient DateFormat dformat;
   public static final transient Locale MY_LOCALE = Locale.getDefault();

   public JMSManagedConnectionFactory() {
      dformat = DateFormat.getDateTimeInstance(2, 1, MY_LOCALE);
   }

   public Object createConnectionFactory() throws ResourceException {
      this.factory = new JMSBaseConnectionFactory(this, (ConnectionManager)null);
      return this.factory;
   }

   public Object createConnectionFactory(ConnectionManager var1) throws ResourceException {
      this.factory = new JMSBaseConnectionFactory(this, var1);
      return this.factory;
   }

   public synchronized ManagedConnection createManagedConnection(Subject var1, ConnectionRequestInfo var2) throws ResourceException {
      try {
         Object var3 = null;
         String var14 = null;
         int var5 = 0;
         PasswordCredential var7 = Util.getPasswordCredential(this, var1, var2);
         JMSConnectionRequestInfo var8 = (JMSConnectionRequestInfo)var2;
         JMSConnectionSpec var6;
         if (var7 == null) {
            if (var2 == null) {
               var6 = (JMSConnectionSpec)null;
            } else {
               var6 = new JMSConnectionSpec((String)null, (String)null, var8.getUrl(), var8.getInitialContextFactory(), var8.getSelector(), var8.getFactoryJndi(), var8.getDestJndi(), var8.getDestType(), var8.getName(), var8.getDurability(), var8.getClasspath(), var8.getPreserveMsgProperty());
               var5 = var8.getType();
            }
         } else {
            var14 = var7.getUserName();
            char[] var9 = var7.getPassword();
            String var10 = new String(var9);
            if (var2 == null) {
               var6 = new JMSConnectionSpec(var14, var10);
            } else {
               var6 = new JMSConnectionSpec(var14, var10, var8.getUrl(), var8.getInitialContextFactory(), var8.getSelector(), var8.getFactoryJndi(), var8.getDestJndi(), var8.getDestType(), var8.getName(), var8.getDurability(), var8.getClasspath(), var8.getPreserveMsgProperty());
               var5 = var8.getType();
            }
         }

         if (var5 == 1) {
            var3 = this.createSourceConnection(var6);
         } else if (var5 == 2) {
            var3 = this.createTargetConnection(var6);
         } else {
            var3 = this.createConnection(var6);
         }

         JMSManagedConnection var15 = new JMSManagedConnection(this, var14, (AdapterConnection)var3, var8, true, true);
         ((JMSBaseConnection)var3).setManagedConnection(var15);
         if (this.logWriter != null) {
            var15.setLogWriter(this.logWriter);
         }

         if (var5 == 1) {
            if (var8.getDestJndi() == null) {
               printInfo(this.logWriter, this.clientName, "Source connection created to " + this.destJNDI);
            } else {
               printInfo(this.logWriter, this.clientName, "Source connection created to " + var8.getDestJndi());
            }
         } else if (var5 == 2) {
            if (var8.getDestJndi() == null) {
               printInfo(this.logWriter, this.clientName, "Target connection created to " + this.destJNDI);
            } else {
               printInfo(this.logWriter, this.clientName, "Target connection created to " + var8.getDestJndi());
            }
         }

         synchronized(var3) {
            ((AdapterConnection)var3).start();
         }

         return var15;
      } catch (JMSException var13) {
         EISSystemException var4 = new EISSystemException("JMSException: " + var13.getMessage());
         var4.setLinkedException(var13);
         throw var4;
      }
   }

   public synchronized ManagedConnection matchManagedConnections(Set var1, Subject var2, ConnectionRequestInfo var3) throws ResourceException {
      PasswordCredential var4 = Util.getPasswordCredential(this, var2, var3);
      String var5 = null;
      if (var4 != null) {
         var5 = var4.getUserName();
      }

      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         Object var7 = var6.next();
         if (var7 instanceof JMSManagedConnection) {
            JMSManagedConnection var8 = (JMSManagedConnection)var7;
            ManagedConnectionFactory var9 = var8.getManagedConnectionFactory();
            if (Util.isEqual(var8.getUserName(), var5) && var9.equals(this) && var8.getConnectionRequestInfo().equals(var3)) {
               printInfo(this.logWriter, this.clientName, "(ManagedConnectionFactory) Found a matched managed connection");
               return var8;
            }
         }
      }

      return null;
   }

   public synchronized void setLogWriter(PrintWriter var1) throws ResourceException {
      this.logWriter = var1;

      try {
         if (this.factory != null) {
            this.factory.setLogWriter(var1);
         }

      } catch (Exception var4) {
         ResourceException var3 = new ResourceException("Failed to set ManagedConnectionFactory's log writer");
         var3.setLinkedException(var4);
         throw var3;
      }
   }

   public synchronized PrintWriter getLogWriter() throws ResourceException {
      return this.logWriter;
   }

   public synchronized String getConnectionURL() {
      return this.url;
   }

   public synchronized void setConnectionURL(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.url = var1;
      }

   }

   public synchronized String getInitialContextFactory() {
      return this.icFactory;
   }

   public synchronized void setInitialContextFactory(String var1) {
      this.icFactory = var1;
   }

   public synchronized String getConnectionFactoryJNDI() {
      return this.cfJNDI;
   }

   public synchronized void setConnectionFactoryJNDI(String var1) {
      if (this.cfJNDI == null) {
         this.cfJNDI = var1;
      }

   }

   public synchronized String getConnectionFactoryJNDIName() {
      return this.cfJNDI;
   }

   public synchronized void setConnectionFactoryJNDIName(String var1) {
      this.cfJNDI = var1;
   }

   public synchronized String getDestinationJNDI() {
      return this.destJNDI;
   }

   public synchronized void setDestinationJNDI(String var1) {
      if (this.destJNDI == null) {
         this.destJNDI = var1;
      }

   }

   public synchronized String getDestinationJNDIName() {
      return this.destJNDI;
   }

   public synchronized void setDestinationJNDIName(String var1) {
      this.destJNDI = var1;
   }

   public synchronized String getDestinationType() {
      return this.destType;
   }

   public synchronized void setDestinationType(String var1) {
      this.destType = var1;
   }

   public synchronized String getAdapterType() {
      return this.adapterType;
   }

   public synchronized void setAdapterType(String var1) {
      this.adapterType = var1;
   }

   private AdapterConnection createConnectionInternal(ConnectionSpec var1, int var2) throws JMSException {
      JMSBaseConnection var3 = null;

      Exception var5;
      try {
         String var4 = null;
         var5 = null;
         String var6 = null;
         String var7 = null;
         String var8 = null;
         String var9 = null;
         boolean var10 = false;
         String var11 = null;
         String var12 = null;
         String var13 = null;
         boolean var14 = false;
         String var18;
         if (var1 == null) {
            var7 = this.destJNDI;
            var4 = this.cfJNDI;
            var6 = this.url;
            var18 = this.icFactory;
            var8 = this.destType;
         } else {
            if (!(var1 instanceof JMSConnectionSpec)) {
               throw new JMSException("Illegal ConnectionSpec format");
            }

            JMSConnectionSpec var15 = (JMSConnectionSpec)var1;
            if ((var7 = var15.getDestJndi()) == null) {
               var7 = this.destJNDI;
            }

            if ((var8 = var15.getDestType()) == null) {
               var8 = this.destType;
            }

            if ((var6 = var15.getUrl()) == null) {
               var6 = this.url;
            }

            if ((var18 = var15.getInitialContextFactory()) == null) {
               var18 = this.icFactory;
            }

            if (var15.getFactoryJndi() == null) {
               var4 = this.cfJNDI;
            }

            if ((var4 = var15.getFactoryJndi()) == null) {
               var4 = this.cfJNDI;
            }

            var9 = var15.getSelector();
            this.clientName = var15.getName();
            var10 = var15.getDurability();
            var11 = var15.getUser();
            var12 = var15.getPassword();
            var13 = var15.getClasspath();
            var14 = var15.getPreserveMsgProperty();
         }

         var3 = new JMSBaseConnection(var11, var12, this, this.clientName, var6, var18, var4, var7, var8, var9, var10, var13, var14);
      } catch (JMSException var16) {
         var5 = var16.getLinkedException();
         JMSLogger.logStackTrace(var16);
         if (var5 != null) {
            JMSLogger.logStackTraceLinked(var5);
         }

         throw var16;
      } catch (Throwable var17) {
         JMSLogger.logStackTrace(var17);
         this.throwJMSException("Failed to get connection to an adapter", (Exception)null);
      }

      return var3;
   }

   private AdapterConnection createConnection(ConnectionSpec var1) throws JMSException {
      return this.createConnectionInternal(var1, 0);
   }

   private SourceConnection createSourceConnection(ConnectionSpec var1) throws JMSException {
      return (SourceConnection)this.createConnectionInternal(var1, 1);
   }

   private TargetConnection createTargetConnection(ConnectionSpec var1) throws JMSException {
      return (TargetConnection)this.createConnectionInternal(var1, 2);
   }

   synchronized boolean isWLSAdapter() {
      return this.icFactory.equals("weblogic.jndi.WLInitialContextFactory");
   }

   private void throwJMSException(String var1, Exception var2) throws JMSException {
      JMSException var3 = new JMSException(var1);
      if (var2 != null) {
         var3.setLinkedException(var2);
      }

      throw var3;
   }

   public static synchronized void printInfo(PrintWriter var0, String var1, String var2) {
      if (var0 != null) {
         var0.checkError();
         long var3 = System.currentTimeMillis();
         Date var5 = new Date(var3);
         var0.println("<" + dformat.format(var5) + "> Info: " + var1 + " : " + var2);
      }

   }

   public static synchronized void printError(PrintWriter var0, String var1, String var2) {
      if (var0 != null) {
         var0.checkError();
         long var3 = System.currentTimeMillis();
         Date var5 = new Date(var3);
         var0.println("<" + dformat.format(var5) + "> Error: " + var1 + " : " + var2);
      }

   }

   public synchronized boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof JMSManagedConnectionFactory)) {
         return false;
      } else {
         boolean var10000;
         label76: {
            label69: {
               String var2 = ((JMSManagedConnectionFactory)var1).url;
               String var3 = this.url;
               String var4 = ((JMSManagedConnectionFactory)var1).icFactory;
               String var5 = this.icFactory;
               String var6 = ((JMSManagedConnectionFactory)var1).cfJNDI;
               String var7 = this.cfJNDI;
               String var8 = ((JMSManagedConnectionFactory)var1).destJNDI;
               String var9 = this.destJNDI;
               String var10 = ((JMSManagedConnectionFactory)var1).destType;
               String var11 = this.destType;
               String var12 = ((JMSManagedConnectionFactory)var1).adapterType;
               String var13 = this.adapterType;
               if (var2 == null) {
                  if (var3 != null) {
                     break label69;
                  }
               } else if (!var2.equals(var3)) {
                  break label69;
               }

               if (var4 == null) {
                  if (var5 != null) {
                     break label69;
                  }
               } else if (!var4.equals(var5)) {
                  break label69;
               }

               if (var6 == null) {
                  if (var7 != null) {
                     break label69;
                  }
               } else if (!var6.equals(var7)) {
                  break label69;
               }

               if (var8 == null) {
                  if (var9 != null) {
                     break label69;
                  }
               } else if (!var8.equals(var9)) {
                  break label69;
               }

               if (var10 == null) {
                  if (var11 != null) {
                     break label69;
                  }
               } else if (!var10.equals(var11)) {
                  break label69;
               }

               if (var12 == null) {
                  if (var13 == null) {
                     break label76;
                  }
               } else if (var12.equals(var13)) {
                  break label76;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public synchronized int hashCode() {
      String var1 = "";
      if (this.url != null) {
         var1 = var1 + this.url;
      }

      if (this.icFactory != null) {
         var1 = var1 + this.icFactory;
      }

      if (this.cfJNDI != null) {
         var1 = var1 + this.cfJNDI;
      }

      if (this.destJNDI != null) {
         var1 = var1 + this.destJNDI;
      }

      if (this.destType != null) {
         var1 = var1 + this.destType;
      }

      if (this.adapterType != null) {
         var1 = var1 + this.adapterType;
      }

      return var1.hashCode();
   }
}
