package weblogic.wsee.tools.jws.decl;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JField;
import javax.xml.namespace.QName;
import javax.xml.rpc.holders.Holder;
import weblogic.jws.Types;
import weblogic.jws.WildcardParticle;
import weblogic.wsee.tools.jws.validation.Validator;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.XBeanUtil;

public abstract class WebTypeDecl implements Validator {
   private final JClass type;
   private boolean isHolder;
   private JClass heldType;
   private final QName xmlElementOrTypeName;
   private final WebMethodDecl webMethodDecl;
   private final boolean header;
   private final String[] typeClassNames;
   private final TypeFamily typeFamily;
   private boolean isXmlElement;
   private String name = null;
   private String partName = null;
   private boolean annotationSet = false;

   WebTypeDecl(WebMethodDecl var1, JAnnotatedElement var2, JClass var3, Class var4, String var5) {
      this.webMethodDecl = var1;
      this.type = var3;
      JAnnotation var6 = var2.getAnnotation(var4);
      if (var6 != null) {
         this.header = JamUtil.getAnnotationBooleanValue(var6, "header", false);
         this.partName = JamUtil.getAnnotationStringValue(var6, "partName");
      } else {
         this.header = false;
      }

      this.typeFamily = this.getTypeFamily(var3);
      if (StringUtil.isEmpty(this.name) && var6 != null) {
         this.name = JamUtil.getAnnotationStringValue(var6, "name");
         this.annotationSet = true;
      }

      if (StringUtil.isEmpty(this.name)) {
         this.name = var5;
      }

      String var7 = this.getElementOrTypeLocalName();
      if (this.typeFamily == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT) {
         this.name = var7;
      }

      String var8 = this.getTargetNamespace(var6);
      if (var7 != null) {
         this.xmlElementOrTypeName = new QName(var8, var7);
      } else {
         this.xmlElementOrTypeName = null;
      }

      this.typeClassNames = this.getTypeClassNames(var2.getAnnotation(Types.class));
   }

   public String getWebTypeDeclName() {
      return this.name;
   }

   public String getPartName() {
      if (StringUtil.isEmpty(this.partName)) {
         this.partName = this.getDefaultPartName();
      }

      return this.partName;
   }

   abstract String getDefaultPartName();

   private TypeFamily getTypeFamily(JClass var1) throws RuntimeException {
      TypeFamily var2 = WebTypeDecl.TypeFamily.POJO;
      this.isHolder = false;
      this.isXmlElement = true;
      if (XBeanUtil.isXmlBean(var1)) {
         if (XBeanUtil.xmlBeanIsDocumentType(this.webMethodDecl.getContext().getClassLoader(), var1.getQualifiedName(), true)) {
            var2 = WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT;
         } else {
            var2 = WebTypeDecl.TypeFamily.XML_BEAN_TYPE;
            this.isXmlElement = false;
         }
      } else {
         JClass var3 = this.getHeldTypeFromHolder(var1);
         this.isHolder = var3 != null;
         this.heldType = var3;
         if (var3 != null && XBeanUtil.isXmlBean(var3)) {
            if (XBeanUtil.xmlBeanIsDocumentType(this.webMethodDecl.getContext().getClassLoader(), var3.getQualifiedName(), true)) {
               var2 = WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT;
            } else {
               var2 = WebTypeDecl.TypeFamily.XML_BEAN_TYPE;
               this.isXmlElement = false;
            }
         }
      }

      return var2;
   }

   private JClass getHeldTypeFromHolder(JClass var1) {
      JClass var2 = var1.getClassLoader().loadClass(Holder.class.getName());
      if (var2.isAssignableFrom(var1)) {
         JField[] var3 = var1.getFields();
         if (var3.length == 1 && var3[0].getSimpleName().equals("value")) {
            return var3[0].getType();
         }
      }

      return null;
   }

   private String[] getTypeClassNames(JAnnotation var1) {
      String[] var2 = null;
      if (var1 != null) {
         JAnnotationValue var3 = var1.getValue("value");
         if (var3 != null) {
            var2 = var3.asStringArray();
         }
      }

      return var2;
   }

