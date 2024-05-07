package weblogic.jndi.internal;

import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Binding;
import javax.naming.CannotProceedException;
import javax.naming.CompositeName;
import javax.naming.ConfigurationException;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InvalidNameException;
import javax.naming.LinkException;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.event.EventContext;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingListener;
import javax.naming.spi.DirectoryManager;
import javax.naming.spi.NamingManager;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.WLContext;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.work.WorkManagerFactory;

public class BasicNamingNode implements NamingNode {
   private String separators;
   private String nameInNamespace;
   private BasicNamingNode parent;
   private final ConcurrentHashMap map;
   private final ArrayList onelevelNameListenerList;
   private final ConcurrentHashMap objectListenerMap;
   protected final ArrayList subtreeScopeNameListenerList;
   private final Object lock;
   protected NameParser nameParser;
   static final int RESOLVE_TO_LINK = 0;
   private static final int RESOLVE_FULLY = 1;

   public BasicNamingNode() {
      this("/", (BasicNamingNode)null, "");
   }

   protected BasicNamingNode(String var1) {
      this(var1, (BasicNamingNode)null, "");
   }

   protected BasicNamingNode(String var1, BasicNamingNode var2, String var3) {
      this(var1, var2, var3, new ArrayList());
   }

   protected BasicNamingNode(String var1, BasicNamingNode var2, String var3, ArrayList var4) {
      this.map = new ConcurrentHashMap(3);
      this.onelevelNameListenerList = new ArrayList();
      this.objectListenerMap = new ConcurrentHashMap(5);
      this.lock = new Object() {
      };
      this.separators = var1;
      if (var2 == null) {
         this.nameParser = new WLNameParser(var1);
      } else {
         this.nameParser = var2.nameParser;
      }

      this.setParent(var2, var3);
      this.subtreeScopeNameListenerList = var4;
   }

   public String getNameInNamespace() {
      return this.nameInNamespace;
   }

   public String getRelativeName() throws NamingException {
      String var1 = "";

      for(String var2 = this.getNameInNamespace(); var2.length() > 0; var2 = this.getRest(var2)) {
         var1 = this.getPrefix(var2);
      }

      return var1;
   }

   public NamingNode getParent() {
      return this.parent;
   }

   public void setParent(BasicNamingNode var1, String var2) {
      this.parent = var1;
      if (var1 == null) {
         this.nameInNamespace = "";
      } else {
         try {
            this.nameInNamespace = var1.getNameInNamespace(var2);
         } catch (NamingException var4) {
            throw new AssertionError(var4);
         }
      }

   }

