package weblogic.wsee.reliability2.api;

import weblogic.jws.jaxws.WLSWebServiceFeature;

public class WsrmClientInitFeature extends WLSWebServiceFeature {
   private static String ID = "WS-RM Client Initialization Feature";
   private boolean _customized;
   private boolean _forceWsrm10;
   private ReliabilityErrorListener _errorListener;
   private boolean _nonBufferedSource;
   private String _baseRetransmissionInterval;
   private boolean _retransmissionExponentialBackoff;
   private String _inactivityTimeout;
   private String _sequenceExpiration;
   private String _acknowledgementInterval;
   private boolean _nonBufferedDestination;
   private boolean _turnOffOffer;

   public WsrmClientInitFeature() {
      this(true);
   }

   public WsrmClientInitFeature(boolean var1) {
      super.enabled = var1;
      this.setTubelineImpact(true);
   }

   protected WsrmClientInitFeature(WsrmClientInitFeature var1) {
      this.enabled = var1.enabled;
      this._customized = var1._customized;
      this._forceWsrm10 = var1._forceWsrm10;
      this._errorListener = var1._errorListener;
      this._nonBufferedSource = var1._nonBufferedSource;
      this._baseRetransmissionInterval = var1._baseRetransmissionInterval;
      this._retransmissionExponentialBackoff = var1._retransmissionExponentialBackoff;
      this._inactivityTimeout = var1._inactivityTimeout;
      this._sequenceExpiration = var1._sequenceExpiration;
      this._nonBufferedDestination = var1._nonBufferedDestination;
      this._turnOffOffer = var1._turnOffOffer;
      this.setTubelineImpact(var1.isTubelineImpact());
   }

   public boolean isCustomized() {
      return this._customized;
   }

   public void clearCustomized() {
      this._customized = false;
   }

   public String getID() {
      return ID;
   }

   public String getBaseRetransmissionInterval() {
      return this._baseRetransmissionInterval;
   }

   public void setBaseRetransmissionInterval(String var1) {
      this._customized = true;
      this._baseRetransmissionInterval = var1;
   }

   public boolean isRetransmissionExponentialBackoff() {
      return this._retransmissionExponentialBackoff;
   }

   public void setRetransmissionExponentialBackoff(boolean var1) {
      this._customized = true;
      this._retransmissionExponentialBackoff = var1;
   }

   public boolean isNonBufferedSource() {
      return this._nonBufferedSource;
   }

   public void setNonBufferedSource(boolean var1) throws IllegalArgumentException {
      this._customized = true;
      this._nonBufferedSource = var1;
   }

   public String getInactivityTimeout() {
      return this._inactivityTimeout;
   }

   public void setInactivityTimeout(String var1) {
      this._customized = true;
      this._inactivityTimeout = var1;
   }

   public String getSequenceExpiration() {
      return this._sequenceExpiration;
   }

   public void setSequenceExpiration(String var1) {
      this._customized = true;
      this._sequenceExpiration = var1;
   }

   public boolean isNonBufferedDestination() {
      return this._nonBufferedDestination;
   }

   public String getAcknowledgementInterval() {
      return this._acknowledgementInterval;
   }

   public void setAcknowledgementInterval(String var1) {
      this._acknowledgementInterval = var1;
   }

   public void setNonBufferedDestination(boolean var1) throws IllegalArgumentException {
      this._customized = true;
      this._nonBufferedDestination = var1;
   }

   public boolean isForceWsrm10() {
      return this._forceWsrm10;
   }

   public void setForceWsrm10(boolean var1) {
      this._customized = true;
      this._forceWsrm10 = var1;
   }

   public ReliabilityErrorListener getErrorListener() {
      return this._errorListener;
   }

   public void setErrorListener(ReliabilityErrorListener var1) {
      this._errorListener = var1;
   }

   public boolean isTurnOffOffer() {
      return this._turnOffOffer;
   }

   public void setTurnOffOffer(boolean var1) {
      this._customized = true;
      this._turnOffOffer = var1;
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("WsrmClientInitFeature customized=").append(this._customized);
      var1.append(" forceWsrm10=").append(this._forceWsrm10);
      var1.append(" errorListener=").append(this._errorListener);
      var1.append(" nonBuffered=").append(this._nonBufferedSource);
      var1.append(" baseRetransmissionInterval=").append(this._baseRetransmissionInterval);
      var1.append(" retransmissionExponentialBackoff=").append(this._retransmissionExponentialBackoff);
      var1.append(" inactivityTimeout=").append(this._inactivityTimeout);
      var1.append(" sequenceExpiration=").append(this._sequenceExpiration);
      var1.append(" turnOffOffer=").append(this._turnOffOffer);
      return var1.toString();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof WsrmClientInitFeature)) {
         return false;
      } else {
         WsrmClientInitFeature var2 = (WsrmClientInitFeature)var1;
         return var2._customized == this._customized && var2._forceWsrm10 == this._forceWsrm10 && var2._nonBufferedSource == this._nonBufferedSource && this.objectsEqual(var2._baseRetransmissionInterval, this._baseRetransmissionInterval) && var2._retransmissionExponentialBackoff == this._retransmissionExponentialBackoff && this.objectsEqual(var2._inactivityTimeout, this._inactivityTimeout) && this.objectsEqual(var2._sequenceExpiration, this._sequenceExpiration) && var2._turnOffOffer == this._turnOffOffer;
      }
   }

   private boolean objectsEqual(Object var1, Object var2) {
      return var1 == null && var2 == null || var1 != null && var1.equals(var2);
   }
}
