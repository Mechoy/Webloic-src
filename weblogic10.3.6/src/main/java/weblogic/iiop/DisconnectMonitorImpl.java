package weblogic.iiop;

import java.io.IOException;
import java.rmi.Remote;
import org.omg.CORBA.portable.ObjectImpl;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.rmi.cluster.ClusterableRemoteRef;
import weblogic.rmi.extensions.DisconnectListener;
import weblogic.rmi.extensions.server.DisconnectMonitorProvider;
import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.StubInfoIntf;

public class DisconnectMonitorImpl implements DisconnectMonitorProvider {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");

   public boolean addDisconnectListener(Remote var1, DisconnectListener var2) {
      IIOPRemoteRef var3 = this.getRefFromStub(var1);
      if (var3 != null) {
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("registering: " + var1 + ", hashcode: " + var1.hashCode());
         }

         try {
            try {
               if (((ObjectImpl)var1)._non_existent()) {
                  throw new IllegalArgumentException("The remote peer is alive but does not have the object: " + var1);
               }
            } catch (Throwable var6) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  var6.printStackTrace();
                  EndPoint var5 = EndPointManager.findEndPoint(var3.getIOR());
                  if (var5 != null) {
                     p("There is still an EndPoint: " + var5 + ", it should be null!");
                  }
               }
            }

            EndPoint var4 = EndPointManager.findOrCreateEndPoint(var3.getIOR());
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("Registering on EndPoint: " + var4);
            }

            var4.addDisconnectListener(var1, var2);
            return true;
         } catch (IOException var7) {
         }
      }

      return false;
   }

   public boolean removeDisconnectListener(Remote var1, DisconnectListener var2) {
      IIOPRemoteRef var3 = this.getRefFromStub(var1);
      if (var3 != null) {
         EndPoint var4 = EndPointManager.findEndPoint(var3.getIOR());
         if (var4 != null) {
            var4.removeDisconnectListener(var1, var2);
         }

         return true;
      } else {
         return false;
      }
   }

   private IIOPRemoteRef getRefFromStub(Object var1) {
      RemoteReference var2 = null;
      if (var1 instanceof StubInfoIntf) {
         var2 = ((StubInfoIntf)var1).getStubInfo().getRemoteRef();
      } else if (var1 instanceof StubReference) {
         var2 = ((StubReference)var1).getRemoteRef();
      }

      if (var2 instanceof ClusterableRemoteRef) {
         ClusterableRemoteRef var3 = (ClusterableRemoteRef)var2;
         var2 = var3.getCurrentReplica();
      }

      return var2 instanceof IIOPRemoteRef ? (IIOPRemoteRef)var2 : null;
   }

   private static void p(String var0) {
      System.out.println("<HeartbeatMonitorDelegate>: " + var0);
   }
}
