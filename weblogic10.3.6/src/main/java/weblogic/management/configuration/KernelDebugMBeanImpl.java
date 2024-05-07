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
import weblogic.utils.collections.ArrayIterator;
import weblogic.utils.collections.CombinedIterator;

public class KernelDebugMBeanImpl extends DebugMBeanImpl implements KernelDebugMBean, Serializable {
   private boolean _DebugAbbreviation;
   private boolean _DebugConnection;
   private boolean _DebugDGCEnrollment;
   private boolean _DebugFailOver;
   private boolean _DebugIIOP;
   private boolean _DebugIIOPConnection;
   private boolean _DebugIIOPMarshal;
   private boolean _DebugIIOPOTS;
   private boolean _DebugIIOPReplacer;
   private boolean _DebugIIOPSecurity;
   private boolean _DebugIIOPStartup;
   private boolean _DebugIIOPTransport;
   private boolean _DebugLoadBalancing;
   private boolean _DebugMessaging;
   private boolean _DebugMuxer;
   private boolean _DebugMuxerConnection;
   private boolean _DebugMuxerDetail;
   private boolean _DebugMuxerException;
   private boolean _DebugMuxerTimeout;
   private boolean _DebugRC4;
   private boolean _DebugRSA;
   private boolean _DebugRouting;
   private boolean _DebugSSL;
   private boolean _DebugSelfTuning;
   private boolean _DebugWorkContext;
   private boolean _ForceGCEachDGCPeriod;
   private boolean _LogDGCStatistics;
   private static SchemaHelper2 _schemaHelper;

   public KernelDebugMBeanImpl() {
      this._initializeProperty(-1);
   }

   public KernelDebugMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public boolean getDebugAbbreviation() {
      return this._DebugAbbreviation;
   }

   public boolean isDebugAbbreviationSet() {
      return this._isSet(8);
   }

   public void setDebugAbbreviation(boolean var1) {
      boolean var2 = this._DebugAbbreviation;
      this._DebugAbbreviation = var1;
      this._postSet(8, var2, var1);
   }

   public boolean getDebugConnection() {
      return this._DebugConnection;
   }

   public boolean isDebugConnectionSet() {
      return this._isSet(9);
   }

   public void setDebugConnection(boolean var1) {
      boolean var2 = this._DebugConnection;
      this._DebugConnection = var1;
      this._postSet(9, var2, var1);
   }

   public boolean getDebugMessaging() {
      return this._DebugMessaging;
   }

   public boolean isDebugMessagingSet() {
      return this._isSet(10);
   }

   public void setDebugMessaging(boolean var1) {
      boolean var2 = this._DebugMessaging;
      this._DebugMessaging = var1;
      this._postSet(10, var2, var1);
   }

   public boolean getDebugRouting() {
      return this._DebugRouting;
   }

   public boolean isDebugRoutingSet() {
      return this._isSet(11);
   }

   public void setDebugRouting(boolean var1) {
      boolean var2 = this._DebugRouting;
      this._DebugRouting = var1;
      this._postSet(11, var2, var1);
   }

   public boolean getDebugLoadBalancing() {
      return this._DebugLoadBalancing;
   }

   public boolean isDebugLoadBalancingSet() {
      return this._isSet(12);
   }

   public void setDebugLoadBalancing(boolean var1) {
      boolean var2 = this._DebugLoadBalancing;
      this._DebugLoadBalancing = var1;
      this._postSet(12, var2, var1);
   }

   public boolean getDebugWorkContext() {
      return this._DebugWorkContext;
   }

   public boolean isDebugWorkContextSet() {
      return this._isSet(13);
   }

   public void setDebugWorkContext(boolean var1) {
      boolean var2 = this._DebugWorkContext;
      this._DebugWorkContext = var1;
      this._postSet(13, var2, var1);
   }

   public boolean getDebugFailOver() {
      return this._DebugFailOver;
   }

   public boolean isDebugFailOverSet() {
      return this._isSet(14);
   }

   public void setDebugFailOver(boolean var1) {
      boolean var2 = this._DebugFailOver;
      this._DebugFailOver = var1;
      this._postSet(14, var2, var1);
   }

   public boolean getForceGCEachDGCPeriod() {
      return this._ForceGCEachDGCPeriod;
   }

   public boolean isForceGCEachDGCPeriodSet() {
      return this._isSet(15);
   }

   public void setForceGCEachDGCPeriod(boolean var1) {
      boolean var2 = this._ForceGCEachDGCPeriod;
      this._ForceGCEachDGCPeriod = var1;
      this._postSet(15, var2, var1);
   }

   public boolean getDebugDGCEnrollment() {
      return this._DebugDGCEnrollment;
   }

   public boolean isDebugDGCEnrollmentSet() {
      return this._isSet(16);
   }

   public void setDebugDGCEnrollment(boolean var1) {
      boolean var2 = this._DebugDGCEnrollment;
      this._DebugDGCEnrollment = var1;
      this._postSet(16, var2, var1);
   }

   public boolean getLogDGCStatistics() {
      return this._LogDGCStatistics;
   }

   public boolean isLogDGCStatisticsSet() {
      return this._isSet(17);
   }

   public void setLogDGCStatistics(boolean var1) {
      boolean var2 = this._LogDGCStatistics;
      this._LogDGCStatistics = var1;
      this._postSet(17, var2, var1);
   }

   public boolean getDebugSSL() {
      return this._DebugSSL;
   }

   public boolean isDebugSSLSet() {
      return this._isSet(18);
   }

   public void setDebugSSL(boolean var1) {
      boolean var2 = this._DebugSSL;
      this._DebugSSL = var1;
      this._postSet(18, var2, var1);
   }

   public boolean getDebugRC4() {
      return this._DebugRC4;
   }

   public boolean isDebugRC4Set() {
      return this._isSet(19);
   }

   public void setDebugRC4(boolean var1) {
      boolean var2 = this._DebugRC4;
      this._DebugRC4 = var1;
      this._postSet(19, var2, var1);
   }

   public boolean getDebugRSA() {
      return this._DebugRSA;
   }

   public boolean isDebugRSASet() {
      return this._isSet(20);
   }

