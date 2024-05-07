package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.j2ee.descriptor.wl.validators.WseeConfigBeanValidator;
import weblogic.utils.collections.CombinedIterator;

public class WebServiceReliabilityMBeanImpl extends ConfigurationMBeanImpl implements WebServiceReliabilityMBean, Serializable {
   private String _AcknowledgementInterval;
   private String _BaseRetransmissionInterval;
   private String _InactivityTimeout;
   private Boolean _NonBufferedDestination;
   private Boolean _NonBufferedSource;
   private Boolean _RetransmissionExponentialBackoff;
   private String _SequenceExpiration;
   private static SchemaHelper2 _schemaHelper;

   public WebServiceReliabilityMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WebServiceReliabilityMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public String getBaseRetransmissionInterval() {
      return this._BaseRetransmissionInterval;
   }

   public boolean isBaseRetransmissionIntervalSet() {
      return this._isSet(7);
   }

   public void setBaseRetransmissionInterval(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateBaseRetransmissionInterval(var1);
      String var2 = this._BaseRetransmissionInterval;
      this._BaseRetransmissionInterval = var1;
      this._postSet(7, var2, var1);
   }

   public Boolean isRetransmissionExponentialBackoff() {
      return this._RetransmissionExponentialBackoff;
   }

   public boolean isRetransmissionExponentialBackoffSet() {
      return this._isSet(8);
   }

   public void setRetransmissionExponentialBackoff(Boolean var1) {
      Boolean var2 = this._RetransmissionExponentialBackoff;
      this._RetransmissionExponentialBackoff = var1;
      this._postSet(8, var2, var1);
   }

   public Boolean isNonBufferedSource() {
      return this._NonBufferedSource;
   }

   public boolean isNonBufferedSourceSet() {
      return this._isSet(9);
   }

   public void setNonBufferedSource(Boolean var1) {
      Boolean var2 = this._NonBufferedSource;
      this._NonBufferedSource = var1;
      this._postSet(9, var2, var1);
   }

   public String getAcknowledgementInterval() {
      return this._AcknowledgementInterval;
   }

   public boolean isAcknowledgementIntervalSet() {
      return this._isSet(10);
   }

   public void setAcknowledgementInterval(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateAcknowledgementInterval(var1);
      String var2 = this._AcknowledgementInterval;
      this._AcknowledgementInterval = var1;
      this._postSet(10, var2, var1);
   }

   public String getInactivityTimeout() {
      return this._InactivityTimeout;
   }

   public boolean isInactivityTimeoutSet() {
      return this._isSet(11);
   }

   public void setInactivityTimeout(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateInactivityTimeout(var1);
      String var2 = this._InactivityTimeout;
      this._InactivityTimeout = var1;
      this._postSet(11, var2, var1);
   }

   public String getSequenceExpiration() {
      return this._SequenceExpiration;
   }

   public boolean isSequenceExpirationSet() {
      return this._isSet(12);
   }

   public void setSequenceExpiration(String var1) {
      var1 = var1 == null ? null : var1.trim();
      WseeConfigBeanValidator.validateSequenceExpiration(var1);
      String var2 = this._SequenceExpiration;
      this._SequenceExpiration = var1;
      this._postSet(12, var2, var1);
   }

   public Boolean isNonBufferedDestination() {
      return this._NonBufferedDestination;
   }

   public boolean isNonBufferedDestinationSet() {
      return this._isSet(13);
   }

   public void setNonBufferedDestination(Boolean var1) {
      Boolean var2 = this._NonBufferedDestination;
      this._NonBufferedDestination = var1;
      this._postSet(13, var2, var1);
   }

   public Object _getKey() {
      return super._getKey();
   }

   public void _validate() throws IllegalArgumentException {
      super._validate();
   }

   protected void _unSet(int var1) {
      if (!this._initializeProperty(var1)) {
         super._unSet(var1);
      } else {
         this._markSet(var1, false);
      }

   }

   protected AbstractDescriptorBeanHelper _createHelper() {
      return new Helper(this);
   }

   public boolean _isAnythingSet() {
      return super._isAnythingSet();
   }

