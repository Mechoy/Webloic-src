package weblogic.wsee.reliability2.tube;

import com.sun.istack.Nullable;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.api.WsrmClient;
import weblogic.wsee.reliability2.api.WsrmClientFactory;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.tube.processors.TerminateSequenceProcessor;

public class WsrmClientRuntime {
   private static final Logger LOGGER = Logger.getLogger(WsrmClientRuntime.class.getName());
   @Nullable
   private WeakReference<ClientInstance> _clientInstanceRef;
   private String _seqId;
   private ClientInstanceListener _clientInstanceListener;

   public WsrmClientRuntime(@Nullable WeakReference<ClientInstance> var1) {
      this._clientInstanceRef = var1;
   }

   public String getSequenceId() {
      return this._seqId;
   }

   public ClientInstanceIdentity getClientInstanceId() {
      return this._clientInstanceRef != null ? ((ClientInstance)this._clientInstanceRef.get()).getId() : null;
   }

   public void internalSetSequenceId(String var1) {
      if (var1 != null && this._seqId == null || var1 == null && this._seqId != null || var1 != null && !var1.equals(this._seqId)) {
         this._seqId = var1;
         this.internalSyncClientInstanceAndSequenceId();
      }

   }

   private void internalSyncClientInstanceAndSequenceId() {
      ClientInstance var1 = this._clientInstanceRef != null ? (ClientInstance)this._clientInstanceRef.get() : null;
      if (this._clientInstanceListener != null && var1 != null) {
         var1.removeClientInstanceListener(this._clientInstanceListener);
         this._clientInstanceListener = null;
      }

      if (var1 != null) {
         if (this._seqId != null) {
            this._clientInstanceListener = new ClientInstanceListener(this._seqId);
            var1.addClientInstanceListener(this._clientInstanceListener);
         }

         Map var2 = ((BindingProvider)var1.getInstance()).getRequestContext();
         WsrmInvocationPropertyBag var3 = WsrmInvocationPropertyBag.getFromMap(var2);
         var3.internalSetSequenceId(this._seqId);
      }

   }

   private class ClientInstanceListener implements ClientInstance.Listener {
      private String _seqId;

      private ClientInstanceListener(String var2) {
         this._seqId = var2;
      }

      public void clientInstanceClosing(ClientInstance var1) {
         try {
            SourceSequence var2 = (SourceSequence)SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.latest(), this._seqId);
            if (var2 == null || var2.getState() == SequenceState.TERMINATING || var2.getState() == SequenceState.TERMINATED) {
               if (WsrmClientRuntime.LOGGER.isLoggable(Level.FINE)) {
                  WsrmClientRuntime.LOGGER.fine("Client instance " + var1 + " has been closed, but the RM sequence on it does not require termination, no action required");
               }

               return;
            }

            if (WsrmClientRuntime.LOGGER.isLoggable(Level.FINE)) {
               WsrmClientRuntime.LOGGER.fine("Setting final message number on source sequence " + this._seqId + " because it has been closed by the client");
            }

            TerminateSequenceProcessor.setSentFinalMessage(var2, var2.getMaxMessageNum());
         } catch (Exception var3) {
            if (WsrmClientRuntime.LOGGER.isLoggable(Level.WARNING)) {
               WsrmClientRuntime.LOGGER.log(Level.WARNING, var3.toString(), var3);
            }

            WseeRmLogger.logUnexpectedException(var3.toString(), var3);
         }

      }

      public void clientInstanceRecycled(ClientInstance var1) {
         WsrmClient var2 = WsrmClientFactory.getWsrmClientFromPort(var1.getInstance());
         var2.reset();
      }

      // $FF: synthetic method
      ClientInstanceListener(String var2, Object var3) {
         this(var2);
      }
   }
}
