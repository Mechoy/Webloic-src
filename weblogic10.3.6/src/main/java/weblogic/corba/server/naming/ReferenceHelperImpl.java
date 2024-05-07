package weblogic.corba.server.naming;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.portable.Delegate;
import org.omg.CosTransactions.TransactionalObject;
import weblogic.corba.idl.ObjectImpl;
import weblogic.corba.rmi.Stub;
import weblogic.corba.utils.RemoteInfo;
import weblogic.iiop.ClusterComponent;
import weblogic.iiop.IIOPRemoteRef;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IIOPService;
import weblogic.iiop.IOR;
import weblogic.iiop.Utils;
import weblogic.iiop.VendorInfoCluster;
import weblogic.iiop.spi.IORDelegate;
import weblogic.protocol.ClientEnvironment;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.extensions.StubFactory;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.ClientMethodDescriptor;
import weblogic.rmi.internal.ClientRuntimeDescriptor;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.internal.StubInfoIntf;
import weblogic.rmi.utils.io.RemoteObjectReplacer;

public class ReferenceHelperImpl extends ReferenceHelper {
   private static final boolean DEBUG = false;

   public ReferenceHelperImpl() {
      ClientEnvironment.loadEnvironment();
   }

   public Object narrow(Object var1, Class var2) throws NamingException {
      if (var1 != null && var2 != null) {
         Object var3 = null;

         try {
            StubInfo var4 = null;
            if (var1 instanceof StubInfoIntf && var1 instanceof org.omg.CORBA.Object) {
               var4 = ((StubInfoIntf)var1).getStubInfo();
               var1 = RemoteObjectReplacer.resolveStubInfo(var4);
            } else if (var1 instanceof StubReference) {
               var4 = (StubInfo)var1;
               var1 = RemoteObjectReplacer.resolveStubInfo(var4);
            } else if (var1 instanceof StubInfoIntf) {
               var4 = ((StubInfoIntf)var1).getStubInfo();
            }

            if (var2.isInstance(var1)) {
               var3 = var1;
            } else if (var1 instanceof ObjectImpl) {
               var1 = ((ObjectImpl)var1).getRemote();
               if (var2.isInstance(var1)) {
                  var3 = var1;
               }
            } else if (var1 instanceof org.omg.CORBA.portable.ObjectImpl && !(var1 instanceof Stub)) {
               IORDelegate var18 = (IORDelegate)((org.omg.CORBA.portable.ObjectImpl)var1)._get_delegate();
               Class var19 = Utils.getStubFromClass(var2, var18.getIOR().getCodebase());
               org.omg.CORBA.portable.ObjectImpl var20 = (org.omg.CORBA.portable.ObjectImpl)var19.newInstance();
               var20._set_delegate((Delegate)var18);
               var3 = var20;
            } else if (var2.isInterface() && (Remote.class.isAssignableFrom(var2) || org.omg.CORBA.Object.class.isAssignableFrom(var2)) && var4 != null) {
               RemoteReference var5 = var4.getRemoteRef();
               RemoteReference var6 = var5;
               boolean var7 = false;
               if (var5 instanceof ClusterableRemoteRef) {
                  ClusterableRemoteRef var8 = (ClusterableRemoteRef)var5;
                  var6 = var8.getCurrentReplica();
                  if (var8.isInitialized() && var8.getReplicaList() instanceof VendorInfoCluster) {
                     ClusterComponent var9 = ((VendorInfoCluster)var8.getReplicaList()).getClusterInfo();
                     var7 = var9.getIdempotent();
                  }
               }

               if (var6 instanceof IIOPRemoteRef) {
                  IIOPRemoteRef var21 = (IIOPRemoteRef)var6;
                  String var22 = var21.getCodebase();
                  String[] var10 = var4.getInterfaceNames();
                  String[] var11 = new String[var10.length + 1];
                  var11[0] = var2.getName();
                  boolean var12 = var21.getIOR().getProfile().isTransactional();

                  try {
                     if (!var12 && var21.getIOR().getProfile().getComponent(32) == null) {
                        Class var13 = Utils.loadClass(var2.getName(), var22, (ClassLoader)null);
                        if (TransactionalObject.class.isAssignableFrom(var13) || IIOPService.txMechanism == 3) {
                           var12 = true;
                        }
                     }
                  } catch (ClassNotFoundException var16) {
                  }

                  for(int var23 = 1; var23 < var11.length; ++var23) {
                     var11[var23] = var10[var23 - 1];
                  }

                  HashMap var24 = null;
                  RemoteInfo var14 = RemoteInfo.findRemoteInfo(var2);
                  if (var14 != null && var14.getDescriptor() != null) {
                     var24 = var14.getDescriptor().getClientMethodDescriptors();
                  }

                  StubInfo var15 = new StubInfo(var5, new ClientRuntimeDescriptor(var11, var4.getApplicationName(), var24, new ClientMethodDescriptor("*", var12, false, false, var7, 0), var11[0] + "_IIOP_WLStub", var22), var11[0] + "_IIOP_WLStub", Stub.class.getName());
                  var3 = StubFactory.getStub(var15);
               } else if (var5 instanceof RemoteReference) {
                  var3 = var1;
               }
            }
         } catch (Exception var17) {
         }

         if (var3 == null) {
            throw new ClassCastException("Cannot narrow remote object " + var1 + " to " + var2.getName());
         } else {
            return var3;
         }
      } else {
         throw new NullPointerException("narrowFrom'" + var1 + '\'' + " \tnarrowTo:" + '\'' + var2 + "'It is invalid to call narrow with null parameters");
      }
   }

   public Object replaceObject(Object var1) throws IOException {
      Object var2 = IIOPReplacer.getIIOPReplacer().replaceObject(var1);
      return var2 instanceof IOR ? IIOPReplacer.makeInvocationHandler((IOR)var2) : var2;
   }

   public void exportObject(Remote var1) throws RemoteException {
      ServerHelper.exportObject(var1);
   }

   public void unexportObject(Remote var1) throws NoSuchObjectException {
      ServerHelper.unexportObject(var1, true, false);
   }

   public Remote toStub(Remote var1) throws NoSuchObjectException {
      try {
         if (var1 instanceof PortableRemoteObject) {
            Object var2 = RemoteObjectReplacer.getReplacer().replaceObject(var1);
            if (var2 instanceof StubReference) {
               StubReference var3 = (StubReference)var2;
               StubInfo var4 = new StubInfo(var3.getRemoteRef(), var3.getDescriptor(), var3.getStubName() + "IIOPLocal", "weblogic.corba.rmi.Stub");
               return (Remote)StubFactory.getStub(var4);
            } else {
               return (Remote)var2;
            }
         } else {
            return ServerHelper.exportObjectWithPeerReplacement(var1);
         }
      } catch (IOException var5) {
         throw new NoSuchObjectException("Can not export: " + var5);
      }
   }

   static void p(String var0) {
      System.err.println("<ReferenceHelperImpl> " + var0);
   }
}