   private boolean _initializeProperty(int var1) {
      boolean var2 = var1 > -1;
      if (!var2) {
         var1 = 10;
      }

      try {
         switch (var1) {
            case 10:
               this._AcknowledgementInterval = "P0DT0.2S";
               if (var2) {
                  break;
               }
            case 7:
               this._BaseRetransmissionInterval = "P0DT3S";
               if (var2) {
                  break;
               }
            case 11:
               this._InactivityTimeout = "P0DT600S";
               if (var2) {
                  break;
               }
            case 12:
               this._SequenceExpiration = "P1D";
               if (var2) {
                  break;
               }
            case 13:
               this._NonBufferedDestination = false;
               if (var2) {
                  break;
               }
            case 9:
               this._NonBufferedSource = false;
               if (var2) {
                  break;
               }
            case 8:
               this._RetransmissionExponentialBackoff = false;
               if (var2) {
                  break;
               }
            default:
               if (var2) {
                  return false;
               }
         }

         return true;
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw (Error)(new AssertionError("Impossible Exception")).initCause(var5);
      }
   }

   public Munger.SchemaHelper _getSchemaHelper() {
      return null;
   }

   public String _getElementName(int var1) {
      return this._getSchemaHelper2().getElementName(var1);
   }

   protected String getSchemaLocation() {
      return "http://xmlns.oracle.com/weblogic/1.0/domain.xsd";
   }

   protected String getTargetNamespace() {
      return "http://xmlns.oracle.com/weblogic/domain";
   }

   public SchemaHelper _getSchemaHelper2() {
      if (_schemaHelper == null) {
         _schemaHelper = new SchemaHelper2();
      }

      return _schemaHelper;
   }

