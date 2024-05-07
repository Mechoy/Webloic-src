package weblogic.management.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorValidateException;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBeanHelper;
import weblogic.descriptor.internal.Munger;
import weblogic.descriptor.internal.SchemaHelper;
import weblogic.utils.collections.CombinedIterator;

public class WTCtBridgeRedirectMBeanImpl extends ConfigurationMBeanImpl implements WTCtBridgeRedirectMBean, Serializable {
   private String _Direction;
   private String _MetaDataFile;
   private String _ReplyQ;
   private String _SourceAccessPoint;
   private String _SourceName;
   private String _SourceQspace;
   private String _TargetAccessPoint;
   private String _TargetName;
   private String _TargetQspace;
   private String _TranslateFML;
   private static SchemaHelper2 _schemaHelper;

   public WTCtBridgeRedirectMBeanImpl() {
      this._initializeProperty(-1);
   }

   public WTCtBridgeRedirectMBeanImpl(DescriptorBean var1, int var2) {
      super(var1, var2);
      this._initializeProperty(-1);
   }

   public void setDirection(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"JmsQ2TuxQ", "TuxQ2JmsQ", "JmsQ2TuxS", "JmsQ2JmsQ"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("Direction", var1, var2);
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("Direction", var1);
      String var3 = this._Direction;
      this._Direction = var1;
      this._postSet(7, var3, var1);
   }

   public String getDirection() {
      return this._Direction;
   }

   public boolean isDirectionSet() {
      return this._isSet(7);
   }

   public void setTranslateFML(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String[] var2 = new String[]{"NO", "FLAT", "WLXT"};
      var1 = weblogic.descriptor.beangen.LegalChecks.checkInEnum("TranslateFML", var1, var2);
      String var3 = this._TranslateFML;
      this._TranslateFML = var1;
      this._postSet(8, var3, var1);
   }

   public String getTranslateFML() {
      return this._TranslateFML;
   }

   public boolean isTranslateFMLSet() {
      return this._isSet(8);
   }

   public void setMetaDataFile(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._MetaDataFile;
      this._MetaDataFile = var1;
      this._postSet(9, var2, var1);
   }

   public String getMetaDataFile() {
      return this._MetaDataFile;
   }

   public boolean isMetaDataFileSet() {
      return this._isSet(9);
   }

   public void setReplyQ(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._ReplyQ;
      this._ReplyQ = var1;
      this._postSet(10, var2, var1);
   }

   public String getReplyQ() {
      return this._ReplyQ;
   }

   public boolean isReplyQSet() {
      return this._isSet(10);
   }

