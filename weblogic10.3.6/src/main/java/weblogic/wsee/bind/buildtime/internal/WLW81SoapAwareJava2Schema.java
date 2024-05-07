package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.ArrayNamespaceInfo;
import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.SimpleBindingType;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.facade.Java2SchemaContext;
import com.bea.staxb.buildtime.internal.facade.TypegenFacade;
import com.bea.staxb.buildtime.internal.logger.BindingLogger;
import com.bea.util.jam.JClass;
import com.bea.xbean.xb.xsdschema.Attribute;
import com.bea.xbean.xb.xsdschema.ComplexContentDocument;
import com.bea.xbean.xb.xsdschema.ComplexRestrictionType;
import com.bea.xbean.xb.xsdschema.ExplicitGroup;
import com.bea.xbean.xb.xsdschema.ExtensionType;
import com.bea.xbean.xb.xsdschema.LocalElement;
import com.bea.xbean.xb.xsdschema.NumFacet;
import com.bea.xbean.xb.xsdschema.RestrictionDocument;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xbean.xb.xsdschema.TopLevelComplexType;
import com.bea.xbean.xb.xsdschema.TopLevelElement;
import com.bea.xbean.xb.xsdschema.TopLevelSimpleType;
import com.bea.xml.XmlAnySimpleType;
import com.bea.xml.XmlAnySimpleType.Factory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;

class WLW81SoapAwareJava2Schema extends SoapAwareJava2Schema {
   private static final String XSD_NS = "http://www.w3.org/2001/XMLSchema";
   private String mTargetNamespaceForComplexTypes;
   private boolean mIsRpcEncoded;
   private Map<Class, BindingType> mSpecialClasses = new HashMap();
   private Java2SchemaContext mWLW81FacadeContext = new Java2SchemaContext() {
      public BindingLogger getLogger() {
         return WLW81SoapAwareJava2Schema.this;
      }

      public BindingLoader getBindingLoader() {
         return WLW81SoapAwareJava2Schema.this.getBindingLoader();
      }

      public BindingType findOrCreateBindingTypeFor(JClass var1) {
         if (var1.isArrayType()) {
            ArrayNamespaceInfo var2 = new ArrayNamespaceInfo((JClass)null, var1, WLW81SoapAwareJava2Schema.this.mTargetNamespaceForComplexTypes);
            if (WLW81SoapAwareJava2Schema.this.mIsRpcEncoded) {
               WLW81SoapAwareJava2Schema.this.findOrCreateElementNameSetFor(WLW81SoapAwareJava2Schema.this.mTargetNamespaceForComplexTypes, (JClass)null, var1);
               return WLW81SoapAwareJava2Schema.this.generateSoapArray(WLW81SoapAwareJava2Schema.this.mTns2Schemadoc, var2, WLW81SoapAwareJava2Schema.this.mBindingFile, WLW81SoapAwareJava2Schema.this.mGeneratedSoapArrayJavaTypeNames);
            } else {
               WLW81SoapAwareJava2Schema.this.findOrCreateLiteralElementNameSetFor(WLW81SoapAwareJava2Schema.this.mTargetNamespaceForComplexTypes, (JClass)null, var1);
               return WLW81SoapAwareJava2Schema.this.generateLiteralArray(var2, WLW81SoapAwareJava2Schema.this.mGeneratedArrayJavaTypeNames);
            }
         } else {
            return WLW81SoapAwareJava2Schema.this.findOrCreateBindingTypeFor(var1);
         }
      }

      public void checkNsForImport(String var1, String var2) {
         WLW81SoapAwareJava2Schema.this.checkNsForImport(var1, var2);
      }

      public boolean isElementFormDefaultQualified() {
         return WLW81SoapAwareJava2Schema.this.isElementFormDefaultQualified();
      }
   };

   public WLW81SoapAwareJava2Schema(J2SBindingsBuilder var1, boolean var2, String var3) {
      super(var1);
      this.mTargetNamespaceForComplexTypes = var3;
      this.mIsRpcEncoded = var2;
      this.mLiteralArrayConstituentElementNameBySchemaArrayElement = false;
      this.allowNullByMinOccursZeroInWrapperElements = true;
   }

