package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import java.io.Serializable;
import weblogic.wsee.jaxws.persistence.PersistentRequestContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;

public class SourceOfferSequence extends SourceSequence implements OfferSequence<DestinationSequence>, Serializable {
   private static final long serialVersionUID = 1L;
   private String _mainSequenceId;
   private DestinationSequence _mainSequence;
   private boolean _handshaked;
   private boolean _mainSequenceNonBuffered;
   private DeliveryAssurance _mainSequenceDeliveryAssurance;

   public SourceOfferSequence(String var1, String var2, WsrmConstants.RMVersion var3, AddressingVersion var4, SOAPVersion var5, WsrmSecurityContext var6, DestinationSequence var7, boolean var8) {
      super(var1, var2, var3, var4, var5, var6, var8, new PersistentRequestContext());
      this._mainSequenceId = var7 != null ? var7.getId() : null;
      if (var7 != null) {
         var7.setOffer(this);
         this._mainSequenceNonBuffered = var7.isNonBuffered();
         this._mainSequenceDeliveryAssurance = var7.getDeliveryAssurance();
      }

   }

   public String getMainSequenceId() {
      return this._mainSequenceId;
   }

   public void setMainSequenceId(String var1) {
      this._mainSequenceId = var1;
   }

   public DestinationSequence getMainSequence() {
      if (this._mainSequence != null && this._mainSequenceId == null) {
         return this._mainSequence;
      } else {
         this._mainSequence = null;
         return this._mainSequenceId == null ? null : (DestinationSequence)DestinationSequenceManager.getInstance().getSequence(this.getRmVersion(), this._mainSequenceId);
      }
   }

   public String getDestinationId() {
      return this._handshaked ? super.getDestinationId() : this.getId();
   }

   public void takePropertiesFromMainSequence() {
      DestinationSequence var1 = this.getMainSequence();
      this.takeEprsFromSequence(var1);
   }

   public void takeEprsFromSequence(DestinationSequence var1) {
      if (var1 != null) {
         this.setAcksToEpr(var1.getHostEpr());
      }

   }

   public boolean isHandshaked() {
      return this._handshaked;
   }

   public void setHandshaked(boolean var1) {
      this._handshaked = var1;
   }

   public boolean isMainSequenceNonBuffered() {
      return this._mainSequenceNonBuffered;
   }

   public DeliveryAssurance getMainSequenceDeliveryAssurance() {
      return this._mainSequenceDeliveryAssurance != null ? this._mainSequenceDeliveryAssurance : this.getDeliveryAssurance();
   }
}
