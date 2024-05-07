package weblogic.servlet.internal.session;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Iterator;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.servlet.http.HttpSessionEvent;
import weblogic.application.AppClassLoaderManager;
import weblogic.application.ApplicationAccess;
import weblogic.cluster.replication.ApplicationUnavailableException;
import weblogic.cluster.replication.NotFoundException;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.Replicatable;
import weblogic.cluster.replication.ReplicationServices;
import weblogic.logging.Loggable;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.AttributeWrapper;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServerHelper;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.NestedError;
import weblogic.utils.classloaders.Annotation;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.io.Chunk;
import weblogic.utils.io.ChunkedObjectInputStream;
import weblogic.utils.io.UnsyncByteArrayInputStream;

public class ReplicatedSessionData extends SessionData implements Replicatable {
   private static final long serialVersionUID = -1080155161838278576L;
   protected ROID roid;
   protected String contextName;
   private String webserverName;
   private HttpServer srvr;
   private transient ReplicatedSessionChange change;
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
   private String secondaryJVMID;

   public ReplicatedSessionData() {
      this.secondaryJVMID = null;
      this.initializeChange();
   }

   public ReplicatedSessionData(String var1, SessionContext var2) {
      this(var1, var2, true);
   }

   protected ReplicatedSessionData(String var1, SessionContext var2, boolean var3) {
      super(var1, var2, var3);
      this.secondaryJVMID = null;
      this.initializeChange();
      this.srvr = this.getWebAppServletContext().getServer();
      this.contextName = this.getContextPath();
      if (this.srvr == WebService.defaultHttpServer()) {
         this.webserverName = "USE_DEFAULT_WEB_SERVER";
      } else {
         this.webserverName = this.srvr.getName();
      }

      this.roid = this.registerOrAdd(this.srvr, this.id, this.contextName);
      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("registerOrAdd returns " + this.roid);
      }

      if (!this.isValid) {
         HTTPSessionLogger.logSessionGotInvalidatedBeforeCreationCouldComplete(this.getWebAppServletContext().getLogContext(), this.id);
      } else if (DEBUG_SESSIONS.isDebugEnabled()) {
         DEBUG_SESSIONS.debug("sessionId:" + var1 + " associated with roid:" + this.roid);
      }

