package weblogic.jndi.internal;

import java.io.Serializable;
import java.rmi.ConnectIOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.OperationNotSupportedException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.ServiceAdvertiser;
import weblogic.jndi.JNDILogger;
import weblogic.management.provider.ManagementService;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.AuthorizationManager;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.JNDIResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.security.subject.SubjectManager;
import weblogic.utils.AssertionError;
import weblogic.utils.StringUtils;

public class ServerNamingNode extends BasicNamingNode {
   private static final String DEFAULT_NAME_SEPARATORS = "./";
   private static boolean areStaticsInitialized = false;
   private static ServiceAdvertiser advertiser;
   private static AuthorizationManager am = null;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private final AdminModeHandler adminHandler;
   private final VersionHandler versionHandler;
   private String separators;

   private static void initializeStatics() {
      if (!areStaticsInitialized) {
         Class var0 = ServerNamingNode.class;
         synchronized(ServerNamingNode.class) {
            if (!areStaticsInitialized) {
               if (ManagementService.getRuntimeAccess(kernelId).getServer().getCluster() != null) {
                  advertiser = ServiceAdvertiser.theOne();
               }

               String var1 = "weblogicDEFAULT";
               am = (AuthorizationManager)SecurityServiceManager.getSecurityService(kernelId, var1, ServiceType.AUTHORIZE);
               if (am == null) {
                  throw new RuntimeException("Security Services Unavailable");
               }

               areStaticsInitialized = true;
            }
         }
      }

   }

   ServerNamingNode() {
      this("./", (ServerNamingNode)null, "");
   }

   public ServerNamingNode(String var1) {
      this(var1, (ServerNamingNode)null, "");
   }

   ServerNamingNode(String var1, ServerNamingNode var2, String var3) {
      this(var1, var2, var3, new ArrayList());
   }

   private ServerNamingNode(String var1, ServerNamingNode var2, String var3, ArrayList var4) {
      super(var1, var2, var3, var4);
      this.adminHandler = new AdminModeHandler(this);
      this.versionHandler = new VersionHandler(this);
      initializeStatics();
      this.separators = var1;
   }

   protected BasicNamingNode newSubnode(String var1) {
      return new ServerNamingNode(this.separators, this, var1, this.subtreeScopeNameListenerList);
   }

   public Context getContext(Hashtable var1) {
      try {
         String var2 = this.getNameInNamespace();
         ServerHelper.exportObject(this, var2);
         return new WLEventContextImpl(var1, this);
      } catch (RemoteException var3) {
         throw new AssertionError("Failed to create stub for " + this.getClass().getName(), var3);
      }
   }

   protected void bindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NoPermissionException, NamingException {
      String var5 = this.getNameInNamespace(var1);
      this.checkModify(var5);
      Name var6 = this.nameParser.parse(var5);
      Object var7 = WLNamingManager.getStateToBind(var2, var6, (Context)null, var3);
      boolean var8 = this.versionHandler.isBindVersioned();
      if (var4 && var8) {
         this.versionHandler.bindHere(var1, var7, var3);
         if (this.replicateBindings(var3)) {
            this.advertiseBinding(var5, var7);
         }
      } else {
         this.adminHandler.checkBind(var1, var8);
         super.bindHere(var1, var7, var3, false);
         if (var4 && this.replicateBindings(var3)) {
            this.advertiseBinding(var5, var7);
         }
      }

   }

   private void advertiseBinding(String var1, Object var2) throws NamingException {
      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ advertise bind(" + var1 + ", " + var2.getClass().getName() + ")");
      }