   public void setDebugRSA(boolean var1) {
      boolean var2 = this._DebugRSA;
      this._DebugRSA = var1;
      this._postSet(20, var2, var1);
   }

   public boolean getDebugMuxer() {
      return this._DebugMuxer;
   }

   public boolean isDebugMuxerSet() {
      return this._isSet(21);
   }

   public void setDebugMuxer(boolean var1) {
      boolean var2 = this._DebugMuxer;
      this._DebugMuxer = var1;
      this._postSet(21, var2, var1);
   }

   public boolean getDebugMuxerDetail() {
      return this._DebugMuxerDetail;
   }

   public boolean isDebugMuxerDetailSet() {
      return this._isSet(22);
   }

   public void setDebugMuxerDetail(boolean var1) {
      boolean var2 = this._DebugMuxerDetail;
      this._DebugMuxerDetail = var1;
      this._postSet(22, var2, var1);
   }

   public boolean getDebugMuxerTimeout() {
      return this._DebugMuxerTimeout;
   }

   public boolean isDebugMuxerTimeoutSet() {
      return this._isSet(23);
   }

   public void setDebugMuxerTimeout(boolean var1) {
      boolean var2 = this._DebugMuxerTimeout;
      this._DebugMuxerTimeout = var1;
      this._postSet(23, var2, var1);
   }

   public boolean getDebugMuxerConnection() {
      return this._DebugMuxerConnection;
   }

   public boolean isDebugMuxerConnectionSet() {
      return this._isSet(24);
   }

   public void setDebugMuxerConnection(boolean var1) {
      boolean var2 = this._DebugMuxerConnection;
      this._DebugMuxerConnection = var1;
      this._postSet(24, var2, var1);
   }

   public boolean getDebugMuxerException() {
      return this._DebugMuxerException;
   }

   public boolean isDebugMuxerExceptionSet() {
      return this._isSet(25);
   }

   public void setDebugMuxerException(boolean var1) {
      boolean var2 = this._DebugMuxerException;
      this._DebugMuxerException = var1;
      this._postSet(25, var2, var1);
   }

   public boolean getDebugIIOP() {
      return this._DebugIIOP;
   }

   public boolean isDebugIIOPSet() {
      return this._isSet(26);
   }

   public void setDebugIIOP(boolean var1) {
      boolean var2 = this._DebugIIOP;
      this._DebugIIOP = var1;
      this._postSet(26, var2, var1);
   }

   public boolean getDebugIIOPTransport() {
      return this._DebugIIOPTransport;
   }

   public boolean isDebugIIOPTransportSet() {
      return this._isSet(27);
   }

   public void setDebugIIOPTransport(boolean var1) {
      boolean var2 = this._DebugIIOPTransport;
      this._DebugIIOPTransport = var1;
      this._postSet(27, var2, var1);
   }

   public boolean getDebugIIOPMarshal() {
      return this._DebugIIOPMarshal;
   }

   public boolean isDebugIIOPMarshalSet() {
      return this._isSet(28);
   }

   public void setDebugIIOPMarshal(boolean var1) {
      boolean var2 = this._DebugIIOPMarshal;
      this._DebugIIOPMarshal = var1;
      this._postSet(28, var2, var1);
   }

   public boolean getDebugIIOPSecurity() {
      return this._DebugIIOPSecurity;
   }

   public boolean isDebugIIOPSecuritySet() {
      return this._isSet(29);
   }

   public void setDebugIIOPSecurity(boolean var1) {
      boolean var2 = this._DebugIIOPSecurity;
      this._DebugIIOPSecurity = var1;
      this._postSet(29, var2, var1);
   }

   public boolean getDebugIIOPOTS() {
      return this._DebugIIOPOTS;
   }

   public boolean isDebugIIOPOTSSet() {
      return this._isSet(30);
   }

   public void setDebugIIOPOTS(boolean var1) {
      boolean var2 = this._DebugIIOPOTS;
      this._DebugIIOPOTS = var1;
      this._postSet(30, var2, var1);
   }

   public boolean getDebugIIOPReplacer() {
      return this._DebugIIOPReplacer;
   }

   public boolean isDebugIIOPReplacerSet() {
      return this._isSet(31);
   }

   public void setDebugIIOPReplacer(boolean var1) {
      boolean var2 = this._DebugIIOPReplacer;
      this._DebugIIOPReplacer = var1;
      this._postSet(31, var2, var1);
   }

   public boolean getDebugIIOPConnection() {
      return this._DebugIIOPConnection;
   }

   public boolean isDebugIIOPConnectionSet() {
      return this._isSet(32);
   }

   public void setDebugIIOPConnection(boolean var1) {
      boolean var2 = this._DebugIIOPConnection;
      this._DebugIIOPConnection = var1;
      this._postSet(32, var2, var1);
   }

   public boolean getDebugIIOPStartup() {
      return this._DebugIIOPStartup;
   }

   public boolean isDebugIIOPStartupSet() {
      return this._isSet(33);
   }

   public void setDebugIIOPStartup(boolean var1) {
      boolean var2 = this._DebugIIOPStartup;
      this._DebugIIOPStartup = var1;
      this._postSet(33, var2, var1);
   }

   public boolean getDebugSelfTuning() {
      return this._DebugSelfTuning;
   }

   public boolean isDebugSelfTuningSet() {
      return this._isSet(34);
   }

   public void setDebugSelfTuning(boolean var1) {
      boolean var2 = this._DebugSelfTuning;
      this._DebugSelfTuning = var1;
      this._postSet(34, var2, var1);
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
         var1 = 8;
      }