   public void setSourceAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SourceAccessPoint;
      this._SourceAccessPoint = var1;
      this._postSet(11, var2, var1);
   }

   public String getSourceAccessPoint() {
      return this._SourceAccessPoint;
   }

   public boolean isSourceAccessPointSet() {
      return this._isSet(11);
   }

   public void setSourceQspace(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._SourceQspace;
      this._SourceQspace = var1;
      this._postSet(12, var2, var1);
   }

   public String getSourceQspace() {
      return this._SourceQspace;
   }

   public boolean isSourceQspaceSet() {
      return this._isSet(12);
   }

   public void setSourceName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("SourceName", var1);
      String var2 = this._SourceName;
      this._SourceName = var1;
      this._postSet(13, var2, var1);
   }

   public String getSourceName() {
      return this._SourceName;
   }

   public boolean isSourceNameSet() {
      return this._isSet(13);
   }

   public void setTargetAccessPoint(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TargetAccessPoint;
      this._TargetAccessPoint = var1;
      this._postSet(14, var2, var1);
   }

   public String getTargetAccessPoint() {
      return this._TargetAccessPoint;
   }

   public boolean isTargetAccessPointSet() {
      return this._isSet(14);
   }

   public void setTargetQspace(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      String var2 = this._TargetQspace;
      this._TargetQspace = var1;
      this._postSet(15, var2, var1);
   }

   public String getTargetQspace() {
      return this._TargetQspace;
   }

   public boolean isTargetQspaceSet() {
      return this._isSet(15);
   }

   public void setTargetName(String var1) throws InvalidAttributeValueException {
      var1 = var1 == null ? null : var1.trim();
      weblogic.descriptor.beangen.LegalChecks.checkNonNull("TargetName", var1);
      String var2 = this._TargetName;
      this._TargetName = var1;
      this._postSet(16, var2, var1);
   }

   public String getTargetName() {
      return this._TargetName;
   }

   public boolean isTargetNameSet() {
      return this._isSet(16);
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
         var1 = 7;
      }

      try {
         switch (var1) {
            case 7:
               this._Direction = "JmsQ2TuxQ";
               if (var2) {
                  break;
               }
            case 9:
               this._MetaDataFile = null;
               if (var2) {
                  break;
               }
            case 10:
               this._ReplyQ = null;
               if (var2) {
                  break;
               }
            case 11:
               this._SourceAccessPoint = null;
               if (var2) {
                  break;
               }
            case 13:
               this._SourceName = "mySource";
               if (var2) {
                  break;
               }
            case 12:
               this._SourceQspace = null;
               if (var2) {
                  break;
               }
            case 14:
               this._TargetAccessPoint = null;
               if (var2) {
                  break;
               }
            case 16:
               this._TargetName = "myTarget";
               if (var2) {
                  break;
               }
            case 15:
               this._TargetQspace = null;
               if (var2) {
                  break;
               }
            case 8:
               this._TranslateFML = "NO";
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
      return "WTCtBridgeRedirect";
   }

   public void putValue(String var1, Object var2) {
      String var3;
      if (var1.equals("Direction")) {
         var3 = this._Direction;
         this._Direction = (String)var2;
         this._postSet(7, var3, this._Direction);
      } else if (var1.equals("MetaDataFile")) {
         var3 = this._MetaDataFile;
         this._MetaDataFile = (String)var2;
         this._postSet(9, var3, this._MetaDataFile);
      } else if (var1.equals("ReplyQ")) {
         var3 = this._ReplyQ;
         this._ReplyQ = (String)var2;
         this._postSet(10, var3, this._ReplyQ);
      } else if (var1.equals("SourceAccessPoint")) {
         var3 = this._SourceAccessPoint;
         this._SourceAccessPoint = (String)var2;
         this._postSet(11, var3, this._SourceAccessPoint);
      } else if (var1.equals("SourceName")) {
         var3 = this._SourceName;
         this._SourceName = (String)var2;
         this._postSet(13, var3, this._SourceName);
      } else if (var1.equals("SourceQspace")) {
         var3 = this._SourceQspace;
         this._SourceQspace = (String)var2;
         this._postSet(12, var3, this._SourceQspace);
      } else if (var1.equals("TargetAccessPoint")) {
         var3 = this._TargetAccessPoint;
         this._TargetAccessPoint = (String)var2;
         this._postSet(14, var3, this._TargetAccessPoint);
      } else if (var1.equals("TargetName")) {
         var3 = this._TargetName;
         this._TargetName = (String)var2;
         this._postSet(16, var3, this._TargetName);
      } else if (var1.equals("TargetQspace")) {
         var3 = this._TargetQspace;
         this._TargetQspace = (String)var2;
         this._postSet(15, var3, this._TargetQspace);
      } else if (var1.equals("TranslateFML")) {
         var3 = this._TranslateFML;
         this._TranslateFML = (String)var2;
         this._postSet(8, var3, this._TranslateFML);
      } else {
         super.putValue(var1, var2);
      }
   }

   public Object getValue(String var1) {
      if (var1.equals("Direction")) {
         return this._Direction;
      } else if (var1.equals("MetaDataFile")) {
         return this._MetaDataFile;
      } else if (var1.equals("ReplyQ")) {
         return this._ReplyQ;
      } else if (var1.equals("SourceAccessPoint")) {
         return this._SourceAccessPoint;
      } else if (var1.equals("SourceName")) {
         return this._SourceName;
      } else if (var1.equals("SourceQspace")) {
         return this._SourceQspace;
      } else if (var1.equals("TargetAccessPoint")) {
         return this._TargetAccessPoint;
      } else if (var1.equals("TargetName")) {
         return this._TargetName;
      } else if (var1.equals("TargetQspace")) {
         return this._TargetQspace;
      } else {
         return var1.equals("TranslateFML") ? this._TranslateFML : super.getValue(var1);
      }
   }

   public static void validateGeneration() {
      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("Direction", "JmsQ2TuxQ");
      } catch (IllegalArgumentException var3) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property Direction in WTCtBridgeRedirectMBean" + var3.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("SourceName", "mySource");
      } catch (IllegalArgumentException var2) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property SourceName in WTCtBridgeRedirectMBean" + var2.getMessage());
      }

      try {
         weblogic.descriptor.beangen.LegalChecks.checkNonNull("TargetName", "myTarget");
      } catch (IllegalArgumentException var1) {
         throw new DescriptorValidateException("The default value for the property  is null. Properties annotated with false value on @legalZeroLength or @legalNull  should either have @required/@derivedDefault annotations or have a non-null value on @default annotation. Refer annotation legalNull on property TargetName in WTCtBridgeRedirectMBean" + var1.getMessage());
      }
   }

   public static class SchemaHelper2 extends ConfigurationMBeanImpl.SchemaHelper2 implements SchemaHelper {
      public int getPropertyIndex(String var1) {
         switch (var1.length()) {
            case 6:
               if (var1.equals("replyq")) {
                  return 10;
               }
            case 7:
            case 8:
            case 10:
            case 15:
            case 16:
            case 17:
            case 18:
            default:
               break;
            case 9:
               if (var1.equals("direction")) {
                  return 7;
               }
               break;
            case 11:
               if (var1.equals("source-name")) {
                  return 13;
               }

               if (var1.equals("target-name")) {
                  return 16;
               }
               break;
            case 12:
               if (var1.equals("translatefml")) {
                  return 8;
               }
               break;
            case 13:
               if (var1.equals("source-qspace")) {
                  return 12;
               }

               if (var1.equals("target-qspace")) {
                  return 15;
               }
               break;
            case 14:
               if (var1.equals("meta-data-file")) {
                  return 9;
               }
               break;
            case 19:
               if (var1.equals("source-access-point")) {
                  return 11;
               }

               if (var1.equals("target-access-point")) {
                  return 14;
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
               return "direction";
            case 8:
               return "translatefml";
            case 9:
               return "meta-data-file";
            case 10:
               return "replyq";
            case 11:
               return "source-access-point";
            case 12:
               return "source-qspace";
            case 13:
               return "source-name";
            case 14:
               return "target-access-point";
            case 15:
               return "target-qspace";
            case 16:
               return "target-name";
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
      private WTCtBridgeRedirectMBeanImpl bean;

      protected Helper(WTCtBridgeRedirectMBeanImpl var1) {
         super(var1);
         this.bean = var1;
      }

      public String getPropertyName(int var1) {
         switch (var1) {
            case 7:
               return "Direction";
            case 8:
               return "TranslateFML";
            case 9:
               return "MetaDataFile";
            case 10:
               return "ReplyQ";
            case 11:
               return "SourceAccessPoint";
            case 12:
               return "SourceQspace";
            case 13:
               return "SourceName";
            case 14:
               return "TargetAccessPoint";
            case 15:
               return "TargetQspace";
            case 16:
               return "TargetName";
            default:
               return super.getPropertyName(var1);
         }
      }

      public int getPropertyIndex(String var1) {
         if (var1.equals("Direction")) {
            return 7;
         } else if (var1.equals("MetaDataFile")) {
            return 9;
         } else if (var1.equals("ReplyQ")) {
            return 10;
         } else if (var1.equals("SourceAccessPoint")) {
            return 11;
         } else if (var1.equals("SourceName")) {
            return 13;
         } else if (var1.equals("SourceQspace")) {
            return 12;
         } else if (var1.equals("TargetAccessPoint")) {
            return 14;
         } else if (var1.equals("TargetName")) {
            return 16;
         } else if (var1.equals("TargetQspace")) {
            return 15;
         } else {
            return var1.equals("TranslateFML") ? 8 : super.getPropertyIndex(var1);
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
            if (this.bean.isDirectionSet()) {
               var2.append("Direction");
               var2.append(String.valueOf(this.bean.getDirection()));
            }

            if (this.bean.isMetaDataFileSet()) {
               var2.append("MetaDataFile");
               var2.append(String.valueOf(this.bean.getMetaDataFile()));
            }

            if (this.bean.isReplyQSet()) {
               var2.append("ReplyQ");
               var2.append(String.valueOf(this.bean.getReplyQ()));
            }

            if (this.bean.isSourceAccessPointSet()) {
               var2.append("SourceAccessPoint");
               var2.append(String.valueOf(this.bean.getSourceAccessPoint()));
            }

            if (this.bean.isSourceNameSet()) {
               var2.append("SourceName");
               var2.append(String.valueOf(this.bean.getSourceName()));
            }

            if (this.bean.isSourceQspaceSet()) {
               var2.append("SourceQspace");
               var2.append(String.valueOf(this.bean.getSourceQspace()));
            }

            if (this.bean.isTargetAccessPointSet()) {
               var2.append("TargetAccessPoint");
               var2.append(String.valueOf(this.bean.getTargetAccessPoint()));
            }

            if (this.bean.isTargetNameSet()) {
               var2.append("TargetName");
               var2.append(String.valueOf(this.bean.getTargetName()));
            }

            if (this.bean.isTargetQspaceSet()) {
               var2.append("TargetQspace");
               var2.append(String.valueOf(this.bean.getTargetQspace()));
            }

            if (this.bean.isTranslateFMLSet()) {
               var2.append("TranslateFML");
               var2.append(String.valueOf(this.bean.getTranslateFML()));
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
            WTCtBridgeRedirectMBeanImpl var2 = (WTCtBridgeRedirectMBeanImpl)var1;
            this.computeDiff("Direction", this.bean.getDirection(), var2.getDirection(), true);
            this.computeDiff("MetaDataFile", this.bean.getMetaDataFile(), var2.getMetaDataFile(), true);
            this.computeDiff("ReplyQ", this.bean.getReplyQ(), var2.getReplyQ(), true);
            this.computeDiff("SourceAccessPoint", this.bean.getSourceAccessPoint(), var2.getSourceAccessPoint(), true);
            this.computeDiff("SourceName", this.bean.getSourceName(), var2.getSourceName(), true);
            this.computeDiff("SourceQspace", this.bean.getSourceQspace(), var2.getSourceQspace(), true);
            this.computeDiff("TargetAccessPoint", this.bean.getTargetAccessPoint(), var2.getTargetAccessPoint(), true);
            this.computeDiff("TargetName", this.bean.getTargetName(), var2.getTargetName(), true);
            this.computeDiff("TargetQspace", this.bean.getTargetQspace(), var2.getTargetQspace(), true);
            this.computeDiff("TranslateFML", this.bean.getTranslateFML(), var2.getTranslateFML(), true);
         } catch (Exception var3) {
            throw (Error)(new AssertionError("Impossible Exception")).initCause(var3);
         }
      }

      protected void applyPropertyUpdate(BeanUpdateEvent var1, BeanUpdateEvent.PropertyUpdate var2) {
         try {
            WTCtBridgeRedirectMBeanImpl var3 = (WTCtBridgeRedirectMBeanImpl)var1.getSourceBean();
            WTCtBridgeRedirectMBeanImpl var4 = (WTCtBridgeRedirectMBeanImpl)var1.getProposedBean();
            String var5 = var2.getPropertyName();
            int var6 = var2.getUpdateType();
            if (!var2.isDerivedUpdate()) {
               if (var5.equals("Direction")) {
                  var3.setDirection(var4.getDirection());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 7);
               } else if (var5.equals("MetaDataFile")) {
                  var3.setMetaDataFile(var4.getMetaDataFile());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 9);
               } else if (var5.equals("ReplyQ")) {
                  var3.setReplyQ(var4.getReplyQ());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 10);
               } else if (var5.equals("SourceAccessPoint")) {
                  var3.setSourceAccessPoint(var4.getSourceAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 11);
               } else if (var5.equals("SourceName")) {
                  var3.setSourceName(var4.getSourceName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 13);
               } else if (var5.equals("SourceQspace")) {
                  var3.setSourceQspace(var4.getSourceQspace());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 12);
               } else if (var5.equals("TargetAccessPoint")) {
                  var3.setTargetAccessPoint(var4.getTargetAccessPoint());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 14);
               } else if (var5.equals("TargetName")) {
                  var3.setTargetName(var4.getTargetName());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 16);
               } else if (var5.equals("TargetQspace")) {
                  var3.setTargetQspace(var4.getTargetQspace());
                  var3._conditionalUnset(var2.isUnsetUpdate(), 15);
               } else if (var5.equals("TranslateFML")) {
                  var3.setTranslateFML(var4.getTranslateFML());
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
            WTCtBridgeRedirectMBeanImpl var5 = (WTCtBridgeRedirectMBeanImpl)var1;
            super.finishCopy(var5, var2, var3);
            if ((var3 == null || !var3.contains("Direction")) && this.bean.isDirectionSet()) {
               var5.setDirection(this.bean.getDirection());
            }

            if ((var3 == null || !var3.contains("MetaDataFile")) && this.bean.isMetaDataFileSet()) {
               var5.setMetaDataFile(this.bean.getMetaDataFile());
            }

            if ((var3 == null || !var3.contains("ReplyQ")) && this.bean.isReplyQSet()) {
               var5.setReplyQ(this.bean.getReplyQ());
            }

            if ((var3 == null || !var3.contains("SourceAccessPoint")) && this.bean.isSourceAccessPointSet()) {
               var5.setSourceAccessPoint(this.bean.getSourceAccessPoint());
            }

            if ((var3 == null || !var3.contains("SourceName")) && this.bean.isSourceNameSet()) {
               var5.setSourceName(this.bean.getSourceName());
            }

            if ((var3 == null || !var3.contains("SourceQspace")) && this.bean.isSourceQspaceSet()) {
               var5.setSourceQspace(this.bean.getSourceQspace());
            }

            if ((var3 == null || !var3.contains("TargetAccessPoint")) && this.bean.isTargetAccessPointSet()) {
               var5.setTargetAccessPoint(this.bean.getTargetAccessPoint());
            }

            if ((var3 == null || !var3.contains("TargetName")) && this.bean.isTargetNameSet()) {
               var5.setTargetName(this.bean.getTargetName());
            }

            if ((var3 == null || !var3.contains("TargetQspace")) && this.bean.isTargetQspaceSet()) {
               var5.setTargetQspace(this.bean.getTargetQspace());
            }

            if ((var3 == null || !var3.contains("TranslateFML")) && this.bean.isTranslateFMLSet()) {
               var5.setTranslateFML(this.bean.getTranslateFML());
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
