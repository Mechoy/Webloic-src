package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.BindingCompiler;
import com.bea.staxb.buildtime.Java2Schema;
import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.MethodName;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.bts.SimpleBindingType;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.WrappedArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaLocalAttribute;
import com.bea.xml.SchemaParticle;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlObject;
import com.bea.xml.soap.SOAPArrayType;
import com.bea.xml.soap.SchemaWSDLArrayType;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.namespace.QName;
import weblogic.jws.Callback;
import weblogic.jws.wlw.WLW81CallbackJWS;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.XBeanUtil;
import weblogic.wsee.wsdl.WsdlBinding;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlMethod;
import weblogic.wsee.wsdl.WsdlOperation;
import weblogic.wsee.wsdl.WsdlParameter;
import weblogic.wsee.wsdl.WsdlPart;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlPortType;
import weblogic.wsee.wsdl.WsdlService;
import weblogic.wsee.wsdl.builder.WsdlMessageBuilder;
import weblogic.wsee.wsdl.builder.WsdlMethodBuilder;
import weblogic.wsee.wsdl.builder.WsdlOperationBuilder;
import weblogic.wsee.wsdl.builder.WsdlPartBuilder;
import weblogic.wsee.wsdl.builder.WsdlPortTypeBuilder;
import weblogic.wsee.wsdl.builder.WsdlServiceBuilder;
import weblogic.wsee.wsdl.internal.WsdlMethodImpl;
import weblogic.wsee.wsdl.internal.WsdlParameterImpl;

public class WLW81SchemaAndJavaBinder extends BindingCompiler {
   private SchemaTypeSystem sts;
   private BindingLoader bindingLoader;
   private BindingFile bindingFile = new BindingFile();
   private JClass serviceJClass;
   private boolean serviceJClassIsCallbackJws = false;
   private WsdlServiceBuilder _wsdlService;
   private WLW81JavaNamePicker propertyNamePicker = new WLW81JavaNamePicker(false);
   private WLW81JavaNamePicker getterSetterNamePicker = new WLW81JavaNamePicker(true);
   private HashMap<String, JMethod> methodMap = new HashMap();
   private HashMap<String, JMethod> callbackMethodMap = new HashMap();
   private boolean flipCallbackInputAndOutputParts = true;
   static HashSet<String> soapEncodingSimpleElements = new HashSet();
   private static final String COLLECTION_ELEMENT_NAME = "item";
   public static final QName XS_ANYTYPE;
   private Map nameListFromWrapperElement = new HashMap();
   private static final QName arrayType;

   public WLW81SchemaAndJavaBinder(SchemaTypeSystem var1) {
      this.setSchemaTypeSystem(var1);
   }

   WLW81SchemaAndJavaBinder() {
   }

   public void setFlipCallbackInputAndOutputParts(boolean var1) {
      this.flipCallbackInputAndOutputParts = var1;
   }

   public void setServiceJClass(JClass var1) {
      this.serviceJClass = var1;
      this.serviceJClassIsCallbackJws = var1.getAnnotation(WLW81CallbackJWS.class) != null;
      this.populateMethodMap();
   }

   public JClass getServiceJClass() {
      return this.serviceJClass;
   }

   public WsdlService getWsdlService() {
      return this._wsdlService;
   }

   public void setWsdlService(WsdlService var1) {
      this._wsdlService = (WsdlServiceBuilder)var1;
   }

