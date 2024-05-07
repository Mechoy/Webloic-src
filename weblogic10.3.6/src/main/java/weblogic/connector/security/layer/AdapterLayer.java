package weblogic.connector.security.layer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ConnectionEvent;
import javax.resource.spi.ConnectionEventListener;
import javax.resource.spi.ConnectionManager;
import javax.resource.spi.ConnectionRequestInfo;
import javax.resource.spi.DissociatableManagedConnection;
import javax.resource.spi.EISSystemException;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.LocalTransaction;
import javax.resource.spi.LocalTransactionException;
import javax.resource.spi.ManagedConnection;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ManagedConnectionMetaData;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.ValidatingManagedConnectionFactory;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.exception.RACommonException;
import weblogic.connector.exception.RAException;
import weblogic.connector.exception.RAInternalException;
import weblogic.connector.extensions.Suspendable;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.SuspendableEndpointFactory;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.utils.StackTraceUtils;

public class AdapterLayer {
   private RAInstanceManager raIM = null;
   private static AuthenticatedSubject anonymousSubject = null;
   private boolean useCallerForRunAs = false;
   private AuthenticatedSubject runAsSubject = null;
   private AuthenticatedSubject manageAsSubject = null;
   private boolean useCallerForWorkAs = false;
   private AuthenticatedSubject runWorkAsSubject = null;

   public AdapterLayer(RAInstanceManager var1, AuthenticatedSubject var2) throws RAException {
      this.raIM = var1;
      this.initializeSecurityIdentities(var2);
   }

   public void validate(ActivationSpec var1, AuthenticatedSubject var2) throws InvalidPropertyException {
      this.pushSubject(var2);

      try {
         var1.validate();
      } finally {
         this.popSubject(var2);
      }

   }