      if (!(var2 instanceof Serializable) && !(var2 instanceof Remote)) {
         JNDILogger.logCannotReplicateObjectInCluster(var1);
      } else {
         ServiceAdvertiser.theOne().offerService(var1, ApplicationVersionUtils.getBindApplicationId(), var2);
      }

   }

   protected Set listThis(Hashtable var1) throws NoPermissionException, NamingException {
      String var2 = this.getNameInNamespace();
      this.checkList(var2);
      return this.isVersioned() ? this.versionHandler.getAccessibleBindings(super.listThis(var1)) : this.adminHandler.getAccessibleBindings(super.listThis(var1));
   }

   protected Object lookupHere(String var1, Hashtable var2, String var3) throws NoPermissionException, NamingException {
      String var4 = this.getNameInNamespace(var1);
      this.checkLookup(var4);
      this.adminHandler.checkLookup(var1, var3, var2);
      Object var5 = super.lookupHere(var1, var2, var3);
      if (var3.length() > 0) {
         return var5;
      } else {
         if (var5 instanceof ServerNamingNode) {
            var5 = this.versionHandler.getCurrentVersion((ServerNamingNode)var5, var2);
         } else if (!this.isVersioned()) {
            this.versionHandler.checkGlobalResource(var5, var2);
         }

         return var5;
      }
   }

   Object superLookupHere(String var1, Hashtable var2, String var3) throws NoPermissionException, NamingException {
      return super.lookupHere(var1, var2, var3);
   }

   protected void rebindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NoPermissionException, NamingException {
      String var5 = this.getNameInNamespace(var1);
      this.checkModify(var5);
      Name var6 = this.nameParser.parse(var5);
      Object var7 = WLNamingManager.getStateToBind(var2, var6, (Context)null, var3);
      boolean var8 = this.versionHandler.isBindVersioned();
      if (var4 && var8) {
         this.versionHandler.rebindHere(var1, var7, var3);
         if (this.replicateBindings(var3)) {
            this.advertiseRebinding(var5, (Object)null, var7);
         }
      } else {
         this.adminHandler.checkBind(var1, var8);
         super.rebindHere(var1, var7, var3, false);
         if (var4 && this.replicateBindings(var3)) {
            this.advertiseRebinding(var5, (Object)null, var7);
         }
      }

   }

   protected void rebindHere(String var1, Object var2, Object var3, Hashtable var4, boolean var5) throws NoPermissionException, NamingException {
      String var6 = this.getNameInNamespace(var1);
      this.checkModify(var6);
      Name var7 = this.nameParser.parse(var6);
      Object var8 = WLNamingManager.getStateToBind(var3, var7, (Context)null, var4);
      boolean var9 = this.versionHandler.isBindVersioned();
      Object var10;
      if (var5 && var9) {
         this.versionHandler.rebindHere(var1, var8, var4);
         if (this.replicateBindings(var4)) {
            var10 = WLNamingManager.getStateToBind(var2, var7, (Context)null, var4);
            this.advertiseRebinding(var6, var10, var8);
         }
      } else {
         this.adminHandler.checkBind(var1, var9);
         super.rebindHere(var1, var8, var4, false);
         if (var5 && this.replicateBindings(var4)) {
            var10 = WLNamingManager.getStateToBind(var2, var7, (Context)null, var4);
            this.advertiseRebinding(var6, var10, var8);
         }
      }

   }

   private void advertiseRebinding(String var1, Object var2, Object var3) throws NamingException {
      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ advertise rebind(" + var1 + ", " + var3.getClass().getName() + ")");
      }

      ServiceAdvertiser.theOne().replaceService(var1, ApplicationVersionUtils.getBindApplicationId(), var2, var3);
   }

   protected NamingNode createSubnodeHere(String var1, Hashtable var2) throws NoPermissionException, NamingException {
      String var3 = this.getNameInNamespace(var1);
      this.checkModify(var3);
      NamingNode var4 = super.createSubnodeHere(var1, var2);
      if (this.replicateBindings(var2)) {
         if (NamingDebugLogger.isDebugEnabled()) {
            NamingDebugLogger.debug("+++ advertise createSubContext(" + var3 + ")");
         }

         ServiceAdvertiser.theOne().createSubcontext(var3);
      }

      return var4;
   }

   protected void destroySubnodeHere(String var1, Hashtable var2) throws NoPermissionException, NamingException {
      String var3 = this.getNameInNamespace(var1);
      this.checkModify(var3);
      super.destroySubnodeHere(var1, var2);
      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ destroySubContext(" + var3 + ")");
      }

   }

   public void unbindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NoPermissionException, NamingException {
      String var5 = this.getNameInNamespace(var1);
      this.checkModify(var5);
      boolean var6 = this.versionHandler.isBindVersioned();
      if (var4 && var6) {
         this.versionHandler.unbindHere(var1, var2, var3);
         if (this.replicateBindings(var3)) {
            this.advertiseUnbinding(var5, var2);
         }
      } else {
         this.adminHandler.checkUnbind(var1, var6);
         super.unbindHere(var1, var2, var3, false);
         if (var4 && this.replicateBindings(var3)) {
            this.advertiseUnbinding(var5, var2);
         }

         this.versionHandler.checkUnbind(var1, var3);
      }

   }

   private void advertiseUnbinding(String var1, Object var2) throws NamingException {
      if (NamingDebugLogger.isDebugEnabled()) {
         if (var2 == null) {
            NamingDebugLogger.debug("+++ advertise unbind(" + var1 + ")");
         } else {
            NamingDebugLogger.debug("+++ advertise unbind(" + var1 + ", " + var2.getClass().getName() + ")");
         }
      }

      ServiceAdvertiser.theOne().retractService(var1, ApplicationVersionUtils.getBindApplicationId(), var2);
   }

   public void rename(String var1, String var2, Hashtable var3) throws OperationNotSupportedException, NamingException, RemoteException {
      if (this.replicateBindings(var3)) {
         throw new OperationNotSupportedException("replicated rename not supported");
      } else {
         super.rename(var1, var2, var3);
      }
   }

   protected Object resolveObject(String var1, Object var2, int var3, Hashtable var4) throws NamingException {
      Object var5 = var2;
      if (var2 != null) {
         try {
            if (var2 instanceof NamingNode) {
               var5 = ((NamingNode)var2).getContext(var4);
            } else if (var3 != 0 && var3 >= 0) {
               CompositeName var6 = new CompositeName(var1);
               var5 = WLNamingManager.getObjectInstance(var2, var6, (Context)null, var4);
               var5 = this.makeTransportable(var5, var6, var4);
            }
         } catch (NamingException var8) {
            if (!NamingService.getNamingService().isRunning()) {
               var8.setRootCause(new ConnectIOException("Server is being shut down"));
            }

            throw var8;
         } catch (Exception var9) {
            NamingException var7 = this.fillInException(new ConfigurationException("Call to NamingManager.getObjectInstance() failed: "), var1, var2, (String)null);
            var7.setRootCause(var9);
            throw var7;
         }
      }

      return var5;
   }

   final boolean replicateBindings(Hashtable var1) {
      return advertiser != null && (var1 == null || !"false".equals(this.getProperty(var1, "weblogic.jndi.replicateBindings")));
   }

   private final void checkList(String var1) throws NoPermissionException {
      this.checkPermission(var1, "list");
   }

   private final void checkLookup(String var1) throws NoPermissionException {
      this.checkPermission(var1, "lookup");
   }

   private final void checkModify(String var1) throws NoPermissionException {
      this.checkPermission(var1, "modify");
   }

   private final void checkPermission(String var1, String var2) throws NoPermissionException {
      AuthenticatedSubject var3 = SecurityServiceManager.getCurrentSubject(kernelId);
      String[] var4 = StringUtils.splitCompletely(var1, "./");
      JNDIResource var5 = new JNDIResource((String)null, var4, var2);
      if (!am.isAccessAllowed(var3, var5, (ContextHandler)null)) {
         throw new NoPermissionException("User " + SubjectUtils.getUsername(var3) + " does not have permission on " + var1 + " to perform " + var2 + " operation.");
      }
   }

   protected boolean isVersioned() {
      return this.versionHandler.isVersioned();
   }

   final VersionHandler getVersionHandler() {
      return this.versionHandler;
   }
}