   public void setSchemaTypeSystem(SchemaTypeSystem var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null sts");
      } else {
         this.sts = var1;
      }
   }

   protected void internalBind(TylarWriter var1) {
      if (this.sts == null) {
         throw new IllegalStateException("SchemaTypeSystem not set");
      } else if (this.serviceJClass == null) {
         throw new IllegalStateException("Service JClass not set");
      } else if (this._wsdlService == null) {
         throw new IllegalStateException("WsldService not set");
      } else {
         this.bind();

         try {
            var1.writeBindingFile(this.bindingFile);
            var1.writeSchemaTypeSystem(this.sts);
         } catch (IOException var3) {
            if (!this.logError(var3)) {
               return;
            }
         }

      }
   }

   private void bind() {
      this.bindingLoader = super.getBaseBindingLoader();
      this.bind(this._wsdlService);
      QName var1 = getJsCallbackServiceName(this._wsdlService.getDefinitions());
      if (var1 != null) {
         Iterator var2 = this._wsdlService.getDefinitions().getServices().values().iterator();

         while(var2.hasNext()) {
            WsdlServiceBuilder var3 = (WsdlServiceBuilder)var2.next();
            if (var3.getName().equals(var1)) {
               this.bind(var3);
               break;
            }
         }
      }

   }

   public static QName getJsCallbackServiceName(WsdlDefinitions var0) {
      WsdlPartnerLinkType var1 = (WsdlPartnerLinkType)var0.getExtension("PartnerLinkType");
      if (var1 != null) {
         Iterator var2 = var0.getServices().values().iterator();

         while(var2.hasNext()) {
            WsdlService var3 = (WsdlService)var2.next();
            Iterator var4 = var3.getPortTypes().iterator();

            while(var4.hasNext()) {
               WsdlPortType var5 = (WsdlPortType)var4.next();

               try {
                  if (var5.getName().equals(var1.getPortTypeName("Callback"))) {
                     return var3.getName();
                  }
               } catch (WsdlException var7) {
                  return null;
               }
            }
         }
      }

      return null;
   }

   private void bind(WsdlServiceBuilder var1) {
      this.bindingLoader = super.getBaseBindingLoader();
      Iterator var2 = var1.getPortTypes().iterator();

      while(true) {
         WsdlPortTypeBuilder var3;
         String var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (WsdlPortTypeBuilder)var2.next();
            var4 = this.getBindingTypeForPortType(var1, var3);
         } while(!var4.equals("SOAP12") && !var4.equals("SOAP11"));

         Iterator var5 = var3.getOperations().values().iterator();

         while(var5.hasNext()) {
            WsdlOperationBuilder var6 = (WsdlOperationBuilder)var5.next();
            this.bindOperation(var6);
         }
      }
   }

   private String getBindingTypeForPortType(WsdlService var1, WsdlPortType var2) {
      Iterator var3 = var1.getPorts().values().iterator();

      WsdlPort var4;
      WsdlPortType var5;
      do {
         if (!var3.hasNext()) {
            return "unknown";
         }

         var4 = (WsdlPort)var3.next();
         var5 = var4.getPortType();
      } while(!var5.getName().equals(var2.getName()));

      WsdlBinding var6 = var4.getBinding();
      String var7 = var6.getBindingType();
      return var7;
   }

   private void bindOperation(WsdlOperationBuilder var1) {
      JMethod var2 = this.getJMethodThatMapsToOperation(var1);
      Object var3;
      if (this.isCallbackOperation(var1) && this.flipCallbackInputAndOutputParts) {
         var3 = this.getWsdlMethodForCallback(var1);
      } else if (this.serviceJClassIsCallbackJws) {
         if (var2 == null) {
            return;
         }

         var3 = this.getWsdlMethodForCallback(var1);
      } else {
         var3 = var1.getWsdlMethod();
      }

      if (var2 != null) {
         WsdlPart var4 = ((WsdlMethod)var3).getResultPart();
         JClass var5 = var2.getReturnType();
         this.bindReturn(var4, var5, var2, var1.getName());
         int var8 = 0;

         WsdlPart var7;
         for(Iterator var9 = ((WsdlMethod)var3).getParameters().iterator(); var9.hasNext(); var8 += this.bindParameters(var7, var2.getParameters(), var8, var2)) {
            WsdlParameter var6 = (WsdlParameter)var9.next();
            var7 = var6.getPrimaryPart();
         }

      }
   }

   private void bindReturn(WsdlPart var1, JClass var2, JMethod var3, QName var4) {
      if (var1 == null) {
         if (var2 != null && !var2.isVoidType()) {
            throw new IllegalArgumentException("Operation " + var4 + " should map to a java method with a void return type.");
         }
      } else {
         QName var5 = var1.getType();
         if (var5 != null) {
            this.bindParameterForTypeQName(var5, var2);
         } else {
            QName var6 = var1.getElement();
            SchemaGlobalElement var7 = this.sts.findElement(var6);
            SchemaType var8 = var7.getType();
            boolean var9 = this.isWrapped(var3);
            if (!var9) {
               this.bindNonWrappedElement(var2, var6, var8);
            } else {
               this.bindWrappedElement(var2, var6, var8);
            }
         }

      }
   }

   private int bindParameters(WsdlPart var1, JParameter[] var2, int var3, JMethod var4) {
      QName var5 = var1.getType();
      if (var5 != null) {
         this.bindParameterForTypeQName(var5, var2[var3].getType());
         return 1;
      } else {
         QName var6 = var1.getElement();
         SchemaGlobalElement var7 = this.sts.findElement(var6);
         SchemaType var8 = var7.getType();
         boolean var9 = this.isWrapped(var4);
         if (!var9) {
            this.bindNonWrappedElement(var2[var3].getType(), var6, var8);
            return 1;
         } else {
            try {
               return this.bindWrappedElement(var2, var3, var6, var8);
            } catch (IllegalStateException var11) {
               this.logError("Error binding parameters for wrapped method '" + var4.getSimpleName() + "'.");
               this.logError(var11);
               throw var11;
            }
         }
      }
   }

   private void bindParameterForTypeQName(QName var1, JClass var2) {
      SchemaType var3 = this.sts.findType(var1);
      if (var3 != null) {
         this.bindParameter(var3, var2);
      }

   }

   private void bindNonWrappedElement(JClass var1, QName var2, SchemaType var3) {
      JavaTypeName var4 = JavaTypeName.forString(var1.getQualifiedName());
      XmlTypeName var5 = XmlTypeName.forGlobalName('e', var2);
      XmlTypeName var6 = XmlTypeName.forSchemaType(var3);
      BindingTypeName var7 = BindingTypeName.forPair(var4, var5);
      SimpleDocumentBinding var8 = new SimpleDocumentBinding(var7);
      var8.setTypeOfElement(var6);
      this.bindingFile.addBindingType(var8, this.shouldBeFromJavaDefault(var7), true);
      this.bindParameter(var3, var1);
   }

   private void bindWrappedElement(JClass var1, QName var2, SchemaType var3) {
      SchemaProperty[] var4 = var3.getProperties();
      LinkedHashMap var5 = new LinkedHashMap(var4.length * 4 / 3);

      for(int var6 = 0; var6 < var4.length; ++var6) {
         this.bindParameter(var4[var6].getType(), var1);
         var5.put(var4[var6].getName().getLocalPart(), var1.getQualifiedName());
      }

      if (var3.hasElementWildcards()) {
         var5.put(XmlTypeName.forElementWildCardElement().getQName().getLocalPart(), var1.getQualifiedName());
      }

      this.nameListFromWrapperElement.put(var2, var5);
   }

   private int bindWrappedElement(JParameter[] var1, int var2, QName var3, SchemaType var4) {
      if (var4.hasElementWildcards()) {
         SchemaParticle var10 = var4.getContentModel();
         SchemaParticle[] var9;
         if (var10 == null) {
            var9 = new SchemaParticle[0];
         } else if (var10.getParticleType() != 5 && var10.getParticleType() != 4) {
            var9 = var4.getContentModel().getParticleChildren();
         } else {
            var9 = new SchemaParticle[]{var10};
         }

         LinkedHashMap var11 = new LinkedHashMap(var9.length * 4 / 3);
         if (var1.length < var2 + var9.length) {
            throw new IllegalStateException("Java class parameters do not match WSDL definition.");
         } else {
            for(int var8 = 0; var8 < var9.length; ++var8) {
               if (var9[var8].getParticleType() == 5) {
                  var11.put(XmlTypeName.forElementWildCardElement().getQName().getLocalPart(), var1[var2 + var8].getType().getQualifiedName());
               } else {
                  this.bindParameter(var9[var8].getType(), var1[var2 + var8].getType());
                  var11.put(var9[var8].getName().getLocalPart(), var1[var2 + var8].getType().getQualifiedName());
               }
            }

            this.nameListFromWrapperElement.put(var3, var11);
            return var9.length;
         }
      } else {
         SchemaProperty[] var5 = var4.getProperties();
         LinkedHashMap var6 = new LinkedHashMap(var5.length * 4 / 3);
         if (var1.length < var2 + var5.length) {
            throw new IllegalStateException("Java class parameters do not match WSDL definition.");
         } else {
            for(int var7 = 0; var7 < var5.length; ++var7) {
               this.bindParameter(var5[var7].getType(), var1[var2 + var7].getType());
               var6.put(var5[var7].getName().getLocalPart(), var1[var2 + var7].getType().getQualifiedName());
            }

            this.nameListFromWrapperElement.put(var3, var6);
            return var5.length;
         }
      }
   }

   private void bindParameter(SchemaType var1, JClass var2) {
      if (!var1.isSimpleType() && !this.isSoapEncodingSimpleType(var1)) {
         if (var1.isAttributeType()) {
            throw new IllegalArgumentException("Parameter cannot be an attribute.");
         }

         if (isSoapArray(var1)) {
            this.bindParameterSoapArray(var2, var1);
         } else if (isLiteralArray(var1)) {
            this.bindParameterLiteralArray(var2, var1);
         } else if (!isXmlBeanOrXBeanArray(var2)) {
            if (var2.getQualifiedName().equals("javax.activation.DataHandler")) {
               BindingTypeName var3 = this.bindingTypeName(var2, var1);
               ByNameBean var4 = new ByNameBean(var3);
               this.bindingFile.addBindingType(var4, this.shouldBeFromJavaDefault(var3), true);
            } else {
               this.bindParameterComplex(var2, var1);
            }
         }
      } else {
         this.bindParameterAtomic(var2, var1);
      }

   }

   private boolean isSoapEncodingSimpleType(SchemaType var1) {
      if (var1 != null) {
         QName var2 = var1.getName();
         if (var2 != null) {
            String var3 = var2.getNamespaceURI();
            if (var3 != null && var3.equals("http://schemas.xmlsoap.org/soap/encoding/")) {
               return soapEncodingSimpleElements.contains(var1.getName().getLocalPart());
            }
         }
      }

      return false;
   }

   private static boolean isXmlBeanOrXBeanArray(JClass var0) {
      if (var0 == null) {
         return false;
      } else {
         JClass var1;
         if (var0.isArrayType()) {
            var1 = var0.getArrayComponentType();
            return isXmlBeanOrXBeanArray(var1);
         } else {
            var1 = var0.forName("org.apache.xmlbeans.XmlObject");
            if (!var1.isUnresolvedType() && var1.isAssignableFrom(var0)) {
               return true;
            } else {
               var1 = var0.getClassLoader().loadClass(XmlObject.class.getName());
               return var1.isAssignableFrom(var0);
            }
         }
      }
   }

   private void bindParameterAtomic(JClass var1, SchemaType var2) {
      SchemaType var3 = var2.getBaseType();
      if (var3 != null) {
         BindingTypeName var4 = this.bindingTypeName(var1, var2);
         if (this.bindingLoader.getBindingType(var4) == null && this.bindingFile.getBindingType(var4) == null) {
            SimpleBindingType var5 = new SimpleBindingType(var4);
            XmlTypeName var6 = XmlTypeName.forSchemaType(var3);
            var5.setAsIfXmlType(var6);
            this.bindingFile.addBindingType(var5, this.shouldBeFromJavaDefault(var4), true);
         }
      }

   }

   private void bindParameterSoapArray(JClass var1, SchemaType var2) {
      if (!XBeanUtil.isXmlBean(var1)) {
         if (!var1.isArrayType() || !XBeanUtil.isXmlBean(var1.getArrayComponentType())) {
            BindingTypeName var3 = this.bindingTypeName(var1, var2);
            SoapArrayType var4 = new SoapArrayType(this.bindingTypeName(var1, var2));
            SchemaProperty[] var5 = var2.getElementProperties();
            boolean var6 = var5 != null && var5.length == 1;
            boolean var7 = var5 != null && var5.length == 1 ? var5[0].hasNillable() != 0 : false;
            if (var6) {
               var4.setItemName(var5[0].getName());
            }

            var4.setItemNillable(var7);
            SchemaLocalAttribute var8 = var2.getAttributeModel().getAttribute(new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType"));
            SOAPArrayType var9 = ((SchemaWSDLArrayType)var8).getWSDLArrayType();
            boolean var10 = isCollectionType(var1);
            if (!var1.isArrayType() && !var10) {
               throw new IllegalArgumentException("Soap Array type must be mapped to a java array for schema type " + var2.getName() + " - " + var1.getQualifiedName());
            } else {
               if (var10) {
                  WrappedArrayType var11 = createCollectionType(var3);
                  this.bindingFile.addBindingType(var11, this.shouldBeFromJavaDefault(var3), true);
               } else {
                  int var12;
                  SchemaType var13;
                  String var15;
                  BindingTypeName var19;
                  BindingType var22;
                  if (var9 == null) {
                     var12 = 1;
                     var13 = null;
                     var22 = null;
                     SchemaProperty[] var26 = var2.getElementProperties();
                     if (var26.length != 1) {
                        throw new IllegalArgumentException("Array element length must be 1: ");
                     }

                     SchemaType var24 = var5[0].getType();
                     BindingType var20 = this.getOrCreateBindingType(var1.getArrayComponentType(), var24);
                     var19 = var20.getName();
                  } else {
                     var12 = var9.getDimensions().length + var9.getRanks().length;
                     var13 = this.sts.findType(var9.getQName());
                     if (var13 == null) {
                        XmlTypeName var23 = soapArrayTypeName(var2);
                        JavaTypeName var25 = JavaTypeName.forString(var1.getQualifiedName());
                        var19 = BindingTypeName.forPair(var25, var23);
                     } else if (var12 == var1.getArrayDimensions()) {
                        var22 = this.getOrCreateBindingType(var1.getArrayComponentType(), var13);
                        var19 = var22.getName();
                     } else {
                        int var14 = var1.getArrayDimensions();
                        var15 = var1.getArrayComponentType().getQualifiedName();

                        for(int var16 = 0; var16 < var14 - var12; ++var16) {
                           var15 = var15 + "[]";
                        }

                        JClass var27 = var1.forName(var15);
                        BindingType var17 = this.getOrCreateBindingType(var27, var13);
                        var19 = var17.getName();
                     }
                  }

                  var4.setRanks(var12);
                  var4.setItemType(var19);
                  XmlTypeName var21 = soapArrayTypeName(var2);
                  var21 = var21.getOuterComponent();

                  for(var15 = var1.getQualifiedName(); var21.getComponentType() == 121; var21 = var21.getOuterComponent()) {
                     var15 = var15.substring(0, var15.lastIndexOf(91));
                     JavaTypeName var28 = JavaTypeName.forString(var15);
                     BindingTypeName var29 = BindingTypeName.forPair(var28, var21);
                     SoapArrayType var18 = new SoapArrayType(var29);
                     var18.setRanks(1);
                     var18.setItemType(var19);
                     this.bindingFile.addBindingType(var18, true, true);
                  }

                  this.bindingFile.addBindingType(var4, this.shouldBeFromJavaDefault(var3), true);
               }

            }
         }
      }
   }

   public static WrappedArrayType createCollectionType(BindingTypeName var0) {
      WrappedArrayType var1 = new WrappedArrayType(var0);
      var1.setItemNillable(true);
      BindingTypeName var2 = BindingTypeName.forPair(JavaTypeName.forString(Object.class.getName()), XmlTypeName.forTypeNamed(XS_ANYTYPE));
      var1.setItemType(var2);
      var1.setItemName(new QName("item"));
      return var1;
   }

   public static boolean isCollectionType(JClass var0) {
      return Java2Schema.isCollectionType(var0);
   }

   public static boolean isCollectionType(Class var0) {
      return Java2Schema.isCollectionType(var0);
   }

   public static boolean isCollectionType(String var0) {
      return Java2Schema.isCollectionType(var0);
   }

   private void bindParameterLiteralArray(JClass var1, SchemaType var2) {
      if (!XBeanUtil.isXmlBean(var1)) {
         if (!var1.isArrayType() || !XBeanUtil.isXmlBean(var1.getArrayComponentType())) {
            BindingTypeName var3 = this.bindingTypeName(var1, var2);
            boolean var5 = isCollectionType(var1);
            if (!var1.isArrayType() && !var5) {
               throw new IllegalArgumentException("Literal Array type must be mapped to a java array for schema type " + var2.getName() + " - " + var1.getQualifiedName());
            } else {
               WrappedArrayType var4;
               if (var5) {
                  var4 = createCollectionType(var3);
               } else {
                  var4 = new WrappedArrayType(var3);
                  SchemaProperty var6 = var2.getProperties()[0];
                  var4.setItemName(var6.getName());
                  var4.setItemNillable(var6.hasNillable() != 0);
                  SchemaType var7 = getLiteralArrayItemType(var2);
                  String var8 = var1.getQualifiedName();
                  String var9 = var8.substring(0, var8.lastIndexOf(91));
                  JClass var10 = var1.forName(var9);
                  BindingType var11 = this.getOrCreateBindingType(var10, var7);
                  var4.setItemType(var11.getName());
               }

               this.bindingFile.addBindingType(var4, this.shouldBeFromJavaDefault(var3), true);
            }
         }
      }
   }

   private void bindParameterComplex(JClass var1, SchemaType var2) {
      BindingTypeName var3 = this.bindingTypeName(var1, var2);
      if (this.bindingFile.getBindingType(var3) == null) {
         ByNameBean var4 = new ByNameBean(var3);
         this.bindingFile.addBindingType(var4, this.shouldBeFromJavaDefault(var3), true);
         SchemaProperty[] var5 = var2.getProperties();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            this.addPropertyToByNameBean(var4, var5[var6], var1);
         }
      }

   }

   private void addPropertyToByNameBean(ByNameBean var1, SchemaProperty var2, JClass var3) {
      QNameProperty var4 = new QNameProperty();
      String var5 = this.javaPropertyName(var2);
      JClass var6 = this.addGetterSetterOrFieldToQNameProperty(var4, var3, var2, var5);
      JavaTypeName var7 = null;
      boolean var8 = isMultiple(var2);
      if (var8 && var6.getArrayComponentType() != null) {
         var7 = JavaTypeName.forString(var6.getQualifiedName());
         var6 = var6.getArrayComponentType();
      }

      BindingType var9 = this.getOrCreateBindingType(var6, var2);
      if (var9 != null) {
         var4.setQName(var2.getName());
         var4.setAttribute(var2.isAttribute());
         var4.setNillable(var2.hasNillable() != 0);
         var4.setOptional(isOptional(var2));
         var4.setMultiple(var8);
         var4.setBindingType(var9);
         var4.setCollectionClass(var7);
         var1.addProperty(var4);
      }

   }

   private BindingType getOrCreateBindingType(JClass var1, SchemaProperty var2) {
      return this.getOrCreateBindingType(var1, var2.getType());
   }

   private BindingType getOrCreateBindingType(JClass var1, SchemaType var2) {
      BindingTypeName var3 = this.bindingTypeName(var1, var2);
      BindingType var4 = this.bindingLoader.getBindingType(var3);
      if (var4 == null) {
         this.bindParameter(var2, var1);
         var4 = this.bindingFile.getBindingType(var3);
      }

      return var4;
   }

   private JClass addGetterSetterOrFieldToQNameProperty(QNameProperty var1, JClass var2, SchemaProperty var3, String var4) {
      JField var6 = this.getJFieldForProperty(var2, var3);
      JClass var5;
      if (var6 != null && var6.isPublic()) {
         var5 = var6.getType();
         var1.setFieldName(var4);
      } else {
         JMethod var7 = this.getGetterJMethodForProperty(var2, var3);
         JMethod var8 = this.getSetterJMethodForProperty(var2, var3);
         if (var7 == null || var8 == null) {
            throw new IllegalArgumentException("Could not find read/write property for schemaProperty: " + var3.getName() + " on class " + var2.getQualifiedName());
         }

         var5 = var7.getReturnType();
         var1.setGetter(MethodName.create(var7));
         var1.setSetter(MethodName.create(var8));
      }

      return var5;
   }

   private BindingTypeName bindingTypeName(JClass var1, SchemaType var2) {
      JavaTypeName var3 = JavaTypeName.forString(var1.getQualifiedName());
      XmlTypeName var4 = XmlTypeName.forSchemaType(var2);
      BindingTypeName var5 = BindingTypeName.forPair(var3, var4);
      return var5;
   }

   private JMethod getSetterJMethodForProperty(JClass var1, SchemaProperty var2) {
      return this.getJMethodForProperty("set", var1, var2);
   }

   private JMethod getGetterJMethodForProperty(JClass var1, SchemaProperty var2) {
      JMethod var3 = this.getJMethodForProperty("get", var1, var2);
      if (var3 == null) {
         var3 = this.getJMethodForProperty("is", var1, var2);
      }

      return var3;
   }

   private JMethod getJMethodForProperty(String var1, JClass var2, SchemaProperty var3) {
      String var4 = this.methodNameFromProperty(var1, var3);
      JMethod[] var5 = var2.getMethods();

      for(int var6 = 0; var6 < var5.length; ++var6) {
         if (var5[var6].getSimpleName().equals(var4)) {
            return var5[var6];
         }
      }

      return null;
   }

   private JField getJFieldForProperty(JClass var1, SchemaProperty var2) {
      String var3 = this.javaPropertyName(var2);
      JField[] var4 = var1.getFields();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         if (var4[var5].getSimpleName().equals(var3)) {
            return var4[var5];
         }
      }

      return null;
   }

   private JMethod getJMethodThatMapsToOperation(WsdlOperation var1) {
      String var2 = this.methodNameFromOperationName(var1);
      JMethod var3 = (JMethod)this.methodMap.get(var2);
      if (null == var3) {
         var3 = (JMethod)this.callbackMethodMap.get(var2);
      }

      return var3;
   }

   private boolean isCallbackOperation(WsdlOperation var1) {
      return this.callbackMethodMap.get(this.methodNameFromOperationName(var1)) != null;
   }

   private String javaPropertyName(SchemaProperty var1) {
      return this.propertyNamePicker.pick(var1.getName().getLocalPart());
   }

   private String methodNameFromOperationName(WsdlOperation var1) {
      return var1.getName().getLocalPart();
   }

   private String methodNameFromProperty(String var1, SchemaProperty var2) {
      return var1 + this.getterSetterNamePicker.pick(var2.getName().getLocalPart());
   }

   private boolean isWrapped(JMethod var1) {
      return isWrapped(var1, this.serviceJClass);
   }

   public static boolean isWrapped(JMethod var0, JClass var1) {
      JAnnotation var2 = var0.getAnnotation(SOAPBinding.class);
      SOAPBinding.Use var6;
      SOAPBinding.Style var7;
      SOAPBinding.ParameterStyle var8;
      if (var2 != null) {
         var7 = (SOAPBinding.Style)JamUtil.getAnnotationEnumValue(var2, "style", SOAPBinding.Style.class, Style.DOCUMENT);
         var6 = (SOAPBinding.Use)JamUtil.getAnnotationEnumValue(var2, "use", SOAPBinding.Use.class, Use.LITERAL);
         var8 = (SOAPBinding.ParameterStyle)JamUtil.getAnnotationEnumValue(var2, "parameterStyle", SOAPBinding.ParameterStyle.class, ParameterStyle.WRAPPED);
         return var7 == Style.DOCUMENT && var6 == Use.LITERAL && var8 == ParameterStyle.WRAPPED;
      } else {
         var2 = var0.getAnnotation("com.bea.control.ServiceControl$SOAPBinding");
         String var3;
         String var4;
         String var5;
         if (var2 != null) {
            var3 = var2.getValue("use").asString();
            var4 = var2.getValue("style").asString();
            var5 = var2.getValue("parameterStyle").asString();
            if (var3.trim().length() == 0) {
               var3 = "LITERAL";
            }

            if (var4.trim().length() == 0) {
               var3 = "DOCUMENT";
            }

            if (var5.trim().length() == 0) {
               var3 = "WRAPPED";
            }

            return var3.equals("LITERAL") && var4.equals("DOCUMENT") && var5.equals("WRAPPED");
         } else {
            var2 = var1.getAnnotation(SOAPBinding.class);
            if (var2 != null) {
               var7 = (SOAPBinding.Style)JamUtil.getAnnotationEnumValue(var2, "style", SOAPBinding.Style.class, Style.DOCUMENT);
               var6 = (SOAPBinding.Use)JamUtil.getAnnotationEnumValue(var2, "use", SOAPBinding.Use.class, Use.LITERAL);
               var8 = (SOAPBinding.ParameterStyle)JamUtil.getAnnotationEnumValue(var2, "parameterStyle", SOAPBinding.ParameterStyle.class, ParameterStyle.WRAPPED);
               return var7 == Style.DOCUMENT && var6 == Use.LITERAL && var8 == ParameterStyle.WRAPPED;
            } else {
               var2 = var1.getAnnotation("com.bea.control.ServiceControl$SOAPBinding");
               if (var2 != null) {
                  var3 = var2.getValue("use").asString();
                  var4 = var2.getValue("style").asString();
                  var5 = var2.getValue("parameterStyle").asString();
                  if (var3.trim().length() == 0) {
                     var3 = "LITERAL";
                  }

                  if (var4.trim().length() == 0) {
                     var3 = "DOCUMENT";
                  }

                  if (var5.trim().length() == 0) {
                     var3 = "WRAPPED";
                  }

                  return var3.equals("LITERAL") && var4.equals("DOCUMENT") && var5.equals("WRAPPED");
               } else {
                  return true;
               }
            }
         }
      }
   }

   private void populateMethodMap() {
      this.methodMap.clear();
      this.callbackMethodMap.clear();
      populateMethodMap(this.serviceJClass, this.methodMap, this.callbackMethodMap);
   }

   public static void populateMethodMap(JClass var0, HashMap<String, JMethod> var1, HashMap<String, JMethod> var2) {
      JMethod[] var3 = var0.getMethods();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].getSimpleName();
         JAnnotation var6 = var3[var4].getAnnotation(WebMethod.class);
         if (var6 != null) {
            String var7 = JamUtil.getAnnotationStringValue(var6, "operationName");
            if (var7 != null && !var7.equals("")) {
               var5 = var7;
            }
         }

         var1.put(var5, var3[var4]);
      }

      JField[] var13 = var0.getFields();

      JMethod[] var8;
      int var9;
      String var10;
      for(int var14 = 0; var14 < var13.length; ++var14) {
         JField var15 = var13[var14];
         JAnnotation var18 = var15.getAnnotation(Callback.class);
         if (var18 != null) {
            var8 = var15.getType().getMethods();

            for(var9 = 0; var9 < var8.length; ++var9) {
               var10 = var8[var9].getSimpleName();
               JAnnotation var11 = var8[var9].getAnnotation(WebMethod.class);
               if (var11 != null) {
                  String var12 = JamUtil.getAnnotationStringValue(var11, "operationName");
                  if (var12 != null && !var12.equals("")) {
                     var10 = var12;
                  }
               }

               var2.put(var10, var8[var9]);
            }
         }
      }

      JClass[] var16 = var0.getClasses();

      for(int var17 = 0; var17 < var16.length; ++var17) {
         JClass var19 = var16[var17];
         if (var19.getSimpleName().equals(var0.getSimpleName() + "$Callback")) {
            var8 = var19.getMethods();

            for(var9 = 0; var9 < var8.length; ++var9) {
               var10 = var8[var9].getSimpleName();
               var2.put(var10, var8[var9]);
            }
         }
      }

   }

   public WsdlMethod getWsdlMethodForCallback(WsdlOperationBuilder var1) {
      WsdlMethodImpl var2 = new WsdlMethodImpl();
      WsdlMessageBuilder var3 = var1.getOutput();
      WsdlMessageBuilder var4 = var1.getInput();
      WsdlPartBuilder var5 = null;
      String var6 = this.findReturnPartForCallback(var1);
      if (!StringUtil.isEmpty(var6)) {
         var5 = (WsdlPartBuilder)var4.getParts().get(var6);
         var2.setResultPart(var5);
      }

      String var7 = var1.getParameterOrder();
      if (StringUtil.isEmpty(var7)) {
         this.fillWithOutParameterOrder(var2, var3, var4, var5);
      } else {
         this.fillWithParameterOrder(var2, var3, var4, var5, var7);
      }

      return var2;
   }

   private String findReturnPartForCallback(WsdlOperationBuilder var1) {
      String var2 = var1.getParameterOrder();
      HashSet var3 = new HashSet();
      if (var2 != null) {
         StringTokenizer var4 = new StringTokenizer(var2, " ");

         while(var4.hasMoreTokens()) {
            var3.add(var4.nextToken());
         }
      }

      WsdlMessageBuilder var10 = var1.getInput();
      WsdlMessageBuilder var5 = var1.getOutput();
      if (var10 == null) {
         return null;
      } else {
         String var6 = null;
         Iterator var7 = var10.getParts().values().iterator();

         while(true) {
            WsdlPart var8;
            WsdlPart var9;
            do {
               if (!var7.hasNext()) {
                  return var6;
               }

               var8 = (WsdlPart)var7.next();
               var9 = var5 == null ? null : (WsdlPart)var5.getParts().get(var8.getName());
            } while(var9 == null && var3.contains(var8.getName()));

            if (!var8.equals(var9)) {
               if (var6 != null) {
                  if (StringUtil.isEmpty(var2)) {
                     return null;
                  }

                  throw new IllegalStateException("More than one return outPart found in operation: " + var1 + ". Only one outPart name can be " + "missing in the parameterOrder");
               }

               var6 = var8.getName();
            }
         }
      }
   }

   public void fillWithOutParameterOrder(WsdlMethodBuilder var1, WsdlMessageBuilder var2, WsdlMessageBuilder var3, WsdlPart var4) {
      HashSet var5 = new HashSet();
      Iterator var6;
      WsdlPartBuilder var7;
      WsdlPartBuilder var8;
      if (var2 != null) {
         for(var6 = var2.getParts().values().iterator(); var6.hasNext(); var1.addWsdlParameter(new WsdlParameterImpl(var7, var8))) {
            var7 = (WsdlPartBuilder)var6.next();
            var8 = null;
            if (var3 != null) {
               var8 = (WsdlPartBuilder)var3.getParts().get(var7.getName());
               if (var8 != null && var8.equals(var7)) {
                  var5.add(var8);
               } else {
                  var8 = null;
               }
            }
         }
      }

      if (var3 != null) {
         var6 = var3.getParts().values().iterator();

         while(var6.hasNext()) {
            var7 = (WsdlPartBuilder)var6.next();
            if (var7 != var4 && !var5.contains(var7)) {
               var1.addWsdlParameter(new WsdlParameterImpl((WsdlPartBuilder)null, var7));
            }
         }
      }

   }

   public void fillWithParameterOrder(WsdlMethodBuilder var1, WsdlMessageBuilder var2, WsdlMessageBuilder var3, WsdlPart var4, String var5) {
      ArrayList var6 = new ArrayList();
      if (var5 != null) {
         StringTokenizer var7 = new StringTokenizer(var5, " ");

         while(var7.hasMoreTokens()) {
            var6.add(var7.nextToken());
         }
      }

      HashSet var12 = new HashSet();
      Iterator var8 = var6.iterator();

      while(var8.hasNext()) {
         String var9 = (String)var8.next();
         WsdlPartBuilder var10 = (WsdlPartBuilder)var2.getParts().get(var9);
         WsdlPartBuilder var11 = null;
         if (var3 != null) {
            var11 = (WsdlPartBuilder)var3.getParts().get(var9);
            if ((var11 == null || var10 != null) && (var10 == null || !var10.equals(var11))) {
               var11 = null;
            } else {
               var12.add(var11);
            }
         }

         if (var10 == null && var11 == null) {
            throw new IllegalArgumentException("No In part or out part found for: " + var9 + " with Parameter Order = " + var5);
         }

         var1.addWsdlParameter(new WsdlParameterImpl(var10, var11));
      }

      if (var3 != null) {
         var8 = var3.getParts().values().iterator();

         while(var8.hasNext()) {
            WsdlPartBuilder var13 = (WsdlPartBuilder)var8.next();
            if (var13 != var4 && !var12.contains(var13)) {
               var1.addWsdlParameter(new WsdlParameterImpl((WsdlPartBuilder)null, var13));
            }
         }
      }

   }

   public Map getNameListFromWrapperElement() {
      return this.nameListFromWrapperElement;
   }

   private static boolean isLiteralArray(SchemaType var0) {
      return getLiteralArrayItemType(var0) != null;
   }

   private static SchemaType getLiteralArrayItemType(SchemaType var0) {
      if (!var0.isSimpleType() && var0.getContentType() != 2) {
         SchemaProperty[] var1 = var0.getProperties();
         if (var1.length == 1 && !var1[0].isAttribute()) {
            BigInteger var2 = var1[0].getMaxOccurs();
            return var2 != null && var2.compareTo(BigInteger.ONE) <= 0 ? null : var1[0].getType();
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private static boolean isSoapArray(SchemaType var0) {
      while(var0 != null) {
         String var1 = XmlTypeName.forSchemaType(var0).toString();
         if (var1.equals("t=Array@http://schemas.xmlsoap.org/soap/encoding/") || var1.startsWith("t=Array@http://www.w3.org/") && var1.endsWith("/soap-encoding")) {
            return true;
         }

         var0 = var0.getBaseType();
      }

      return false;
   }

   private static XmlTypeName soapArrayTypeName(SchemaType var0) {
      SOAPArrayType var1 = getWsdlArrayType(var0);
      if (var1 != null) {
         return XmlTypeName.forSoapArrayType(var1);
      } else {
         SchemaType var2 = XmlObject.type;
         SchemaProperty[] var3 = var0.getElementProperties();
         if (var3.length == 1) {
            var2 = var3[0].getType();
         }

         return XmlTypeName.forNestedNumber('y', 1, XmlTypeName.forSchemaType(var2));
      }
   }

   private static SOAPArrayType getWsdlArrayType(SchemaType var0) {
      SchemaLocalAttribute var1 = var0.getAttributeModel().getAttribute(arrayType);
      return var1 != null ? ((SchemaWSDLArrayType)var1).getWSDLArrayType() : null;
   }

   private static boolean isMultiple(SchemaProperty var0) {
      return var0.getMaxOccurs() == null || var0.getMaxOccurs().compareTo(BigInteger.ONE) > 0;
   }

   private static boolean isOptional(SchemaProperty var0) {
      return var0.getMinOccurs().signum() == 0 && !isMultiple(var0);
   }

   private boolean shouldBeFromJavaDefault(BindingTypeName var1) {
      JavaTypeName var2 = var1.getJavaName();
      XmlTypeName var3 = var1.getXmlName();
      if (var3.isSchemaType()) {
         return this.bindingFile.lookupTypeFor(var2) == null && this.bindingLoader.lookupTypeFor(var2) == null;
      } else if (var3.getComponentType() != 101) {
         return false;
      } else {
         return this.bindingFile.lookupElementFor(var2) == null && this.bindingLoader.lookupElementFor(var2) == null;
      }
   }

   static {
      soapEncodingSimpleElements.add("ENTITIES");
      soapEncodingSimpleElements.add("ENTITY");
      soapEncodingSimpleElements.add("ID");
      soapEncodingSimpleElements.add("IDREF");
      soapEncodingSimpleElements.add("IDREFS");
      soapEncodingSimpleElements.add("NCName");
      soapEncodingSimpleElements.add("NMTOKEN");
      soapEncodingSimpleElements.add("NMTOKENS");
      soapEncodingSimpleElements.add("NOTATION");
      soapEncodingSimpleElements.add("Name");
      soapEncodingSimpleElements.add("QName");
      soapEncodingSimpleElements.add("anyURI");
      soapEncodingSimpleElements.add("base64Binary");
      soapEncodingSimpleElements.add("boolean");
      soapEncodingSimpleElements.add("byte");
      soapEncodingSimpleElements.add("date");
      soapEncodingSimpleElements.add("dateTime");
      soapEncodingSimpleElements.add("decimal");
      soapEncodingSimpleElements.add("double");
      soapEncodingSimpleElements.add("duration");
      soapEncodingSimpleElements.add("float");
      soapEncodingSimpleElements.add("gDay");
      soapEncodingSimpleElements.add("gMonth");
      soapEncodingSimpleElements.add("gMonthDay");
      soapEncodingSimpleElements.add("gYear");
      soapEncodingSimpleElements.add("gYearMonth");
      soapEncodingSimpleElements.add("hexBinary");
      soapEncodingSimpleElements.add("int");
      soapEncodingSimpleElements.add("integer");
      soapEncodingSimpleElements.add("language");
      soapEncodingSimpleElements.add("long");
      soapEncodingSimpleElements.add("negativeInteger");
      soapEncodingSimpleElements.add("nonNegativeInteger");
      soapEncodingSimpleElements.add("nonPositiveInteger");
      soapEncodingSimpleElements.add("normalizedString");
      soapEncodingSimpleElements.add("positiveInteger");
      soapEncodingSimpleElements.add("short");
      soapEncodingSimpleElements.add("string");
      soapEncodingSimpleElements.add("time");
      soapEncodingSimpleElements.add("token");
      soapEncodingSimpleElements.add("unsignedByte");
      soapEncodingSimpleElements.add("unsignedInt");
      soapEncodingSimpleElements.add("unsignedLong");
      soapEncodingSimpleElements.add("unsignedShort");
      XS_ANYTYPE = Java2Schema.XS_ANYTYPE;
      arrayType = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
   }
}
