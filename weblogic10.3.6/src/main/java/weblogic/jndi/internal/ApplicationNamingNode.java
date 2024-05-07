package weblogic.jndi.internal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.naming.event.ObjectChangeListener;
import weblogic.jndi.Environment;
import weblogic.jndi.OpaqueReference;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.CBVWrapper;
import weblogic.utils.classloaders.ClassLoaderUtils;

public class ApplicationNamingNode extends BasicNamingNode {
   private static final String DEFAULT_SEPARATORS = "/";
   private String separators;
   private static final boolean DEBUG = false;
   private final ConcurrentHashMap cache;
   private String relativeName;
   ApplicationNamingNode parent;
   private final ApplicationNamingInfo info;

   public ApplicationNamingNode() {
      this("/", (ApplicationNamingNode)null, "", (ApplicationNamingInfo)null);
   }

   public ApplicationNamingNode(String var1, ApplicationNamingInfo var2) {
      this(var1, (ApplicationNamingNode)null, "", var2);
   }

   public ApplicationNamingNode(String var1) {
      this(var1, (ApplicationNamingNode)null, "", (ApplicationNamingInfo)null);
   }

   public ApplicationNamingNode(String var1, ApplicationNamingNode var2, String var3, ApplicationNamingInfo var4) {
      super(var1, var2, var3);
      this.cache = new ConcurrentHashMap(1);
      this.relativeName = "";
      this.separators = var1;
      this.relativeName = var3;
      this.parent = var2;
      if (var4 == null) {
         this.info = new ApplicationNamingInfo();
      } else {
         this.info = var4;
      }

   }

   protected BasicNamingNode newSubnode(String var1) {
      return new ApplicationNamingNode(this.separators, this, var1, this.info);
   }

   public Context getContext(Hashtable var1) {
      return new WLEventContextImpl(var1, this, true);
   }

   protected NamingNode createSubnodeHere(String var1, Hashtable var2) throws NoPermissionException, NamingException {
      NamingNode var3 = super.createSubnodeHere(var1, var2);
      return var3;
   }

