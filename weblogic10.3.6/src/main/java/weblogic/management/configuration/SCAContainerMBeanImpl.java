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
import weblogic.utils.collections.CombinedIterator;

public class SCAContainerMBeanImpl extends ConfigurationMBeanImpl implements SCAContainerMBean, Serializable {
   private boolean _AllowsPassByReference;
   private boolean _Autowire;
   private String _MaxAge;
   private String _MaxIdleTime;
   private boolean _Remotable;
   private boolean _SinglePrincipal;
   private int _Timeout;
   private static SchemaHelper2 _schemaHelper;

   public SCAContainerMBeanImpl() {
      this._initializeProperty(-1);
   }

   public SCAContainerMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public int getTimeout() {
      return this._Timeout;
   }

   public boolean isTimeoutSet() {
      return this._isSet(7);
   }

   public void setTimeout(int var1) {
      int var2 = this._Timeout;
      this._Timeout = var1;
      this._postSet(7, var2, var1);
   }

   public boolean isAutowire() {
      return this._Autowire;
   }

   public boolean isAutowireSet() {
      return this._isSet(8);
   }

   public void setAutowire(boolean var1) {
      boolean var2 = this._Autowire;
      this._Autowire = var1;
      this._postSet(8, var2, var1);
   }

   public boolean isAllowsPassByReference() {
      return this._AllowsPassByReference;
   }

   public boolean isAllowsPassByReferenceSet() {
      return this._isSet(9);
   }

   public void setAllowsPassByReference(boolean var1) {
      boolean var2 = this._AllowsPassByReference;
      this._AllowsPassByReference = var1;
      this._postSet(9, var2, var1);
   }

   public boolean isRemotable() {
      return this._Remotable;
   }

   public boolean isRemotableSet() {
      return this._isSet(10);
   }

   public void setRemotable(boolean var1) {
      boolean var2 = this._Remotable;
      this._Remotable = var1;
      this._postSet(10, var2, var1);
   }

   public String getMaxIdleTime() {
      return this._MaxIdleTime;
   }

   public boolean isMaxIdleTimeSet() {
      return this._isSet(11);
   }

   public void setMaxIdleTime(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MaxIdleTime;
      this._MaxIdleTime = var1;
      this._postSet(11, var2, var1);
   }

   public String getMaxAge() {
      return this._MaxAge;
   }

   public boolean isMaxAgeSet() {
      return this._isSet(12);
   }

   public void setMaxAge(String var1) {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MaxAge;
      this._MaxAge = var1;
      this._postSet(12, var2, var1);
   }

   public boolean isSinglePrincipal() {
      return this._SinglePrincipal;
   }

   public boolean isSinglePrincipalSet() {
      return this._isSet(13);
   }

   public void setSinglePrincipal(boolean var1) {
      boolean var2 = this._SinglePrincipal;
      this._SinglePrincipal = var1;
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
         var1 = 12;
      }