   private String getTargetNamespace(JAnnotation var1) {
      String var2 = null;
      QName var3;
      if (this.typeFamily == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT) {
         if (this.isHolder) {
            var3 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.heldType.getQualifiedName());
         } else {
            var3 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.type.getQualifiedName());
         }

         if (var3 != null) {
            var2 = var3.getNamespaceURI();
         }
      } else if (this.typeFamily == WebTypeDecl.TypeFamily.XML_BEAN_TYPE) {
         if (this.isHolder) {
            var3 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.heldType.getQualifiedName());
         } else {
            var3 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.type.getQualifiedName());
         }

         if (var3 != null) {
            var2 = var3.getNamespaceURI();
         }
      } else {
         if ((this.webMethodDecl.getSoapBinding().isDocumentStyle() || this.header) && var1 != null) {
            var2 = JamUtil.getAnnotationStringValue(var1, "targetNamespace");
         }

         if (StringUtil.isEmpty(var2)) {
            var2 = this.webMethodDecl.getWebService().getTargetNamespace();
         }
      }

      assert var2 != null : "Target namespace not found";

      return var2;
   }

   private String getElementOrTypeLocalName() {
      String var1 = null;
      QName var2;
      if (this.typeFamily == WebTypeDecl.TypeFamily.XML_BEAN_DOCUMENT) {
         if (this.isHolder) {
            var2 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.heldType.getQualifiedName());
         } else {
            var2 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.type.getQualifiedName());
         }

         if (var2 != null) {
            var1 = var2.getLocalPart();
         }
      } else if (this.typeFamily == WebTypeDecl.TypeFamily.XML_BEAN_TYPE) {
         if (this.isHolder) {
            var2 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.heldType.getQualifiedName());
         } else {
            var2 = XBeanUtil.getQNameFromXmlBean(this.webMethodDecl.getContext().getClassLoader(), this.type.getQualifiedName());
         }

         if (var2 != null) {
            var1 = var2.getLocalPart();
         }
      } else {
         var1 = this.name;
      }

      assert var1 != null : "Name not found";

      return var1;
   }

   public String getName() {
      return this.xmlElementOrTypeName == null ? null : this.xmlElementOrTypeName.getLocalPart();
   }

   public String getTargetNamespace() {
      return this.xmlElementOrTypeName == null ? null : this.xmlElementOrTypeName.getNamespaceURI();
   }

   public QName getElementQName() {
      return this.xmlElementOrTypeName;
   }

   public boolean isXmlElement() {
      return this.isXmlElement;
   }

   public boolean isXmlType() {
      return !this.isXmlElement;
   }

   public String getType() {
      return this.type.isVoidType() ? null : this.type.getQualifiedName();
   }

   public JClass getJClass() {
      return this.type;
   }

   public boolean isHeader() {
      return this.header;
   }

   public WebMethodDecl getWebMethodDecl() {
      return this.webMethodDecl;
   }

   public String[] getTypeClassNames() {
      return this.typeClassNames;
   }

   public TypeFamily getTypeFamily() {
      return this.typeFamily;
   }

   public boolean isBoundToAnyType() {
      WildcardBindingsDecl var1 = this.getWebMethodDecl().getWebService().getWildcardBindings();
      return var1.getBindings().get(this.getType()) == WildcardParticle.ANYTYPE;
   }

   public boolean isWildcardType() {
      return WildcardUtil.WILDCARD_CLASSNAMES.contains(this.getType());
   }

   public boolean isArray() {
      return this.getType().endsWith("[]");
   }

   public boolean isAnnotationSet() {
      return this.annotationSet;
   }

   public boolean validate() {
      boolean var1 = true;
      if (this.xmlElementOrTypeName == null) {
         var1 = false;
      }

      return var1;
   }

   public static enum TypeFamily {
      POJO,
      XML_BEAN_TYPE,
      XML_BEAN_DOCUMENT;
   }
}
