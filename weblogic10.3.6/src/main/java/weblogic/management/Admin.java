package weblogic.management;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.jndi.Environment;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ProductionModeHelper;
import weblogic.management.provider.ManagementConstants;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.PropertyService;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

/** @deprecated */
public class Admin implements ManagementConstants {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean isPasswordEchoAllowed;

   private Admin() {
      this.isPasswordEchoAllowed = Boolean.getBoolean("weblogic.management.allowPasswordEcho");
   }

   public static final Admin getInstance() {
      return Admin.AdminSingleton.SINGLETON;
   }

   public static final boolean isAdminServer() {
      return ManagementService.getPropertyService(kernelId).isAdminServer();
   }

   public static boolean isBooting() {
      return ManagementService.getRuntimeAccess(kernelId) != null;
   }

   public static final synchronized String getAdminT3Url() {
      return ManagementService.getPropertyService(kernelId).getAdminBinaryURL();
   }

   public static boolean isConfigLoaded() {
      return ManagementService.getRuntimeAccess(kernelId) != null;
   }

   public boolean isProductionModeEnabled() {
      return !isConfigLoaded() ? ProductionModeHelper.getProductionModeProperty() : ManagementService.getRuntimeAccess(kernelId).getDomain().isProductionModeEnabled();
   }

   public boolean isPasswordEchoAllowed() {
      return this.isPasswordEchoAllowed;
   }

   public static synchronized void setAdminURL(String var0) throws MalformedURLException {
      ManagementService.getPropertyService(kernelId).setAdminHost(var0);
   }

   public static final Admin createInstance() {
      throw new UnsupportedOperationException("Admin.createInstance");
   }

   public final ServerMBean getLocalServer() {
      throw new UnsupportedOperationException("Admin.getLocalServer");
   }

   public final DomainMBean getActiveDomain() {
      return this.getMBeanHome().getActiveDomain();
   }

   public final String getDomainName() {
      throw new UnsupportedOperationException("Admin.getDomainName");
   }

   public final MBeanHome getMBeanHome(String var1) throws NamingException {
      Environment var2 = new Environment();
      Context var3 = var2.getInitialContext();
      return (MBeanHome)var3.lookup("weblogic.management.home." + var1);
   }

   public final MBeanHome getMBeanHome() {
      return this.lookupMBeanHome("weblogic.management.home.localhome");
   }

   private MBeanHome lookupMBeanHome(String var1) {
      try {
         Context var2 = (new Environment()).getInitialContext();
         return (MBeanHome)var2.lookup(var1);
      } catch (NamingException var3) {
         throw new AssertionError("MBeanHome lookup failed" + var3);
      }
   }

   public final MBeanHome getAdminMBeanHome() {
      return this.lookupMBeanHome("weblogic.management.adminhome");
   }

   public final DomainMBean getDomain() {
      try {
         WebLogicObjectName var1 = new WebLogicObjectName(this.getDomainName(), "Domain", this.getDomainName());
         return (DomainMBean)this.getAdminMBeanHome().getMBean(var1);
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   public final ServerMBean getServer() {
      try {
         return (ServerMBean)this.getAdminMBeanHome().getAdminMBean(getServerName(), "Server", this.getDomainName());
      } catch (Exception var2) {
         throw new AssertionError(var2);
      }
   }

   public final URL getAdminURL() {
      return ManagementService.getPropertyService(kernelId).getAdminURL();
   }

   public String getAdminServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getDomain().getAdminServerName();
   }

   public ServerIdentity getAdminIdentity() {
      return LocalServerIdentity.getIdentity();
   }

   private ServerMBean getAdminServerMBean() {
      throw new UnsupportedOperationException("Admin.getAdminServerMBean");
   }

   /** @deprecated */
   public int getMasterEmbeddedLDAPPortFromConfig() {
      throw new UnsupportedOperationException("Admin.getMasterEmbeddedLDAPPortFromConfig");
   }

   public boolean isLocalAdminServer() {
      return false;
   }

   /** @deprecated */
   public static final synchronized String getAdminHttpUrl() {
      ManagementService.getPropertyService(kernelId);
      return PropertyService.getAdminHttpUrl();
   }

   /** @deprecated */
   public static final String getServerName() {
      return ManagementService.getPropertyService(kernelId).getServerName();
   }

   // $FF: synthetic method
   Admin(Object var1) {
      this();
   }

   private static final class AdminSingleton {
      private static final Admin SINGLETON = new Admin();
   }
}