      try {
         switch (var1) {
            case 8:
               this._DebugAbbreviation = false;
               if (var2) {
                  break;
               }
            case 9:
               this._DebugConnection = false;
               if (var2) {
                  break;
               }
            case 16:
               this._DebugDGCEnrollment = false;
               if (var2) {
                  break;
               }
            case 14:
               this._DebugFailOver = false;
               if (var2) {
                  break;
               }
            case 26:
               this._DebugIIOP = false;
               if (var2) {
                  break;
               }
            case 32:
               this._DebugIIOPConnection = false;
               if (var2) {
                  break;
               }
            case 28:
               this._DebugIIOPMarshal = false;
               if (var2) {
                  break;
               }
            case 30:
               this._DebugIIOPOTS = false;
               if (var2) {
                  break;
               }
            case 31:
               this._DebugIIOPReplacer = false;
               if (var2) {
                  break;
               }
            case 29:
               this._DebugIIOPSecurity = false;
               if (var2) {
                  break;
               }
            case 33:
               this._DebugIIOPStartup = false;
               if (var2) {
                  break;
               }
            case 27:
               this._DebugIIOPTransport = false;
               if (var2) {
                  break;
               }
            case 12:
               this._DebugLoadBalancing = false;
               if (var2) {
                  break;
               }
            case 10:
               this._DebugMessaging = false;
               if (var2) {
                  break;
               }
            case 21:
               this._DebugMuxer = false;
               if (var2) {
                  break;
               }
            case 24:
               this._DebugMuxerConnection = false;
               if (var2) {
                  break;
               }
            case 22:
               this._DebugMuxerDetail = false;
               if (var2) {
                  break;
               }
            case 25:
               this._DebugMuxerException = false;
               if (var2) {
                  break;
               }
            case 23:
               this._DebugMuxerTimeout = false;
               if (var2) {
                  break;
               }
            case 19:
               this._DebugRC4 = false;
               if (var2) {
                  break;
               }
            case 20:
               this._DebugRSA = false;
               if (var2) {
                  break;
               }
            case 11:
               this._DebugRouting = false;
               if (var2) {
                  break;
               }
            case 18:
               this._DebugSSL = false;
               if (var2) {
                  break;
               }
            case 34:
               this._DebugSelfTuning = false;
               if (var2) {
                  break;
               }
            case 13:
               this._DebugWorkContext = false;
               if (var2) {
                  break;
               }
            case 15:
               this._ForceGCEachDGCPeriod = false;
               if (var2) {
                  break;
               }
            case 17:
               this._LogDGCStatistics = false;
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
      return "KernelDebug";
   }

   public void putValue(String var1, Object var2) {
      boolean var3;
      if (var1.equals("DebugAbbreviation")) {
         var3 = this._DebugAbbreviation;
         this._DebugAbbreviation = (Boolean)var2;
         this._postSet(8, var3, this._DebugAbbreviation);
      } else if (var1.equals("DebugConnection")) {
         var3 = this._DebugConnection;
         this._DebugConnection = (Boolean)var2;
         this._postSet(9, var3, this._DebugConnection);
      } else if (var1.equals("DebugDGCEnrollment")) {
         var3 = this._DebugDGCEnrollment;
         this._DebugDGCEnrollment = (Boolean)var2;
         this._postSet(16, var3, this._DebugDGCEnrollment);
      } else if (var1.equals("DebugFailOver")) {
         var3 = this._DebugFailOver;
         this._DebugFailOver = (Boolean)var2;
         this._postSet(14, var3, this._DebugFailOver);
      } else if (var1.equals("DebugIIOP")) {
         var3 = this._DebugIIOP;
         this._DebugIIOP = (Boolean)var2;
         this._postSet(26, var3, this._DebugIIOP);
      } else if (var1.equals("DebugIIOPConnection")) {
         var3 = this._DebugIIOPConnection;
         this._DebugIIOPConnection = (Boolean)var2;
         this._postSet(32, var3, this._DebugIIOPConnection);
      } else if (var1.equals("DebugIIOPMarshal")) {
         var3 = this._DebugIIOPMarshal;
         this._DebugIIOPMarshal = (Boolean)var2;
         this._postSet(28, var3, this._DebugIIOPMarshal);
      } else if (var1.equals("DebugIIOPOTS")) {
         var3 = this._DebugIIOPOTS;
         this._DebugIIOPOTS = (Boolean)var2;
         this._postSet(30, var3, this._DebugIIOPOTS);
      } else if (var1.equals("DebugIIOPReplacer")) {
         var3 = this._DebugIIOPReplacer;
         this._DebugIIOPReplacer = (Boolean)var2;
         this._postSet(31, var3, this._DebugIIOPReplacer);
      } else if (var1.equals("DebugIIOPSecurity")) {
         var3 = this._DebugIIOPSecurity;
         this._DebugIIOPSecurity = (Boolean)var2;
         this._postSet(29, var3, this._DebugIIOPSecurity);
      } else if (var1.equals("DebugIIOPStartup")) {
         var3 = this._DebugIIOPStartup;
         this._DebugIIOPStartup = (Boolean)var2;
         this._postSet(33, var3, this._DebugIIOPStartup);
      } else if (var1.equals("DebugIIOPTransport")) {
         var3 = this._DebugIIOPTransport;
         this._DebugIIOPTransport = (Boolean)var2;
         this._postSet(27, var3, this._DebugIIOPTransport);
      } else if (var1.equals("DebugLoadBalancing")) {
         var3 = this._DebugLoadBalancing;
         this._DebugLoadBalancing = (Boolean)var2;
         this._postSet(12, var3, this._DebugLoadBalancing);
      } else if (var1.equals("DebugMessaging")) {
         var3 = this._DebugMessaging;
         this._DebugMessaging = (Boolean)var2;
         this._postSet(10, var3, this._DebugMessaging);
      } else if (var1.equals("DebugMuxer")) {
         var3 = this._DebugMuxer;
         this._DebugMuxer = (Boolean)var2;
         this._postSet(21, var3, this._DebugMuxer);
      } else if (var1.equals("DebugMuxerConnection")) {
         var3 = this._DebugMuxerConnection;
         this._DebugMuxerConnection = (Boolean)var2;
         this._postSet(24, var3, this._DebugMuxerConnection);
      } else if (var1.equals("DebugMuxerDetail")) {
         var3 = this._DebugMuxerDetail;
         this._DebugMuxerDetail = (Boolean)var2;
         this._postSet(22, var3, this._DebugMuxerDetail);
      } else if (var1.equals("DebugMuxerException")) {
         var3 = this._DebugMuxerException;
         this._DebugMuxerException = (Boolean)var2;
         this._postSet(25, var3, this._DebugMuxerException);
      } else if (var1.equals("DebugMuxerTimeout")) {
         var3 = this._DebugMuxerTimeout;
         this._DebugMuxerTimeout = (Boolean)var2;
         this._postSet(23, var3, this._DebugMuxerTimeout);
      } else if (var1.equals("DebugRC4")) {
         var3 = this._DebugRC4;
         this._DebugRC4 = (Boolean)var2;
         this._postSet(19, var3, this._DebugRC4);
      } else if (var1.equals("DebugRSA")) {
         var3 = this._DebugRSA;
         this._DebugRSA = (Boolean)var2;
         this._postSet(20, var3, this._DebugRSA);
      } else if (var1.equals("DebugRouting")) {
         var3 = this._DebugRouting;
         this._DebugRouting = (Boolean)var2;
         this._postSet(11, var3, this._DebugRouting);
      } else if (var1.equals("DebugSSL")) {
         var3 = this._DebugSSL;
         this._DebugSSL = (Boolean)var2;
         this._postSet(18, var3, this._DebugSSL);
      } else if (var1.equals("DebugSelfTuning")) {
         var3 = this._DebugSelfTuning;
         this._DebugSelfTuning = (Boolean)var2;
         this._postSet(34, var3, this._DebugSelfTuning);
      } else if (var1.equals("DebugWorkContext")) {
         var3 = this._DebugWorkContext;
         this._DebugWorkContext = (Boolean)var2;
         this._postSet(13, var3, this._DebugWorkContext);
      } else if (var1.equals("ForceGCEachDGCPeriod")) {
         var3 = this._ForceGCEachDGCPeriod;
         this._ForceGCEachDGCPeriod = (Boolean)var2;
         this._postSet(15, var3, this._ForceGCEachDGCPeriod);
      } else if (var1.equals("LogDGCStatistics")) {
         var3 = this._LogDGCStatistics;
         this._LogDGCStatistics = (Boolean)var2;
         this._postSet(17, var3, this._LogDGCStatistics);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("DebugAbbreviation")) {
         return new Boolean(this._DebugAbbreviation);
      } else if (var1.equals("DebugConnection")) {
         return new Boolean(this._DebugConnection);
      } else if (var1.equals("DebugDGCEnrollment")) {
         return new Boolean(this._DebugDGCEnrollment);
      } else if (var1.equals("DebugFailOver")) {
         return new Boolean(this._DebugFailOver);
      } else if (var1.equals("DebugIIOP")) {
         return new Boolean(this._DebugIIOP);
      } else if (var1.equals("DebugIIOPConnection")) {
         return new Boolean(this._DebugIIOPConnection);
      } else if (var1.equals("DebugIIOPMarshal")) {
         return new Boolean(this._DebugIIOPMarshal);
      } else if (var1.equals("DebugIIOPOTS")) {
         return new Boolean(this._DebugIIOPOTS);
      } else if (var1.equals("DebugIIOPReplacer")) {
         return new Boolean(this._DebugIIOPReplacer);
      } else if (var1.equals("DebugIIOPSecurity")) {
         return new Boolean(this._DebugIIOPSecurity);
      } else if (var1.equals("DebugIIOPStartup")) {
         return new Boolean(this._DebugIIOPStartup);
      } else if (var1.equals("DebugIIOPTransport")) {
         return new Boolean(this._DebugIIOPTransport);
      } else if (var1.equals("DebugLoadBalancing")) {
         return new Boolean(this._DebugLoadBalancing);
      } else if (var1.equals("DebugMessaging")) {
         return new Boolean(this._DebugMessaging);
      } else if (var1.equals("DebugMuxer")) {
         return new Boolean(this._DebugMuxer);
      } else if (var1.equals("DebugMuxerConnection")) {
         return new Boolean(this._DebugMuxerConnection);
      } else if (var1.equals("DebugMuxerDetail")) {
         return new Boolean(this._DebugMuxerDetail);
      } else if (var1.equals("DebugMuxerException")) {
         return new Boolean(this._DebugMuxerException);
      } else if (var1.equals("DebugMuxerTimeout")) {
         return new Boolean(this._DebugMuxerTimeout);
      } else if (var1.equals("DebugRC4")) {
         return new Boolean(this._DebugRC4);
      } else if (var1.equals("DebugRSA")) {
         return new Boolean(this._DebugRSA);
      } else if (var1.equals("DebugRouting")) {
         return new Boolean(this._DebugRouting);
      } else if (var1.equals("DebugSSL")) {
         return new Boolean(this._DebugSSL);
      } else if (var1.equals("DebugSelfTuning")) {
         return new Boolean(this._DebugSelfTuning);
      } else if (var1.equals("DebugWorkContext")) {
         return new Boolean(this._DebugWorkContext);
      } else if (var1.equals("ForceGCEachDGCPeriod")) {
         return new Boolean(this._ForceGCEachDGCPeriod);
      } else {
         return var1.equals("LogDGCStatistics") ? new Boolean(this._LogDGCStatistics) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends DebugMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 8:
               if (var1.equals("debugrc4")) {
                  return 19;
               }

               if (var1.equals("debugrsa")) {
                  return 20;
               }

               if (var1.equals("debugssl")) {
                  return 18;
               }
               break;
            case 9:
               if (var1.equals("debugiiop")) {
                  return 26;
               }
            case 10:
            case 14:
            default:
               break;
            case 11:
               if (var1.equals("debug-muxer")) {
                  return 21;
               }
               break;
            case 12:
               if (var1.equals("debugiiopots")) {
                  return 30;
               }
               break;
            case 13:
               if (var1.equals("debug-routing")) {
                  return 11;
               }
               break;
            case 15:
               if (var1.equals("debug-fail-over")) {
                  return 14;
               }

               if (var1.equals("debug-messaging")) {
                  return 10;
               }
               break;
            case 16:
               if (var1.equals("debug-connection")) {
                  return 9;
               }
               break;
            case 17:
               if (var1.equals("debugiiop-marshal")) {
                  return 28;
               }

               if (var1.equals("debugiiop-startup")) {
                  return 33;
               }

               if (var1.equals("debug-self-tuning")) {
                  return 34;
               }

               if (var1.equals("logdgc-statistics")) {
                  return 17;
               }
               break;
            case 18:
               if (var1.equals("debug-abbreviation")) {
                  return 8;
               }

               if (var1.equals("debugiiop-replacer")) {
                  return 31;
               }

               if (var1.equals("debugiiop-security")) {
                  return 29;
               }

               if (var1.equals("debug-muxer-detail")) {
                  return 22;
               }

               if (var1.equals("debug-work-context")) {
                  return 13;
               }
               break;
            case 19:
               if (var1.equals("debugdgc-enrollment")) {
                  return 16;
               }

               if (var1.equals("debugiiop-transport")) {
                  return 27;
               }

               if (var1.equals("debug-muxer-timeout")) {
                  return 23;
               }
               break;
            case 20:
               if (var1.equals("debugiiop-connection")) {
                  return 32;
               }

               if (var1.equals("debug-load-balancing")) {
                  return 12;
               }
               break;
            case 21:
               if (var1.equals("debug-muxer-exception")) {
                  return 25;
               }
               break;
            case 22:
               if (var1.equals("debug-muxer-connection")) {
                  return 24;
               }

               if (var1.equals("forcegc-eachdgc-period")) {
                  return 15;
               }
         }

         return super.getPropertyIndex(var1);
      }

      public SchemaHelper getSchemaHelper(int var1) {
         switch (var1) {
            case 7:
               return new DebugScopeMBeanImpl.SchemaHelper2();
            default:
               return super.getSchemaHelper(var1);
         }
      }

      public String getElementName(int var1) {
         switch (var1) {
            case 8:
               return "debug-abbreviation";
            case 9:
               return "debug-connection";
            case 10:
               return "debug-messaging";
            case 11:
               return "debug-routing";
            case 12:
               return "debug-load-balancing";
            case 13:
               return "debug-work-context";
            case 14:
               return "debug-fail-over";
            case 15:
               return "forcegc-eachdgc-period";
            case 16:
               return "debugdgc-enrollment";
            case 17:
               return "logdgc-statistics";
            case 18:
               return "debugssl";
            case 19:
               return "debugrc4";
            case 20:
               return "debugrsa";
            case 21:
               return "debug-muxer";
            case 22:
               return "debug-muxer-detail";
            case 23:
               return "debug-muxer-timeout";
            case 24:
               return "debug-muxer-connection";
            case 25:
               return "debug-muxer-exception";
            case 26:
               return "debugiiop";
            case 27:
               return "debugiiop-transport";
            case 28:
               return "debugiiop-marshal";
            case 29:
               return "debugiiop-security";
            case 30:
               return "debugiiopots";
            case 31:
               return "debugiiop-replacer";
            case 32:
               return "debugiiop-connection";
            case 33:
               return "debugiiop-startup";
            case 34:
               return "debug-self-tuning";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isArray(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isArray(var1);
         }
      }

      public boolean isBean(int var1) {
         switch (var1) {
            case 7:
               return true;
            default:
               return super.isBean(var1);
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

   protected static class Helper extends DebugMBeanImpl.Helper {
      private KernelDebugMBeanImpl bean;

      protected Helper(KernelDebugMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 8:
               return "DebugAbbreviation";
            case 9:
               return "DebugConnection";
            case 10:
               return "DebugMessaging";
            case 11:
               return "DebugRouting";
            case 12:
               return "DebugLoadBalancing";
            case 13:
               return "DebugWorkContext";
            case 14:
               return "DebugFailOver";
            case 15:
               return "ForceGCEachDGCPeriod";
            case 16:
               return "DebugDGCEnrollment";
            case 17:
               return "LogDGCStatistics";
            case 18:
               return "DebugSSL";
            case 19:
               return "DebugRC4";
            case 20:
               return "DebugRSA";
            case 21:
               return "DebugMuxer";
            case 22:
               return "DebugMuxerDetail";
            case 23:
               return "DebugMuxerTimeout";
            case 24:
               return "DebugMuxerConnection";
            case 25:
               return "DebugMuxerException";
            case 26:
               return "DebugIIOP";
            case 27:
               return "DebugIIOPTransport";
            case 28:
               return "DebugIIOPMarshal";
            case 29:
               return "DebugIIOPSecurity";
            case 30:
               return "DebugIIOPOTS";
            case 31:
               return "DebugIIOPReplacer";
            case 32:
               return "DebugIIOPConnection";
            case 33:
               return "DebugIIOPStartup";
            case 34:
               return "DebugSelfTuning";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("DebugAbbreviation")) {
            return 8;
         } else if (var1.equals("DebugConnection")) {
            return 9;
         } else if (var1.equals("DebugDGCEnrollment")) {
            return 16;
         } else if (var1.equals("DebugFailOver")) {
            return 14;
         } else if (var1.equals("DebugIIOP")) {
            return 26;
         } else if (var1.equals("DebugIIOPConnection")) {
            return 32;
         } else if (var1.equals("DebugIIOPMarshal")) {
            return 28;
         } else if (var1.equals("DebugIIOPOTS")) {
            return 30;
         } else if (var1.equals("DebugIIOPReplacer")) {
            return 31;
         } else if (var1.equals("DebugIIOPSecurity")) {
            return 29;
         } else if (var1.equals("DebugIIOPStartup")) {
            return 33;
         } else if (var1.equals("DebugIIOPTransport")) {
            return 27;
         } else if (var1.equals("DebugLoadBalancing")) {
            return 12;
         } else if (var1.equals("DebugMessaging")) {
            return 10;
         } else if (var1.equals("DebugMuxer")) {
            return 21;
         } else if (var1.equals("DebugMuxerConnection")) {
            return 24;
         } else if (var1.equals("DebugMuxerDetail")) {
            return 22;
         } else if (var1.equals("DebugMuxerException")) {
            return 25;
         } else if (var1.equals("DebugMuxerTimeout")) {
            return 23;
         } else if (var1.equals("DebugRC4")) {
            return 19;
         } else if (var1.equals("DebugRSA")) {
            return 20;
         } else if (var1.equals("DebugRouting")) {
            return 11;
         } else if (var1.equals("DebugSSL")) {
            return 18;
         } else if (var1.equals("DebugSelfTuning")) {
            return 34;
         } else if (var1.equals("DebugWorkContext")) {
            return 13;
         } else if (var1.equals("ForceGCEachDGCPeriod")) {
            return 15;
         } else {
            return var1.equals("LogDGCStatistics") ? 17 : super.getPropertyIndex(var1);
         }
      }

      public Iterator getChildren() {
         ArrayList var1 = new ArrayList();
         var1.add(new ArrayIterator(this.bean.getDebugScopes()));
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
            if (this.bean.isDebugAbbreviationSet()) {
               var2.append("DebugAbbreviation");
               var2.append(String.valueOf(this.bean.getDebugAbbreviation()));
            }

            if (this.bean.isDebugConnectionSet()) {
               var2.append("DebugConnection");
               var2.append(String.valueOf(this.bean.getDebugConnection()));
            }

            if (this.bean.isDebugDGCEnrollmentSet()) {
               var2.append("DebugDGCEnrollment");
               var2.append(String.valueOf(this.bean.getDebugDGCEnrollment()));
            }

            if (this.bean.isDebugFailOverSet()) {
               var2.append("DebugFailOver");
               var2.append(String.valueOf(this.bean.getDebugFailOver()));
            }

            if (this.bean.isDebugIIOPSet()) {
               var2.append("DebugIIOP");
               var2.append(String.valueOf(this.bean.getDebugIIOP()));
            }

            if (this.bean.isDebugIIOPConnectionSet()) {
               var2.append("DebugIIOPConnection");
               var2.append(String.valueOf(this.bean.getDebugIIOPConnection()));
            }

            if (this.bean.isDebugIIOPMarshalSet()) {
               var2.append("DebugIIOPMarshal");
               var2.append(String.valueOf(this.bean.getDebugIIOPMarshal()));
            }

            if (this.bean.isDebugIIOPOTSSet()) {
               var2.append("DebugIIOPOTS");
               var2.append(String.valueOf(this.bean.getDebugIIOPOTS()));
            }

            if (this.bean.isDebugIIOPReplacerSet()) {
               var2.append("DebugIIOPReplacer");
               var2.append(String.valueOf(this.bean.getDebugIIOPReplacer()));
            }

            if (this.bean.isDebugIIOPSecuritySet()) {
               var2.append("DebugIIOPSecurity");
               var2.append(String.valueOf(this.bean.getDebugIIOPSecurity()));
            }

            if (this.bean.isDebugIIOPStartupSet()) {
               var2.append("DebugIIOPStartup");
               var2.append(String.valueOf(this.bean.getDebugIIOPStartup()));
            }

            if (this.bean.isDebugIIOPTransportSet()) {
               var2.append("DebugIIOPTransport");
               var2.append(String.valueOf(this.bean.getDebugIIOPTransport()));
            }

            if (this.bean.isDebugLoadBalancingSet()) {
               var2.append("DebugLoadBalancing");
               var2.append(String.valueOf(this.bean.getDebugLoadBalancing()));
            }

            if (this.bean.isDebugMessagingSet()) {
               var2.append("DebugMessaging");
               var2.append(String.valueOf(this.bean.getDebugMessaging()));
            }

            if (this.bean.isDebugMuxerSet()) {
               var2.append("DebugMuxer");
               var2.append(String.valueOf(this.bean.getDebugMuxer()));
            }

            if (this.bean.isDebugMuxerConnectionSet()) {
               var2.append("DebugMuxerConnection");
               var2.append(String.valueOf(this.bean.getDebugMuxerConnection()));
            }

            if (this.bean.isDebugMuxerDetailSet()) {
               var2.append("DebugMuxerDetail");
               var2.append(String.valueOf(this.bean.getDebugMuxerDetail()));
            }

            if (this.bean.isDebugMuxerExceptionSet()) {
               var2.append("DebugMuxerException");
               var2.append(String.valueOf(this.bean.getDebugMuxerException()));
            }

            if (this.bean.isDebugMuxerTimeoutSet()) {
               var2.append("DebugMuxerTimeout");
               var2.append(String.valueOf(this.bean.getDebugMuxerTimeout()));
            }

            if (this.bean.isDebugRC4Set()) {
               var2.append("DebugRC4");
               var2.append(String.valueOf(this.bean.getDebugRC4()));
            }

            if (this.bean.isDebugRSASet()) {
               var2.append("DebugRSA");
               var2.append(String.valueOf(this.bean.getDebugRSA()));
            }

            if (this.bean.isDebugRoutingSet()) {
               var2.append("DebugRouting");
               var2.append(String.valueOf(this.bean.getDebugRouting()));
            }

            if (this.bean.isDebugSSLSet()) {
               var2.append("DebugSSL");
               var2.append(String.valueOf(this.bean.getDebugSSL()));
            }

            if (this.bean.isDebugSelfTuningSet()) {
               var2.append("DebugSelfTuning");
               var2.append(String.valueOf(this.bean.getDebugSelfTuning()));
            }

            if (this.bean.isDebugWorkContextSet()) {
               var2.append("DebugWorkContext");
               var2.append(String.valueOf(this.bean.getDebugWorkContext()));
            }

            if (this.bean.isForceGCEachDGCPeriodSet()) {
               var2.append("ForceGCEachDGCPeriod");
               var2.append(String.valueOf(this.bean.getForceGCEachDGCPeriod()));
            }

            if (this.bean.isLogDGCStatisticsSet()) {
               var2.append("LogDGCStatistics");
               var2.append(String.valueOf(this.bean.getLogDGCStatistics()));
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
            KernelDebugMBeanImpl var2 = (KernelDebugMBeanImpl)var1;
            this.computeDiff("DebugAbbreviation", this.bean.getDebugAbbreviation(), var2.getDebugAbbreviation(), false);
            this.computeDiff("DebugConnection", this.bean.getDebugConnection(), var2.getDebugConnection(), false);
            this.computeDiff("DebugDGCEnrollment", this.bean.getDebugDGCEnrollment(), var2.getDebugDGCEnrollment(), false);
            this.computeDiff("DebugFailOver", this.bean.getDebugFailOver(), var2.getDebugFailOver(), false);
            this.computeDiff("DebugIIOP", this.bean.getDebugIIOP(), var2.getDebugIIOP(), true);
            this.computeDiff("DebugIIOPConnection", this.bean.getDebugIIOPConnection(), var2.getDebugIIOPConnection(), true);
            this.computeDiff("DebugIIOPMarshal", this.bean.getDebugIIOPMarshal(), var2.getDebugIIOPMarshal(), true);
            this.computeDiff("DebugIIOPOTS", this.bean.getDebugIIOPOTS(), var2.getDebugIIOPOTS(), true);
            this.computeDiff("DebugIIOPReplacer", this.bean.getDebugIIOPReplacer(), var2.getDebugIIOPReplacer(), true);
            this.computeDiff("DebugIIOPSecurity", this.bean.getDebugIIOPSecurity(), var2.getDebugIIOPSecurity(), true);
            this.computeDiff("DebugIIOPStartup", this.bean.getDebugIIOPStartup(), var2.getDebugIIOPStartup(), true);
            this.computeDiff("DebugIIOPTransport", this.bean.getDebugIIOPTransport(), var2.getDebugIIOPTransport(), true);
            this.computeDiff("DebugLoadBalancing", this.bean.getDebugLoadBalancing(), var2.getDebugLoadBalancing(), false);
            this.computeDiff("DebugMessaging", this.bean.getDebugMessaging(), var2.getDebugMessaging(), false);
            this.computeDiff("DebugMuxer", this.bean.getDebugMuxer(), var2.getDebugMuxer(), false);
            this.computeDiff("DebugMuxerConnection", this.bean.getDebugMuxerConnection(), var2.getDebugMuxerConnection(), false);
            this.computeDiff("DebugMuxerDetail", this.bean.getDebugMuxerDetail(), var2.getDebugMuxerDetail(), false);
            this.computeDiff("DebugMuxerException", this.bean.getDebugMuxerException(), var2.getDebugMuxerException(), false);
            this.computeDiff("DebugMuxerTimeout", this.bean.getDebugMuxerTimeout(), var2.getDebugMuxerTimeout(), false);
            this.computeDiff("DebugRC4", this.bean.getDebugRC4(), var2.getDebugRC4(), false);
            this.computeDiff("DebugRSA", this.bean.getDebugRSA(), var2.getDebugRSA(), false);
            this.computeDiff("DebugRouting", this.bean.getDebugRouting(), var2.getDebugRouting(), false);
            this.computeDiff("DebugSSL", this.bean.getDebugSSL(), var2.getDebugSSL(), false);
            this.computeDiff("DebugSelfTuning", this.bean.getDebugSelfTuning(), var2.getDebugSelfTuning(), true);
            this.computeDiff("DebugWorkContext", this.bean.getDebugWorkContext(), var2.getDebugWorkContext(), false);
            this.computeDiff("ForceGCEachDGCPeriod", this.bean.getForceGCEachDGCPeriod(), var2.getForceGCEachDGCPeriod(), false);
            this.computeDiff("LogDGCStatistics", this.bean.getLogDGCStatistics(), var2.getLogDGCStatistics(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            KernelDebugMBeanImpl var3 = (KernelDebugMBeanImpl)var1.getSourceBean();
            KernelDebugMBeanImpl var4 = (KernelDebugMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("DebugAbbreviation")) {
                  var3.setDebugAbbreviation(var4.getDebugAbbreviation());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("DebugConnection")) {
                  var3.setDebugConnection(var4.getDebugConnection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("DebugDGCEnrollment")) {
                  var3.setDebugDGCEnrollment(var4.getDebugDGCEnrollment());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("DebugFailOver")) {
                  var3.setDebugFailOver(var4.getDebugFailOver());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("DebugIIOP")) {
                  var3.setDebugIIOP(var4.getDebugIIOP());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 26);
               } else if (var5.equals("DebugIIOPConnection")) {
                  var3.setDebugIIOPConnection(var4.getDebugIIOPConnection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 32);
               } else if (var5.equals("DebugIIOPMarshal")) {
                  var3.setDebugIIOPMarshal(var4.getDebugIIOPMarshal());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 28);
               } else if (var5.equals("DebugIIOPOTS")) {
                  var3.setDebugIIOPOTS(var4.getDebugIIOPOTS());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 30);
               } else if (var5.equals("DebugIIOPReplacer")) {
                  var3.setDebugIIOPReplacer(var4.getDebugIIOPReplacer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 31);
               } else if (var5.equals("DebugIIOPSecurity")) {
                  var3.setDebugIIOPSecurity(var4.getDebugIIOPSecurity());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 29);
               } else if (var5.equals("DebugIIOPStartup")) {
                  var3.setDebugIIOPStartup(var4.getDebugIIOPStartup());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 33);
               } else if (var5.equals("DebugIIOPTransport")) {
                  var3.setDebugIIOPTransport(var4.getDebugIIOPTransport());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 27);
               } else if (var5.equals("DebugLoadBalancing")) {
                  var3.setDebugLoadBalancing(var4.getDebugLoadBalancing());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("DebugMessaging")) {
                  var3.setDebugMessaging(var4.getDebugMessaging());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("DebugMuxer")) {
                  var3.setDebugMuxer(var4.getDebugMuxer());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 21);
               } else if (var5.equals("DebugMuxerConnection")) {
                  var3.setDebugMuxerConnection(var4.getDebugMuxerConnection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 24);
               } else if (var5.equals("DebugMuxerDetail")) {
                  var3.setDebugMuxerDetail(var4.getDebugMuxerDetail());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 22);
               } else if (var5.equals("DebugMuxerException")) {
                  var3.setDebugMuxerException(var4.getDebugMuxerException());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 25);
               } else if (var5.equals("DebugMuxerTimeout")) {
                  var3.setDebugMuxerTimeout(var4.getDebugMuxerTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 23);
               } else if (var5.equals("DebugRC4")) {
                  var3.setDebugRC4(var4.getDebugRC4());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 19);
               } else if (var5.equals("DebugRSA")) {
                  var3.setDebugRSA(var4.getDebugRSA());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 20);
               } else if (var5.equals("DebugRouting")) {
                  var3.setDebugRouting(var4.getDebugRouting());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("DebugSSL")) {
                  var3.setDebugSSL(var4.getDebugSSL());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 18);
               } else if (var5.equals("DebugSelfTuning")) {
                  var3.setDebugSelfTuning(var4.getDebugSelfTuning());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 34);
               } else if (var5.equals("DebugWorkContext")) {
                  var3.setDebugWorkContext(var4.getDebugWorkContext());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("ForceGCEachDGCPeriod")) {
                  var3.setForceGCEachDGCPeriod(var4.getForceGCEachDGCPeriod());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("LogDGCStatistics")) {
                  var3.setLogDGCStatistics(var4.getLogDGCStatistics());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 17);
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
            KernelDebugMBeanImpl var5 = (KernelDebugMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("DebugAbbreviation")) && this.bean.isDebugAbbreviationSet()) {
               var5.setDebugAbbreviation(this.bean.getDebugAbbreviation());
            }

            if ((var3 == null || !var3.contains("DebugConnection")) && this.bean.isDebugConnectionSet()) {
               var5.setDebugConnection(this.bean.getDebugConnection());
            }

            if ((var3 == null || !var3.contains("DebugDGCEnrollment")) && this.bean.isDebugDGCEnrollmentSet()) {
               var5.setDebugDGCEnrollment(this.bean.getDebugDGCEnrollment());
            }

            if ((var3 == null || !var3.contains("DebugFailOver")) && this.bean.isDebugFailOverSet()) {
               var5.setDebugFailOver(this.bean.getDebugFailOver());
            }

            if ((var3 == null || !var3.contains("DebugIIOP")) && this.bean.isDebugIIOPSet()) {
               var5.setDebugIIOP(this.bean.getDebugIIOP());
            }

            if ((var3 == null || !var3.contains("DebugIIOPConnection")) && this.bean.isDebugIIOPConnectionSet()) {
               var5.setDebugIIOPConnection(this.bean.getDebugIIOPConnection());
            }

            if ((var3 == null || !var3.contains("DebugIIOPMarshal")) && this.bean.isDebugIIOPMarshalSet()) {
               var5.setDebugIIOPMarshal(this.bean.getDebugIIOPMarshal());
            }

            if ((var3 == null || !var3.contains("DebugIIOPOTS")) && this.bean.isDebugIIOPOTSSet()) {
               var5.setDebugIIOPOTS(this.bean.getDebugIIOPOTS());
            }

            if ((var3 == null || !var3.contains("DebugIIOPReplacer")) && this.bean.isDebugIIOPReplacerSet()) {
               var5.setDebugIIOPReplacer(this.bean.getDebugIIOPReplacer());
            }

            if ((var3 == null || !var3.contains("DebugIIOPSecurity")) && this.bean.isDebugIIOPSecuritySet()) {
               var5.setDebugIIOPSecurity(this.bean.getDebugIIOPSecurity());
            }

            if ((var3 == null || !var3.contains("DebugIIOPStartup")) && this.bean.isDebugIIOPStartupSet()) {
               var5.setDebugIIOPStartup(this.bean.getDebugIIOPStartup());
            }

            if ((var3 == null || !var3.contains("DebugIIOPTransport")) && this.bean.isDebugIIOPTransportSet()) {
               var5.setDebugIIOPTransport(this.bean.getDebugIIOPTransport());
            }

            if ((var3 == null || !var3.contains("DebugLoadBalancing")) && this.bean.isDebugLoadBalancingSet()) {
               var5.setDebugLoadBalancing(this.bean.getDebugLoadBalancing());
            }

            if ((var3 == null || !var3.contains("DebugMessaging")) && this.bean.isDebugMessagingSet()) {
               var5.setDebugMessaging(this.bean.getDebugMessaging());
            }

            if ((var3 == null || !var3.contains("DebugMuxer")) && this.bean.isDebugMuxerSet()) {
               var5.setDebugMuxer(this.bean.getDebugMuxer());
            }

            if ((var3 == null || !var3.contains("DebugMuxerConnection")) && this.bean.isDebugMuxerConnectionSet()) {
               var5.setDebugMuxerConnection(this.bean.getDebugMuxerConnection());
            }

            if ((var3 == null || !var3.contains("DebugMuxerDetail")) && this.bean.isDebugMuxerDetailSet()) {
               var5.setDebugMuxerDetail(this.bean.getDebugMuxerDetail());
            }

            if ((var3 == null || !var3.contains("DebugMuxerException")) && this.bean.isDebugMuxerExceptionSet()) {
               var5.setDebugMuxerException(this.bean.getDebugMuxerException());
            }

            if ((var3 == null || !var3.contains("DebugMuxerTimeout")) && this.bean.isDebugMuxerTimeoutSet()) {
               var5.setDebugMuxerTimeout(this.bean.getDebugMuxerTimeout());
            }

            if ((var3 == null || !var3.contains("DebugRC4")) && this.bean.isDebugRC4Set()) {
               var5.setDebugRC4(this.bean.getDebugRC4());
            }

            if ((var3 == null || !var3.contains("DebugRSA")) && this.bean.isDebugRSASet()) {
               var5.setDebugRSA(this.bean.getDebugRSA());
            }

            if ((var3 == null || !var3.contains("DebugRouting")) && this.bean.isDebugRoutingSet()) {
               var5.setDebugRouting(this.bean.getDebugRouting());
            }

            if ((var3 == null || !var3.contains("DebugSSL")) && this.bean.isDebugSSLSet()) {
               var5.setDebugSSL(this.bean.getDebugSSL());
            }

            if ((var3 == null || !var3.contains("DebugSelfTuning")) && this.bean.isDebugSelfTuningSet()) {
               var5.setDebugSelfTuning(this.bean.getDebugSelfTuning());
            }

            if ((var3 == null || !var3.contains("DebugWorkContext")) && this.bean.isDebugWorkContextSet()) {
               var5.setDebugWorkContext(this.bean.getDebugWorkContext());
            }

            if ((var3 == null || !var3.contains("ForceGCEachDGCPeriod")) && this.bean.isForceGCEachDGCPeriodSet()) {
               var5.setForceGCEachDGCPeriod(this.bean.getForceGCEachDGCPeriod());
            }

            if ((var3 == null || !var3.contains("LogDGCStatistics")) && this.bean.isLogDGCStatisticsSet()) {
               var5.setLogDGCStatistics(this.bean.getLogDGCStatistics());
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
