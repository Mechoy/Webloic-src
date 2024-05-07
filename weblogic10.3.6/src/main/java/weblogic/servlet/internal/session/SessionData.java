package weblogic.servlet.internal.session;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;
import weblogic.common.internal.PassivationUtils;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.ejb.spi.BusinessObject;
import weblogic.logging.Loggable;
import weblogic.management.runtime.ServletSessionRuntimeMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.rjvm.LocalRJVM;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.internal.AttributeWrapper;
import weblogic.servlet.internal.HttpServer;
import weblogic.servlet.internal.ServerHelper;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.servlet.internal.WebService;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.NestedRuntimeException;
import weblogic.utils.concurrent.Latch;

public abstract class SessionData implements HttpSession, SessionInternal, Externalizable, SessionConstants {
   static final long serialVersionUID = -4398986144473197373L;
   private transient boolean needToSerializeAttributes;
   private static final transient int AVALABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
   protected transient SessionContext sessionContext;
   private transient ServletSessionRuntimeMBean runtime;
   protected String id;
   protected String versionId;
   protected long creationTime;
   protected long accessTime;
   protected int maxInactiveInterval;
   protected boolean isNew;
   protected boolean isValid;
   protected Map<String, Object> attributes;
   protected transient Hashtable transientAttributes;
   protected Map<String, Object> internalAttributes;
   protected transient Hashtable internalTransientAttributes;
   private transient int activeRequestCount;
   protected static final String DEFAULT_SERVER_INDICATOR = "USE_DEFAULT_WEB_SERVER";
   private static final boolean WIN_32;
   private transient Object removeLock;
   private Latch invalidationLatch;
   private static int currProcessedSessions;
   protected static final String VERSION_ID = "weblogic.versionId";
   public static final DebugCategory DEBUG_APP_VERSION;
   public static final DebugLogger DEBUG_SESSIONS;
   protected static final String DEBUG_SESSION_INDICATOR = "wl_debug_session";
   private transient CRC32 crc;
   private int invalidatingThreadID;
   private static final char[] SESSION_CHARS;
   public static final Hashtable ht;
   private static boolean unlimited;
   private boolean processed;
   private boolean special;

   public SessionData() {
      this.needToSerializeAttributes = false;
      this.sessionContext = null;
      this.runtime = null;
      this.id = null;
      this.versionId = null;
      this.creationTime = 0L;
      this.accessTime = 0L;
      this.maxInactiveInterval = -1;
      this.isNew = true;
      this.isValid = true;
      this.attributes = new ConcurrentHashMap(16, 0.75F, AVALABLE_PROCESSORS);
      this.transientAttributes = null;
      this.internalAttributes = new ConcurrentHashMap(16, 0.75F, AVALABLE_PROCESSORS);
      this.internalTransientAttributes = null;
      this.activeRequestCount = 0;
      this.removeLock = new Object();
      this.invalidationLatch = new Latch();
      this.crc = null;
      this.processed = false;
      this.special = false;
      this.setModified(false);
      this.initializeCRC32();
   }

   public SessionData(String var1, SessionContext var2, boolean var3) {
      this();
      this.id = var1;
      this.sessionContext = var2;
      this.isNew = var3;
      if (this.isNew) {
         if (this.id != null) {
            if (!this.reuseSessionId(this.id)) {
               this.id = null;
            } else {
               this.id = RSID.getID(this.id);
            }
         }

         if (this.id == null) {
            this.id = this.generateSessionId();
         }

         if (this.isDebugEnabled()) {
            Loggable var4 = HTTPSessionLogger.logCreateNewSessionForPathLoggable(this.id, this.getContextPath());
            DEBUG_SESSIONS.debug(var4.getMessage());
         }

         this.creationTime = System.currentTimeMillis();
         this.accessTime = this.creationTime;
         this.maxInactiveInterval = this.sessionContext.getConfigMgr().getSessionTimeoutSecs();
         this.isValid = true;
         if (this.sessionContext.getConfigMgr().isMonitoringEnabled()) {
            this.runtime = ServletSessionRuntimeManager.getInstance().findOrCreate(this);
         }
      }

      WebAppServletContext var5 = this.getWebAppServletContext();
      this.versionId = var5.getVersionId();
   }

   protected void setMonitoringId() {
      this.setInternalAttribute("weblogic.servlet.monitoringId", this.getNextId());
   }

   protected boolean isDebugEnabled() {
      return DEBUG_SESSIONS.isDebugEnabled() || this.sessionContext != null && this.sessionContext.getConfigMgr() != null && this.sessionContext.getConfigMgr().isDebugEnabled();
   }

   private String generateSessionId() {
      if (WIN_32) {
         while(true) {
            this.id = this.getNextId();
            if (!FileSessionContext.containsReservedKeywords(this.id)) {
               break;
            }

            if (this.isDebugEnabled()) {
               HTTPSessionLogger.logSessionIDContainsReservedKeyword(this.id);
            }
         }
      } else {
         this.id = this.getNextId();
      }

      return this.id;
   }

