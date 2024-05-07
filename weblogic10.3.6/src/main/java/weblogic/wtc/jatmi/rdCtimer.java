package weblogic.wtc.jatmi;

import com.bea.core.jatmi.internal.TCTaskHelper;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;
import org.omg.CORBA.NO_RESPONSE;
import weblogic.iiop.ConnectionManager;
import weblogic.iiop.SequencedRequestMessage;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.tgiop.TGIOPConnection;
import weblogic.tgiop.TGIOPEndPointImpl;
import weblogic.wtc.gwt.MethodParameters;
import weblogic.wtc.gwt.ServiceParameters;

class rdCtimer extends TimerTask {
   private dsession myDSession;
   private int reqId;
   private int giopReqId;

   public rdCtimer(dsession var1, int var2, int var3) {
      this.myDSession = var1;
      this.reqId = var2;
      this.giopReqId = var3;
   }

   public void run() {
      TdomTcb var1 = new TdomTcb(3, this.reqId, 4194304, (String)null);
      var1.set_diagnostic(13);
      tfmh var2 = new tfmh(1);
      var2.tdom = new tcm((short)7, var1);
      Object[] var3 = null;
      HashMap var4 = this.myDSession.getRMICallList();
      if (var4 != null) {
         synchronized(var4) {
            if ((var3 = (Object[])((Object[])var4.remove(new Integer(this.reqId)))) != null) {
               RMIReplyRequest var6 = new RMIReplyRequest(var2, var3, this.myDSession);
               TCTaskHelper.schedule(var6);
            } else {
               MethodParameters var14 = new MethodParameters((ServiceParameters)null, (Objrecv)null, (Object[])null, this.myDSession);
               TGIOPConnection var7 = null;

               try {
                  var7 = new TGIOPConnection(var14);
               } catch (IOException var12) {
                  return;
               }

               TGIOPEndPointImpl var8 = new TGIOPEndPointImpl(var7, ConnectionManager.getConnectionManager(), (AuthenticatedSubject)null);
               SequencedRequestMessage var9 = var8.removePendingResponse(this.giopReqId);
               if (var9 != null) {
                  NO_RESPONSE var10 = new NO_RESPONSE();
                  var9.notify((Throwable)var10);
               }
            }
         }
      }

   }
}