   public NameParser getNameParser(String var1, Hashtable var2) throws NameNotFoundException, NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      if (var3.length() == 0) {
         return this.nameParser;
      } else {
         Object var5 = this.lookupHere(var3, var2, var4);

         try {
            NameParser var6;
            if (var5 instanceof NamingNode) {
               var6 = ((NamingNode)var5).getNameParser(var4, var2);
            } else {
               var6 = this.getContinuationCtx(var5, var3, var4, var2).getNameParser(var4);
            }

            return var6;
         } catch (NamingException var7) {
            throw this.prependResolvedNameToException(var3, var7);
         }
      }
   }

   private String composeName(String var1, String var2) {
      if (var2.length() == 0) {
         return var1;
      } else {
         return var1 != null && var1.length() != 0 ? var2 + this.separators.charAt(0) + var1 : var2;
      }
   }

   public String getNameInNamespace(String var1) throws NamingException {
      if (var1.length() == 0) {
         return this.nameInNamespace;
      } else if (this.nameInNamespace.length() > 0) {
         return this.nameParser.equals(this.parent.nameParser) ? this.composeName(this.escapeBinding(var1), this.nameInNamespace) : this.nameInNamespace + "/" + this.escapeBinding(var1);
      } else {
         return var1;
      }
   }

   public Object lookup(String var1, Hashtable var2) throws NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      Object var5 = this.lookupHere(var3, var2, var4);
      if (var4.length() == 0) {
         return this.resolveObject(var3, var5, var2);
      } else {
         try {
            if (var5 instanceof NamingNode) {
               var5 = ((NamingNode)var5).lookup(var4, var2);
            } else {
               var5 = this.getContinuationCtx(var5, var3, var4, var2).lookup(var4);
            }
         } catch (NamingException var7) {
            throw this.prependResolvedNameToException(var3, var7);
         }

         return this.makeTransportable(var5, var4, var2);
      }
   }

   protected Object lookupHere(String var1, Hashtable var2, String var3) throws NameNotFoundException, NamingException {
      if (var1.length() == 0) {
         return this;
      } else {
         Object var4 = null;
         synchronized(this) {
            var4 = this.map.get(var1);
         }

         if (var4 == null) {
            if (NamingResolutionDebugLogger.isDebugEnabled()) {
               NamingResolutionDebugLogger.debug("--- failed to find " + var1);
            }

            String var5;
            if (this.separators.length() == 0) {
               var5 = var3 + this.getNameInNamespace(var1);
            } else {
               var5 = this.composeName(var3, this.getNameInNamespace(var1));
            }

            if (var3 != null && var3.length() > 0) {
               throw this.newNameNotFoundException("While trying to lookup '" + var5 + "' didn't find subcontext '" + var1 + "'. Resolved '" + this.getNameInNamespace() + "'", this.composeName(var3, var1), var2);
            } else {
               throw this.newNameNotFoundException("Unable to resolve '" + var5 + "'. Resolved '" + this.getNameInNamespace() + "'", this.composeName(var3, var1), var2);
            }
         } else {
            if (NamingDebugLogger.isDebugEnabled()) {
               NamingDebugLogger.debug("+++ lookup(" + this.getNameInNamespace(var1) + ", " + var4.getClass().getName() + ") succeeded");
            }

            return var4;
         }
      }
   }

   private Object lookupHereOrCreate(String var1, Hashtable var2, String var3) throws NamingException {
      try {
         return this.lookupHere(var1, var2, var3);
      } catch (NameNotFoundException var5) {
         if ("true".equals(this.getProperty(var2, "weblogic.jndi.createIntermediateContexts"))) {
            return this.createSubnodeHere(var1, var2);
         } else {
            throw var5;
         }
      }
   }

   public Object lookupLink(String var1, Hashtable var2) throws NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      Object var5 = this.lookupHere(var3, var2, var4);
      if (var4.length() == 0) {
         return this.resolveObject(var3, var5, 0, var2);
      } else {
         try {
            if (var5 instanceof NamingNode) {
               var5 = ((NamingNode)var5).lookupLink(var4, var2);
            } else {
               var5 = this.getContinuationCtx(var5, var3, var4, var2).lookupLink(var4);
            }

            return var5;
         } catch (NamingException var7) {
            throw this.prependResolvedNameToException(var3, var7);
         }
      }
   }

   public void bind(String var1, Object var2, Hashtable var3) throws NamingException, RemoteException {
      if (var2 == null) {
         throw new NamingException("Cannot bind null object to jndi with name " + var1);
      } else {
         String var4 = this.getPrefix(var1);
         String var5 = this.getRest(var1);
         if (var5.length() == 0) {
            this.bindHere(var4, var2, var3, true);
         } else {
            Object var6 = this.lookupHereOrCreate(var4, var3, var5);

            try {
               if (var6 instanceof NamingNode) {
                  ((NamingNode)var6).bind(var5, var2, var3);
               } else {
                  this.getContinuationCtx(var6, var4, var5, var3).bind(var5, var2);
               }
            } catch (NamingException var8) {
               throw this.prependResolvedNameToException(var4, var8);
            }
         }

      }
   }

   protected void bindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NamingException {
      Object var5;
      try {
         if (!(var2 instanceof Aggregatable)) {
            synchronized(this.map) {
               var5 = this.map.get(var1);
               if (var5 == null) {
                  if (var2 instanceof BasicNamingNode) {
                     ((BasicNamingNode)var2).setParent(this, var1);
                  }

                  this.map.put(var1, var2);
               } else if (!var2.equals(var5)) {
                  throw this.fillInException(new NameAlreadyBoundException(var1 + " is already bound"), var1, var5, "");
               }
            }
         } else {
            synchronized(this.map) {
               var5 = this.map.get(var1);
               if (var5 == null) {
                  ((Aggregatable)var2).onBind(this, var1, (Aggregatable)null);
                  this.map.put(var1, var2);
               } else {
                  if (!(var5 instanceof Aggregatable)) {
                     throw this.fillInException(new NameAlreadyBoundException(var1 + " is already bound"), var1, var5, "");
                  }

                  ((Aggregatable)var5).onBind(this, var1, (Aggregatable)var2);
               }
            }
         }

         if (NamingDebugLogger.isDebugEnabled()) {
            NamingDebugLogger.debug("+++ bind(" + this.getNameInNamespace(var1) + ", " + var2.getClass().getName() + ") succeeded");
         }
      } catch (NamingException var11) {
         NamingException var6 = this.fillInException(var11, var1, this, this.getNameInNamespace());
         if (NamingDebugLogger.isDebugEnabled()) {
            NamingDebugLogger.debug("+++ bind(" + var1 + ", " + var2.getClass().getName() + ") failed due to: " + var6);
         }

         throw var6;
      }

      if (this.notifyNameListeners()) {
         this.fireNameListeners(var1, this.setUpNotification(var1, 0, var3, var2, var5));
      }

   }

   public void rebind(String var1, Object var2, Hashtable var3) throws NamingException, RemoteException {
      if (var2 == null) {
         throw new NamingException("Cannot rebind null object into jndi tree " + var1);
      } else {
         String var4 = this.getPrefix(var1);
         String var5 = this.getRest(var1);
         if (var5.length() == 0) {
            this.rebindHere(var4, var2, var3, true);
         } else {
            Object var6 = this.lookupHereOrCreate(var4, var3, var5);

            try {
               if (var6 instanceof NamingNode) {
                  ((NamingNode)var6).rebind(var5, var2, var3);
               } else {
                  this.getContinuationCtx(var6, var4, var5, var3).rebind(var5, var2);
               }
            } catch (NamingException var8) {
               throw this.prependResolvedNameToException(var4, var8);
            }
         }

      }
   }

   public void rebind(Name var1, Object var2, Hashtable var3) throws NamingException, RemoteException {
      int var4 = var1.size();
      if (var4 != 0) {
         String var5;
         if (var4 == 1) {
            var5 = var1.get(0);
            this.rebindHere(var5, var2, var3, true);
         } else {
            var5 = var1.get(0);
            var1.remove(0);
            Object var6 = this.lookupHereOrCreate(var5, var3, var1.toString());

            try {
               if (var6 instanceof NamingNode) {
                  ((NamingNode)var6).rebind(var1, var2, var3);
               } else {
                  this.getContinuationCtx(var6, var5, var1.toString(), var3).rebind(var1, var2);
               }
            } catch (NamingException var8) {
               throw this.prependResolvedNameToException(var5, var8);
            }
         }

      }
   }

   public void rebind(String var1, Object var2, Object var3, Hashtable var4) throws NamingException, RemoteException {
      String var5 = this.getPrefix(var1);
      String var6 = this.getRest(var1);
      if (var6.length() == 0) {
         this.rebindHere(var5, var2, var3, var4, true);
      } else {
         Object var7 = this.lookupHereOrCreate(var5, var4, var6);

         try {
            if (var7 instanceof NamingNode) {
               ((NamingNode)var7).rebind(var6, var2, var3, var4);
            } else {
               ((WLContext)this.getContinuationCtx(var7, var5, var6, var4)).rebind(var6, var2, var3);
            }
         } catch (NamingException var9) {
            throw this.prependResolvedNameToException(var5, var9);
         }
      }

   }

   protected void rebindHere(String var1, Object var2, Object var3, Hashtable var4, boolean var5) throws NamingException {
      this.rebindHere(var1, var3, var4, var5);
   }

   protected void rebindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NamingException {
      Object var5;
      try {
         if (!(var2 instanceof Aggregatable)) {
            synchronized(this.map) {
               var5 = this.map.get(var1);
               if (var5 instanceof BasicNamingNode && ((BasicNamingNode)var5).isVersioned()) {
                  throw this.fillInException(new NamingException(var1 + " was bound with version previously" + ".  Cannot rebind without version."), var1, var5, "");
               }

               this.map.put(var1, var2);
            }
         } else {
            synchronized(this.map) {
               var5 = this.map.get(var1);
               if (var5 instanceof BasicNamingNode && ((BasicNamingNode)var5).isVersioned()) {
                  throw this.fillInException(new NamingException(var1 + " was bound with version previously" + ".  Cannot rebind without version."), var1, var5, "");
               }

               if (var5 == null) {
                  ((Aggregatable)var2).onBind(this, var1, (Aggregatable)null);
                  this.map.put(var1, var2);
               } else if (var5 instanceof Aggregatable) {
                  ((Aggregatable)var5).onRebind(this, var1, (Aggregatable)var2);
               }
            }
         }

         if (NamingDebugLogger.isDebugEnabled()) {
            NamingDebugLogger.debug("+++ rebind(" + var1 + ", " + var2.getClass().getName() + ") succeeded");
         }
      } catch (NamingException var11) {
         NamingException var6 = this.fillInException(var11, var1, this, "");
         if (NamingDebugLogger.isDebugEnabled()) {
            NamingDebugLogger.debug("+++ rebind(" + var1 + ", " + var2.getClass().getName() + ") failed due to: " + var6);
         }

         throw var6;
      }

      if (this.notifyNameListeners()) {
         this.fireNameListeners(var1, this.setUpNotification(var1, 3, var3, var2, var5));
      }

   }

   public void unbind(String var1, Hashtable var2) throws NamingException, RemoteException {
      this.unbind(var1, (Object)null, var2);
   }

   public void unbind(String var1, Object var2, Hashtable var3) throws NamingException, RemoteException {
      String var4 = this.getPrefix(var1);
      String var5 = this.getRest(var1);
      if (var5.length() == 0) {
         this.unbindHere(var4, var2, var3, true);
      } else {
         Object var6;
         try {
            var6 = this.lookupHere(var4, var3, var5);
         } catch (NameNotFoundException var9) {
            return;
         }

         try {
            if (var6 instanceof NamingNode) {
               ((NamingNode)var6).unbind(var5, var2, var3);
            } else {
               this.getContinuationCtx(var6, var4, var5, var3).unbind(var5);
            }
         } catch (NamingException var8) {
            throw this.prependResolvedNameToException(var4, var8);
         }
      }

   }

   protected void unbindHere(String var1, Object var2, Hashtable var3, boolean var4) throws NamingException {
      synchronized(this.map) {
         Object var5 = this.map.get(var1);
         if (var5 instanceof Aggregatable) {
            if ((var2 == null || var2 instanceof Aggregatable) && ((Aggregatable)var5).onUnbind(this, var1, (Aggregatable)var2)) {
               this.map.remove(var1);
               if (this.notifyNameListeners()) {
                  this.fireNameListeners(var1, this.setUpNotification(var1, 1, var3, var2, var5));
               }
            }
         } else {
            this.map.remove(var1);
            if (this.notifyNameListeners()) {
               this.fireNameListeners(var1, this.setUpNotification(var1, 1, var3, var2, var5));
            }
         }
      }

      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ unbind(" + var1 + ") succeeded ");
      }

   }

   public void rename(String var1, String var2, Hashtable var3) throws NamingException, RemoteException {
      Object var4 = this.lookupLink(var1, var3);
      this.bind(var2, var4, var3);
      this.unbind(var1, var3);
   }

   public NamingEnumeration list(String var1, Hashtable var2) throws NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      if (var3.length() != 0) {
         Object var13 = this.lookupHere(var3, var2, var4);

         NamingEnumeration var14;
         try {
            if (var13 instanceof NamingNode) {
               var14 = ((NamingNode)var13).list(var4, var2);
            } else {
               var14 = this.getContinuationCtx(var13, var3, var4, var2).list(var4);
            }
         } catch (NamingException var12) {
            throw this.prependResolvedNameToException(var3, var12);
         }

         return (NamingEnumeration)this.makeTransportable(var14, (String)var4, var2);
      } else {
         Set var5 = this.listThis(var2);
         Iterator var6 = var5.iterator();
         NameClassPair[] var7 = new NameClassPair[var5.size()];

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Map.Entry var9 = (Map.Entry)var6.next();
            String var10 = (String)var9.getKey();
            String var11 = var9.getValue().getClass().getName();
            var7[var8] = new NameClassPair(var10, var11);
         }

         return new NameClassPairEnumeration(var7);
      }
   }

   protected Set listThis(Hashtable var1) throws NamingException {
      return this.map.entrySet();
   }

   public NamingEnumeration listBindings(String var1, Hashtable var2) throws NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      if (var3.length() != 0) {
         Object var16 = this.lookupHere(var3, var2, var4);

         NamingEnumeration var17;
         try {
            if (var16 instanceof NamingNode) {
               var17 = ((NamingNode)var16).listBindings(var4, var2);
            } else {
               var17 = this.getContinuationCtx(var16, var3, var4, var2).listBindings(var4);
            }
         } catch (NamingException var15) {
            throw this.prependResolvedNameToException(var3, var15);
         }

         return (NamingEnumeration)this.makeTransportable(var17, (String)var4, var2);
      } else {
         Set var5 = this.listThis(var2);
         Iterator var6 = var5.iterator();
         Binding[] var7 = new Binding[var5.size()];

         for(int var8 = 0; var8 < var7.length; ++var8) {
            Map.Entry var9 = (Map.Entry)var6.next();
            String var10 = (String)var9.getKey();

            Object var11;
            try {
               var11 = this.resolveObject(var10, var9.getValue(), var2);
            } catch (LinkException var13) {
               var11 = var9.getValue();
            } catch (NameNotFoundException var14) {
               var11 = var9.getValue();
            }

            var7[var8] = new Binding(var10, var11);
         }

         return new BindingEnumeration(var7);
      }
   }

   public Context createSubcontext(String var1, Hashtable var2) throws NamingException, RemoteException {
      if (this.separators.length() == 0) {
         OperationNotSupportedException var8 = new OperationNotSupportedException("Cannot call createSubcontext is a flat namespace");
         throw this.fillInException(var8, var1, (Object)null, (String)null);
      } else {
         String var3 = this.getPrefix(var1);
         String var4 = this.getRest(var1);
         if (var4.length() == 0) {
            return this.createSubnodeHere(var3, var2).getContext(var2);
         } else {
            Object var5 = this.lookupHereOrCreate(var3, var2, var4);

            try {
               return var5 instanceof NamingNode ? ((NamingNode)var5).createSubcontext(var4, var2) : this.getContinuationCtx(var5, var3, var4, var2).createSubcontext(var4);
            } catch (NamingException var7) {
               throw this.prependResolvedNameToException(var3, var7);
            }
         }
      }
   }

   protected NamingNode createSubnodeHere(String var1, Hashtable var2) throws NamingException {
      synchronized(this.map) {
         Object var4 = this.map.get(var1);
         if (var4 == null) {
            if (var1.length() == 0) {
               return this;
            } else {
               BasicNamingNode var5 = this.newSubnode(var1);
               if (NamingResolutionDebugLogger.isDebugEnabled()) {
                  NamingResolutionDebugLogger.debug("--- created sub node " + var1 + " " + var5);
               }

               this.map.put(var1, var5);
               if (this.notifyNameListeners()) {
                  this.fireNameListeners(var1, this.setUpNotification(var1, 0, var2, var5.getContext(var2), (Object)null));
               }

               return var5;
            }
         } else if (var4 instanceof NamingNode) {
            return (NamingNode)var4;
         } else {
            throw this.fillInException(new NameAlreadyBoundException(var1), var1, (Object)null, "");
         }
      }
   }

   protected BasicNamingNode newSubnode(String var1) {
      return new BasicNamingNode(this.separators, this, var1);
   }

   public void destroySubcontext(String var1, Hashtable var2) throws NamingException, RemoteException {
      String var3 = this.getPrefix(var1);
      String var4 = this.getRest(var1);
      if (var4.length() == 0) {
         this.destroySubnodeHere(var1, var2);
      } else {
         Object var5 = this.map.get(var3);
         if (var5 == null) {
            throw this.newNameNotFoundException("Cannot destroy non exisiting context " + var3 + " in " + this.getNameInNamespace(var3), var4, var2);
         }

         try {
            if (var5 instanceof NamingNode) {
               ((NamingNode)var5).destroySubcontext(var4, var2);
            } else {
               this.getContinuationCtx(var5, var3, var4, var2).destroySubcontext(var4);
            }
         } catch (NamingException var7) {
            throw this.prependResolvedNameToException(var3, var7);
         }
      }

   }

   protected void destroySubnodeHere(String var1, Hashtable var2) throws NameNotFoundException, ContextNotEmptyException, NamingException {
      Object var3 = this.map.get(var1);
      if (var3 != null) {
         if (!(var3 instanceof BasicNamingNode)) {
            throw this.newNameNotFoundException("Cannot destroy non existing subcontext " + var1 + " in " + this.getNameInNamespace(var1), "", var2);
         } else {
            BasicNamingNode var4 = (BasicNamingNode)var3;
            if (var4.map.size() > 0) {
               throw this.fillInException(new ContextNotEmptyException(), var1, var3, "");
            } else {
               try {
                  if (var3 instanceof Remote) {
                     ServerHelper.unexportObject((Remote)var3, true);
                  }
               } catch (NoSuchObjectException var6) {
                  if (NamingDebugLogger.isDebugEnabled()) {
                     NamingDebugLogger.debug("No such object exception when trying to destroy the subcontext. Possibly a race condition between two threads both unexporting the object. This should not cause any errors in and of itself, however. Both threads have achieved  their goal. Exception: " + var6);
                  }
               }

               this.map.remove(var1);
            }
         }
      }
   }

   public Context getContext(Hashtable var1) {
      return new WLEventContextImpl(var1, this);
   }

   private EventContext getEventContext(Hashtable var1) {
      return new WLEventContextImpl(var1, this);
   }

   private Object resolveObject(String var1, Object var2, Hashtable var3) throws NamingException {
      return this.resolveObject(var1, var2, 1, var3);
   }

   protected Object resolveObject(String var1, Object var2, int var3, Hashtable var4) throws NamingException {
      Object var5 = var2;
      if (var2 != null) {
         try {
            if (var2 instanceof NamingNode) {
               var5 = ((NamingNode)var2).getContext(var4);
            } else if (var3 != 0 && var3 >= 0) {
               var5 = WLNamingManager.getObjectInstance(var2, new CompositeName(var1), (Context)null, var4);
               var5 = this.makeTransportable(var5, var1, var4);
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

   private String getPrefix(String var1) throws NamingException {
      if (var1.length() == 0) {
         return var1;
      } else {
         int var4;
         if (var1.charAt(0) == '"') {
            var4 = var1.indexOf(34, 1);
            if (var4 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var4 < var1.length() - 1 && !this.isSeparator(var1.charAt(var4 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               return var1.substring(1, var4);
            }
         } else if (var1.charAt(0) == '\'') {
            var4 = var1.indexOf(39, 1);
            if (var4 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var4 < var1.length() - 1 && !this.isSeparator(var1.charAt(var4 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               return var1.substring(1, var4);
            }
         } else {
            StringBuffer var2 = new StringBuffer();

            for(int var3 = 0; var3 < var1.length(); ++var3) {
               switch (var1.charAt(var3)) {
                  case '"':
                  case '\'':
                     throw new InvalidNameException("Unescaped quote in a component");
                  case '\\':
                     ++var3;
                     if (var3 == var1.length()) {
                        throw new InvalidNameException("An escape at the end of a name must be escaped");
                     }

                     var2.append(var1.charAt(var3));
                     break;
                  default:
                     if (this.isSeparator(var1.charAt(var3))) {
                        return var2.toString();
                     }

                     var2.append(var1.charAt(var3));
               }
            }

            return var2.toString();
         }
      }
   }

   private boolean isSeparator(char var1) {
      for(int var2 = 0; var2 < this.separators.length(); ++var2) {
         if (var1 == this.separators.charAt(var2)) {
            return true;
         }
      }

      return false;
   }

   private String getRest(String var1) throws NamingException {
      if (var1.length() == 0) {
         return var1;
      } else {
         int var2;
         if (var1.charAt(0) == '"') {
            var2 = var1.indexOf(34, 1);
            if (var2 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var2 < var1.length() - 1 && !this.isSeparator(var1.charAt(var2 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               ++var2;
               if (var2 < var1.length() && this.isSeparator(var1.charAt(var2))) {
                  ++var2;
               }

               return var1.substring(var2);
            }
         } else if (var1.charAt(0) == '\'') {
            var2 = var1.indexOf(39, 1);
            if (var2 < 0) {
               throw new InvalidNameException("No closing quote");
            } else if (var2 < var1.length() - 1 && !this.isSeparator(var1.charAt(var2 + 1))) {
               throw new InvalidNameException("Closing quote must be at component end");
            } else {
               ++var2;
               if (var2 < var1.length() && this.isSeparator(var1.charAt(var2))) {
                  ++var2;
               }

               return var1.substring(var2);
            }
         } else {
            for(var2 = 0; var2 < var1.length(); ++var2) {
               switch (var1.charAt(var2)) {
                  case '"':
                  case '\'':
                     throw new InvalidNameException("Unescaped quote in a component");
                  case '\\':
                     ++var2;
                     if (var2 == var1.length()) {
                        throw new InvalidNameException("An escape at the end of a name must be escaped");
                     }
                     break;
                  default:
                     if (this.isSeparator(var1.charAt(var2))) {
                        return var1.substring(var2 + 1);
                     }
               }
            }

            return "";
         }
      }
   }

   protected String escapeBinding(String var1) {
      StringBuffer var2 = new StringBuffer("");

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (var3 != 0 && var3 != var1.length() - 1 && isQuote(var1.charAt(var3)) || this.isSeparator(var1.charAt(var3))) {
            var2.append('\\');
         }

         var2.append(var1.charAt(var3));
      }

      return var2.toString();
   }

   private static boolean isQuote(char var0) {
      return var0 == '"' || var0 == '\'';
   }

   protected final Object makeTransportable(Object var1, String var2, Hashtable var3) throws NamingException {
      return this.makeTransportable(var1, (Name)(new CompositeName(var2)), var3);
   }

   protected final Object makeTransportable(Object var1, Name var2, Hashtable var3) throws NamingException {
      return WLNamingManager.getTransportableInstance(var1, var2, (Context)null, var3);
   }

   protected final NamingException fillInException(NamingException var1, String var2, Object var3, String var4) {
      try {
         if (var4 == null) {
            var4 = "";
         } else {
            var4 = var4.replace('.', '/');
         }

         var1.setResolvedName(WLNameParser.defaultParse(var2));
         var1.setResolvedObj(var3);
         var1.setRemainingName(new CompositeName(var4));
         if (!NamingService.getNamingService().isRunning()) {
            var1.setRootCause(new ConnectIOException("Server is being shut down"));
         }

         return var1;
      } catch (NamingException var6) {
         throw new AssertionError(var6);
      }
   }

   protected final NameNotFoundException newNameNotFoundException(String var1, String var2, Hashtable var3) {
      return this.newNameNotFoundException((NamingException)(new NameNotFoundException(var1)), var2, var3);
   }

   public NameNotFoundException newNameNotFoundException(NamingException var1, String var2, Hashtable var3) {
      return NamingService.getNamingService().isRunning() ? (NameNotFoundException)this.fillInException(var1, "", this.getContext(var3), var2) : (NameNotFoundException)this.fillInException(var1, "", this, var2);
   }

   private Context getContinuationCtx(Object var1, String var2, String var3, Hashtable var4) throws NamingException {
      CannotProceedException var5 = new CannotProceedException();
      Hashtable var6 = (Hashtable)var4.clone();
      if (var6.get("java.naming.factory.url.pkgs") == null) {
         var6.put("java.naming.factory.url.pkgs", NamingService.getNamingService().getUrlPkgPrefixes());
      }

      var5.setEnvironment(var6);
      this.fillInException(var5, var2, var1, var3);
      return (Context)(this instanceof DirContext ? DirectoryManager.getContinuationDirContext(var5) : NamingManager.getContinuationContext(var5));
   }

   private NamingException prependResolvedNameToException(String var1, NamingException var2) {
      try {
         Name var3 = var2.getResolvedName();
         if (var3 == null) {
            try {
               var3 = WLNameParser.defaultParse("");
            } catch (NamingException var5) {
               throw new AssertionError(var5);
            }
         } else {
            var3.add(0, var1);
         }

         var2.setResolvedName(var3);
         return var2;
      } catch (InvalidNameException var6) {
         throw new AssertionError(var6);
      }
   }

   protected final String getProperty(Hashtable var1, String var2) {
      String var3 = null;
      if (var1 != null) {
         var3 = (String)var1.get(var2);
      }

      if (var3 == null) {
         var3 = System.getProperty(var2);
      }

      return var3 == null ? "" : var3;
   }

   protected int getNumOfBindings() {
      return this.map.size();
   }

   protected boolean isVersioned() {
      return false;
   }

   public void addNamingListener(String var1, int var2, NamingListener var3, Hashtable var4) throws NamingException {
      String var5 = this.getPrefix(var1);
      String var6 = this.getRest(var1);
      if (var6.length() == 0) {
         if (var2 == 1) {
            NamingNode var7 = (NamingNode)this.lookupHereOrCreate(var5, var4, var6);
            var7.addOneLevelScopeNamingListener(var3);
         } else {
            this.addListener(var5, var2, var3);
         }

         if (NamingResolutionDebugLogger.isDebugEnabled()) {
            Name var8 = this.nameParser.parse(this.getNameInNamespace(var1));
            NamingResolutionDebugLogger.debug("+++ Added listener of scope " + var2 + " at " + var8);
         }
      } else {
         Object var9 = this.lookupHereOrCreate(var5, var4, var6);
         if (!(var9 instanceof NamingNode)) {
            throw new AssertionError("Tried to create context but failed" + var9.toString());
         }

         ((NamingNode)var9).addNamingListener(var6, var2, var3, var4);
      }

   }

   private synchronized void addListener(String var1, int var2, NamingListener var3) {
      if (var2 == 0) {
         synchronized(this.objectListenerMap) {
            ArrayList var5 = (ArrayList)this.objectListenerMap.get(var1);
            if (var5 == null) {
               var5 = new ArrayList();
               this.objectListenerMap.put(var1, var5);
            }

            var5.add(var3);
         }
      } else if (var2 == 2) {
         this.subtreeScopeNameListenerList.add(var3);
      }

   }

   public void addOneLevelScopeNamingListener(NamingListener var1) {
      synchronized(this.lock) {
         this.onelevelNameListenerList.add(var1);
      }
   }

   public void removeNamingListener(NamingListener var1, Hashtable var2) {
      synchronized(this.lock) {
         this.onelevelNameListenerList.remove(var1);
      }

      Iterator var3 = this.objectListenerMap.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         ArrayList var5 = (ArrayList)this.objectListenerMap.get(var4);
         int var6 = var5.indexOf(var1);
         if (var6 > -1) {
            var5.remove(var6);
            break;
         }
      }

      if (NamingResolutionDebugLogger.isDebugEnabled()) {
         NamingResolutionDebugLogger.debug("--- Removed listener " + var1 + " from current context ");
      }

   }

   public List getOneLevelScopeNamingListeners() {
      return this.onelevelNameListenerList;
   }

   protected boolean notifyNameListeners() {
      return this.getOneLevelScopeNamingListeners().size() > 0 || this.objectListenerMap.size() > 0 || this.subtreeScopeNameListenerList.size() > 0;
   }

   protected void fireNameListeners(String var1, NamingEvent var2) {
      ArrayList var3 = new ArrayList();
      if (this.objectListenerMap.get(var1) != null) {
         ArrayList var4 = (ArrayList)this.objectListenerMap.get(var1);
         var3.addAll(var4);
      }

      if (this.getOneLevelScopeNamingListeners().size() > 0) {
         var3.addAll(0, this.getOneLevelScopeNamingListeners());
      }

      if (this.subtreeScopeNameListenerList.size() > 0) {
         var3.addAll(0, this.subtreeScopeNameListenerList);
      }

      final NotifyEventListeners var6 = new NotifyEventListeners(var3, var2, var2.getType());
      Runnable var5 = new Runnable() {
         public void run() {
            var6.notifyListeners();
         }
      };
      WorkManagerFactory.getInstance().getSystem().schedule(var5);
   }

   private NamingEvent setUpNotification(String var1, int var2, Hashtable var3, Object var4, Object var5) throws NamingException {
      String var6 = this.nameParser.parse(this.getNameInNamespace(var1)).toString();
      return new NamingEvent(this.getEventContext(var3), var2, new Binding(var6, var4), new Binding(var6, var5), (Object)null);
   }

   protected boolean isUnbound(String var1) {
      synchronized(this.map) {
         return this.map.get(var1) == null;
      }
   }
}