   public String getType() {
      return "WebServiceReliability";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("AcknowledgementInterval")) {
         var3 = this._AcknowledgementInterval;
         this._AcknowledgementInterval = (String)var2;
         this._postSet(10, var3, this._AcknowledgementInterval);
      } else if (var1.equals("BaseRetransmissionInterval")) {
         var3 = this._BaseRetransmissionInterval;
         this._BaseRetransmissionInterval = (String)var2;
         this._postSet(7, var3, this._BaseRetransmissionInterval);
      } else if (var1.equals("InactivityTimeout")) {
         var3 = this._InactivityTimeout;
         this._InactivityTimeout = (String)var2;
         this._postSet(11, var3, this._InactivityTimeout);
      } else {
         Boolean var4;
         if (var1.equals("NonBufferedDestination")) {
            var4 = this._NonBufferedDestination;
            this._NonBufferedDestination = (Boolean)var2;
            this._postSet(13, var4, this._NonBufferedDestination);
         } else if (var1.equals("NonBufferedSource")) {
            var4 = this._NonBufferedSource;
            this._NonBufferedSource = (Boolean)var2;
            this._postSet(9, var4, this._NonBufferedSource);
         } else if (var1.equals("RetransmissionExponentialBackoff")) {
            var4 = this._RetransmissionExponentialBackoff;
            this._RetransmissionExponentialBackoff = (Boolean)var2;
            this._postSet(8, var4, this._RetransmissionExponentialBackoff);
         } else if (var1.equals("SequenceExpiration")) {
            var3 = this._SequenceExpiration;
            this._SequenceExpiration = (String)var2;
            this._postSet(12, var3, this._SequenceExpiration);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AcknowledgementInterval")) {
         return this._AcknowledgementInterval;
      } else if (var1.equals("BaseRetransmissionInterval")) {
         return this._BaseRetransmissionInterval;
      } else if (var1.equals("InactivityTimeout")) {
         return this._InactivityTimeout;
      } else if (var1.equals("NonBufferedDestination")) {
         return this._NonBufferedDestination;
      } else if (var1.equals("NonBufferedSource")) {
         return this._NonBufferedSource;
      } else if (var1.equals("RetransmissionExponentialBackoff")) {
         return this._RetransmissionExponentialBackoff;
      } else {
         return var1.equals("SequenceExpiration") ? this._SequenceExpiration : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 18:
               if (var1.equals("inactivity-timeout")) {
                  return 11;
               }
               break;
            case 19:
               if (var1.equals("sequence-expiration")) {
                  return 12;
               }

               if (var1.equals("non-buffered-source")) {
                  return 9;
               }
               break;
            case 24:
               if (var1.equals("acknowledgement-interval")) {
                  return 10;
               }

               if (var1.equals("non-buffered-destination")) {
                  return 13;
               }
               break;
            case 28:
               if (var1.equals("base-retransmission-interval")) {
                  return 7;
               }
               break;
            case 34:
               if (var1.equals("retransmission-exponential-backoff")) {
                  return 8;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 7:
               return "base-retransmission-interval";
            case 8:
               return "retransmission-exponential-backoff";
            case 9:
               return "non-buffered-source";
            case 10:
               return "acknowledgement-interval";
            case 11:
               return "inactivity-timeout";
            case 12:
               return "sequence-expiration";
            case 13:
               return "non-buffered-destination";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isKey(int var1) {
         switch (var1) {
            case 2:
               return true;
            default:
               return super.isKey(var1);
         }
      }

      public String[] getKeyElementNames() {
         ArrayList var1 = new ArrayList();
         var1.add("name");
         return (String[])((String[])var1.toArray(new String[0]));
      }
   }

   protected static class Helper extends ConfigurationMBeanImpl.Helper {
      private WebServiceReliabilityMBeanImpl bean;

      protected Helper(WebServiceReliabilityMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "BaseRetransmissionInterval";
            case 8:
               return "RetransmissionExponentialBackoff";
            case 9:
               return "NonBufferedSource";
            case 10:
               return "AcknowledgementInterval";
            case 11:
               return "InactivityTimeout";
            case 12:
               return "SequenceExpiration";
            case 13:
               return "NonBufferedDestination";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("AcknowledgementInterval")) {
            return 10;
         } else if (var1.equals("BaseRetransmissionInterval")) {
            return 7;
         } else if (var1.equals("InactivityTimeout")) {
            return 11;
         } else if (var1.equals("SequenceExpiration")) {
            return 12;
         } else if (var1.equals("NonBufferedDestination")) {
            return 13;
         } else if (var1.equals("NonBufferedSource")) {
            return 9;
         } else {
            return var1.equals("RetransmissionExponentialBackoff") ? 8 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         return new CombinedIterator(var1);
      }

      protected long computeHashValue(CRC32 var1) {
         try {
            StringBuffer var2 = new StringBuffer();
            long var3 = super.computeHashValue(var1);
            if (var3 != 0L) {
               var2.append(String.valueOf(var3));
            }

            long var5 = 0L;
            if (this.bean.isAcknowledgementIntervalSet()) {
               var2.append("AcknowledgementInterval");
               var2.append(String.valueOf(this.bean.getAcknowledgementInterval()));
            }

            if (this.bean.isBaseRetransmissionIntervalSet()) {
               var2.append("BaseRetransmissionInterval");
               var2.append(String.valueOf(this.bean.getBaseRetransmissionInterval()));
            }

            if (this.bean.isInactivityTimeoutSet()) {
               var2.append("InactivityTimeout");
               var2.append(String.valueOf(this.bean.getInactivityTimeout()));
            }

            if (this.bean.isSequenceExpirationSet()) {
               var2.append("SequenceExpiration");
               var2.append(String.valueOf(this.bean.getSequenceExpiration()));
            }

            if (this.bean.isNonBufferedDestinationSet()) {
               var2.append("NonBufferedDestination");
               var2.append(String.valueOf(this.bean.isNonBufferedDestination()));
            }

            if (this.bean.isNonBufferedSourceSet()) {
               var2.append("NonBufferedSource");
               var2.append(String.valueOf(this.bean.isNonBufferedSource()));
            }

            if (this.bean.isRetransmissionExponentialBackoffSet()) {
               var2.append("RetransmissionExponentialBackoff");
               var2.append(String.valueOf(this.bean.isRetransmissionExponentialBackoff()));
            }

            var1.update(var2.toString().getBytes());
            return var1.getValue();
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void computeDiff(AbstractDescriptorBean var1) {
         try {
            super.computeDiff(var1);
            WebServiceReliabilityMBeanImpl var2 = (WebServiceReliabilityMBeanImpl)var1;
            this.computeDiff("AcknowledgementInterval", this.bean.getAcknowledgementInterval(), var2.getAcknowledgementInterval(), true);
            this.computeDiff("BaseRetransmissionInterval", this.bean.getBaseRetransmissionInterval(), var2.getBaseRetransmissionInterval(), true);
            this.computeDiff("InactivityTimeout", this.bean.getInactivityTimeout(), var2.getInactivityTimeout(), true);
            this.computeDiff("SequenceExpiration", this.bean.getSequenceExpiration(), var2.getSequenceExpiration(), true);
            this.computeDiff("NonBufferedDestination", this.bean.isNonBufferedDestination(), var2.isNonBufferedDestination(), true);
            this.computeDiff("NonBufferedSource", this.bean.isNonBufferedSource(), var2.isNonBufferedSource(), true);
            this.computeDiff("RetransmissionExponentialBackoff", this.bean.isRetransmissionExponentialBackoff(), var2.isRetransmissionExponentialBackoff(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WebServiceReliabilityMBeanImpl var3 = (WebServiceReliabilityMBeanImpl)var1.getSourceBean();
            WebServiceReliabilityMBeanImpl var4 = (WebServiceReliabilityMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("AcknowledgementInterval")) {
                  var3.setAcknowledgementInterval(var4.getAcknowledgementInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("BaseRetransmissionInterval")) {
                  var3.setBaseRetransmissionInterval(var4.getBaseRetransmissionInterval());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("InactivityTimeout")) {
                  var3.setInactivityTimeout(var4.getInactivityTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("SequenceExpiration")) {
                  var3.setSequenceExpiration(var4.getSequenceExpiration());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("NonBufferedDestination")) {
                  var3.setNonBufferedDestination(var4.isNonBufferedDestination());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("NonBufferedSource")) {
                  var3.setNonBufferedSource(var4.isNonBufferedSource());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("RetransmissionExponentialBackoff")) {
                  var3.setRetransmissionExponentialBackoff(var4.isRetransmissionExponentialBackoff());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else {
                  super.applyPropertyUpdate(var1, var2);
               }

            }
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var8);
         }
      }

      protected AbstractDescriptorBean finishCopy(AbstractDescriptorBean var1, boolean var2, List var3) {
         try {
            WebServiceReliabilityMBeanImpl var5 = (WebServiceReliabilityMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("AcknowledgementInterval")) && this.bean.isAcknowledgementIntervalSet()) {
               var5.setAcknowledgementInterval(this.bean.getAcknowledgementInterval());
            }

            if ((var3 == null || !var3.contains("BaseRetransmissionInterval")) && this.bean.isBaseRetransmissionIntervalSet()) {
               var5.setBaseRetransmissionInterval(this.bean.getBaseRetransmissionInterval());
            }

            if ((var3 == null || !var3.contains("InactivityTimeout")) && this.bean.isInactivityTimeoutSet()) {
               var5.setInactivityTimeout(this.bean.getInactivityTimeout());
            }

            if ((var3 == null || !var3.contains("SequenceExpiration")) && this.bean.isSequenceExpirationSet()) {
               var5.setSequenceExpiration(this.bean.getSequenceExpiration());
            }

            if ((var3 == null || !var3.contains("NonBufferedDestination")) && this.bean.isNonBufferedDestinationSet()) {
               var5.setNonBufferedDestination(this.bean.isNonBufferedDestination());
            }

            if ((var3 == null || !var3.contains("NonBufferedSource")) && this.bean.isNonBufferedSourceSet()) {
               var5.setNonBufferedSource(this.bean.isNonBufferedSource());
            }

            if ((var3 == null || !var3.contains("RetransmissionExponentialBackoff")) && this.bean.isRetransmissionExponentialBackoffSet()) {
               var5.setRetransmissionExponentialBackoff(this.bean.isRetransmissionExponentialBackoff());
            }

            return var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var7);
         }
      }

      protected void inferSubTree(Class var1, Object var2) {
         super.inferSubTree(var1, var2);
         Object var3 = null;
      }
   }
}
