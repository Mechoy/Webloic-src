package weblogic.ejb20.internal;

import java.io.Serializable;
import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.spi.PortableReplaceable;
import weblogic.logging.Loggable;
import weblogic.utils.AssertionError;

public final class EJBMetaDataImpl implements Serializable, EJBMetaData, PortableReplaceable {
   private static final long serialVersionUID = -7427414006230547782L;
   private final EJBHome ejbHome;
   private final String homeInterfaceClassName;
   private final String primaryKeyClassName;
   private final String remoteInterfaceClassName;
   private transient Class homeInterfaceClass;
   private transient Class primaryKeyClass;
   private transient Class remoteInterfaceClass;
   private final boolean isSession;
   private final boolean isStatelessSession;

   public EJBMetaDataImpl(EJBHome var1, Class var2, Class var3, Class var4, boolean var5, boolean var6) {
      if (var5 && var4 != null) {
         throw new AssertionError("PK class should be null for session beans!");
      } else {
         this.ejbHome = var1;
         this.homeInterfaceClass = var2;
         this.remoteInterfaceClass = var3;
         this.primaryKeyClass = var4;
         this.isSession = var5;
         this.isStatelessSession = var6;
         this.homeInterfaceClassName = var2.getName();
         this.remoteInterfaceClassName = var3.getName();
         if (var4 == null) {
            this.primaryKeyClassName = null;
         } else {
            this.primaryKeyClassName = var4.getName();
         }

      }
   }

   public EJBHome getEJBHome() {
      return this.ejbHome;
   }

   public Class getHomeInterfaceClass() {
      if (this.homeInterfaceClass == null) {
         try {
            this.homeInterfaceClass = this.ejbHome.getClass().getClassLoader().loadClass(this.homeInterfaceClassName);
         } catch (Exception var2) {
            throw new AssertionError("Unable to load home class: " + var2);
         }
      }

      return this.homeInterfaceClass;
   }

   public Class getPrimaryKeyClass() {
      if (this.isSession) {
         Loggable var1 = EJBLogger.logillegalAttemptToInvokeGetPrimaryKeyClassLoggable();
         throw new RuntimeException(var1.getMessage());
      } else {
         if (this.primaryKeyClass == null) {
            try {
               this.primaryKeyClass = this.ejbHome.getClass().getClassLoader().loadClass(this.primaryKeyClassName);
            } catch (Exception var2) {
               throw new AssertionError("Unable to load pk class: " + var2);
            }
         }

         return this.primaryKeyClass;
      }
   }

   public Class getRemoteInterfaceClass() {
      if (this.remoteInterfaceClass == null) {
         try {
            this.remoteInterfaceClass = this.ejbHome.getClass().getClassLoader().loadClass(this.remoteInterfaceClassName);
         } catch (Exception var2) {
            throw new AssertionError("Unable to load remote interface: " + var2);
         }
      }

      return this.remoteInterfaceClass;
   }

   public boolean isSession() {
      return this.isSession;
   }

   public boolean isStatelessSession() {
      return this.isStatelessSession;
   }
}
