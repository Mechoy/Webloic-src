package weblogic.iiop;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteStub;
import java.util.ArrayList;
import java.util.HashMap;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CosTransactions.TransactionalObject;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import weblogic.corba.ejb.CorbaBean;
import weblogic.corba.idl.CorbaServerRef;
import weblogic.corba.idl.CorbaStub;
import weblogic.corba.idl.DelegateFactory;
import weblogic.corba.idl.ObjectImpl;
import weblogic.corba.idl.poa.POAImpl;
import weblogic.corba.j2ee.naming.ContextImpl;
import weblogic.corba.rmi.Stub;
import weblogic.corba.utils.RemoteInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.spi.IORDelegate;
import weblogic.kernel.Kernel;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.protocol.ServerIdentity;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.cluster.ClusterableServerRef;
import weblogic.rmi.cluster.MigratableReplicaHandler;
import weblogic.rmi.cluster.PrimarySecondaryReplicaHandler;
import weblogic.rmi.cluster.ReplicaAwareInfo;
import weblogic.rmi.cluster.ReplicaList;
import weblogic.rmi.cluster.Version;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.ActivatableRemoteReference;
import weblogic.rmi.extensions.server.CollocatedRemoteReference;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.ClientMethodDescriptor;
import weblogic.rmi.internal.ClientRuntimeDescriptor;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.RuntimeDescriptor;
import weblogic.rmi.internal.ServerReference;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.io.RemoteObjectReplacer;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.io.Replacer;

public final class IIOPReplacer implements Replacer {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static RemoteObjectReplacer replacer = (RemoteObjectReplacer)RemoteObjectReplacer.getReplacer();
   private static final IIOPReplacer theIIOPReplacer = new IIOPReplacer();
   private static final DebugCategory debugReplacer = Debug.getCategory("weblogic.iiop.replacer");
   private static final DebugLogger debugIIOPReplacer = DebugLogger.getDebugLogger("DebugIIOPRepalcer");

   static void p(String var0) {
      System.out.println("<IIOPReplacer>: " + var0);
   }

   private IIOPReplacer() {
   }

   public static final IIOPReplacer getIIOPReplacer() {
      return theIIOPReplacer;
   }

   public static final Replacer getReplacer() {
      return theIIOPReplacer;
   }

