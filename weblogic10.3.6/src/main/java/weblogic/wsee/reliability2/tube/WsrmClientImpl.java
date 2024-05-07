package weblogic.wsee.reliability2.tube;

import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.SortedSet;
import javax.xml.ws.BindingProvider;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api.WsrmClient;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.policy.WsrmPolicyHelper;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownSourceSequenceException;
import weblogic.wsee.reliability2.tube.processors.TerminateSequenceProcessor;

public class WsrmClientImpl implements WsrmClient {
   public static final String CLIENT_CURRENT_SEQUENCE_ID_PROP_NAME = "weblogic.wsee.reliability2.client.CurrentSequenceID";
   private BindingProvider _port;
   private WsrmTubeUtils _tubeUtil;

   public WsrmClientImpl(BindingProvider var1, WsrmClientRuntimeFeature var2) throws PolicyException {
      ClientTubeAssemblerContext var3 = var2.getContext();
      WSBinding var4 = var3.getBinding();
      this._port = var1;
      WsrmPolicyHelper var5 = new WsrmPolicyHelper(var2.getPort());
      this._tubeUtil = new WsrmTubeUtils(true, var4, var3.getWsdlModel(), var5, new WsrmClientDispatchFactory(this.getClientId()), var3, (ServerTubeAssemblerContext)null);
   }

   public void dispose() {
      try {
         SequenceState var1 = this.getSequenceState();
         if (var1 != null && var1 == SequenceState.CREATED) {
            this.terminateSequence();
         }
      } catch (Exception var2) {
         WseeRmLogger.logUnexpectedException(var2.toString(), var2);
      }

   }

   private <T> T getProperty(String var1, Class<T> var2) {
      WsrmInvocationPropertyBag var3 = this.getRmInvokeProps();
      Object var4;
      if (var3.containsProp(var1)) {
         var4 = var3.getProp(var1);
         return var4 != null && var2.isAssignableFrom(var4.getClass()) ? var4 : null;
      } else {
         var3 = this.getRmInvokePropsFromResponseContext();
         if (var3 != null) {
            var4 = var3.getProp(var1);
            return var4 != null && var2.isAssignableFrom(var4.getClass()) ? var4 : null;
         } else {
            return null;
         }
      }
   }

   private WsrmInvocationPropertyBag getRmInvokeProps() {
      WsrmInvocationPropertyBag var1 = this.getRmInvokePropsFromRequestContext();
      if (var1 == null) {
         var1 = this.getRmInvokePropsFromResponseContext();
      }

      return var1;
   }

   public void reset() {
      this.setSequenceId((String)null);
      WsrmInvocationPropertyBag var1 = this.getRmInvokeProps();
      var1.setFinalMsgFlag(false);
      var1.setMostRecentMsgNum(0L);
      WeakReference var2 = (WeakReference)this._port.getRequestContext().get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
      ClientInstance var3 = var2 != null ? (ClientInstance)var2.get() : null;
      if (var3 != null) {
         var3.getProps().remove("weblogic.wsee.reliability2.client.CurrentSequenceID");
      }

   }

   public String getId() {
      WeakReference var1 = (WeakReference)this._port.getRequestContext().get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
      return ((ClientInstance)var1.get()).getId().toString();
   }

   private String getClientId() {
      WeakReference var1 = (WeakReference)this._port.getRequestContext().get("weblogic.wsee.jaxws.spi.ClientInstanceWeakRef");
      return ((ClientInstance)var1.get()).getId().getClientId();
   }

   public String getSequenceId() {
      return this.getRmInvokePropsFromRequestContext().getSequenceId();
   }

   public SequenceState getSequenceState() {
      String var1 = this.getSequenceId();
      if (var1 == null) {
         return null;
      } else {
         SourceSequence var2 = null;

         try {
            var2 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var1, true);
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         return var2 != null ? var2.getState() : null;
      }
   }

   public void setSequenceId(String var1) {
      WsrmInvocationPropertyBag var2 = this.getRmInvokePropsFromRequestContext();
      var2.setSequenceId(var1);
   }

   public void requestAcknowledgement() throws WsrmException {
      SourceSequence var1 = this.getSequence();
      WsrmTubeUtils.sendAcknowledgementRequest(var1, this._tubeUtil.getDispatchFactory());
   }

   public SortedSet<MessageRange> getAckRanges() throws UnknownSourceSequenceException {
      SourceSequence var1 = this.getSequence();
      SortedSet var2 = var1.getAckRanges();
      return var2;
   }

   public long getMostRecentMessageNumber() {
      Long var1 = (Long)this.getProperty("weblogic.wsee.reliability2.MostRecentMsgNum", Long.class);
      return var1 == null ? -1L : var1;
   }

   public SourceMessageInfo getMessageInfo(long var1) throws UnknownSourceSequenceException {
      SourceSequence var3 = this.getSequence();
      SourceMessageInfo var4 = (SourceMessageInfo)var3.getRequest(var1);
      return var4;
   }

   public void setFinalMessage() {
      WsrmInvocationPropertyBag var1 = this.getRmInvokeProps();
      var1.setFinalMsgFlag(true);
   }

   public void closeSequence() throws WsrmException {
      SourceSequence var1 = this.getSequence();
      WsrmTubeUtils.sendCloseSequence(var1, this._tubeUtil.getDispatchFactory());
   }

   public void sendWsrm10EmptyLastMessage() throws WsrmException {
      SourceSequence var1 = this.getSequence();
      if (var1.getRmVersion() == WsrmConstants.RMVersion.RM_10) {
         TerminateSequenceProcessor.sendEmptyLastMessage(var1);
      }
   }

   public void terminateSequence() throws WsrmException {
      SourceSequence var1 = this.getSequence();
      WsrmTubeUtils.sendTerminateSequence(var1, this._tubeUtil.getDispatchFactory());
   }

   private SourceSequence getSequence() throws UnknownSourceSequenceException {
      String var1 = this.getSequenceId();
      SourceSequence var2 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), var1, false);
      return var2;
   }

   private WsrmInvocationPropertyBag getRmInvokePropsFromRequestContext() {
      Map var1 = this._port.getRequestContext();
      WsrmInvocationPropertyBag var2 = (WsrmInvocationPropertyBag)var1.get(WsrmInvocationPropertyBag.key);
      if (var2 == null) {
         var2 = new WsrmInvocationPropertyBag(var1);
         var1.put(WsrmInvocationPropertyBag.key, var2);
      }

      return var2;
   }

   private WsrmInvocationPropertyBag getRmInvokePropsFromResponseContext() {
      Map var1 = this._port.getResponseContext();
      if (var1 == null) {
         return null;
      } else {
         WsrmInvocationPropertyBag var2 = (WsrmInvocationPropertyBag)var1.get(WsrmInvocationPropertyBag.key);
         if (var2 == null) {
            var2 = new WsrmInvocationPropertyBag(var1);
            var1.put(WsrmInvocationPropertyBag.key, var2);
         }

         return var2;
      }
   }
}