      try {
         switch (var1) {
            case 12:
               this._MaxAge = "";
               if (var2) {
                  break;
               }
            case 11:
               this._MaxIdleTime = "";
               if (var2) {
                  break;
               }
            case 7:
               this._Timeout = 0;
               if (var2) {
                  break;
               }
            case 9:
               this._AllowsPassByReference = false;
               if (var2) {
                  break;
               }
            case 8:
               this._Autowire = false;
               if (var2) {
                  break;
               }
            case 10:
               this._Remotable = false;
               if (var2) {
                  break;
               }
            case 13:
               this._SinglePrincipal = false;
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
      return "SCAContainer";
   }

   public void putValue(String var1, Object var2) {
      boolean var4;
      if (var1.equals("AllowsPassByReference")) {
         var4 = this._AllowsPassByReference;
         this._AllowsPassByReference = (Boolean)var2;
         this._postSet(9, var4, this._AllowsPassByReference);
      } else if (var1.equals("Autowire")) {
         var4 = this._Autowire;
         this._Autowire = (Boolean)var2;
         this._postSet(8, var4, this._Autowire);
      } else {
         String var5;
         if (var1.equals("MaxAge")) {
            var5 = this._MaxAge;
            this._MaxAge = (String)var2;
            this._postSet(12, var5, this._MaxAge);
         } else if (var1.equals("MaxIdleTime")) {
            var5 = this._MaxIdleTime;
            this._MaxIdleTime = (String)var2;
            this._postSet(11, var5, this._MaxIdleTime);
         } else if (var1.equals("Remotable")) {
            var4 = this._Remotable;
            this._Remotable = (Boolean)var2;
            this._postSet(10, var4, this._Remotable);
         } else if (var1.equals("SinglePrincipal")) {
            var4 = this._SinglePrincipal;
            this._SinglePrincipal = (Boolean)var2;
            this._postSet(13, var4, this._SinglePrincipal);
         } else if (var1.equals("Timeout")) {
            int var3 = this._Timeout;
            this._Timeout = (Integer)var2;
            this._postSet(7, var3, this._Timeout);
         } else {
            super.putValue(var1, var2);
         }
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("AllowsPassByReference")) {
         return new Boolean(this._AllowsPassByReference);
      } else if (var1.equals("Autowire")) {
         return new Boolean(this._Autowire);
      } else if (var1.equals("MaxAge")) {
         return this._MaxAge;
      } else if (var1.equals("MaxIdleTime")) {
         return this._MaxIdleTime;
      } else if (var1.equals("Remotable")) {
         return new Boolean(this._Remotable);
      } else if (var1.equals("SinglePrincipal")) {
         return new Boolean(this._SinglePrincipal);
      } else {
         return var1.equals("Timeout") ? new Integer(this._Timeout) : super.getValue(var1);
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 7:
               if (var1.equals("max-age")) {
                  return 12;
               }

               if (var1.equals("timeout")) {
                  return 7;
               }
               break;
            case 8:
               if (var1.equals("autowire")) {
                  return 8;
               }
               break;
            case 9:
               if (var1.equals("remotable")) {
                  return 10;
               }
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            default:
               break;
            case 13:
               if (var1.equals("max-idle-time")) {
                  return 11;
               }
               break;
            case 16:
               if (var1.equals("single-principal")) {
                  return 13;
               }
               break;
            case 24:
               if (var1.equals("allows-pass-by-reference")) {
                  return 9;
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
               return "timeout";
            case 8:
               return "autowire";
            case 9:
               return "allows-pass-by-reference";
            case 10:
               return "remotable";
            case 11:
               return "max-idle-time";
            case 12:
               return "max-age";
            case 13:
               return "single-principal";
            default:
               return super.getElementName(var1);
         }
      }

      public boolean isConfigurable(int var1) {
         switch (var1) {
            case 7:
               return true;
            case 8:
               return true;
            case 9:
            case 10:
            default:
               return super.isConfigurable(var1);
            case 11:
               return true;
            case 12:
               return true;
            case 13:
               return true;
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
      private SCAContainerMBeanImpl bean;

      protected Helper(SCAContainerMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Timeout";
            case 8:
               return "Autowire";
            case 9:
               return "AllowsPassByReference";
            case 10:
               return "Remotable";
            case 11:
               return "MaxIdleTime";
            case 12:
               return "MaxAge";
            case 13:
               return "SinglePrincipal";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("MaxAge")) {
            return 12;
         } else if (var1.equals("MaxIdleTime")) {
            return 11;
         } else if (var1.equals("Timeout")) {
            return 7;
         } else if (var1.equals("AllowsPassByReference")) {
            return 9;
         } else if (var1.equals("Autowire")) {
            return 8;
         } else if (var1.equals("Remotable")) {
            return 10;
         } else {
            return var1.equals("SinglePrincipal") ? 13 : super.getPropertyIndex(var1);
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
            if (this.bean.isMaxAgeSet()) {
               var2.append("MaxAge");
               var2.append(String.valueOf(this.bean.getMaxAge()));
            }

            if (this.bean.isMaxIdleTimeSet()) {
               var2.append("MaxIdleTime");
               var2.append(String.valueOf(this.bean.getMaxIdleTime()));
            }

            if (this.bean.isTimeoutSet()) {
               var2.append("Timeout");
               var2.append(String.valueOf(this.bean.getTimeout()));
            }

            if (this.bean.isAllowsPassByReferenceSet()) {
               var2.append("AllowsPassByReference");
               var2.append(String.valueOf(this.bean.isAllowsPassByReference()));
            }

            if (this.bean.isAutowireSet()) {
               var2.append("Autowire");
               var2.append(String.valueOf(this.bean.isAutowire()));
            }

            if (this.bean.isRemotableSet()) {
               var2.append("Remotable");
               var2.append(String.valueOf(this.bean.isRemotable()));
            }

            if (this.bean.isSinglePrincipalSet()) {
               var2.append("SinglePrincipal");
               var2.append(String.valueOf(this.bean.isSinglePrincipal()));
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
            SCAContainerMBeanImpl var2 = (SCAContainerMBeanImpl)var1;
            this.computeDiff("MaxAge", this.bean.getMaxAge(), var2.getMaxAge(), false);
            this.computeDiff("MaxIdleTime", this.bean.getMaxIdleTime(), var2.getMaxIdleTime(), false);
            this.computeDiff("Timeout", this.bean.getTimeout(), var2.getTimeout(), false);
            this.computeDiff("AllowsPassByReference", this.bean.isAllowsPassByReference(), var2.isAllowsPassByReference(), false);
            this.computeDiff("Autowire", this.bean.isAutowire(), var2.isAutowire(), false);
            this.computeDiff("Remotable", this.bean.isRemotable(), var2.isRemotable(), false);
            this.computeDiff("SinglePrincipal", this.bean.isSinglePrincipal(), var2.isSinglePrincipal(), false);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            SCAContainerMBeanImpl var3 = (SCAContainerMBeanImpl)var1.getSourceBean();
            SCAContainerMBeanImpl var4 = (SCAContainerMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("MaxAge")) {
                  var3.setMaxAge(var4.getMaxAge());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("MaxIdleTime")) {
                  var3.setMaxIdleTime(var4.getMaxIdleTime());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("Timeout")) {
                  var3.setTimeout(var4.getTimeout());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("AllowsPassByReference")) {
                  var3.setAllowsPassByReference(var4.isAllowsPassByReference());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("Autowire")) {
                  var3.setAutowire(var4.isAutowire());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 8);
               } else if (var5.equals("Remotable")) {
                  var3.setRemotable(var4.isRemotable());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("SinglePrincipal")) {
                  var3.setSinglePrincipal(var4.isSinglePrincipal());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
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
            SCAContainerMBeanImpl var5 = (SCAContainerMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("MaxAge")) && this.bean.isMaxAgeSet()) {
               var5.setMaxAge(this.bean.getMaxAge());
            }

            if ((var3 == null || !var3.contains("MaxIdleTime")) && this.bean.isMaxIdleTimeSet()) {
               var5.setMaxIdleTime(this.bean.getMaxIdleTime());
            }

            if ((var3 == null || !var3.contains("Timeout")) && this.bean.isTimeoutSet()) {
               var5.setTimeout(this.bean.getTimeout());
            }

            if ((var3 == null || !var3.contains("AllowsPassByReference")) && this.bean.isAllowsPassByReferenceSet()) {
               var5.setAllowsPassByReference(this.bean.isAllowsPassByReference());
            }

            if ((var3 == null || !var3.contains("Autowire")) && this.bean.isAutowireSet()) {
               var5.setAutowire(this.bean.isAutowire());
            }

            if ((var3 == null || !var3.contains("Remotable")) && this.bean.isRemotableSet()) {
               var5.setRemotable(this.bean.isRemotable());
            }

            if ((var3 == null || !var3.contains("SinglePrincipal")) && this.bean.isSinglePrincipalSet()) {
               var5.setSinglePrincipal(this.bean.isSinglePrincipal());
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