   private boolean reuseSessionId(String var1) {
      if (!this.getWebAppServletContext().getServer().getMBean().isSingleSignonDisabled() && this.sessionContext.getConfigMgr().getCookiePath().equals("/")) {
         WebAppServletContext[] var2 = this.getWebAppServletContext().getServer().getServletContextManager().getAllContexts();
         if (var2 != null && var2.length >= 2) {
            String var3 = this.sessionContext.getConfigMgr().getCookieName();

            for(int var4 = 0; var4 < var2.length; ++var4) {
               if (var2[var4] != this.getWebAppServletContext() && var2[var4].getSessionContext() != null && var2[var4].getSessionContext().getConfigMgr().getCookieName().equals(var3) && var2[var4].getSessionContext().getConfigMgr().getCookiePath().equals("/") && var2[var4].getSessionContext().hasSession(var1)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   final void reinitRuntimeMBean() {
      if (this.sessionContext.getConfigMgr().isMonitoringEnabled()) {
         if (this.runtime == null) {
            this.runtime = ServletSessionRuntimeManager.getInstance().findOrCreate(this);
         }
      }
   }

   protected void unregisterRuntimeMBean() {
      if (this.sessionContext.getConfigMgr().isMonitoringEnabled()) {
         ServletSessionRuntimeManager.getInstance().destroy(this);
      }
   }

   ServletSessionRuntimeMBean getServletSessionRuntimeMBean() {
      return this.runtime;
   }

   public final boolean isModified() {
      return this.needToSerializeAttributes;
   }

   public final void setModified(boolean var1) {
      this.needToSerializeAttributes = var1;
   }

   final String getContextPath() {
      WebAppServletContext var1 = this.getWebAppServletContext();
      return var1 == null ? null : var1.getContextPath();
   }

   final String getContextName() {
      WebAppServletContext var1 = this.getWebAppServletContext();
      return var1 == null ? null : var1.getName();
   }

   public ServletContext getServletContext() {
      return this.sessionContext.getServletContext();
   }

   public final WebAppServletContext getWebAppServletContext() {
      return this.sessionContext.getServletContext();
   }

   public long getCreationTime() throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         return this.creationTime;
      }
   }

   public final String getInternalId() {
      return this.id;
   }

   public String getId() {
      return this.getIdWithServerInfo() + "!" + this.creationTime;
   }

   public String getIdWithServerInfo() {
      if (this.getContext() != null && !this.getWebAppServletContext().getServer().isWAPEnabled()) {
         StringBuffer var1 = new StringBuffer(this.sessionContext.getConfigMgr().getIDLength() + 12);
         var1.append(this.id);
         var1.append("!");
         if (ServerHelper.useExtendedSessionFormat()) {
            var1.append(ServerHelper.createServerEntry(ServerHelper.getNetworkChannelName(), LocalServerIdentity.getIdentity(), "!"));
         } else {
            var1.append(this.getWebAppServletContext().getServer().getServerHash());
         }

         return var1.toString();
      } else {
         return this.id;
      }
   }

   public final String getVersionId() {
      return this.versionId;
   }

   public final void setVersionId(String var1) {
      this.versionId = var1;
   }

   public long getLastAccessedTime() {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         return this.accessTime;
      }
   }

   public final long getLAT() {
      return this.accessTime;
   }

   public void setLastAccessedTime(long var1) {
      this.accessTime = var1;
   }

   public int getMaxInactiveInterval() {
      return this.maxInactiveInterval;
   }

   public void setMaxInactiveInterval(int var1) {
      this.maxInactiveInterval = var1;
   }

   public HttpSessionContext getSessionContext() {
      return this.sessionContext;
   }

   final void setSessionContext(SessionContext var1) {
      this.sessionContext = var1;
   }

   public final SessionContext getContext(String var1, String var2) {
      if (this.sessionContext != null) {
         return this.sessionContext;
      } else {
         WebAppServletContext var3 = getServletContext(var1, var2, this.versionId);
         if (var3 == null) {
            return null;
         } else {
            this.sessionContext = var3.getSessionContext();
            return this.sessionContext;
         }
      }
   }

   protected static final WebAppServletContext getServletContext(String var0, String var1, String var2) {
      HttpServer var3 = null;
      if (var0.equals("USE_DEFAULT_WEB_SERVER")) {
         var3 = WebService.defaultHttpServer();
      } else {
         var3 = WebService.getHttpServer(var0);
      }

      if (var3 == null) {
         throw new AssertionError("WebService.getHttpServer(" + var0 + ") returns null");
      } else {
         return var3.getServletContextManager().getContextForContextPath(var1, var2);
      }
   }

   public final SessionContext getContext() {
      return this.sessionContext;
   }

   public final Object getValue(String var1) throws IllegalStateException {
      return this.getAttribute(var1);
   }

   public final void putValue(String var1, Object var2) throws IllegalStateException {
      this.setAttribute(var1, var2);
   }

   public final String[] getValueNames() throws IllegalStateException {
      ArrayList var1 = new ArrayList();
      Enumeration var2 = this.getAttributeNames();

      while(var2.hasMoreElements()) {
         var1.add(var2.nextElement());
      }

      String[] var3 = new String[var1.size()];
      return (String[])((String[])var1.toArray(var3));
   }

   public final void removeValue(String var1) {
      this.removeAttribute(var1);
   }

   public Object getAttribute(String var1) throws IllegalStateException {
      this.check(var1);
      Object var2 = this.getSecurityModuleAttribute(var1);
      return var2 != null ? var2 : this.getAttributeInternal(var1);
   }

   protected Object getSecurityModuleAttribute(String var1) {
      if (var1.equals("weblogic.formauth.targeturl")) {
         return this.getContext().getServletContext().getConfigManager().isServletAuthFromURL() ? this.getInternalAttribute("weblogic.formauth.targeturl") : this.getInternalAttribute("weblogic.formauth.targeturi");
      } else {
         return null;
      }
   }

   protected Object getAttributeInternal(String var1) {
      if (this.attributes != null) {
         Object var2 = this.attributes.get(var1);
         if (var2 != null) {
            AttributeWrapper var3 = (AttributeWrapper)var2;

            try {
               Object var4 = var3.getObject();
               if (var4 != null && var3 instanceof EJBAttributeWrapper && var4 instanceof BusinessHandle) {
                  BusinessHandle var15 = (BusinessHandle)var4;

                  try {
                     return var15.getBusinessObject();
                  } catch (RemoteException var7) {
                     throw new NestedRuntimeException(var7);
                  }
               }

               Loggable var5;
               if (var4 != null && var3 instanceof EJBAttributeWrapper && var4 instanceof Handle) {
                  if (this.isDebugEnabled()) {
                     var5 = HTTPSessionLogger.logGetAttributeEJBObjectLoggable(var1);
                     DEBUG_SESSIONS.debug(var5.getMessage());
                  }

                  Handle var14 = (Handle)var4;

                  try {
                     return var14.getEJBObject();
                  } catch (RemoteException var8) {
                     HTTPSessionLogger.logErrorReconstructingEJBObject(var1, var8);
                     throw new NestedRuntimeException(var8);
                  }
               }

               if (var4 != null && var3 instanceof EJBHomeAttributeWrapper && var4 instanceof HomeHandle) {
                  if (this.isDebugEnabled()) {
                     var5 = HTTPSessionLogger.logGetAttributeEJBHomeLoggable(var1);
                     DEBUG_SESSIONS.debug(var5.getMessage());
                  }

                  HomeHandle var13 = (HomeHandle)var4;

                  try {
                     return var13.getEJBHome();
                  } catch (RemoteException var9) {
                     HTTPSessionLogger.logErrorReconstructingEJBHome(var1, var9);
                     throw new NestedRuntimeException(var9);
                  }
               }

               return var4;
            } catch (ClassNotFoundException var10) {
               HTTPSessionLogger.logUnableToDeserializeSessionData(var10);
               this.attributes.remove(var1);
            } catch (IOException var11) {
               HTTPSessionLogger.logUnableToDeserializeSessionData(var11);
               this.attributes.remove(var1);
            } catch (RuntimeException var12) {
               HTTPSessionLogger.logUnableToDeserializeSessionData(var12);
               this.attributes.remove(var1);
            }

            return null;
         }
      }

      return this.transientAttributes != null ? this.transientAttributes.get(var1) : null;
   }

   protected void check(String var1) {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Key for session.getAttribute() is null");
      }
   }

   public Enumeration getAttributeNames() {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         Vector var1 = new Vector();
         if (this.attributes != null) {
            Iterator var2 = this.attributes.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               var1.addElement(var3);
            }
         }

         if (this.transientAttributes != null) {
            Enumeration var4 = this.transientAttributes.keys();

            while(var4.hasMoreElements()) {
               var1.addElement(var4.nextElement());
            }
         }

         return var1.elements();
      }
   }

   public void setAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      this.setAttribute(var1, var2, true);
   }

   public void setAttribute(String var1, Object var2, boolean var3) throws IllegalStateException, IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException("Key for session.setAttribute() is null");
      } else if (var2 == null) {
         this.removeAttribute(var1, var3);
      } else if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         Object var4 = this.getAttribute(var1);
         AttributeInfo var5 = this.validateAttributeValue(var1, var2);
         boolean var6 = false;
         if (var4 != null) {
            var6 = var4 == var2 || var4.hashCode() == var2.hashCode() && var4.equals(var2);
            if (!var6) {
               this.removeAttribute(var1, true, true);
            }
         }

         if (var3 && !var6 && var2 instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener)var2).valueBound(new HttpSessionBindingEvent(this, var1, var2));
         }

         EJBObject var7;
         if (var2 instanceof EJBObject) {
            var7 = (EJBObject)var2;
            this.setEJBObject(var1, var7);
         } else if (var2 instanceof EJBHome) {
            EJBHome var11 = (EJBHome)var2;
            this.setEJBHome(var1, var11);
         } else if (!(var2 instanceof Serializable) && !(var2 instanceof Remote)) {
            if (this.transientAttributes == null) {
               this.transientAttributes = new Hashtable();
               if (!HttpServer.isProductionModeEnabled()) {
                  this.logTransientAttributeError(var1);
               }
            }

            this.transientAttributes.put(var1, var2);
         } else if (var2 instanceof BusinessObject) {
            this.setBusinessObject(var1, (BusinessObject)var2);
         } else if (var2 instanceof Remote) {
            var7 = null;

            try {
               BusinessObject var12 = (BusinessObject)PortableRemoteObject.narrow(var2, BusinessObject.class);
               this.setBusinessObject(var1, var12);
            } catch (ClassCastException var10) {
               this.setModified(true);
               AttributeWrapper var9 = new AttributeWrapper(var2);
               if (var5 != null) {
                  var9.setCheckSum(var5.checksum);
                  var9.setPreviousChecksum(var5.oldChecksum);
               }

               this.attributes.put(var1, var9);
            }
         } else {
            this.setModified(true);
            AttributeWrapper var13 = new AttributeWrapper(var2);
            if (var5 != null) {
               var13.setCheckSum(var5.checksum);
               var13.setPreviousChecksum(var5.oldChecksum);
            }

            this.attributes.put(var1, var13);
         }

         if (var3) {
            this.sessionContext.notifySessionAttributeChange(this, var1, var4, var2);
         }

      }
   }

   protected void setOldChecksum(String var1, AttributeWrapper var2) {
      if (this.isDebugEnabled()) {
         AttributeWrapper var3 = (AttributeWrapper)this.attributes.get(var1);
         if (var3 != null) {
            var2.setPreviousChecksum(var3.getCheckSum());
         }
      }

   }

   protected AttributeInfo validateAttributeValue(String var1, Object var2) {
      AttributeInfo var3 = null;
      if (this.isDebugEnabled() && var2 != null) {
         try {
            var3 = this.testSerializability(var1, var2);
            Loggable var4 = HTTPSessionLogger.logSessionObjectSizeLoggable(var1, var3.size);
            DEBUG_SESSIONS.debug(var4.getMessage());
            if (var3.oldChecksum >= 0L) {
               String var7 = "Attribute value for " + var1 + " has";
               var7 = var7 + (var3.oldChecksum == var3.checksum ? " NOT " : " ");
               var7 = var7 + "changed. old value checksum: " + var3.oldChecksum + ", new value checksum: " + var3.checksum;
               DEBUG_SESSIONS.debug(var7);
            } else {
               DEBUG_SESSIONS.debug("Checksum for attribute '" + var1 + "', value: " + var3.checksum);
            }
         } catch (Exception var6) {
            Loggable var5 = HTTPSessionLogger.logObjectNotSerializableLoggable(var1, var6);
            DEBUG_SESSIONS.debug(var5.getMessage());
         }
      }

      return var3;
   }

   private void setEJBHome(String var1, EJBHome var2) {
      Loggable var3;
      if (this.isDebugEnabled()) {
         var3 = HTTPSessionLogger.logSetAttributeEJBHomeLoggable(var1);
         DEBUG_SESSIONS.debug(var3.getMessage());
      }

      if (this.attributes == null) {
         this.attributes = new ConcurrentHashMap();
      }

      var3 = null;

      HomeHandle var6;
      try {
         var6 = var2.getHomeHandle();
      } catch (RemoteException var5) {
         HTTPSessionLogger.logErrorFindingHomeHandle(var1, var5);
         throw new NestedRuntimeException(var5);
      }

      this.attributes.put(var1, new EJBHomeAttributeWrapper(var6));
      this.setModified(true);
   }

   private void setEJBObject(String var1, EJBObject var2) {
      if (this.isDebugEnabled()) {
         Loggable var3 = HTTPSessionLogger.logSetAttributeEJBObjectLoggable(var1);
         DEBUG_SESSIONS.debug(var3.getMessage());
      }

      Handle var6;
      try {
         var6 = var2.getHandle();
      } catch (RemoteException var5) {
         HTTPSessionLogger.logErrorFindingHandle(var1, var5);
         throw new NestedRuntimeException(var5);
      }

      this.attributes.put(var1, new EJBAttributeWrapper(var6));
      this.setModified(true);
   }

   private void setBusinessObject(String var1, BusinessObject var2) {
      BusinessHandle var3;
      try {
         var3 = var2._WL_getBusinessObjectHandle();
      } catch (RemoteException var5) {
         HTTPSessionLogger.logErrorFindingHandle(var1, var5);
         throw new NestedRuntimeException(var5);
      }

      this.attributes.put(var1, new EJBAttributeWrapper(var3));
      this.setModified(true);
   }

   protected abstract void logTransientAttributeError(String var1);

   public void removeAttribute(String var1) throws IllegalStateException {
      this.removeAttribute(var1, true);
   }

   protected void removeAttribute(String var1, boolean var2) throws IllegalStateException {
      this.removeAttribute(var1, false, var2);
   }

   protected void removeAttribute(String var1, boolean var2, boolean var3) throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Key for session.removeAttribute() is null");
      } else {
         Object var4 = null;
         Object var5;
         if (this.attributes != null && this.attributes.get(var1) != null) {
            var5 = this.attributes.remove(var1);
            if (var5 != null) {
               this.setModified(true);
               AttributeWrapper var6 = (AttributeWrapper)var5;

               try {
                  var4 = var5 = var6.getObject();
                  if (var3 && var5 instanceof HttpSessionBindingListener) {
                     ((HttpSessionBindingListener)var5).valueUnbound(new HttpSessionBindingEvent(this, var1, var5));
                  }
               } catch (ClassNotFoundException var8) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var8);
               } catch (IOException var9) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var9);
               } catch (RuntimeException var10) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var10);
               }
            }
         }

         if (this.transientAttributes != null && this.transientAttributes.get(var1) != null) {
            var5 = this.transientAttributes.remove(var1);
            if (var5 != null) {
               var4 = var5;
               if (var3 && var5 instanceof HttpSessionBindingListener) {
                  ((HttpSessionBindingListener)var5).valueUnbound(new HttpSessionBindingEvent(this, var1, var5));
               }
            }
         }

         if (var3 && !var2 && var4 != null) {
            this.sessionContext.notifySessionAttributeChange(this, var1, var4, (Object)null);
         }

      }
   }

   public Object getInternalAttribute(String var1) {
      return this.getInternalAttribute(var1, true);
   }

   private Object getInternalAttribute(String var1, boolean var2) throws IllegalStateException {
      if (!this.isValid && var2) {
         throw new IllegalStateException("HttpSession is invalid");
      } else if (var1 == null) {
         return null;
      } else {
         if (this.internalAttributes != null) {
            Object var3 = this.internalAttributes.get(var1);
            if (var3 != null) {
               AttributeWrapper var4 = (AttributeWrapper)var3;

               try {
                  return var4.getObject();
               } catch (ClassNotFoundException var6) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var6);
               } catch (IOException var7) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var7);
               } catch (RuntimeException var8) {
                  HTTPSessionLogger.logUnableToDeserializeSessionData(var8);
               }

               this.internalAttributes.remove(var1);
               return null;
            }
         }

         return this.internalTransientAttributes != null ? this.internalTransientAttributes.get(var1) : null;
      }
   }

   public void setInternalAttribute(String var1, Object var2) throws IllegalStateException, IllegalArgumentException {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Key for session.setInternalAttribute() is null");
      } else if (var2 == null) {
         this.removeInternalAttribute(var1);
      } else {
         if (!(var2 instanceof Serializable) && !(var2 instanceof Remote)) {
            if (this.internalTransientAttributes == null) {
               this.internalTransientAttributes = new Hashtable();
            }

            this.internalTransientAttributes.put(var1, var2);
            if (this.internalAttributes != null) {
               this.internalAttributes.remove(var1);
            }
         } else {
            this.setModified(true);
            this.internalAttributes.put(var1, new AttributeWrapper(var2));
            if (this.internalTransientAttributes != null) {
               this.internalTransientAttributes.remove(var1);
            }
         }

      }
   }

   public void removeInternalAttribute(String var1) throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else if (var1 == null) {
         throw new IllegalArgumentException("Key for session.removeInternalAttribute() is null");
      } else {
         if (this.internalAttributes != null) {
            Object var2 = this.internalAttributes.remove(var1);
            if (var2 != null) {
               this.setModified(true);
               return;
            }
         }

         if (this.internalTransientAttributes != null) {
            this.internalTransientAttributes.remove(var1);
         }

      }
   }

   public Enumeration getInternalAttributeNames() {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         Vector var1 = new Vector();
         if (this.internalAttributes != null) {
            Iterator var2 = this.internalAttributes.keySet().iterator();

            while(var2.hasNext()) {
               String var3 = (String)var2.next();
               var1.addElement(var3);
            }
         }

         if (this.internalTransientAttributes != null) {
            Enumeration var4 = this.internalTransientAttributes.keys();

            while(var4.hasMoreElements()) {
               var1.addElement(var4.nextElement());
            }
         }

         return var1.elements();
      }
   }

   protected boolean acquireInvalidationLock() {
      if (this.invalidationLatch.tryLock()) {
         this.invalidatingThreadID = System.identityHashCode(Thread.currentThread());
         return true;
      } else {
         return false;
      }
   }

   public void invalidate(boolean var1) throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("Session already invalidated");
      } else if (this.acquireInvalidationLock()) {
         if (this.sessionContext != null) {
            this.sessionContext.invalidateSession(this, false, var1);
         }

         this.setValid(false);
      } else if (System.identityHashCode(Thread.currentThread()) != this.invalidatingThreadID) {
         throw new IllegalStateException("Session invalidation is in progress with different thread");
      }
   }

   public void invalidate() throws IllegalStateException {
      this.invalidate(true);
   }

   public boolean isNew() throws IllegalStateException {
      if (!this.isValid) {
         throw new IllegalStateException("HttpSession is invalid");
      } else {
         return this.isNew;
      }
   }

   public final void setNew(boolean var1) {
      if (var1 != this.isNew) {
         this.isNew = var1;
      }

   }

   public boolean isValid() {
      return this.isValid;
   }

   public int getConcurrentRequestCount() {
      return this.getActiveRequestCount();
   }

   final int getActiveRequestCount() {
      return this.activeRequestCount;
   }

   final synchronized void incrementActiveRequestCount() {
      ++this.activeRequestCount;
   }

   final synchronized void decrementActiveRequestCount() {
      --this.activeRequestCount;
   }

   final synchronized boolean sessionInUse() {
      return this.activeRequestCount > 0;
   }

   final boolean isValidForceCheck() {
      if (!this.isValid) {
         return false;
      } else if (this.sessionInUse()) {
         return true;
      } else if (this.maxInactiveInterval >= 0 && (System.currentTimeMillis() - this.accessTime) / 1000L > (long)this.maxInactiveInterval) {
         ClassLoader var1 = Thread.currentThread().getContextClassLoader();
         boolean var2 = false;

         try {
            ClassLoader var3 = this.getWebAppServletContext().getServletClassLoader();
            if (var1 != var3) {
               Thread.currentThread().setContextClassLoader(var3);
               var2 = true;
            }

            this.invalidate();
         } catch (IllegalStateException var7) {
         } finally {
            if (var2) {
               Thread.currentThread().setContextClassLoader(var1);
            }

         }

         this.isValid = false;
         return false;
      } else {
         return true;
      }
   }

   public final void setValid(boolean var1) {
      if (var1 != this.isValid) {
         this.isValid = var1;
      }

   }

   void remove() {
      this.remove(true);
   }

   void remove(boolean var1) {
      synchronized(this.removeLock) {
         this.removeSessionLogin();
         this.getWebAppServletContext().getEventsManager().notifySessionLifetimeEvent(this, false);
         String var4;
         if (this.attributes != null) {
            Iterator var3 = this.attributes.keySet().iterator();

            while(var3.hasNext()) {
               var4 = (String)var3.next();

               try {
                  this.removeAttribute(var4, var1);
               } catch (Exception var8) {
                  HTTPSessionLogger.logAttributeRemovalFailure(this.getWebAppServletContext().getLogContext(), this.getId(), var8);
               }
            }
         }

         if (this.transientAttributes != null) {
            Enumeration var10 = this.transientAttributes.keys();

            while(var10.hasMoreElements()) {
               var4 = (String)var10.nextElement();

               try {
                  this.removeAttribute(var4, var1);
               } catch (Exception var7) {
                  HTTPSessionLogger.logAttributeRemovalFailure(this.getWebAppServletContext().getLogContext(), this.getId(), var7);
               }
            }
         }

         this.internalAttributes = null;
         this.unregisterRuntimeMBean();
         this.isValid = false;
      }
   }

   private void removeSessionLogin() {
      if (this.sessionContext != null) {
         WebAppServletContext var1 = this.sessionContext.getServletContext();
         SessionConfigManager var2 = this.sessionContext.getConfigMgr();
         HttpServer.SessionLogin var3 = var1.getServer().getSessionLogin();
         if (var2.isSessionSharingEnabled()) {
            var3.unregister(this.id);
         } else {
            var3.unregister(this.id, var1.getContextPath());
         }

      }
   }

   void syncSession() {
      if (this.isDebugEnabled()) {
         int var1 = calculateSessionSize(new ConcurrentHashMap(this.attributes));
         if (var1 > -1) {
            Loggable var2 = HTTPSessionLogger.logSessionSizeLoggable(this.id, var1);
            DEBUG_SESSIONS.debug(var2.getMessage());
         }
      }

   }

   static final int calculateSessionSize(Object var0) {
      try {
         return PassivationUtils.sizeOf(var0);
      } catch (IOException var2) {
         return -1;
      }
   }

   void storeAttributesInBytes() {
      Iterator var1;
      Object var2;
      AttributeWrapper var3;
      if (this.attributes != null) {
         var1 = this.attributes.values().iterator();

         while(var1.hasNext()) {
            var2 = var1.next();
            var3 = (AttributeWrapper)var2;

            try {
               var3.convertToBytes();
            } catch (IOException var6) {
               HTTPSessionLogger.logExceptionSerializingAttributeWrapper(var6);
            }
         }
      }

      if (this.internalAttributes != null) {
         var1 = this.internalAttributes.values().iterator();

         while(var1.hasNext()) {
            var2 = var1.next();
            var3 = (AttributeWrapper)var2;

            try {
               var3.convertToBytes();
            } catch (IOException var5) {
               HTTPSessionLogger.logExceptionSerializingAttributeWrapper(var5);
            }
         }
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.creationTime = var1.readLong();
      this.accessTime = var1.readLong();
      this.maxInactiveInterval = var1.readInt();
      this.isNew = var1.readBoolean();
      this.isValid = var1.readBoolean();
      this.attributes = convertToConcurrentHashMap(var1.readObject());
      this.id = (String)var1.readObject();

      try {
         this.internalAttributes = convertToConcurrentHashMap(var1.readObject());
         this.setupVersionIdFromAttrs();
      } catch (OptionalDataException var3) {
         this.internalAttributes = null;
         if (this.isDebugEnabled()) {
            DEBUG_SESSIONS.debug("Ignoring the OptionalDataException " + var3.getMessage(), var3);
         }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.initVersionAttrsIfNeeded();
      var1.writeLong(this.creationTime);
      var1.writeLong(this.accessTime);
      var1.writeInt(this.maxInactiveInterval);
      var1.writeBoolean(this.isNew);
      var1.writeBoolean(this.isValid);
      var1.writeObject(convertToHashtable(this.attributes));
      var1.writeObject(this.id);
      var1.writeObject(convertToHashtable(this.internalAttributes));
   }

   final String getMainAttributeValue() {
      String var1 = this.sessionContext.getConfigMgr().getMonitoringAttributeName();
      if (var1 == null) {
         return null;
      } else {
         Object var2 = this.getAttribute(var1);
         if (var2 == null) {
            return null;
         } else {
            return var2 instanceof String ? (String)var2 : var2.toString();
         }
      }
   }

   private String getNextId() {
      char[] var1 = new char[this.sessionContext.getConfigMgr().getIDLength()];
      int var2 = (int)(System.currentTimeMillis() / 1000L);
      SecureRandom var3 = LocalRJVM.getLocalRJVM().getSecureRandom();

      for(int var4 = 0; var4 < this.sessionContext.getConfigMgr().getIDLength(); ++var4) {
         if (var4 <= 3 || var4 >= 8) {
            var1[var4] = SESSION_CHARS[var3.nextInt(SESSION_CHARS.length)];
         }
      }

      var1[4] = SESSION_CHARS[var2 >> 24 & 63];
      var1[5] = SESSION_CHARS[var2 >> 16 & 63];
      var1[6] = SESSION_CHARS[var2 >> 8 & 63];
      var1[7] = SESSION_CHARS[var2 & 31];
      return new String(var1);
   }

   public final void notifyAboutToPassivate(HttpSessionEvent var1) {
      notifyAboutToPassivate(this.attributes, var1);
      notifyAboutToPassivate(this.transientAttributes, var1);
   }

   private AttributeInfo testSerializability(String var1, Object var2) throws Exception {
      byte[] var3 = this.serialize(var2);
      this.deserialize(var3);
      AttributeInfo var4 = new AttributeInfo();
      if (this.crc != null) {
         try {
            this.crc.update(var3);
            var4.checksum = this.crc.getValue();
            Object var5 = this.attributes.get(var1);
            if (var5 != null && var5 instanceof AttributeWrapper) {
               AttributeWrapper var6 = (AttributeWrapper)var5;
               var4.oldChecksum = var6.getCheckSum();
            }
         } finally {
            this.crc.reset();
         }
      }

      var4.size = var3.length;
      return var4;
   }

   protected Object deserialize(byte[] var1) throws ClassNotFoundException, IOException {
      return PassivationUtils.toObject(var1);
   }

   protected byte[] serialize(Object var1) throws IOException {
      byte[] var2 = PassivationUtils.toByteArray(var1);
      return var2;
   }

   private static void notifyAboutToPassivate(Map<String, Object> var0, HttpSessionEvent var1) {
      if (var0 != null) {
         Iterator var2 = var0.values().iterator();

         while(true) {
            Object var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = var2.next();
               if (!(var3 instanceof AttributeWrapper)) {
                  break;
               }

               try {
                  AttributeWrapper var4 = (AttributeWrapper)var3;
                  var3 = var4.getObject(false);
               } catch (Exception var5) {
                  var3 = null;
               }
            } while(var3 == null);

            if (var3 instanceof HttpSessionActivationListener) {
               ((HttpSessionActivationListener)var3).sessionWillPassivate(var1);
            }
         }
      }
   }

   public final void notifyActivated(HttpSessionEvent var1) {
      notifyActivated(this.attributes, var1);
      notifyActivated(this.transientAttributes, var1);
   }

   private static void notifyActivated(Map<String, Object> var0, HttpSessionEvent var1) {
      if (var0 != null) {
         Iterator var2 = var0.values().iterator();

         while(true) {
            Object var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = var2.next();
               if (!(var3 instanceof AttributeWrapper)) {
                  break;
               }

               try {
                  AttributeWrapper var4 = (AttributeWrapper)var3;
                  var3 = var4.getObject();
               } catch (Exception var5) {
                  var3 = null;
               }
            } while(var3 == null);

            if (var3 instanceof HttpSessionActivationListener) {
               ((HttpSessionActivationListener)var3).sessionDidActivate(var1);
            }
         }
      }
   }

   protected final boolean isProcessed() {
      if (!this.processed && !unlimited && !this.special) {
         int[] var1 = (int[])((int[])ht.get(this.id));
         boolean var2 = false;
         if (var1 != null) {
            int var3 = var1[0];
            ++var3;
            var1[0] = var3;
            this.processed = true;
            return true;
         } else {
            return this.processed;
         }
      } else {
         return true;
      }
   }

   protected final void setProcessed(boolean var1) {
      this.processed = var1;
      if (var1) {
         int[] var2 = (int[])((int[])ht.get(this.id));
         int var3 = 0;
         if (var2 != null) {
            var3 = var2[0];
         } else {
            var2 = new int[1];
            incrementProcessedSessionsCount();
         }

         ++var3;
         var2[0] = var3;
         ht.put(this.id, var2);
      }

   }

   protected static final void checkSpecial(ServletRequestImpl var0, SessionData var1) {
      var1.special = var0.getContext().isInternalApp();
   }

   public static final synchronized void invalidateProcessedSession(SessionData var0) {
      if (!unlimited && !var0.special) {
         if (var0.isProcessed()) {
            int[] var1 = (int[])((int[])ht.get(var0.id));
            int var2 = var1[0];
            --var2;
            if (var2 == 0) {
               decrementProcessedSessionsCount();
               ht.remove(var0.id);
            } else {
               var1[0] = var2;
            }
         }

      }
   }

   static final ConcurrentHashMap<String, Object> convertToConcurrentHashMap(Object var0) {
      ConcurrentHashMap var1 = null;
      if (var0 instanceof Hashtable) {
         Map var2 = (Map)var0;
         var1 = new ConcurrentHashMap(var2);
      } else if (var0 instanceof Dictionary) {
         Dictionary var5 = (Dictionary)var0;
         var1 = new ConcurrentHashMap();
         Enumeration var3 = var5.keys();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            var1.put(var4, var5.get(var4));
         }
      } else {
         if (!(var0 instanceof ConcurrentHashMap)) {
            throw new IllegalArgumentException("unkonwn attribute map, and its type is " + var0.getClass());
         }

         var1 = (ConcurrentHashMap)var0;
      }

      return var1;
   }

   static final Hashtable<String, Object> convertToHashtable(Map<String, Object> var0) {
      return !(var0 instanceof Hashtable) ? new Hashtable(var0) : (Hashtable)var0;
   }

   public static final synchronized void incrementProcessedSessionsCount() {
      ++currProcessedSessions;
   }

   public static final synchronized void decrementProcessedSessionsCount() {
      --currProcessedSessions;
   }

   public static final int getCurrProcessedSessionsCount() {
      return currProcessedSessions;
   }

   protected void initVersionAttrsIfNeeded() {
      if (this.versionId != null && this.getInternalAttribute("weblogic.versionId") == null) {
         this.setInternalAttribute("weblogic.versionId", this.versionId);
      }

   }

   protected void setupVersionIdFromAttrs() {
      if (this.internalAttributes != null) {
         Object var1 = this.getInternalAttribute("weblogic.versionId", false);
         if (this.versionId == null && var1 instanceof String) {
            this.versionId = (String)var1;
         }

      }
   }

   protected final void updateVersionIfNeeded(SessionContext var1) {
      String var2 = (String)this.getInternalAttribute("weblogic.versionId");
      if (var2 != null) {
         if (var1 == null) {
            if (DEBUG_APP_VERSION.isEnabled()) {
               HTTPLogger.logDebug("Cannot find SessionContext of version=" + var2 + ", using existing version=" + this.versionId + " for id=" + this.id + ", sessionContext is null");
            }

         } else {
            WebAppServletContext var3 = var1.getServletContext();
            if (var3 == null) {
               if (DEBUG_APP_VERSION.isEnabled()) {
                  HTTPLogger.logDebug("Cannot find SessionContext of version=" + var2 + ", using existing version=" + this.versionId + " for id=" + this.id + ", servletContext is null");
               }

            } else if (var3.getVersionId() != null && !var3.getVersionId().equals(var2)) {
               String var4 = var3.getServer().getName();
               String var5 = var3.getContextPath();
               if (var4 != null && var5 != null) {
                  if (DEBUG_APP_VERSION.isEnabled()) {
                     HTTPLogger.logDebug("SessionData with id=" + this.id + " of version=" + this.versionId + " is being rewired to version=" + var2);
                  }

                  String var6 = this.versionId;
                  this.sessionContext = null;
                  this.versionId = var2;

                  try {
                     this.getContext(var4, var5);
                  } finally {
                     if (this.sessionContext == null) {
                        this.sessionContext = var1;
                        this.versionId = var6;
                        if (DEBUG_APP_VERSION.isEnabled()) {
                           HTTPLogger.logDebug("Cannot find SessionContext of version=" + var2 + ", using existing version=" + this.versionId);
                        }
                     }

                  }

               } else {
                  if (DEBUG_APP_VERSION.isEnabled()) {
                     HTTPLogger.logDebug("Cannot find SessionContext of version=" + var2 + ", using existing version=" + this.versionId + " for id=" + this.id + ", webServerName or contextPath is null");
                  }

               }
            }
         }
      }
   }

   public String getMonitoringId() {
      String var1 = this.sessionContext.getConfigMgr().getMonitoringAttributeName();
      if (var1 == null) {
         return (String)this.getInternalAttribute("weblogic.servlet.monitoringId");
      } else {
         Object var2 = this.getAttribute(var1);
         return var2 == null ? (String)this.getInternalAttribute("weblogic.servlet.monitoringId") : var2.toString();
      }
   }

   public boolean hasStateAttributes() {
      return this.attributes.size() > 0 || this.internalAttributes != null && (this.internalAttributes.size() > 1 || this.internalAttributes.get("weblogic.workContexts") == null);
   }

   public final void setDebugFlag(boolean var1) {
      if (var1) {
         this.setAttribute("wl_debug_session", Boolean.TRUE);
      } else {
         this.removeAttribute("wl_debug_session");
      }

   }

   public final boolean isDebuggingSession() {
      if (HttpServer.isProductionModeEnabled()) {
         return false;
      } else {
         return this.getAttribute("wl_debug_session") != null;
      }
   }

   public String toString() {
      if (!this.isValid()) {
         return "HttpSession is invalid: " + this.getId();
      } else {
         return !this.isDebuggingSession() ? super.toString() : this.dump(true);
      }
   }

   private String dump(boolean var1) {
      StringBuilder var2 = new StringBuilder(512);
      var2.append(super.toString()).append(" - \n").append(" session-id: ").append(this.getId()).append('\n').append(" create-time: ").append(new Date(this.creationTime)).append('\n').append(" access-time: ").append(new Date(this.accessTime)).append('\n').append(" max-inactive-interval: ").append(this.maxInactiveInterval).append("\n -");
      this.addUpAttributesTo(this.attributes, var2);
      this.addUpAttributesTo(this.transientAttributes, var2);
      if (var1) {
         return var2.toString();
      } else {
         this.addUpAttributesTo(this.internalAttributes, var2);
         this.addUpAttributesTo(this.internalTransientAttributes, var2);
         return var2.toString();
      }
   }

   protected final void logSessionAttributeChanged(String var1, String var2, Object var3, Object var4) {
      var3 = var3 == null ? "null" : var3;
      var4 = var4 == null ? "null" : var4;
      HTTPSessionLogger.logAttributeChanged(var1, this.getServletContext().getContextPath(), this.getId(), var2, var3.toString(), var4.toString());
   }

   public static final void dumpSessionToLog(HttpSession var0) {
      if (var0 != null) {
         HTTPSessionLogger.logDumpSession(var0 instanceof SessionData ? ((SessionData)var0).dump(true) : var0.toString());
      }
   }

   private final void addUpAttributesTo(Map<String, Object> var1, StringBuilder var2) {
      if (var1 != null && var1.size() != 0) {
         Iterator var3 = var1.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var2.append("\n").append(var4).append(": ").append(this.getAttribute(var4));
         }

      }
   }

   private void initializeCRC32() {
      if (this.isDebugEnabled() && this.crc == null) {
         this.crc = new CRC32();
      }

   }

   static {
      WIN_32 = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH).indexOf("windows") >= 0;
      currProcessedSessions = 0;
      DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
      DEBUG_SESSIONS = DebugLogger.getDebugLogger("DebugHttpSessions");
      SESSION_CHARS = new char[]{'Q', 'B', 'C', 'D', 'G', 'F', 'G', 'H', 'L', 'J', 'K', 'L', 'M', 'N', 'T', 'P', 'Q', 'R', 'S', 'T', 'J', 'V', 'W', 'X', 'Y', 'Z', 'h', 'b', 'c', 'd', 'p', 'f', 'g', 'h', 'v', 'j', 'k', 'l', 'm', 'n', 'y', 'p', 'q', 'r', 's', 't', 'n', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '2', '1'};
      ht = new Hashtable();
      unlimited = false;
   }

   protected static class AttributeInfo {
      int size;
      long checksum = -1L;
      long oldChecksum = -1L;
   }
}