   protected TypegenFacade createTypegenFacade(TopLevelComplexType var1, ExtensionType var2, String var3, ByNameBean var4) {
      return new WLW81TypegenFacade(this.mWLW81FacadeContext, var1, var2, var3, var4);
   }

   public void addClassToBind(JClass var1) {
      if (var1.getContainingClass() == null) {
         super.addClassToBind(var1);
      } else {
         this.assertCompilationStarted(false);
         if (this.mClassesToBind.contains(var1)) {
            return;
         }

         this.assertResolved(var1);
         if (!isValidInputType(var1)) {
            this.logError("Invalid input java type '" + var1.getQualifiedName() + "'");
            return;
         }

         this.mClassesToBind.add(var1);
      }

   }

   private BindingType findOrCreateBindingTypeForSpecialClasses(JClass var1) {
      Class var2 = null;
      if (Character.class.getName().equals(var1.getQualifiedName())) {
         var2 = Character.class;
      } else if ("char".equals(var1.getQualifiedName())) {
         var2 = Character.TYPE;
      }

      if (var2 == null) {
         return null;
      } else {
         BindingType var3 = (BindingType)this.mSpecialClasses.get(var2);
         if (var3 != null) {
            return var3;
         } else {
            JavaTypeName var4 = JavaTypeName.forString(var2.getName());
            QName var5 = new QName(this.mTargetNamespaceForComplexTypes, var2.getSimpleName());
            XmlTypeName var6 = XmlTypeName.forTypeNamed(var5);
            BindingTypeName var7 = BindingTypeName.forPair(var4, var6);
            SimpleBindingType var8 = new SimpleBindingType(var7);
            QName var9 = new QName("http://www.w3.org/2001/XMLSchema", "string");
            XmlTypeName var10 = XmlTypeName.forTypeNamed(var9);
            var8.setAsIfXmlType(var10);
            this.mBindingFile.addBindingType(var8, true, true);
            this.mSpecialClasses.put(var2, var8);
            return var8;
         }
      }
   }

   protected String getDefaultNamespaceForJClass(JClass var1) {
      return this.mTargetNamespaceForComplexTypes;
   }

   protected String getDefaultLocalNameFor(JClass var1) {
      String var2 = var1.getSimpleName();
      int var3 = var2.lastIndexOf(36);
      if (var3 >= 0) {
         var2 = var2.substring(var3 + 1);
      }

      return makeNcNameSafe(var2);
   }

   protected QName getLiteralArrayTypeName(ArrayNamespaceInfo var1, String var2) {
      JClass var3 = var1.getArrayClass();
      int var4 = var3.getArrayDimensions();
      JClass var5 = var3.getArrayComponentType();
      String var6 = var1.getNamespace();
      QName var7;
      if (var4 >= 2 && var5.isPrimitiveType() && var5.getQualifiedName().equals("byte")) {
         StringBuffer var11 = new StringBuffer();

         for(int var12 = 1; var12 < var4; ++var12) {
            var11.append("ArrayOf");
         }

         var7 = new QName(var6, var11.toString() + "Binary");
      } else {
         String var8 = this.arrayComponentNameForJClass(var5, var2);
         StringBuffer var9 = new StringBuffer();

         for(int var10 = 0; var10 < var4; ++var10) {
            var9.append("ArrayOf");
         }

         var7 = new QName(var6, var9.toString() + var8);
      }

      return var7;
   }

   protected String arrayComponentNameForJClass(JClass var1, String var2) {
      String var3 = var1.getQualifiedName();
      if (var3.equals("java.lang.Integer")) {
         return "Int";
      } else if (var3.equals("java.math.BigDecimal")) {
         return "Decimal";
      } else if (var3.equals("java.math.BigInteger")) {
         return "Integer";
      } else {
         return var3.equals("java.util.Calendar") ? "Date" : this.getDefaultLocalNameFor(var1);
      }
   }