   protected void destroySubnodeHere(String var1, Hashtable var2) throws NoPermissionException, NamingException {
      String var3 = this.getNameInNamespace(var1);
      super.destroySubnodeHere(var1, var2);
      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ destroySubContext(" + var3 + ")");
      }

   }

   public Object lookup(String var1, Hashtable var2) throws NamingException, RemoteException {
      try {
         Object var3 = super.lookup(var1, var2);
         var3 = this.getObjectOrStub(var3, var1);
         return var3;
      } catch (NameNotFoundException var5) {
         String var4 = this.getRelativeName();
         if (var4.endsWith("comp/env")) {
            var4 = "java:comp/env";
         }

         throw this.newNameNotFoundException("While trying to look up " + var1 + " in " + var4 + ".", var1, var2);
      }
   }

   protected Object resolveObject(String var1, Object var2, int var3, Hashtable var4) throws NamingException {
      Object var5;
      if (var2 != null) {
         try {
            if (var2 instanceof NamingNode) {
               NamingNode var16 = (NamingNode)var2;
               return var16.getContext(var4);
            }

            if (var3 >= 0 && var2 instanceof LinkRef) {
               LinkRef var8 = (LinkRef)var2;
               String var17 = var8.getLinkName();
               Object var10 = null;
               var10 = this.cache.get(var1);
               if (var10 == null) {
                  EventContext var6;
                  if (var17.indexOf("java:") > -1) {
                     int var11 = var17.lastIndexOf("/");
                     String var7 = var17.substring(var11 + 1);
                     String var12 = var17.substring(0, var11);
                     InitialContext var13 = new InitialContext();
                     var6 = (EventContext)var13.lookup(var12);
                     var5 = var6.lookup(var7);
                     if (!this.isOpaqueReference(var6, var7)) {
                        this.cache.put(var1, var5);
                        new CacheInvalidationListener(var7, var1, var6, this.cache);
                     }
                  } else if (!this.linkRefResolvesToRemoteServer(var17)) {
                     var5 = super.resolveObject(var1, var2, var3, var4);
                     new Environment(var4);
                     String[] var19 = this.parseName(var17);
                     if (var4 == null) {
                        var4 = new Hashtable();
                     }

                     var4.put("weblogic.jndi.events.enable", "true");
                     Environment var18 = new Environment(var4);
                     var6 = (EventContext)var18.getContext(var19[0]);
                     if (!this.isOpaqueReference(var6, var19[1])) {
                        this.cache.put(var1, var5);
                        new CacheInvalidationListener(var19[1], var1, var6, this.cache);
                     }
                  } else {
                     var5 = super.resolveObject(var1, var2, var3, var4);
                  }
               } else {
                  var5 = var10;
               }
            } else {
               var5 = super.resolveObject(var1, var2, var3, var4);
            }
         } catch (NamingException var14) {
            this.cache.remove(var1);
            throw var14;
         } catch (Exception var15) {
            this.cache.remove(var1);
            NamingException var9 = this.fillInException(new ConfigurationException("Call to NamingManager.getObjectInstance() failed: "), var1, var2, (String)null);
            var9.setRootCause(var15);
            throw var9;
         }
      } else {
         var5 = super.resolveObject(var1, var2, var3, var4);
      }

      return var5;
   }

   private boolean isOpaqueReference(Context var1, String var2) {
      try {
         return var1.lookupLink(var2) instanceof OpaqueReference;
      } catch (NamingException var4) {
         return false;
      }
   }

   private Object getObjectOrStub(Object var1, String var2) throws RemoteException {
      if (!(var1 instanceof ForceCallByReference) && !this.info.isForceCallByReferenceEnabled()) {
         if (ClassLoaderUtils.visibleToClassLoader(var1)) {
            return var1;
         } else {
            Object var4 = var1;
            if (var1 instanceof Remote) {
               if (ServerHelper.isIIOPStub((Remote)var1)) {
                  return var1;
               }

               if (ServerHelper.isClusterable((Remote)var1) && RemoteHelper.isCollocated(var1)) {
                  ServerHelper.exportObject((Remote)var1, var2);
               } else {
                  ServerHelper.exportObject((Remote)var1);
               }

               var4 = StubFactory.getStub((Remote)var1);
            } else if (var1 instanceof StubReference) {
               var4 = StubFactory.getStub((StubReference)var1);
            }

            return var4;
         }
      } else if (var1 instanceof CBVWrapper) {
         CBVWrapper var3 = (CBVWrapper)var1;
         return var3.getDelegate();
      } else {
         return var1;
      }
   }

   private String[] parseName(String var1) {
      String var2 = "./";
      int var3 = var1.length();

      for(int var4 = var3 - 1; var4 >= 0; --var4) {
         char var5 = var1.charAt(var4);

         for(int var6 = 0; var6 < var2.length(); ++var6) {
            if (var5 == var2.charAt(var6)) {
               return new String[]{var1.substring(0, var4), var1.substring(var4 + 1)};
            }
         }
      }

      return new String[]{"", var1};
   }

   private boolean linkRefResolvesToRemoteServer(String var1) {
      return var1.indexOf(58) > -1;
   }

   public String getRelativeName() throws NamingException {
      String var1 = this.relativeName;
      if (this.parent != null) {
         var1 = this.parent.getRelativeName() + "/" + this.relativeName;
      }

      return var1;
   }

   private static final class CacheInvalidationListener implements ObjectChangeListener {
      private final String name;
      private final EventContext eventContext;
      private final ConcurrentHashMap cache;
      private final String objectName;
      private final String fullName;

      private CacheInvalidationListener(String var1, String var2, EventContext var3, ConcurrentHashMap var4) {
         this.name = var2;
         this.eventContext = var3;
         this.cache = var4;
         this.objectName = var1;
         String var5 = null;

         try {
            this.eventContext.addNamingListener(var1, 0, this);
            var5 = var3.getNameInNamespace();
         } catch (NamingException var7) {
            var4.remove(var2);
         }

         this.fullName = var5 + "/" + var1;
      }

      public void objectChanged(NamingEvent var1) {
         try {
            this.cache.remove(this.name);
            this.eventContext.removeNamingListener(this);
         } catch (NamingException var3) {
         }

      }

      public void namingExceptionThrown(NamingExceptionEvent var1) {
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof CacheInvalidationListener) {
            CacheInvalidationListener var2 = (CacheInvalidationListener)var1;
            return this.fullName.equals(var2.fullName);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.fullName.hashCode();
      }

      // $FF: synthetic method
      CacheInvalidationListener(String var1, String var2, EventContext var3, ConcurrentHashMap var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
