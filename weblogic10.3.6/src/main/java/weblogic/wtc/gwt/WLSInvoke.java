package weblogic.wtc.gwt;

import com.bea.core.jatmi.common.ntrace;
import com.bea.core.jatmi.internal.TCTaskHelper;
import weblogic.kernel.Kernel;
import weblogic.wtc.jatmi.InvokeInfo;
import weblogic.wtc.jatmi.InvokeSvc;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TuxedoService;
import weblogic.wtc.jatmi.gwatmi;

public final class WLSInvoke implements InvokeSvc {
   private TDMLocal myLocalDomain;
   private TDMRemote myRemoteDomain;

   public WLSInvoke(TDMLocal var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WLSInvoke/" + var1);
      }

      this.myLocalDomain = var1;
      if (var2) {
         ntrace.doTrace("]/WLSInvoke/10");
      }

   }

   public WLSInvoke(TDMLocal var1, TDMRemote var2) {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WLSInvoke/" + var1 + "/" + var2);
      }

      this.myLocalDomain = var1;
      this.myRemoteDomain = var2;
      if (var3) {
         ntrace.doTrace("]/WLSInvoke/10");
      }

   }

   public void setRemoteDomain(TDMRemote var1) {
      boolean var2 = ntrace.isTraceEnabled(2);
      if (var2) {
         ntrace.doTrace("[/WLSInvoke/setRemoteDomain/" + var1);
      }

      this.myRemoteDomain = var1;
      if (var2) {
         ntrace.doTrace("]/WLSInvoke/setRemoteDomain/10");
      }

   }

   public TDMRemote getRemoteDomain() {
      boolean var1 = ntrace.isTraceEnabled(2);
      if (var1) {
         ntrace.doTrace("[/WLSInvoke/getRemoteDomain/");
      }

      TDMRemote var2 = this.myRemoteDomain;
      if (var1) {
         ntrace.doTrace("]/WLSInvoke/getRemoteDomain/10/" + var2);
      }

      return var2;
   }

   public void invoke(InvokeInfo var1, gwatmi var2) throws TPException {
      boolean var3 = ntrace.isTraceEnabled(2);
      if (var3) {
         ntrace.doTrace("[/WLSInvoke/invoke/");
      }

      if (var1 == null) {
         if (var3) {
            ntrace.doTrace("*]/WLSInvoke/invoke/20/");
         }

         throw new TPException(4);
      } else {
         if (WTCService.applicationQueueId == -1) {
            InboundEJBRequest var4 = new InboundEJBRequest(new ServiceParameters(var1, var2), this.myLocalDomain, this.myRemoteDomain);
            TCTaskHelper.schedule(var4);
         } else {
            InboundEJBRequestKEQ var5 = new InboundEJBRequestKEQ(new ServiceParameters(var1, var2), this.myLocalDomain, this.myRemoteDomain);
            Kernel.execute(var5, WTCService.applicationQueueId);
         }

         if (var3) {
            ntrace.doTrace("]/WLSInvoke/invoke/30/");
         }

      }
   }

   public void advertise(String var1, TuxedoService var2) throws TPException {
      throw new TPException(7);
   }

   public void unadvertise(String var1) throws TPException {
      throw new TPException(7);
   }

   public void shutdown() {
   }
}