   public final Object replaceObject(Object var1) throws IOException {
      Object var2 = var1;
      if (var1 == null) {
         return null;
      } else {
         IOR var3 = null;
         String var4 = null;
         if (var1 instanceof ContextImpl && ((ContextImpl)var1).getContext() != null) {
            var1 = ((ContextImpl)var1).getContext();
         }

         if (var1 instanceof ObjectImpl) {
            var3 = ((ObjectImpl)var1).getIOR();
            if (var3 == null) {
               var4 = ((ObjectImpl)var1).getTypeId().toString();
            }
         } else if (var1 instanceof IIOPRemoteRef) {
            var3 = ((IIOPRemoteRef)var1).getIOR();
         } else if (!(var1 instanceof Remote) && var1 instanceof Servant) {
            Servant var16 = (Servant)var1;
            ((POAImpl)((POAImpl)var16._poa())).export();

            try {
               var16._poa().servant_to_id(var16);
            } catch (ServantNotActive var11) {
               throw (IOException)(new IOException()).initCause(var11);
            } catch (WrongPolicy var12) {
               throw (IOException)(new IOException()).initCause(var12);
            }

            ServerReference var13 = OIDManager.getInstance().getServerReference(var16._poa());
            var3 = ((IORDelegate)var16._get_delegate()).getIOR();
            var3 = this.getIORFromReference(var13.getRemoteRef(), var3.getTypeId().toString(), var13.getApplicationName(), var3.getProfile().getObjectKey().getActivationID());
         } else if (!(var1 instanceof Remote) && var1 instanceof InvokeHandler) {
            ServerReference var15 = OIDManager.getInstance().getServerReference(var1);
            if (var15 == null) {
               synchronized(var1) {
                  var15 = OIDManager.getInstance().getServerReference(var1);
                  if (var15 == null && !var1.getClass().getName().equals("weblogic.corba.cos.transactions.CoordinatorImpl")) {
                     (new CorbaServerRef((InvokeHandler)var1)).exportObject();
                  }
               }
            }
         } else if (!(var1 instanceof Remote) && var1 instanceof org.omg.CORBA.portable.ObjectImpl) {
            Delegate var14 = ((org.omg.CORBA.portable.ObjectImpl)var1)._get_delegate();
            if (var14 instanceof IORDelegate) {
               var3 = ((IORDelegate)var14).getIOR();
               if (Kernel.isServer() && var3.isLocal() && !var3.getProfile().isClusterable()) {
                  ObjectKey var6 = var3.getProfile().getObjectKey();
                  ServerReference var7 = OIDManager.getInstance().getServerReference(var6.getObjectID());
                  if (var7.getDescriptor().isClusterable()) {
                     var3 = this.getIORFromReference(var7.getRemoteRef(), var3.getTypeId().toString(), var7.getApplicationName(), var6.getActivationID());
                  }
               }
            }
         } else if (var1 instanceof CorbaBean) {
            CorbaServerRef var5 = (CorbaServerRef)OIDManager.getInstance().getServerReference(var1);
            if (var5 == null) {
               synchronized(var1) {
                  var5 = (CorbaServerRef)OIDManager.getInstance().getServerReference(var1);
                  if (var5 == null) {
                     var5 = new CorbaServerRef(var1);
                     var5.exportObject();
                  }
               }
            }

            var3 = var5.getIOR();
         }

         if (var3 == null) {
            if (var1 instanceof CollocatedRemoteReference) {
               var1 = ((CollocatedRemoteReference)var1).getServerReference().getImplementation();
            }

            var1 = replacer.replaceObject(var1);
            if (var1 instanceof StubInfoIntf) {
               var3 = this.getIORFromStub((StubInfoIntf)var1, var4);
            } else {
               if (!(var1 instanceof StubReference)) {
                  if (var1 instanceof RemoteReference) {
                     return var1;
                  }

                  if (var1 instanceof CollocatedRemoteReference) {
                     throw new AssertionError("Impossible for: " + var1 + " to be collocated after replacement");
                  }

                  if (var1 instanceof RemoteStub) {
                     throw new MARSHAL("Couldn't export RemoteStub " + var1);
                  }

                  return var1;
               }

               var3 = this.getIORFromStub((StubInfo)var1, var4);
            }
         }

         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
            IIOPLogger.logDebugReplacer("replaced: " + var2.getClass().getName() + "{" + var2 + "} ==> " + var3);
         }

         return var3;
      }
   }

   public final void insertReplacer(Replacer var1) {
   }

   public final IOR replaceRemote(Object var1) throws IOException {
      Object var2 = this.replaceObject(var1);
      if (var2 == null) {
         return IOR.NULL;
      } else if (!(var2 instanceof IOR)) {
         throw new MARSHAL("Couldn't export Object: " + var1 + " resolved to: " + var2);
      } else {
         return (IOR)var2;
      }
   }

   private IOR getIORFromStub(StubInfoIntf var1, String var2) throws RemoteException, IOException {
      return this.getIORFromStub(var1.getStubInfo(), var2);
   }

   public IOR getIORFromStub(StubInfo var1) throws RemoteException, IOException {
      return this.getIORFromStub((StubInfo)var1, (String)null);
   }

   private IOR getIORFromStub(StubInfo var1, String var2) throws RemoteException, IOException {
      RemoteReference var3 = var1.getRemoteRef();
      if (var2 == null && !(var3 instanceof IORDelegate)) {
         var2 = Utils.getRepositoryID(var1);
      }

      return this.getIORFromReference(var3, var2, var1.getApplicationName(), (Object)null);
   }

   private IOR getIORFromReference(RemoteReference var1, String var2, String var3, Object var4) throws RemoteException, IOException {
      IOR var5;
      if (var4 == null && var1 instanceof IORDelegate) {
         var5 = ((IORDelegate)var1).getIOR();
      } else {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("TypeID = " + var2);
         }

         if (var1 instanceof ClusterableRemoteRef) {
            ClusterableRemoteRef var6 = (ClusterableRemoteRef)var1;
            ClusterComponent var7 = null;
            if (var6.isInitialized() && var6.getReplicaList() instanceof VendorInfoCluster && (var7 = ((VendorInfoCluster)var6.getReplicaList()).getClusterInfo()) != null) {
               Debug.assertion(var7 != null);
               var5 = new IOR(((IIOPRemoteRef)var6.getCurrentReplica()).getIOR(), var7);
            } else if (var6.getHostID().isLocal()) {
               ClusterableServerRef var8 = (ClusterableServerRef)OIDManager.getInstance().getServerReference(var6.getObjectID());
               RuntimeDescriptor var9 = var8.getDescriptor();
               String var10 = "default";
               if (var9.getLoadAlgorithm() != null) {
                  var10 = var9.getLoadAlgorithm();
               }

               ReplicaAwareInfo var11 = var8.getInfo();
               if (!var6.isInitialized()) {
                  var6.initialize(var11);
               }

               boolean var12 = var11.getStickToFirstServer();
               if (var6.getReplicaHandler() instanceof PrimarySecondaryReplicaHandler) {
                  var10 = "primary-secondary";
                  var12 = true;
               } else if (var6.getReplicaHandler() instanceof MigratableReplicaHandler) {
                  var10 = "migratable";
               }

               ReplicaList var13 = var6.getReplicaList();
               if (var13 != null) {
                  int var14 = var13.size();
                  ArrayList var15 = new ArrayList();

                  for(int var16 = 0; var16 < var14; ++var16) {
                     var15.add(this.getIOR(var13.get(var16), var2, var3, (ClusterComponent)null, var9, var4));
                  }

                  var6.getReplicaHandler();
                  var7 = new ClusterComponent(var9.getMethodsAreIdempotent(), var12, var10, var11.getJNDIName(), var15, (Version)var13.version());
                  if (var13 instanceof VendorInfoCluster) {
                     ((VendorInfoCluster)var13).setClusterInfo(var7);
                  }
               }

               var5 = this.getIOR(var6.getCurrentReplica(), var2, var3, var7, var9, var4);
            } else {
               IIOPLogger.logReplacerFailure("no local server reference for: " + var1 + " using pinned primary reference");
               var5 = this.getIOR(var6.getCurrentReplica(), var2, var3, (ClusterComponent)null, (RuntimeDescriptor)null, var4);
            }
         } else {
            ServerReference var17 = OIDManager.getInstance().getServerReference(var1.getObjectID());
            var5 = this.getIOR(var1, var2, var3, (ClusterComponent)null, var17.getDescriptor(), var4);
         }
      }

      return var5;
   }

   final IOR getIOR(RemoteReference var1, String var2, String var3, ClusterComponent var4, RuntimeDescriptor var5, Object var6) {
      IOR var7;
      if (var6 == null && var1 instanceof IORDelegate) {
         var7 = ((IORDelegate)var1).getIOR();
      } else {
         int var8 = var1.getObjectID();
         Debug.assertion(var8 != -1);
         ServerChannel var9 = ServerChannelManager.findServerChannel(var1.getHostID(), ProtocolHandlerIIOP.PROTOCOL_IIOP);
         String var10 = null;
         int var11 = -1;
         if (var9 != null) {
            var10 = var9.getPublicAddress();
            var11 = var9.getPublicPort();
         }

         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("creating ior for port = " + var11 + " address = " + var10 + " channel = " + var9);
         }

         ObjectKey var12;
         if (var6 != null) {
            var12 = new ObjectKey(var2, var8, var6);
         } else if (var1 instanceof ActivatableRemoteReference) {
            var12 = new ObjectKey(var2, var8, ((ActivatableRemoteReference)var1).getActivationID());
         } else {
            var12 = new ObjectKey(var2, var8, (ServerIdentity)var1.getHostID());
         }

         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("IIOPReplacer created ObjectKey = " + var12);
         }

         var7 = new IOR(var2, var10, var11, var12, var3, var4, var5);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("exported IOR: " + var7);
         }
      }

      return var7;
   }

   public Object resolveObject(Object var1) throws IOException {
      Object var2 = var1;
      if (var1 instanceof ObjectImpl) {
         var2 = ((ObjectImpl)var1).getRemote();
      } else if (var1 instanceof IOR) {
         var2 = resolveObject((IOR)var1);
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
         IIOPLogger.logDebugReplacer("resolved: " + var1.getClass().getName() + "{" + var1 + "} ==> " + var2.getClass().getName() + "{" + var2 + "}");
      }

      return var2;
   }

   public static Object resolveObject(IOR var0) throws IOException {
      if (var0.isNull()) {
         return null;
      } else {
         RemoteInfo var1 = RemoteInfo.findRemoteInfo(var0.getTypeId(), var0.getCodebase());
         Class var2 = var1 == null ? null : var1.getTheClass();
         if (var0.getTypeId().isIDLType()) {
            try {
               Class var3 = var2 == null ? null : Utils.getStubFromClass(var2);
               return createCORBAStub(var0, var2, var3);
            } catch (InstantiationException var4) {
               return resolveObject(var0, var1);
            } catch (IllegalAccessException var5) {
               throw new MARSHAL("IllegalAccessException reading CORBA object " + var5.getMessage());
            }
         } else {
            if (var1 == null) {
               var1 = RemoteInfo.findRemoteInfo(var0.getTypeId(), org.omg.CORBA.Object.class);
            }

            return resolveObject(var0, var1);
         }
      }
   }

   public static org.omg.CORBA.Object createCORBAStub(IOR var0, Class var1, Class var2) throws InstantiationException, IllegalAccessException {
      Object var3;
      if (var2 != null && org.omg.CORBA.portable.ObjectImpl.class.isAssignableFrom(var2)) {
         try {
            var3 = (org.omg.CORBA.portable.ObjectImpl)var2.newInstance();
            ((org.omg.CORBA.portable.ObjectImpl)var3)._set_delegate(DelegateFactory.createDelegate(var0));
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
               IIOPLogger.logDebugReplacer("resolved: " + var0.getTypeId() + " using portable streaming CORBA stub: " + var2.getName());
            }
         } catch (InstantiationException var11) {
            try {
               Constructor var5 = var2.getConstructor(Delegate.class);
               var3 = (org.omg.CORBA.portable.ObjectImpl)var5.newInstance(DelegateFactory.createDelegate(var0));
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
                  IIOPLogger.logDebugReplacer("resolved: " + var0.getTypeId() + " using DII-style CORBA stub: " + var2.getName());
               }
            } catch (NoSuchMethodException var8) {
               throw (InstantiationException)(new InstantiationException("Could not instantiate stub")).initCause(var8);
            } catch (IllegalArgumentException var9) {
               throw (InstantiationException)(new InstantiationException("Could not instantiate stub")).initCause(var9);
            } catch (InvocationTargetException var10) {
               throw (InstantiationException)(new InstantiationException("Could not instantiate stub")).initCause(var10.getTargetException());
            }
         }
      } else {
         var3 = new AnonymousCORBAStub(var0.getTypeId().toString(), DelegateFactory.createDelegate(var0));
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
            IIOPLogger.logDebugReplacer("resolved: " + var0.getTypeId() + " using anonymous CORBA stub");
         }
      }

      if (var1 != null && !var1.isInstance(var3)) {
         try {
            return Utils.narrow((org.omg.CORBA.portable.ObjectImpl)var3, var1);
         } catch (InvocationTargetException var6) {
         } catch (ClassCastException var7) {
         }
      }

      return (org.omg.CORBA.Object)var3;
   }

   public static Object resolveObject(IOR var0, RemoteInfo var1) throws IOException {
      if (var0.isNull()) {
         return null;
      } else {
         StubInfo var2 = getStubInfo(var0, var1);
         RemoteObjectReplacer var10000 = replacer;
         Object var3 = RemoteObjectReplacer.resolveStubInfo(var2);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled() || debugReplacer.isEnabled() || debugIIOPReplacer.isDebugEnabled()) {
            IIOPLogger.logDebugReplacer("resolved " + var0.getTypeId() + ", " + var1.getClassName() + " => " + var3);
         }

         return var3;
      }
   }

   public static Remote getRemoteIDLStub(Object var0) throws RemoteException {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("getRemoteIDLStub(" + var0 + ")");
      }

      try {
         IOR var1 = (IOR)getReplacer().replaceObject(var0);
         if (!var1.isLocal()) {
            return null;
         } else {
            int var2 = var1.getProfile().getObjectKey().getObjectID();
            ServerReference var3 = OIDManager.getInstance().getServerReference(var2);
            Object var4 = new IIOPRemoteRef(var1);
            if (var3.getDescriptor().isClusterable()) {
               var4 = (new ClusterableRemoteRef((RemoteReference)var4)).initialize(new ReplicaAwareInfo(var3.getDescriptor()));
            }

            StubInfo var5 = new StubInfo((RemoteReference)var4, var3.getDescriptor().getClientRuntimeDescriptor((String)null), (String)null, CorbaStub.class.getName());
            return (Remote)StubFactory.getStub(var5);
         }
      } catch (IOException var6) {
         return null;
      }
   }

   private static StubInfo getStubInfo(IOR var0, RemoteInfo var1) {
      ClusterComponent var2 = (ClusterComponent)var0.getProfile().getComponent(1111834883);
      if (var1 == null) {
         throw new MARSHAL(var0.getTypeId().toString());
      } else {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("typeid = " + var1.getClassName());
         }

         boolean var5 = var0.getProfile().isTransactional();
         if (TransactionalObject.class.isAssignableFrom(var1.getTheClass()) || IIOPService.txMechanism == 3) {
            var5 = true;
         }

         StubInfo var3;
         if (var2 != null && !var0.isLocal()) {
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("Creating ClusterableRemoteRef for : " + var0 + " stickToFirstServer: " + var2.getStickToFirstServer() + " clusterAlgorithm: " + var2.getClusterAlgorithm() + " jndiName: " + var2.getJndiName());
            }

            ClusterableRemoteRef var6 = new ClusterableRemoteRef(new IIOPRemoteRef(var0, var1));
            var6.initialize(new ReplicaAwareInfo(var2.getStickToFirstServer(), var2.getClusterAlgorithm(), var2.getJndiName()));
            ReplicaList var7 = var2.getReplicaList();
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p(var6.toString() + " replicaList reset from: " + var6.getReplicaList().version() + " to: " + var7);
            }

            var6.resetReplicaList(var7);
            var3 = getStubInfo(var6, var1, new ClientMethodDescriptor("*", var5, false, false, var2.getIdempotent(), 0));
         } else {
            var3 = getStubInfo(new IIOPRemoteRef(var0, var1), var1, new ClientMethodDescriptor("*", var5, false, false, false, 0));
         }

         return var3;
      }
   }

   private static StubInfo getStubInfo(RemoteReference var0, RemoteInfo var1, ClientMethodDescriptor var2) {
      String var3 = var0.getCodebase();
      int var5 = 0;
      String[] var4;
      if (var1 != null && var1.getTheClass() != org.omg.CORBA.Object.class && var1.getTheClass().isInterface()) {
         var4 = new String[3];
         var4[var5++] = var1.getClassName();
      } else {
         var4 = new String[2];
      }

      var4[var5++] = StubInfoIntf.class.getName();
      var4[var5++] = Remote.class.getName();
      HashMap var6 = null;
      String var7 = var1.getClassName();
      if (var1 != null && var1.getDescriptor() != null) {
         var6 = var1.getDescriptor().getClientMethodDescriptors();
         var7 = var1.getDescriptor().getStubClassName();
      }

      ClientRuntimeDescriptor var8 = new ClientRuntimeDescriptor(var4, Utils.getAnnotation((ClassLoader)null), var6, var2, var7, var3);
      var8 = var8.intern();
      return new StubInfo(var0, var8, var4[0] + "_IIOP_WLStub", Stub.class.getName());
   }

   public static org.omg.CORBA.Object makeInvocationHandler(IOR var0) throws IOException {
      return makeInvocationHandler(var0, (Class)null);
   }

   public static org.omg.CORBA.Object makeInvocationHandler(IOR var0, Class var1) throws IOException {
      Class var2 = null;
      if (var1 != null) {
         var2 = Utils.getClassFromStub(var1);
      }

      RemoteInfo var3 = null;
      RepositoryId var4 = var0.getTypeId();
      if (var2 == null) {
         boolean var5 = true;
         if (var0.isRemote() && var4 != null && !var4.isIDLType()) {
            ObjectKey var6 = var0.getProfile().getObjectKey();
            var5 = var6.isRepositoryIdAnInterface();
         }

         if (var5) {
            var3 = RemoteInfo.findRemoteInfo(var4, var0.getCodebase());
         }

         if (var3 != null) {
            var2 = var3.getTheClass();
         } else {
            var3 = RemoteInfo.findRemoteInfo(var4, org.omg.CORBA.Object.class);
         }
      } else {
         var3 = RemoteInfo.findRemoteInfo(var4, var2);
      }

      StubInfo var7 = getStubInfo(var0, var3);
      RemoteObjectReplacer var10000 = replacer;
      Object var8 = RemoteObjectReplacer.resolveStubInfo(var7);
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("makeInvocationHandler(" + var0 + ", " + var2 + "): " + var7 + " -> " + var8.getClass().getName() + "(" + var8 + ")");
      }

      if (var8 instanceof org.omg.CORBA.Object && var3.getTheClass().isInstance(var8)) {
         return (org.omg.CORBA.Object)var8;
      } else {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("makeInvocationHandler(): creating proxy stub");
         }

         return (org.omg.CORBA.Object)StubFactory.getStub(var7);
      }
   }

   public static class AnonymousCORBAStub extends org.omg.CORBA_2_3.portable.ObjectImpl {
      private final String[] ids;

      public AnonymousCORBAStub(String var1, Delegate var2) {
         this.ids = new String[]{var1};
         this._set_delegate(var2);
      }

      public String[] _ids() {
         return this.ids;
      }
   }
}
