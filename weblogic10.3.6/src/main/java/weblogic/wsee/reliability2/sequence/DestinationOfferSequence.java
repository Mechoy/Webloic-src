package weblogic.wsee.reliability2.sequence;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import java.io.Serializable;
import weblogic.wsee.jaxws.persistence.PersistentRequestContext;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;

public class DestinationOfferSequence extends DestinationSequence implements OfferSequence<SourceSequence>, Serializable {
   private static final long serialVersionUID = 1L;
   private String _mainSequenceId;
   private transient SourceSequence _mainSequence;
   private PersistentRequestContext _firstRequestContext;

   public DestinationOfferSequence(String var1, String var2, WsrmConstants.RMVersion var3, AddressingVersion var4, SOAPVersion var5, WsrmSecurityContext var6, boolean var7) {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public void setMainSequence(SourceSequence var1) {
      this._mainSequenceId = var1 != null ? var1.getId() : null;
      if (var1 != null) {
         var1.setOffer(this);
      }

   }

   public String getMainSequenceId() {
      return this._mainSequenceId;
   }

   public SourceSequence getMainSequence() {
      if (this._mainSequence != null && this._mainSequenceId == null) {
      }

      this._mainSequence = null;
      return this._mainSequenceId == null ? null : (SourceSequence)SourceSequenceManager.getInstance().getSequence(this.getRmVersion(), this._mainSequenceId);
   }

   public void takeEprsFromMainSequence() {
      SourceSequence var1 = this.getMainSequence();
      this.takeEprsFromSequence(var1);
   }

   public void takeEprsFromSequence(SourceSequence var1) {
      if (var1 != null) {
         this.setAcksToEpr(var1.getEndpointEpr());
         this.setHostEpr(var1.getAcksToEpr());
      }

   }

   public void takeFirstRequestContextFromMainSequence() {
      SourceSequence var1 = this.getMainSequence();
      this._firstRequestContext = var1.getFirstRequestContext();
   }

   @NotNull
   public PersistentRequestContext getMainSequenceFirstRequestContext() {
      return this._firstRequestContext;
   }

   public void takeSecurityContextFromMainSequence() {
      SourceSequence var1 = this.getMainSequence();
      if (var1 != null && var1.getSecurityContext() != null) {
         this.setSecurityContext(var1.getSecurityContext());
      }

   }
}
