package weblogic.wsee.config;

import weblogic.management.configuration.WebServiceReliabilityMBean;

public class WebServiceReliabilityMBeanImpl extends DummyConfigurationMBeanImpl implements WebServiceReliabilityMBean {
   private String _baseRetransmissionInterval = "P0DT8S";
   private boolean _retransmissionExponentialBackoff = false;
   private boolean _nonBufferedSource = false;
   private String _acknowledgementInterval = "P0DT0.2S";
   private String _inactivityTimeout = "P0DT600S";
   private String _sequenceExpiration = "P1D";
   private boolean _nonBufferedDestination = false;

   public String getBaseRetransmissionInterval() {
      return this._baseRetransmissionInterval;
   }

   public void setBaseRetransmissionInterval(String var1) {
      this._baseRetransmissionInterval = var1;
   }

   public Boolean isRetransmissionExponentialBackoff() {
      return this._retransmissionExponentialBackoff;
   }

   public void setRetransmissionExponentialBackoff(Boolean var1) {
      this._retransmissionExponentialBackoff = var1;
   }

   public Boolean isNonBufferedSource() {
      return this._nonBufferedSource;
   }

   public void setNonBufferedSource(Boolean var1) {
      this._nonBufferedSource = var1;
   }

   public String getAcknowledgementInterval() {
      return this._acknowledgementInterval;
   }

   public void setAcknowledgementInterval(String var1) {
      this._acknowledgementInterval = var1;
   }

   public String getInactivityTimeout() {
      return this._inactivityTimeout;
   }

   public void setInactivityTimeout(String var1) {
      this._inactivityTimeout = var1;
   }

   public String getSequenceExpiration() {
      return this._sequenceExpiration;
   }

   public void setSequenceExpiration(String var1) {
      this._sequenceExpiration = var1;
   }

   public Boolean isNonBufferedDestination() {
      return this._nonBufferedDestination;
   }

   public void setNonBufferedDestination(Boolean var1) {
      this._nonBufferedDestination = var1;
   }
}
