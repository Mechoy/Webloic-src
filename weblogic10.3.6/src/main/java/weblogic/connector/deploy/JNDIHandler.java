package weblogic.connector.deploy;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.security.AccessController;
import java.util.Hashtable;
import javax.naming.CompositeName;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import javax.resource.Referenceable;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.ResourceAdapter;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.exception.RACommonException;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.outbound.RAOutboundManager;
import weblogic.jndi.Environment;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JNDIHandler implements ObjectFactory {
   private static Context initialCtx = null;
   private static Hashtable keyNameToOutboundManagerMap = new Hashtable();
   protected static Hashtable keyNameToAdminObj = new Hashtable();
   protected static Hashtable keyNameToRA = new Hashtable();
   public static final String J2CA_CONTEXTNAME = "j2ca";
   public static final String ADMINOBJECT_NODENAME = "AdminObject";
   public static final String CONNECTIONFACTORY_NODENAME = "ConnectionFactory";
   public static final String ADMINOBJECT_CONTEXTNAME = "j2ca/AdminObject";
   public static final String CONNECTIONFACTORY_CONTEXTNAME = "j2ca/ConnectionFactory";
   public static final String THIS_FACTORY_NAME = JNDIHandler.class.getName();

   public static boolean verifyJNDIName(String var0) throws RAException {
      if (var0 != null && !var0.equals("")) {
         return isJndiNameBound(var0);
      } else {
         String var1 = Debug.getExceptionJndiNameNull();
         throw new RAException(var1);
      }
   }

   public static boolean verifyResourceLink(String var0, Context var1) throws RAException {
      if (var0 != null && !var0.equals("")) {
         return isResourceLinkAlreadyBound(var0, var1);
      } else {
         String var2 = Debug.getExceptionResourceLinkNull();
         throw new RAException(var2);
      }
   }

   public static void bindConnectionFactory(OutboundInfo var0, RAOutboundManager var1, Object var2) throws DeploymentException {
      Debug.enter("JNDIHandler.jndiBind(...) ");
      if (var0 == null) {
         Debug.throwAssertionError("OutboundInfo is null");
      }

      if (var1 == null) {
         Debug.throwAssertionError("RAOutboundManager is null");
      }

      String var3 = var0.getJndiName();
      String var4 = var1.getRA().getVersionId();
      String var5 = getJndiNameAndVersion(var3, var4);

      try {
         String var7;
         try {
            Debug.println("Binding to the JNDI tree : " + var3);
            boolean var6 = isJndiNameBound(var3);
            var7 = Debug.getExceptionAlreadyDeployed(var3);
            assertDeployment(var6, var7);
            Reference var8 = null;
            if (var2 instanceof Remote) {
               Debug.println(".bindConnectionFactory() connFactory " + var2 + " is instanceof java.rmi.Remote");
               var8 = new Reference(var0.getCFImpl(), new StringRefAddr("keyName", var5), TransportableJNDIHandler.class.getName(), (String)null);
               if (ServerHelper.isClusterable((Remote)var2)) {
                  ServerHelper.exportObject((Remote)var2, var3);
               } else {
                  ServerHelper.exportObject((Remote)var2);
               }
            } else {
               Debug.println(".bindConnectionFactory() connFactory " + var2 + " is not instanceof java.rmi.Remote");
               var8 = new Reference(var0.getCFImpl(), new StringRefAddr("keyName", var5), THIS_FACTORY_NAME, (String)null);
            }

            Debug.println("Adding to the keyNameToOutboundManagerMap : " + var5);
            keyNameToOutboundManagerMap.put(var5, var1);
            if (var2 instanceof Referenceable) {
               ((Referenceable)var2).setReference(var8);
            }

            getInitialContext().bind(new CompositeName(var3), var8);
         } catch (InvalidNameException var15) {
            var7 = Debug.getExceptionBindingFailed(var3, var15.toString());
            throw new DeploymentException(var7, var15);
         } catch (NamingException var16) {
            var7 = Debug.getExceptionBindingFailed(var3, var16.toString());
            throw new DeploymentException(var7, var16);
         } catch (Throwable var17) {
            var7 = Debug.getExceptionBindingFailed(var3, var17.toString());
            throw new DeploymentException(var7, var17);
         }
      } finally {
         Debug.exit("JNDIHandler.jndiBind(...) ");
      }

   }

   public static void bindAppScopedConnectionFactory(OutboundInfo var0, RAOutboundManager var1, ApplicationContext var2, Context var3, String var4) throws DeploymentException {
      Debug.enter("JNDIHandler.bindAppScopedConnectionFactory(...) ");
      if (var0 == null) {
         Debug.throwAssertionError("OutboundInfo is null");
      }

      if (var1 == null) {
         Debug.throwAssertionError("RAOutboundManager is null");
      }

      String var5 = var0.getResourceLink();

      try {
         String var7;
         try {
            Debug.println("Binding to the App-Scoped JNDI tree : " + var5);
            boolean var6 = isResourceLinkAlreadyBound(var4 + "#" + var5, var3);
            var7 = Debug.getExceptionAlreadyDeployed(var5);
            assertDeployment(var6, var7);
            String var8 = var2.getApplicationId() + "/" + var5;
            Reference var9 = new Reference(var0.getCFImpl(), new StringRefAddr("keyName", var8), THIS_FACTORY_NAME, (String)null);
            Debug.println("Adding to the keyNameToOutboundManagerMap : " + var8 + " : " + var1);
            keyNameToOutboundManagerMap.put(var8, var1);
            var3.bind(new CompositeName(var4 + "#" + var5), var9);
            Context var10 = getInitialContext();
            String var11 = "weblogic." + var2.getApplicationId() + "." + var5;
            var10.bind(new CompositeName(var11), new LinkRef("java:app/j2ca/ConnectionFactory/" + var4 + "#" + var5));
         } catch (InvalidNameException var18) {
            var7 = Debug.getExceptionBindingFailed(var5, var18.toString());
            throw new DeploymentException(var7, var18);
         } catch (NamingException var19) {
            var7 = Debug.getExceptionBindingFailed(var5, var19.toString());
            throw new DeploymentException(var7, var19);
         } catch (Throwable var20) {
            var7 = Debug.getExceptionBindingFailed(var5, var20.toString());
            throw new DeploymentException(var7, var20);
         }
      } finally {
         Debug.exit("JNDIHandler.bindAppScopedConnectionFactory(...) ");
      }

   }

   public static void assertDeployment(boolean var0, String var1) throws DeploymentException {
      if (var0) {
         throw new DeploymentException(var1);
      }
   }

   public static void unbindConnectionFactory(OutboundInfo var0, RAOutboundManager var1, Object var2) throws UndeploymentException {
      Debug.enter("JNDIHandler.unbindConnectionFactory(...) ");
      if (var0 == null) {
         Debug.throwAssertionError("OutboundInfo is null");
      }

      try {
         String var3 = var0.getJndiName();
         boolean var18 = var3 == null || var3.length() == 0;
         String var5 = getJndiNameAndVersion(var3, var1.getRA().getVersionId());
         if (!var18) {
            Context var6 = getInitialContext();
            Debug.println("Unbind JNDI name : " + var3);
            var6.unbind(new CompositeName(var3));
            Debug.println("Remove from keyNameToOutboundManagerMap : " + var5);
            keyNameToOutboundManagerMap.remove(var5);

            try {
               ServerHelper.unexportObject(var2, true, true);
            } catch (NoSuchObjectException var15) {
            }
         } else {
            String var19 = var0.getResourceLink();
            Debug.println("Unbind app-scoped ConnectionFactory, resource-link : " + var19);
            ApplicationContextInternal var7 = var1.getRA().getAppContext();
            var7.getEnvContext().unbind(new CompositeName("j2ca/ConnectionFactory/" + var1.getModuleName() + "#" + var19));
            Context var8 = getInitialContext();
            var8.unbind("weblogic." + var7.getApplicationId() + "." + var19);
            String var9 = var7.getApplicationId() + "/" + var19;
            Debug.println("Remove from keyNameToOutboundManagerMap : " + var9);
            keyNameToOutboundManagerMap.remove(var9);
         }
      } catch (NamingException var16) {
         String var4 = Debug.getExceptionNoInitialContextForUnbind(var16.toString());
         throw new UndeploymentException(var4, var16);
      } finally {
         Debug.exit("JNDIHandler.unbindConnectionFactory(...) ");
      }

   }

   public static void bindRA(String var0, ResourceAdapter var1, String var2) throws RACommonException {
      if (var1 == null) {
         Debug.throwAssertionError("ResourceAdapter is null");
      }

      if (var0 != null && !var0.equals("")) {
         try {
            String var3 = getJndiNameAndVersion(var0, var2);
            boolean var9 = isJndiNameBound(var0);
            String var5 = Debug.getExceptionJndiNameAlreadyBound(var0);
            assertDeployment(var9, var5);
            keyNameToRA.put(var3, var1);
            Reference var6 = null;
            if (var1 instanceof Remote) {
               var6 = new Reference(var1.getClass().toString(), new StringRefAddr("keyName", var3), TransportableJNDIHandler.class.getName(), (String)null);
               Remote var7;
               if (ServerHelper.isClusterable((Remote)var1)) {
                  var7 = ServerHelper.exportObject((Remote)var1, var0);
               } else {
                  var7 = ServerHelper.exportObject((Remote)var1);
               }

               if (Debug.isDeploymentEnabled()) {
                  Debug.deployment("Bind Remote RA " + var7 + " with JNDI name = " + var0);
               }
            } else {
               var6 = new Reference(var1.getClass().toString(), new StringRefAddr("keyName", var3), JNDIHandler.class.getName(), (String)null);
               if (Debug.isDeploymentEnabled()) {
                  Debug.deployment("Bind non-Remote RA ref" + var6 + " with JNDI name = " + var0);
               }
            }

            getInitialContext().bind(new CompositeName(var0), var6);
         } catch (Exception var8) {
            String var4 = Debug.getExceptionBindingFailed(var0, var8.toString());
            throw new RACommonException(var4, var8);
         }
      }

   }

   public static void unbindRA(String var0, ResourceAdapter var1, String var2) throws NamingException {
      if (var0 != null && !var0.equals("")) {
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Unbind RA JNDI name '" + var0 + "'");
         }

         getInitialContext().unbind(new CompositeName(var0));
         keyNameToRA.remove(getJndiNameAndVersion(var0, var2));

         try {
            ServerHelper.unexportObject(var1, true, true);
         } catch (NoSuchObjectException var4) {
         }
      }

   }

   public static void bindAdminObj(Object var0, String var1, String var2, RAInstanceManager var3) throws RACommonException {
      if (var1 == null || var1.trim().equals("")) {
         Debug.throwAssertionError("JNDI name null or blank");
      }

      if (var0 == null) {
         Debug.throwAssertionError("Administered object is null");
      }

      String var5;
      try {
         boolean var4 = isJndiNameBound(var1);
         var5 = Debug.getExceptionJndiNameAlreadyBound(var1);
         assertDeployment(var4, var5);
         String var6 = getJndiNameAndVersion(var1, var2);
         keyNameToAdminObj.put(var6, var0);
         Reference var7 = null;
         if (var0 instanceof Remote) {
            var7 = new Reference(var0.getClass().toString(), new StringRefAddr("keyName", var6), TransportableJNDIHandler.class.getName(), (String)null);
            if (ServerHelper.isClusterable((Remote)var0)) {
               ServerHelper.exportObject((Remote)var0, var1);
            } else {
               ServerHelper.exportObject((Remote)var0);
            }
         } else {
            var7 = new Reference(var0.getClass().toString(), new StringRefAddr("keyName", var6), JNDIHandler.class.getName(), (String)null);
         }

         getInitialContext().bind(new CompositeName(var1), var7);
         if (Debug.isDeploymentEnabled()) {
            AuthenticatedSubject var8 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
            Debug.deployment("Binding admin object '" + var3.getAdapterLayer().toString(var0, var8) + "' to JNDI name '" + var1 + "'.");
         }

      } catch (Exception var9) {
         var5 = Debug.getExceptionBindingFailed(var1, var9.toString());
         throw new RACommonException(var5, var9);
      }
   }

   public static void bindAppScopedAdminObj(Object var0, String var1, ApplicationContext var2, Context var3, String var4) throws RACommonException {
      if (var1 == null || var1.equals("")) {
         Debug.throwAssertionError("resourceLink is null or empty");
      }

      if (var0 == null) {
         Debug.throwAssertionError("Administer object is null");
      }

      String var6;
      try {
         boolean var5 = isResourceLinkAlreadyBound(var4 + "#" + var1, var3);
         var6 = Debug.getExceptionResourceLinkAlreadyBound(var1);
         assertDeployment(var5, var6);
         String var7 = var2.getApplicationId() + "/" + var1;
         keyNameToAdminObj.put(var7, var0);
         Reference var8 = new Reference(var0.getClass().toString(), new StringRefAddr("keyName", var7), THIS_FACTORY_NAME, (String)null);
         var3.bind(new CompositeName(var4 + "#" + var1), var8);
         Context var9 = getInitialContext();
         String var10 = "weblogic." + var2.getApplicationId() + "." + var1;
         String var11 = "java:app/j2ca/AdminObject/" + var4 + "#" + var1;
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Binding app-scoped admin object ref = '" + var11 + "' to JNDI name '" + var10 + "'.");
         }

         var9.bind(new CompositeName(var10), new LinkRef(var11));
      } catch (Exception var12) {
         var6 = Debug.getExceptionAppScopedBindFailed(var4, var1, var12.toString());
         throw new RACommonException(var6, var12);
      }
   }

   public static void unbindAdminObj(AdminObjInfo var0, RAInstanceManager var1) throws UndeploymentException {
      String var2 = var0.getJndiName();

      String var4;
      try {
         String var3 = getJndiNameAndVersion(var2, var1.getVersionId());
         if (var2 != null && !var2.equals("")) {
            Debug.deployment("Unbind Admin Object, jndi-name : " + var2);
            getInitialContext().unbind(new CompositeName(var2));
            Debug.println("Remove from keyNameToAdminObj : " + var3);
            keyNameToAdminObj.remove(var3);
         } else {
            var4 = var0.getResourceLink();
            Debug.deployment("Unbind app-scoped Admin Object resource-link : " + var4);
            ApplicationContextInternal var5 = var1.getAppContext();
            var5.getEnvContext().unbind(new CompositeName("j2ca/AdminObject/" + var1.getModuleName() + "#" + var4));
            Context var6 = getInitialContext();
            var6.unbind("weblogic." + var5.getApplicationId() + "." + var4);
            String var7 = var5.getApplicationId() + "/" + var4;
            Debug.println("Remove from keyNameToAdminObj : " + var7);
            keyNameToAdminObj.remove(var7);
         }

      } catch (NamingException var8) {
         var4 = Debug.getExceptionUnbindAdminObjectFailed(var8.toString());
         throw new UndeploymentException(var4, var8);
      }
   }

   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      Reference var5 = null;
      String var6 = null;
      if (!(var1 instanceof Reference)) {
         return null;
      } else {
         var5 = (Reference)var1;
         var6 = var5.getFactoryClassName();
         return !var6.equals(THIS_FACTORY_NAME) ? null : this.lookupObject(var1, var2, var3, var4, var5, var6);
      }
   }

   protected Object lookupObject(Object var1, Name var2, Context var3, Hashtable var4, Reference var5, String var6) throws ResourceException, ApplicationServerInternalException, NoPermissionException, NameNotFoundException {
      Object var7 = null;

      Object var11;
      try {
         Debug.enter(this, "getObjectInstance( " + var1 + ", " + var2 + ", " + var3 + ", " + var4 + " )");
         String var8 = var5.getClassName();
         RefAddr var9 = var5.get("keyName");
         String var10 = null;
         if (var9 != null) {
            var10 = (String)var9.getContent();
         }

         Debug.println(this, ".getObjectInstance() FactoryClassName = " + var6 + ", ClassName = " + var8 + ", Ref = " + var9 + ", Key name = " + var10);
         if (var10 == null) {
            var11 = null;
            return var11;
         }

         var7 = this.getConnectionFactory(var10);
         if (var7 == null) {
            var7 = keyNameToAdminObj.get(var10);
            if (var7 == null) {
               var7 = keyNameToRA.get(var10);
            }
         }

         if (var7 == null) {
            throw new NameNotFoundException("No Object found:  " + var10);
         }

         var11 = var7;
      } finally {
         Debug.exit(this, "getObjectInstance(...) returning: " + var7);
      }

      return var11;
   }

   private static Context getInitialContext() throws NamingException {
      if (initialCtx == null) {
         Environment var0 = new Environment();
         var0.setCreateIntermediateContexts(true);
         var0.setReplicateBindings(false);
         initialCtx = var0.getInitialContext();
      }

      return initialCtx;
   }

   public static boolean isJndiNameBound(String var0) {
      Debug.enter("JNDIHandler.isJndiNameBound( " + var0 + " )");
      if (var0 == null || var0.trim().equals("")) {
         Debug.throwAssertionError("JNDI Name is null");
      }

      Context var1 = null;
      Object var2 = null;

      boolean var3;
      try {
         var1 = getInitialContext();
         var2 = var1.lookup(var0);
      } catch (NameNotFoundException var10) {
      } catch (Throwable var11) {
         Debug.throwAssertionError("No Initial Context for Jndi:  " + var11, var11);
      } finally {
         var3 = var2 != null;
         Debug.exit("JNDIHandler.isJndiNameBound( " + var0 + " ) returning " + var3);
      }

      return var3;
   }

   private static boolean isResourceLinkAlreadyBound(String var0, Context var1) {
      Debug.enter("JNDIHandler.isResourceLinkAlreadyBound( " + var0 + " )");
      if (var0 == null || var0.trim().equals("")) {
         Debug.throwAssertionError("ResourceLink is null or empty");
      }

      Object var2 = null;

      boolean var3;
      try {
         var2 = var1.lookup(var0);
      } catch (NameNotFoundException var10) {
      } catch (Throwable var11) {
         Debug.throwAssertionError("isResourceLinkAlreadyBound threw Exception:  " + var11);
      } finally {
         var3 = var2 != null;
         Debug.exit("JNDIHandler.isResourceLinkAlreadyBound( " + var0 + " ) returning " + var3);
      }

      return var3;
   }

   protected Object getConnectionFactory(String var1) throws ApplicationServerInternalException, NoPermissionException, ResourceException {
      Debug.enter(this, "getConnectionFactory( keyName = " + var1 + " ) ");
      Object var2 = null;

      Object var4;
      try {
         RAOutboundManager var3 = (RAOutboundManager)keyNameToOutboundManagerMap.get(var1);
         Debug.println(this, ".getConnectionFactory(): Got the outbound manager associated with the Key name : " + var1 + " : " + var3);
         if (var3 != null) {
            Debug.println("Get the connection factory from the outbound manager");
            var2 = var3.getConnectionFactory(var1);
         }

         var4 = var2;
      } finally {
         Debug.exit(this, "getConnectionFactory( " + var1 + " ) returning: " + var2);
      }

      return var4;
   }

   public static String getJndiNameAndVersion(String var0, String var1) {
      return var0 + "|" + var1;
   }
}