      this.reinitializeSecondary();
      this.getWebAppServletContext().getEventsManager().notifySessionLifetimeEvent(this, true);
      this.incrementActiveRequestCount();
      ((SessionContext)this.getSessionContext()).addSession(this.id, this);
      this.srvr.getReplicator().putPrimary(this.id, this.getROID(), this.contextName);
      ((SessionContext)this.getSessionContext()).incrementOpenSessionsCount();
   }

   protected void initializeChange() {
      this.change = new ReplicatedSessionChange();
   }

   protected ReplicatedSessionChange getSessionChange() {
      return this.change;
   }

   protected ROID registerOrAdd(HttpServer var1, String var2, String var3) {
      ROID var4 = var1.getReplicator().getPrimary(var2);
      if (var4 != null) {
         this.getReplicationServices().add(var4, this);
         return var4;
      } else {
         var4 = var1.getReplicator().getSecondary(var2);
         if (var4 != null) {
            var1.getReplicator().putPrimary(var2, var4, var3);
            this.getReplicationServices().add(var4, this);
            return var4;
         } else {
            return this.getReplicationServices().register(this).getROID();
         }
      }
   }

   ROID getROID() {
      return this.roid;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      GenericClassLoader var2 = null;
      String var3 = (String)var1.readObject();
      var2 = AppClassLoaderManager.getAppClassLoaderManager().findLoader(new Annotation(var3));
      Thread var4 = Thread.currentThread();
      ClassLoader var5 = var4.getContextClassLoader();
      if (var2 != null) {
         var4.setContextClassLoader(var2);
      }

      try {
         this.attributes = convertToConcurrentHashMap(var1.readObject());
         this.setNew(false);
         this.isValid = var1.readBoolean();
         this.creationTime = var1.readLong();
         this.maxInactiveInterval = var1.readInt();
         this.id = (String)var1.readObject();
         this.roid = (ROID)var1.readObject();
         this.contextName = (String)var1.readObject();
         this.webserverName = (String)var1.readObject();
         this.internalAttributes = convertToConcurrentHashMap(var1.readObject());
         this.accessTime = var1.readLong();
         long var6 = var1.readLong();
         long var8 = System.currentTimeMillis() - var6;
         this.accessTime += var8;
         this.setupVersionIdFromAttrs();
      } finally {
         if (var2 != null) {
            var4.setContextClassLoader(var5);
         }

      }

      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("ReplicatedSessionData.readExternal: versionId=" + this.versionId + ", contextName=" + this.contextName + ", id=" + this.id + ", workCtxs=" + this.getWorkContexts());
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.getWebAppAnnotation());
      this.initVersionAttrsIfNeeded();
      var1.writeObject(convertToHashtable(this.attributes));
      var1.writeBoolean(this.isValid);
      var1.writeLong(this.creationTime);
      var1.writeInt(this.maxInactiveInterval);
      var1.writeObject(this.id);
      var1.writeObject(this.roid);
      var1.writeObject(this.contextName);
      var1.writeObject(this.webserverName);
      var1.writeObject(convertToHashtable(this.internalAttributes));
      var1.writeLong(this.accessTime);
      var1.writeLong(System.currentTimeMillis());
      if (DEBUG_APP_VERSION.isEnabled()) {
         HTTPLogger.logDebug("ReplicatedSessionData.writeExternal: versionId=" + this.versionId + ", contextName=" + this.contextName + ", id=" + this.id + ", workCtxs=" + this.getWorkContexts());
      }

   }

   private String getWebAppAnnotation() throws IOException {
      WebAppServletContext var1 = getServletContext(this.webserverName, this.contextName, this.versionId);
      if (var1 != null) {
         ClassLoader var2 = var1.getServletClassLoader();
         if (var2 != null && var2 instanceof GenericClassLoader) {
            return ((GenericClassLoader)var2).getAnnotation().getAnnotationString();
         }
      }

      return getAnnotation();
   }

   public void becomePrimary(ROID var1) {
      if (var1 != null) {
         this.roid = var1;
      }

      SessionContext var2 = this.getContext(this.webserverName, this.contextName);
      if (var2 == null) {
         HTTPSessionLogger.logContextNotFound(this.contextName, "becomePrimary");
         throw new RuntimeException("WebApp with contextPath: " + this.contextName + " not found in the secondary server");
      } else if (!(var2 instanceof ReplicatedSessionContext)) {
         HTTPSessionLogger.logPersistentStoreTypeNotReplicated(this.contextName, "becomePrimary");
         throw new RuntimeException("WebApp with contextPath: " + this.contextName + " is not replicatable in the secondary server");
      } else {
         ReplicatedSessionContext var3 = (ReplicatedSessionContext)var2;
         this.setSessionContext(var3);
         this.setLastAccessedTime(System.currentTimeMillis());
         this.reinitRuntimeMBean();
         this.getWebAppServletContext().getServer().getSessionLogin().register(this.id, this.getContextPath());
         if (!this.isNew()) {
            this.setForceToConvertAttributes(true);
         }

         ClassLoader var4 = Thread.currentThread().getContextClassLoader();

         try {
            Thread.currentThread().setContextClassLoader(this.getWebAppServletContext().getServletClassLoader());
            this.notifyActivated(new HttpSessionEvent(this));
         } finally {
            Thread.currentThread().setContextClassLoader(var4);
         }

         if (!this.isNew()) {
            var3.incrementOpenSessionsCount();
         }

         this.getHttpServer().getReplicator().putPrimary(this.id, var1, this.contextName);
         var3.addSession(this.id, this);
         if (this.isDebugEnabled()) {
            Loggable var5 = HTTPSessionLogger.logBecomePrimaryLoggable(this.id);
            DEBUG_SESSIONS.debug(var5.getMessage());
         }

      }
   }

   private void notifyActivated() {
      Thread var1 = Thread.currentThread();
      ClassLoader var2 = var1.getContextClassLoader();
      String var3 = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
      if (var3 == null) {
         this.getWebAppServletContext().pushEnvironment(var1);
      }

      try {
         this.notifyActivated(new HttpSessionEvent(this));
      } finally {
         if (var3 == null) {
            this.getWebAppServletContext();
            WebAppServletContext.popEnvironment(var1, var2);
         }

      }

   }

   public Object becomeSecondary(ROID var1) {
      this.roid = var1;
      SessionContext var2 = this.getContext(this.webserverName, this.contextName);
      if (var2 == null) {
         HTTPSessionLogger.logContextNotFound(this.contextName, "becomeSecondary");
         throw new ApplicationUnavailableException("WebApp with contextPath: " + this.contextName + " not found in the secondary server");
      } else if (!(var2 instanceof ReplicatedSessionContext)) {
         HTTPSessionLogger.logPersistentStoreTypeNotReplicated(this.contextName, "becomeSecondary");
         throw new ApplicationUnavailableException("WebApp with contextPath: " + this.contextName + " is not replicatable in the secondary server");
      } else {
         ReplicatedSessionContext var3 = (ReplicatedSessionContext)var2;
         this.setSessionContext(var3);
         if (var3.getOpenSession(this.id) != null) {
            var3.decrementOpenSessionsCount();
         }

         this.getHttpServer().getReplicator().putSecondary(this.id, var1, this.contextName);
         this.getHttpServer().getSessionLogin().unregister(this.id, this.getContextPath());
         this.unregisterRuntimeMBean();
         if (this.isDebugEnabled()) {
            Loggable var4 = HTTPSessionLogger.logBecomeSecondaryLoggable(this.id);
            DEBUG_SESSIONS.debug(var4.getMessage());
         }

         return LocalServerIdentity.getIdentity();
      }
   }

   public void becomeUnregistered(ROID var1) {
      SessionContext var2 = this.getContext(this.webserverName, this.contextName);
      if (var2 != null) {
         ReplicatedSessionContext var3 = (ReplicatedSessionContext)var2;
         if (var3.getOpenSession(this.id) != null) {
            var3.decrementOpenSessionsCount();
         }

         this.getHttpServer().getReplicator().removeSecondary(this.id, this.contextName);
         this.getHttpServer().getSessionLogin().unregister(this.id, this.getContextPath());
      }

      if (this.isDebugEnabled()) {
         Loggable var4 = HTTPSessionLogger.logUnregisterLoggable(this.id);
         DEBUG_SESSIONS.debug(var4.getMessage());
      }

   }

   public void update(ROID var1, Serializable var2) {
      if (this.isDebugEnabled()) {
         Debug.assertion(var2 instanceof ReplicatedSessionChange);
      }

      if (var2 != null) {
         ReplicatedSessionChange var3 = null;

         try {
            var3 = (ReplicatedSessionChange)var2;
         } catch (ClassCastException var5) {
            if (this.isDebugEnabled()) {
               DEBUG_SESSIONS.debug("Failed to update the secondary: ", var5);
            }

            return;
         }

         this.applySessionChange(var3);
         if (this.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Received Change = " + var3.toString() + ", workCtxs=" + this.getWorkContexts());
         }

      }
   }

   protected void applySessionChange(ReplicatedSessionChange var1) {
      this.maxInactiveInterval = var1.getMaxInActiveInterval();
      this.accessTime = var1.getLastAccessTime();
      long var2 = System.currentTimeMillis() - var1.getTimeOnPrimaryAtUpdate();
      this.accessTime += var2;
      Iterator var4;
      Object var5;
      Object var6;
      if (!var1.getAttributeChanges().isEmpty()) {
         var4 = var1.getAttributeChanges().keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var6 = var1.getAttributeChanges().get(var5);
            this.justPutValue((String)var5, var6, var1.isUseLazyDeserialization());
         }
      }

      if (!var1.getInternalAttributeChanges().isEmpty()) {
         var4 = var1.getInternalAttributeChanges().keySet().iterator();

         while(var4.hasNext()) {
            var5 = var4.next();
            var6 = var1.getInternalAttributeChanges().get(var5);
            if (var6 != null) {
               super.setInternalAttribute((String)var5, var6);
               if (var5.equals("weblogic.workContexts")) {
                  this.updateWorkContextsIfNeeded();
               }
            } else {
               super.removeInternalAttribute((String)var5);
            }
         }
      }

   }

   private Object checkEJBAttribute(Object var1) {
      if (var1 != null && var1 instanceof AttributeWrapper) {
         AttributeWrapper var2 = (AttributeWrapper)var1;
         if (var1 instanceof EJBAttributeWrapper) {
            try {
               Handle var3 = (Handle)var2.getObject();
               if (var3 != null) {
                  var1 = var3.getEJBObject();
               }
            } catch (Exception var5) {
               HTTPSessionLogger.logErrorReconstructingEJBObject(var1.toString(), var5);
            }
         } else if (var1 != null && var1 instanceof EJBHomeAttributeWrapper) {
            try {
               HomeHandle var6 = (HomeHandle)var2.getObject();
               if (var6 != null) {
                  var1 = var6.getEJBHome();
               } else {
                  var1 = null;
               }
            } catch (Exception var4) {
               HTTPSessionLogger.logErrorReconstructingEJBHome(var1.toString(), var4);
            }
         }
      }

      return var1;
   }

   private void justPutValue(String var1, Object var2, boolean var3) {
      if (var2 != null) {
         if (var3) {
            this.attributes.put(var1, var2);
         } else {
            Object var4 = this.checkEJBAttribute(var2);
            SessionData.AttributeInfo var5 = this.validateAttributeValue(var1, var4);
            AttributeWrapper var6 = new AttributeWrapper(var4);
            if (var5 != null) {
               var6.setCheckSum(var5.checksum);
               var6.setPreviousChecksum(var5.oldChecksum);
            }

            this.attributes.put(var1, var6);
         }
      } else {
         this.attributes.remove(var1);
      }

   }

   private HttpServer getHttpServer() {
      if (this.srvr != null) {
         return this.srvr;
      } else {
         if (this.webserverName.equals("USE_DEFAULT_WEB_SERVER")) {
            this.srvr = WebService.defaultHttpServer();
         } else {
            this.srvr = WebService.getHttpServer(this.webserverName);
         }

         return this.srvr;
      }
   }

   private String getSecondaryJVMID() {
      String var1 = this.secondaryJVMID;
      if (var1 != null) {
         return var1;
      } else {
         Loggable var3;
         try {
            ServerIdentity var2 = (ServerIdentity)this.getReplicationServices().getSecondaryInfo(this.roid);
            if (var2 != null) {
               if (ServerHelper.useExtendedSessionFormat()) {
                  var1 = ServerHelper.createServerEntry(ServerHelper.getNetworkChannelName(), var2, "!");
               } else {
                  var1 = Integer.toString(var2.hashCode());
               }
            } else {
               var1 = "NONE";
            }
         } catch (NotFoundException var4) {
            if (this.isDebugEnabled()) {
               var3 = HTTPSessionLogger.logSecondaryIDNotFoundLoggable(this.getWebAppServletContext().getLogContext(), this.id, var4);
               DEBUG_SESSIONS.debug(var3.getMessage());
            }

            var1 = "NONE";
         } catch (RuntimeException var5) {
            if (this.isDebugEnabled()) {
               var3 = HTTPSessionLogger.logFailedToFindSecondaryInfoLoggable(this.getWebAppServletContext().getLogContext(), this.id, var5);
               DEBUG_SESSIONS.debug(var3.getMessage());
            }

            var1 = "NONE";
         }

         this.secondaryJVMID = var1;
         return var1;
      }
   }

   void reinitializeSecondary() {
      this.secondaryJVMID = null;
   }

   public String getIdWithServerInfo() {
      SessionContext var1 = this.getContext();
      if (var1 == null) {
         return this.id;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.id);
         var2.append("!");
         if (ServerHelper.useExtendedSessionFormat()) {
            var2.append(ServerHelper.createServerEntry(ServerHelper.getNetworkChannelName(), LocalServerIdentity.getIdentity(), "!"));
         } else {
            var2.append(this.getWebAppServletContext().getServer().getServerHash());
         }

         var2.append("!");
         var2.append(this.getSecondaryJVMID());
         return var2.toString();
      }
   }

   void remove(boolean var1, boolean var2) {
      ((ReplicatedSessionContext)this.getContext()).getTimer().unregisterLAT(this.roid);
      if (!var1) {
         this.getReplicationServices().unregister((ROID)this.roid, this.contextName);
      }

      super.remove(var2);
   }

   void unregisterSecondary() {
      this.getReplicationServices().removeOrphanedSecondary(this.roid, this.contextName);
   }

   public void setMaxInactiveInterval(int var1) {
      super.setMaxInactiveInterval(var1);
      this.getSessionChange().init(this.maxInactiveInterval, this.accessTime);
   }

   private void setForceToConvertAttributes(boolean var1) {
      if (this.attributes != null) {
         Iterator var2 = this.attributes.values().iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof AttributeWrapper) {
               AttributeWrapper var4 = (AttributeWrapper)var3;
               var4.setForceToConvert(var1);
            }
         }
      }

   }

   public void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      if (var2 == null) {
         this.removeInternalAttribute(var1);
      } else {
         super.setInternalAttribute(var1, var2);
         if (var2 instanceof Serializable || var2 instanceof Remote) {
            this.getSessionChange().init(this.maxInactiveInterval, this.accessTime);
            synchronized(this.getSessionChange()) {
               this.getSessionChange().getInternalAttributeChanges().put(var1, var2);
            }
         }

      }
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      super.removeInternalAttribute(var1);
      this.getSessionChange().init(this.maxInactiveInterval, this.accessTime);
      synchronized(this.getSessionChange()) {
         this.getSessionChange().addInternalAttributeChange(var1, (Object)null);
      }
   }

   public void setAttribute(String var1, Object var2, boolean var3) throws IllegalStateException {
      if (var2 == null) {
         this.removeAttribute(var1, var3);
      } else {
         super.setAttribute(var1, var2, var3);
         var2 = this.replaceObject(var2, var1);
         if (!(var2 instanceof Serializable) && !(var2 instanceof Remote)) {
            if (this.isDebugEnabled()) {
               DEBUG_SESSIONS.debug("Session attribute with name:" + var1 + " class:" + var2.getClass().getName() + " is not serializable ane will " + " not be replicated or persisted");
            }
         } else {
            if (this.isDebugEnabled()) {
               Object var4 = this.attributes.get(var1);
               if (var4 != null && var4 instanceof AttributeWrapper) {
                  AttributeWrapper var5 = (AttributeWrapper)var4;
                  if (var5.getPreviousChecksum() == var5.getCheckSum()) {
                     String var6 = "Attribute value for '" + var1 + "' is being replicated although it has not changed. previous value checksum: " + var5.getPreviousChecksum() + ", new value checksum: " + var5.getCheckSum();
                     DEBUG_SESSIONS.debug(var6);
                  }
               }
            }

            this.getSessionChange().init(this.maxInactiveInterval, this.accessTime);
            synchronized(this.getSessionChange()) {
               this.getSessionChange().addAttributeChange(var1, var2);
            }
         }

      }
   }

   private Object replaceObject(Object var1, String var2) {
      Object var3 = var1;
      if (var1 instanceof EJBObject) {
         try {
            Handle var4 = ((EJBObject)var1).getHandle();
            var3 = new EJBAttributeWrapper(var4);
         } catch (RemoteException var6) {
            HTTPSessionLogger.logErrorFindingHandle(var2, var6);
         }
      } else if (var1 instanceof EJBHome) {
         try {
            HomeHandle var7 = ((EJBHome)var1).getHomeHandle();
            var3 = new EJBHomeAttributeWrapper(var7);
         } catch (RemoteException var5) {
            HTTPSessionLogger.logErrorFindingHomeHandle(var2, var5);
         }
      }

      return var3;
   }

   protected void removeAttribute(String var1, boolean var2) throws IllegalStateException {
      this.getAttribute(var1);
      super.removeAttribute(var1, var2);
      this.getSessionChange().init(this.maxInactiveInterval, this.accessTime);
      synchronized(this.getSessionChange()) {
         this.getSessionChange().addAttributeChange(var1, (Object)null);
      }
   }

   void syncSession() {
      super.syncSession();
      if (DEBUG_SESSIONS.isDebugEnabled()) {
         DEBUG_SESSIONS.debug("The change associated with this SessionData(" + this.getROID() + " or " + this + ") is: " + this.getSessionChange().hashCode());
      }

      if (this.isValid) {
         if (this.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("SessionData.syncSession() the change is modified: " + this.getSessionChange().getModified() + " and the active request count is: " + this.getActiveRequestCount() + " for " + this.getROID() + " and this is: " + this);
         }

         if (this.getSessionChange().getModified()) {
            if (this.isDebugEnabled()) {
               DEBUG_SESSIONS.debug("Replicating session : " + this.getROID() + " and " + this);
            }

            ((ReplicatedSessionContext)this.getContext()).getTimer().unregisterLAT(this.roid);

            Loggable var2;
            try {
               this.notifyAboutToPassivate(new HttpSessionEvent(this));
               this.getSessionChange().setTimeOnPrimaryAtUpdate(System.currentTimeMillis());
               synchronized(this.getSessionChange()) {
                  this.getReplicationServices().updateSecondary(this.roid, this.getUpdateObject(), this.contextName);
               }
            } catch (RemoteRuntimeException var6) {
               if (this.isDebugEnabled()) {
                  var2 = HTTPSessionLogger.logFailedToUpdateSecondaryLoggable(this.id, var6);
                  DEBUG_SESSIONS.debug(var2.getMessage());
               }
            } catch (NotFoundException var7) {
               if (this.isDebugEnabled()) {
                  var2 = HTTPSessionLogger.logSecondaryNotFoundLoggable(this.id, var7);
                  DEBUG_SESSIONS.debug(var2.getMessage());
               }

               throw new NestedError("Could not find secondary on remote server", var7);
            }

            synchronized(this.getSessionChange()) {
               this.getSessionChange().clear();
            }
         } else {
            ((ReplicatedSessionContext)this.getContext()).getTimer().registerLAT(this.roid, this.accessTime, this.maxInactiveInterval);
         }

      }
   }

   protected Serializable getUpdateObject() {
      return this.change;
   }

   protected void logTransientAttributeError(String var1) {
      HTTPSessionLogger.logTransientReplicatedAttributeError(this.getWebAppServletContext().getLogContext(), var1, this.getId());
   }

   public Object getKey() {
      return this.contextName;
   }

   protected ReplicationServices getReplicationServices() {
      ReplicatedSessionContext var1 = (ReplicatedSessionContext)this.getSessionContext();
      return var1.getReplicationServices();
   }

   protected void initVersionAttrsIfNeeded() {
      if (this.versionId != null && this.getInternalAttribute("weblogic.versionId") == null) {
         this.setInternalAttribute("weblogic.versionId", this.versionId);
      }

   }

   private byte[] getWorkContexts() {
      return (byte[])((byte[])this.getInternalAttribute("weblogic.workContexts"));
   }

   private void updateWorkContextsIfNeeded() {
      byte[] var1 = this.getWorkContexts();
      if (var1 != null) {
         SessionContext var2 = this.getContext(this.webserverName, this.contextName);
         if (var2 != null) {
            var2.getServletContext().getServer().getWorkContextManager().updateWorkContexts(this.id, var1);
         }
      }

   }

   static String getAnnotation() {
      ClassLoader var0 = Thread.currentThread().getContextClassLoader();
      return getAnnotation(var0);
   }

   private static String getAnnotation(ClassLoader var0) {
      return var0 instanceof GenericClassLoader ? ((GenericClassLoader)var0).getAnnotation().getAnnotationString() : null;
   }

   public Object getAttribute(String var1) throws IllegalStateException {
      this.check(var1);
      Object var2 = this.getSecurityModuleAttribute(var1);
      if (var2 != null) {
         return var2;
      } else {
         if (this.attributes != null) {
            Object var3 = this.attributes.get(var1);
            if (var3 != null && var3 instanceof byte[]) {
               return this.deserializeAttribute(var1, (byte[])((byte[])var3));
            }
         }

         return this.getAttributeInternal(var1);
      }
   }

   private Object deserializeAttribute(String var1, byte[] var2) {
      Chunk var3 = null;
      ChunkedObjectInputStream var4 = null;

      try {
         var3 = Chunk.getChunk();
         Chunk.chunkFully(var3, new UnsyncByteArrayInputStream(var2));
         var4 = new ChunkedObjectInputStream(var3, 0);
         var4.setReplacer(RemoteObjectReplacer.getReplacer());
         Object var5 = var4.readObject();
         var5 = this.checkEJBAttribute(var5);
         SessionData.AttributeInfo var6 = this.validateAttributeValue(var1, var5);
         AttributeWrapper var7 = new AttributeWrapper(var5);
         if (var6 != null) {
            var7.setCheckSum(var6.checksum);
            var7.setPreviousChecksum(var6.oldChecksum);
         }

         this.attributes.put(var1, var7);
         Object var8 = var5;
         return var8;
      } catch (Exception var13) {
         if (this.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Unable to deserialize attribute value for key: " + var1, var13);
         }
      } finally {
         if (var4 != null) {
            var4.close();
         } else if (var3 != null) {
            Chunk.releaseChunks(var3);
         }

      }

      return null;
   }
}