   protected BindingType findOrCreateBindingTypeFor(JClass var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null clazz");
      } else {
         BindingType var2 = this.findOrCreateBindingTypeForSpecialClasses(var1);
         return var2 != null ? var2 : super.findOrCreateBindingTypeFor(var1);
      }
   }

   protected BindingType generateSoapArray(Map var1, ArrayNamespaceInfo var2, BindingFile var3, Collection var4) {
      if (this.typeIs1DPrimitiveByteArray(var2.getArrayClass())) {
         XmlTypeName var45 = XmlTypeName.forTypeNamed(XS_BASE_64_BINARY);
         JavaTypeName var46 = JavaTypeName.forJClass(var2.getArrayClass());
         BindingTypeName var47 = BindingTypeName.forPair(var46, var45);
         return this.mBindingFile.getBindingType(var47);
      } else {
         Map var5 = this.mGeneratedArrayJavaTypeNames;
         JClass var7 = var2.getArrayClass();
         JavaTypeName var8 = JavaTypeName.forJClass(var7);
         JClass var9 = var7.getArrayComponentType();
         JClass var10 = var9;
         int var11 = var7.getArrayDimensions();
         if (var11 > 1) {
            String var12 = var9.getQualifiedName();

            for(int var13 = 0; var13 < var11 - 1; ++var13) {
               var12 = var12 + "[]";
            }

            var10 = var9.forName(var12);
            ArrayNamespaceInfo var49 = new ArrayNamespaceInfo(var2.getServiceClass(), var10, var2.getNamespace());
            this.generateSoapArray(var1, var49, var3, (Collection)null);
         }

         boolean var48 = var9.isPrimitiveType();
         JavaTypeName var50 = JavaTypeName.forJClass(var9);
         BindingTypeName var14 = null;
         if (!var9.getQualifiedName().equals("char") && !var9.getQualifiedName().equals(Character.class.getName())) {
            var14 = this.getBindingLoader().lookupTypeFor(var50);
         } else {
            var14 = this.mBindingFile.lookupTypeFor(var50);
         }

         if (var14 == null) {
            throw new IllegalArgumentException("could not find binding type for literal array element class type: " + var9);
         } else {
            QName var15 = var14.getXmlName().getQName();
            String var16 = this.arrayComponentNameForJClass(var9, var15.getLocalPart());
            QName[] var17 = this.getElementNamesForSoapArrayClass(var2, var16);
            JavaTypeName var18 = JavaTypeName.forJClass(var10);
            BindingTypeName var19 = null;
            if (!var9.getQualifiedName().equals("char") && !var9.getQualifiedName().equals(Character.class.getName())) {
               var19 = this.getBindingLoader().lookupTypeFor(var18);
            } else {
               var19 = this.mBindingFile.lookupTypeFor(var18);
            }

            if (var19 == null) {
               throw new IllegalArgumentException("could not find binding type for literal array constituent class type: " + var10);
            } else {
               QName var20 = var19.getXmlName().getQName();
               QName var21 = this.getLiteralArrayTypeName(var2, var15.getLocalPart());
               String var22 = null;
               if (var11 <= 1) {
                  var22 = this.arrayComponentNameForJClass(var9, var15.getLocalPart());
               } else {
                  var22 = var20.getLocalPart();
               }

               Object var6;
               if (!var5.keySet().contains(var8)) {
                  var5.put(var8, var21.getNamespaceURI());
                  SchemaDocument.Schema var23 = getSchemaFor(var21.getNamespaceURI(), var1);
                  TopLevelComplexType var24 = var23.addNewComplexType();
                  var24.setName(var21.getLocalPart());
                  ComplexContentDocument.ComplexContent var25 = var24.addNewComplexContent();
                  ComplexRestrictionType var26 = var25.addNewRestriction();
                  var26.setBase(SOAPENC_ARRAY);
                  ExplicitGroup var27 = var26.addNewSequence();
                  LocalElement var28 = var27.addNewElement();
                  var28.setName(var22);
                  var28.setType(var20);
                  var28.setMaxOccurs("unbounded");
                  if (var48) {
                     var28.setNillable(false);
                  } else {
                     var28.setNillable(true);
                  }

                  Attribute var29 = var26.addNewAttribute();
                  var29.setRef(SOAPENC_ARRAYTYPE);
                  String var30 = "type";
                  Element var31 = (Element)var28.getDomNode();
                  String var32 = var31.getAttribute(var30);
                  Element var33 = (Element)var29.getDomNode();
                  String var34 = "http://schemas.xmlsoap.org/wsdl/";
                  String var35 = "arrayType";
                  StringBuffer var36 = new StringBuffer();
                  var36.append("[]");
                  var33.setAttributeNS(var34, var35, var32 + var36.toString());
                  String var37 = var20.getNamespaceURI();
                  String var38 = var32.substring(0, var32.indexOf(":"));
                  var33.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + var38, var37);
                  XmlTypeName var39 = XmlTypeName.forTypeNamed(var21);
                  BindingTypeName var40 = BindingTypeName.forPair(var8, var39);
                  SoapArrayType var41 = new SoapArrayType(var40);
                  var41.setRanks(var11);
                  if (var48) {
                     var41.setItemNillable(false);
                  } else {
                     var41.setItemNillable(true);
                  }

                  var41.setItemName(var15);
                  var41.setItemType(var14);
                  var3.addBindingType(var41, true, true);
                  var6 = var41;
                  this.checkNsForImport(var21.getNamespaceURI(), var15.getNamespaceURI());

                  for(int var54 = 0; var54 < var17.length; ++var54) {
                     XmlTypeName var55 = XmlTypeName.forGlobalName('e', var17[var54]);
                     BindingTypeName var56 = BindingTypeName.forPair(var8, var55);
                     if (this.getBindingLoader().getBindingType(var56) != null) {
                        SchemaDocument.Schema var42 = getSchemaFor(var17[var54].getNamespaceURI(), var1);
                        TopLevelElement var43 = var42.addNewElement();
                        var43.setName(var17[var54].getLocalPart());
                        var43.setType(var21);
                        this.checkNsForImport(var17[var54].getNamespaceURI(), var21.getNamespaceURI());
                        SimpleDocumentBinding var44 = new SimpleDocumentBinding(var56);
                        var44.setTypeOfElement(XmlTypeName.forTypeNamed(var21));
                        var3.addBindingType(var44, true, true);
                     }
                  }
               } else {
                  String var51 = (String)var5.get(var8);
                  if (!var51.equals(var21.getNamespaceURI())) {
                     var21 = new QName(var51, var21.getLocalPart(), var21.getPrefix());
                  }

                  XmlTypeName var52 = XmlTypeName.forTypeNamed(var21);
                  BindingTypeName var53 = BindingTypeName.forPair(var8, var52);
                  var6 = this.mBindingFile.getBindingType(var53);
               }

               return (BindingType)var6;
            }
         }
      }
   }

   protected void postProcessOutputs(Map var1, BindingFile var2) {
      super.postProcessOutputs(var1, var2);
      SchemaDocument.Schema var3 = this.findOrCreateSchema(this.mTargetNamespaceForComplexTypes);
      Iterator var4 = this.mSpecialClasses.keySet().iterator();

      while(var4.hasNext()) {
         Class var5 = (Class)var4.next();
         this.createStringBasedSimpleType(var3, var5.getSimpleName());
      }

   }

   private TopLevelSimpleType createStringBasedSimpleType(SchemaDocument.Schema var1, String var2) {
      TopLevelSimpleType[] var3 = var1.getSimpleTypeArray();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3.equals(var3[var4].getName())) {
            this.logWarning("Simple type {" + var1.getTargetNamespace() + "}" + var2 + " already existed! " + var2 + " may not be encoded/decoded correctly at runtime!");
            return var3[var4];
         }
      }

      TopLevelComplexType[] var9 = var1.getComplexTypeArray();

      for(int var5 = 0; var5 < var9.length; ++var5) {
         if (var9.equals(var9[var5].getName())) {
            this.logError("Complex type {" + var1.getTargetNamespace() + "}" + var2 + " already existed!" + var2 + " may not be encoded/decoded correctly at runtime!");
         }
      }

      TopLevelSimpleType var10 = var1.addNewSimpleType();
      var10.setName(var2);
      RestrictionDocument.Restriction var6 = var10.addNewRestriction();
      var6.setBase(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      NumFacet var7 = var6.addNewLength();
      XmlAnySimpleType var8 = Factory.newInstance();
      var8.setStringValue("1");
      var7.setValue(var8);
      return var10;
   }
}