   public Object getConnectionHandle(ConnectionEvent var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Object var3;
      try {
         var3 = var1.getConnectionHandle();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Exception getException(ConnectionEvent var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Exception var3;
      try {
         var3 = var1.getException();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public int getId(ConnectionEvent var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      int var3;
      try {
         var3 = var1.getId();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void setConnectionHandle(ConnectionEvent var1, Object var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.setConnectionHandle(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void dissociateConnections(DissociatableManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.dissociateConnections();
      } finally {
         this.popSubject(var2);
      }

   }

   private void xaDebug(String var1, Xid var2, Integer var3, Boolean var4, Throwable var5) {
      if (Debug.isXAoutEnabled()) {
         boolean var6 = var5 != null && var5 instanceof XAException;
         int var7 = 0;
         String var8 = null;
         if (var6) {
            var7 = ((XAException)var5).errorCode;
            var8 = xaErrorCodeToString(var7, false);
         }

         Debug.xaOut("RA with JNDI name = " + this.raIM.getJndiName() + (var5 != null ? " threw exception" : "") + " calling " + var1 + (var2 != null ? " Xid = " + var2 : "") + (var3 != null ? " flags = " + var3 : "") + (var4 != null ? " onePhase = " + var4 : "") + (var6 ? " xaErrorCode = " + var7 : "") + (var8 != null ? " xaErrorString = " + var8 : "") + (var5 != null ? " throwable = " + var5 + " message = " + var5.getMessage() + "\nStack trace = \n" + StackTraceUtils.throwable2StackTrace(var5) : ""));
      }

   }

   public void commit(XAResource var1, Xid var2, boolean var3, AuthenticatedSubject var4) throws XAException {
      this.pushSubject(var4);

      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("commit", var2, (Integer)null, new Boolean(var3), (Throwable)null);
         }

         var1.commit(var2, var3);
      } catch (XAException var11) {
         this.xaDebug("commit", var2, (Integer)null, new Boolean(var3), var11);
         throw var11;
      } catch (Throwable var12) {
         this.xaDebug("commit", var2, (Integer)null, new Boolean(var3), var12);
         throw (XAException)(new XAException(-3)).initCause(var12);
      } finally {
         this.popSubject(var4);
      }

   }

   public void end(XAResource var1, Xid var2, int var3, AuthenticatedSubject var4) throws XAException {
      this.pushSubject(var4);

      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("end", var2, new Integer(var3), (Boolean)null, (Throwable)null);
         }

         var1.end(var2, var3);
      } catch (XAException var11) {
         this.xaDebug("end", var2, new Integer(var3), (Boolean)null, var11);
         throw var11;
      } catch (Throwable var12) {
         this.xaDebug("end", var2, new Integer(var3), (Boolean)null, var12);
         throw (XAException)(new XAException(-3)).initCause(var12);
      } finally {
         this.popSubject(var4);
      }

   }

   public void forget(XAResource var1, Xid var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("forget", var2, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var1.forget(var2);
      } catch (XAException var10) {
         this.xaDebug("forget", var2, (Integer)null, (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("forget", var2, (Integer)null, (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

   }

   public int getTransactionTimeout(XAResource var1, AuthenticatedSubject var2) throws XAException {
      this.pushSubject(var2);

      int var3;
      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("getTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var3 = var1.getTransactionTimeout();
      } catch (XAException var9) {
         this.xaDebug("getTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, var9);
         throw var9;
      } catch (Throwable var10) {
         this.xaDebug("getTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, var10);
         throw (XAException)(new XAException(-3)).initCause(var10);
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public boolean isSameRM(XAResource var1, XAResource var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      boolean var4;
      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("isSameRM", (Xid)null, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var4 = var1.isSameRM(var2);
      } catch (XAException var10) {
         this.xaDebug("isSameRM", (Xid)null, (Integer)null, (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("isSameRM", (Xid)null, (Integer)null, (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public int prepare(XAResource var1, Xid var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      int var4;
      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("prepare", var2, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var4 = var1.prepare(var2);
      } catch (XAException var10) {
         this.xaDebug("prepare", var2, (Integer)null, (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("prepare", var2, (Integer)null, (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public Xid[] recover(XAResource var1, int var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      Xid[] var4;
      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("recover", (Xid)null, new Integer(var2), (Boolean)null, (Throwable)null);
         }

         var4 = var1.recover(var2);
      } catch (XAException var10) {
         this.xaDebug("recover", (Xid)null, new Integer(var2), (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("recover", (Xid)null, new Integer(var2), (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public void rollback(XAResource var1, Xid var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("rollback", var2, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var1.rollback(var2);
      } catch (XAException var10) {
         this.xaDebug("rollback", var2, (Integer)null, (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("rollback", var2, (Integer)null, (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

   }

   public boolean setTransactionTimeout(XAResource var1, int var2, AuthenticatedSubject var3) throws XAException {
      this.pushSubject(var3);

      boolean var4;
      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("setTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, (Throwable)null);
         }

         var4 = var1.setTransactionTimeout(var2);
      } catch (XAException var10) {
         this.xaDebug("setTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, var10);
         throw var10;
      } catch (Throwable var11) {
         this.xaDebug("setTransactionTimeout", (Xid)null, (Integer)null, (Boolean)null, var11);
         throw (XAException)(new XAException(-3)).initCause(var11);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public void start(XAResource var1, Xid var2, int var3, AuthenticatedSubject var4) throws XAException {
      this.pushSubject(var4);

      try {
         if (Debug.isXAoutEnabled()) {
            this.xaDebug("start", var2, new Integer(var3), (Boolean)null, (Throwable)null);
         }

         var1.start(var2, var3);
      } catch (XAException var11) {
         this.xaDebug("start", var2, new Integer(var3), (Boolean)null, var11);
         throw var11;
      } catch (Throwable var12) {
         this.xaDebug("start", var2, new Integer(var3), (Boolean)null, var12);
         throw (XAException)(new XAException(-3)).initCause(var12);
      } finally {
         this.popSubject(var4);
      }

   }

   public void begin(LocalTransaction var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.begin();
      } catch (LocalTransactionException var12) {
      } catch (ResourceAdapterInternalException var13) {
      } catch (EISSystemException var14) {
      } catch (ResourceException var15) {
      } catch (Throwable var16) {
      } finally {
         this.popSubject(var2);
      }

   }

   public void commit(LocalTransaction var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.commit();
      } finally {
         this.popSubject(var2);
      }

   }

   public void rollback(LocalTransaction var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.rollback();
      } finally {
         this.popSubject(var2);
      }

   }

   public void addConnectionEventListener(ManagedConnection var1, ConnectionEventListener var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.addConnectionEventListener(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void associateConnection(ManagedConnection var1, Object var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.associateConnection(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void cleanup(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.cleanup();
      } finally {
         this.popSubject(var2);
      }

   }

   public void destroy(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.destroy();
      } finally {
         this.popSubject(var2);
      }

   }

   public Object getConnection(ManagedConnection var1, Subject var2, ConnectionRequestInfo var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      Object var5;
      try {
         var5 = var1.getConnection(var2, var3);
      } finally {
         this.popSubject(var4);
      }

      return var5;
   }

   public LocalTransaction getLocalTransaction(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      LocalTransaction var3;
      try {
         var3 = var1.getLocalTransaction();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public PrintWriter getLogWriter(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      PrintWriter var3;
      try {
         var3 = var1.getLogWriter();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public ManagedConnectionMetaData getMetaData(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      ManagedConnectionMetaData var3;
      try {
         var3 = var1.getMetaData();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public XAResource getXAResource(ManagedConnection var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      XAResource var3;
      try {
         var3 = var1.getXAResource();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void removeConnectionEventListener(ManagedConnection var1, ConnectionEventListener var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.removeConnectionEventListener(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void setLogWriter(ManagedConnection var1, PrintWriter var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.setLogWriter(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public Object createConnectionFactory(ManagedConnectionFactory var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      Object var3;
      try {
         var3 = var1.createConnectionFactory();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Object createConnectionFactory(ManagedConnectionFactory var1, ConnectionManager var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      Object var4;
      try {
         var4 = var1.createConnectionFactory(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public ManagedConnection createManagedConnection(ManagedConnectionFactory var1, Subject var2, ConnectionRequestInfo var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      ManagedConnection var5;
      try {
         var5 = var1.createManagedConnection(var2, var3);
      } finally {
         this.popSubject(var4);
      }

      return var5;
   }

   public PrintWriter getLogWriter(ManagedConnectionFactory var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      PrintWriter var3;
      try {
         var3 = var1.getLogWriter();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public ManagedConnection matchManagedConnection(ManagedConnectionFactory var1, Set var2, Subject var3, ConnectionRequestInfo var4, AuthenticatedSubject var5) throws ResourceException {
      this.pushSubject(var5);

      ManagedConnection var6;
      try {
         var6 = var1.matchManagedConnections(var2, var3, var4);
      } finally {
         this.popSubject(var5);
      }

      return var6;
   }

   public void setLogWriter(ManagedConnectionFactory var1, PrintWriter var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.setLogWriter(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public String getEISProductName(ManagedConnectionMetaData var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getEISProductName();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public String getEISProductVersion(ManagedConnectionMetaData var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getEISProductVersion();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public int getMaxConnections(ManagedConnectionMetaData var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      int var3;
      try {
         var3 = var1.getMaxConnections();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public String getUserName(ManagedConnectionMetaData var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getUserName();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void endpointActivation(ResourceAdapter var1, MessageEndpointFactory var2, ActivationSpec var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         var1.endpointActivation(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public void endpointDeactivation(ResourceAdapter var1, MessageEndpointFactory var2, ActivationSpec var3, AuthenticatedSubject var4) {
      this.pushSubject(var4);

      try {
         var1.endpointDeactivation(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public XAResource[] getXAResources(ResourceAdapter var1, ActivationSpec[] var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      XAResource[] var4;
      try {
         var4 = var1.getXAResources(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public void start(ResourceAdapter var1, BootstrapContext var2, AuthenticatedSubject var3) throws ResourceAdapterInternalException {
      this.pushSubject(var3);

      try {
         var1.start(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void stop(ResourceAdapter var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      try {
         var1.stop();
      } finally {
         this.popSubject(var2);
      }

   }

   public void init(Suspendable var1, ResourceAdapter var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         var1.init(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public boolean isSuspended(Suspendable var1, int var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.isSuspended(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public void resume(Suspendable var1, int var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         var1.resume(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public void startVersioning(Suspendable var1, ResourceAdapter var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         var1.startVersioning(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public boolean supportsInit(Suspendable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      boolean var3;
      try {
         var3 = var1.supportsInit();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public boolean supportsSuspend(Suspendable var1, int var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.supportsSuspend(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public boolean supportsVersioning(Suspendable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      boolean var3;
      try {
         var3 = var1.supportsVersioning();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void suspend(Suspendable var1, int var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         var1.suspend(var2, var3);
      } finally {
         this.popSubject(var4);
      }

   }

   public void suspendInbound(Suspendable var1, MessageEndpointFactory var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         if (var1.supportsSuspend(1)) {
            var1.suspendInbound(var2, var3);
         }
      } finally {
         this.popSubject(var4);
      }

   }

   public void resumeInbound(Suspendable var1, MessageEndpointFactory var2, Properties var3, AuthenticatedSubject var4) throws ResourceException {
      this.pushSubject(var4);

      try {
         if (var1.supportsSuspend(1)) {
            var1.resumeInbound(var2, var3);
         }
      } finally {
         this.popSubject(var4);
      }

   }

   public void suspend(SuspendableEndpointFactory var1, Properties var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.suspend(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void disconnect(SuspendableEndpointFactory var1, AuthenticatedSubject var2) throws ResourceException {
      this.pushSubject(var2);

      try {
         var1.disconnect();
      } finally {
         this.popSubject(var2);
      }

   }

   public void resume(SuspendableEndpointFactory var1, Properties var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.resume(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public boolean isSuspended(SuspendableEndpointFactory var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      boolean var3;
      try {
         var3 = var1.isSuspended();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public ResourceAdapter getResourceAdapter(ResourceAdapterAssociation var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      ResourceAdapter var3;
      try {
         var3 = var1.getResourceAdapter();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void setResourceAdapter(ResourceAdapterAssociation var1, ResourceAdapter var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      try {
         var1.setResourceAdapter(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public Set getInvalidConnections(ValidatingManagedConnectionFactory var1, Set var2, AuthenticatedSubject var3) throws ResourceException {
      this.pushSubject(var3);

      Set var4;
      try {
         var4 = var1.getInvalidConnections(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public boolean equals(Object var1, Object var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.equals(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public int hashCode(Object var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      int var3;
      try {
         var3 = var1.hashCode();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public String toString(Object var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      String var3;
      try {
         if (var1 != null) {
            var3 = var1.toString();
            return var3;
         }

         var3 = "";
      } catch (Throwable var9) {
         String var4 = "null due to " + var9.toString();
         return var4;
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Object invoke(Method var1, Object var2, Object[] var3, AuthenticatedSubject var4) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      this.pushSubject(var4);

      Object var5;
      try {
         var5 = var1.invoke(var2, var3);
      } finally {
         this.popSubject(var4);
      }

      return var5;
   }

   public Object newInstance(Class var1, AuthenticatedSubject var2) throws InstantiationException, IllegalAccessException, RAInternalException {
      this.pushSubject(var2);

      Object var3;
      try {
         var3 = var1.newInstance();
      } catch (InstantiationException var11) {
         throw var11;
      } catch (IllegalAccessException var12) {
         throw var12;
      } catch (Throwable var13) {
         String var4 = Debug.getExceptionRANewInstanceFailed(var1.getName(), this.toString(var13, var2));
         throw new RAInternalException(var4, var13);
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Class forName(String var1, boolean var2, ClassLoader var3, AuthenticatedSubject var4) throws ClassNotFoundException {
      this.pushSubject(var4);

      Class var5;
      try {
         var5 = Class.forName(var1, var2, var3);
      } finally {
         this.popSubject(var4);
      }

      return var5;
   }

   public Class loadClass(ClassLoader var1, String var2, AuthenticatedSubject var3) throws ClassNotFoundException {
      this.pushSubject(var3);

      Class var4;
      try {
         var4 = var1.loadClass(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public Object htClone(Hashtable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Object var3;
      try {
         var3 = var1.clone();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public boolean htContains(Hashtable var1, Object var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.contains(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public boolean htContainsKey(Hashtable var1, Object var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.containsKey(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public boolean htContainsValue(Hashtable var1, Object var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      boolean var4;
      try {
         var4 = var1.containsValue(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public Object htGet(Hashtable var1, Object var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      Object var4;
      try {
         var4 = var1.get(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public Object htPut(Hashtable var1, Object var2, Object var3, AuthenticatedSubject var4) throws NullPointerException {
      this.pushSubject(var4);

      Object var5;
      try {
         var5 = var1.put(var2, var3);
      } finally {
         this.popSubject(var4);
      }

      return var5;
   }

   public void htPutAll(Hashtable var1, Map var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      try {
         var1.putAll(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public Object htRemove(Hashtable var1, Object var2, AuthenticatedSubject var3) throws NullPointerException {
      this.pushSubject(var3);

      Object var4;
      try {
         var4 = var1.remove(var2);
      } finally {
         this.popSubject(var3);
      }

      return var4;
   }

   public Throwable fillInStackTrace(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Throwable var3;
      try {
         var3 = var1.fillInStackTrace();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Throwable getCause(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Throwable var3;
      try {
         var3 = var1.getCause();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public String getLocalizedMessage(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getLocalizedMessage();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public String getMessage(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getMessage();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public StackTraceElement[] getStackTrace(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      StackTraceElement[] var3;
      try {
         var3 = var1.getStackTrace();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void initCause(Throwable var1, Throwable var2, AuthenticatedSubject var3) throws IllegalArgumentException, IllegalStateException {
      this.pushSubject(var3);

      try {
         var1.initCause(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void printStackTrace(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      try {
         var1.printStackTrace();
      } finally {
         this.popSubject(var2);
      }

   }

   public void printStackTrace(Throwable var1, PrintStream var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.printStackTrace(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void printStackTrace(Throwable var1, PrintWriter var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.printStackTrace(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void setStackTrace(Throwable var1, StackTraceElement[] var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.setStackTrace(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public String getErrorCode(ResourceException var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = var1.getErrorCode();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public Exception getLinkedException(ResourceException var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      Exception var3;
      try {
         var3 = var1.getLinkedException();
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   public void setErrorCode(ResourceException var1, String var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.setErrorCode(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public void setLinkedException(ResourceException var1, Exception var2, AuthenticatedSubject var3) {
      this.pushSubject(var3);

      try {
         var1.setLinkedException(var2);
      } finally {
         this.popSubject(var3);
      }

   }

   public String throwable2StackTrace(Throwable var1, AuthenticatedSubject var2) {
      this.pushSubject(var2);

      String var3;
      try {
         var3 = StackTraceUtils.throwable2StackTrace(var1);
      } finally {
         this.popSubject(var2);
      }

      return var3;
   }

   void pushSubject(AuthenticatedSubject var1) {
      AuthenticatedSubject var2 = null;
      if (isManaging()) {
         var2 = this.manageAsSubject;
      } else if (this.useCallerForRunAs) {
         var2 = SecurityServiceManager.getCurrentSubject(var1);
      } else {
         var2 = this.runAsSubject;
      }

      if (var2 == null) {
         var2 = getAnonymousSubject(var1);
      }

      SecurityManager.pushSubject(var1, var2);
   }

   void pushWorkSubject(AuthenticatedSubject var1) {
      AuthenticatedSubject var2 = null;
      if (this.useCallerForWorkAs) {
         var2 = SecurityServiceManager.getCurrentSubject(var1);
         String var3 = "<run-work-as-principal-name><use-caller-identity>";
      } else {
         var2 = this.runWorkAsSubject;
      }

      if (var2 == null) {
         var2 = getAnonymousSubject(var1);
      }

      SecurityManager.pushSubject(var1, var2);
   }

   void popSubject(AuthenticatedSubject var1) {
      SecurityManager.popSubject(var1);
   }

   private static AuthenticatedSubject getAnonymousSubject(AuthenticatedSubject var0) {
      if (anonymousSubject == null) {
         try {
            anonymousSubject = getAuthenticatedSubject((String)null, var0, "<use-anonymous-identity>");
         } catch (RACommonException var3) {
            String var2 = Debug.logGetAnonymousSubjectFailed();
            Debug.logStackTrace(var2, var3);
         }
      }

      return anonymousSubject;
   }

   private static boolean isManaging() {
      return Utils.getManagementCount() > 0;
   }

   private void initializeSecurityIdentities(AuthenticatedSubject var1) throws RAException {
      RAInfo var2 = this.raIM.getRAInfo();
      String var3 = null;
      String var4 = null;
      String var5 = null;
      String var6 = null;
      boolean var7 = false;
      boolean var8 = false;
      AuthenticatedSubject var9 = null;
      if (var2.getSecurityIdentityInfo() != null) {
         var3 = var2.getSecurityIdentityInfo().getDefaultPrincipalName();
         if (var3 != null && var3.length() == 0) {
            var3 = null;
         }

         var7 = var2.getSecurityIdentityInfo().useCallerForRunAs();
         var8 = var2.getSecurityIdentityInfo().useCallerForRunWorkAs();
         var4 = var2.getSecurityIdentityInfo().getRunAsPrincipalName();
         if (var4 == null || var4.length() == 0) {
            var4 = var3;
         }

         var5 = var2.getSecurityIdentityInfo().getRunWorkAsPrincipalName();
         if (var5 == null || var5.length() == 0) {
            var5 = var3;
         }

         var6 = var2.getSecurityIdentityInfo().getManageAsPrincipalName();
         if (var6 == null || var6.length() == 0) {
            var6 = var3;
         }
      }

      var9 = getAuthenticatedSubject(var3, var1, "<default-principal-name>");
      this.checkDeployUserPrivileges(var9, "<default-principal-name>", var1);
      this.runAsSubject = getAuthenticatedSubject(var4, var1, "<run-as-principal-name>");
      this.checkDeployUserPrivileges(this.runAsSubject, "<run-as-principal-name>", var1);
      if (var5 != null && var5.length() != 0) {
         this.runWorkAsSubject = getAuthenticatedSubject(var5, var1, "<run-work-as-principal-name>");
         this.checkDeployUserPrivileges(this.runWorkAsSubject, "<run-work-as-principal-name>", var1);
      } else {
         this.runWorkAsSubject = this.runAsSubject;
      }

      if (var6 != null && var6.length() != 0) {
         this.manageAsSubject = getAuthenticatedSubject(var6, var1, "<manage-as-principal-name>");
         this.checkDeployUserPrivileges(this.manageAsSubject, "<manage-as-principal-name>", var1);
      } else {
         this.manageAsSubject = null;
      }

   }

   private static AuthenticatedSubject getAuthenticatedSubject(String var0, AuthenticatedSubject var1, String var2) throws RACommonException {
      AuthenticatedSubject var3 = null;
      PrincipalAuthenticator var4 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(var1, SecurityServiceManager.getDefaultRealmName(), ServiceType.AUTHENTICATION);

      try {
         var3 = var4.impersonateIdentity(var0);
         return var3;
      } catch (LoginException var7) {
         if (var0 == null || var0.length() == 0) {
            var0 = Debug.getStringAnonymousUser();
         }

         String var6 = Debug.getExceptionLoginException(var0, var7.toString(), var2);
         throw new RACommonException(var6);
      }
   }

   private void checkDeployUserPrivileges(AuthenticatedSubject var1, String var2, AuthenticatedSubject var3) throws RAException {
      if (SubjectUtils.isUserAnAdministrator(var1)) {
         AuthenticatedSubject var4 = null;
         if (this.raIM != null && this.raIM.getAppContext() != null) {
            var4 = this.raIM.getAppContext().getDeploymentInitiator();
         }

         if (var4 != null && (!this.raIM.getAppContext().isStaticDeploymentOperation() || !SubjectUtils.isUserAnonymous(var4)) && !SubjectUtils.isUserAnAdministrator(var4)) {
            Set var5 = var1.getPrincipals();
            Iterator var6 = var5.iterator();
            String var7 = "";
            if (var6.hasNext()) {
               Principal var8 = (Principal)var6.next();
               var7 = var8.getName();
            }

            Set var12 = var4.getPrincipals();
            var6 = var12.iterator();
            String var9 = "";
            if (var6.hasNext()) {
               Principal var10 = (Principal)var6.next();
               var9 = var10.getName();
            }

            String var11 = Debug.getDeploySecurityBumpUpFailed(var2, var7, var9);
            throw new RAException(var11);
         }
      }

   }

   static String xaFlagsToString(int var0) {
      switch (var0) {
         case 0:
            return "TMNOFLAGS";
         case 2097152:
            return "TMJOIN";
         case 33554432:
            return "TMSUSPEND";
         case 67108864:
            return "TMSUCCESS";
         case 134217728:
            return "TMRESUME";
         case 536870912:
            return "TMFAIL";
         case 1073741824:
            return "TMONEPHASE";
         default:
            return Integer.toHexString(var0).toUpperCase(Locale.ENGLISH);
      }
   }

   private static String xaErrorCodeToString(int var0, boolean var1) {
      StringBuffer var2 = new StringBuffer(10);
      switch (var0) {
         case -9:
            var2.append("XAER_OUTSIDE");
            if (var1) {
               var2.append(" : The resource manager is doing work outside global transaction");
            }

            return var2.toString();
         case -8:
            var2.append("XAER_DUPID");
            if (var1) {
               var2.append(" : The XID already exists");
            }

            return var2.toString();
         case -7:
            var2.append("XAER_RMFAIL");
            if (var1) {
               var2.append(" : Resource manager is unavailable");
            }

            return var2.toString();
         case -6:
            var2.append("XAER_PROTO");
            if (var1) {
               var2.append(" : Routine was invoked in an inproper context");
            }

            return var2.toString();
         case -5:
            var2.append("XAER_INVAL");
            if (var1) {
               var2.append(" : Invalid arguments were given");
            }

            return var2.toString();
         case -4:
            var2.append("XAER_NOTA");
            if (var1) {
               var2.append(" : The XID is not valid");
            }

            return var2.toString();
         case -3:
            var2.append("XAER_RMERR");
            if (var1) {
               var2.append(" : A resource manager error has occured in the transaction branch");
            }

            return var2.toString();
         case -2:
            var2.append("XAER_ASYNC");
            if (var1) {
               var2.append(" : Asynchronous operation already outstanding");
            }

            return var2.toString();
         case 0:
            return "XA_OK";
         case 3:
            return "XA_RDONLY";
         case 5:
            var2.append("XA_HEURMIX");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically committed and rolled back");
            }

            return var2.toString();
         case 6:
            var2.append("XA_HEURRB");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically rolled back");
            }

            return var2.toString();
         case 7:
            var2.append("XA_HEURCOM");
            if (var1) {
               var2.append(" : The transaction branch has been heuristically committed");
            }

            return var2.toString();
         case 8:
            var2.append("XA_HEURHAZ");
            if (var1) {
               var2.append(" : The transaction branch may have been heuristically completed");
            }

            return var2.toString();
         case 100:
            var2.append("XA_RBROLLBACK");
            if (var1) {
               var2.append(" : Rollback was caused by unspecified reason");
            }

            return var2.toString();
         case 101:
            var2.append("XA_RBCOMMFAIL");
            if (var1) {
               var2.append(" : Rollback was caused by communication failure");
            }

            return var2.toString();
         case 102:
            var2.append("XA_RBDEADLOCK");
            if (var1) {
               var2.append(" : A deadlock was detected");
            }

            return var2.toString();
         case 103:
            var2.append("XA_RBINTEGRITY");
            if (var1) {
               var2.append(" : A condition that violates the integrity of the resource was detected");
            }

            return var2.toString();
         case 104:
            var2.append("XA_RBOTHER");
            if (var1) {
               var2.append(" : The resource manager rolled back the transaction branch for a reason not on this list");
            }

            return var2.toString();
         case 105:
            var2.append("XA_RBPROTO");
            if (var1) {
               var2.append(" : A protocol error occured in the resource manager");
            }

            return var2.toString();
         case 106:
            var2.append("XA_RBTIMEOUT");
            if (var1) {
               var2.append(" : A transaction branch took too long");
            }

            return var2.toString();
         case 107:
            var2.append("XA_RBTRANSIENT");
            if (var1) {
               var2.append(" : May retry the transaction branch");
            }

            return var2.toString();
         default:
            return Integer.toHexString(var0).toUpperCase(Locale.ENGLISH);
      }
   }
}
