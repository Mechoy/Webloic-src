package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.JaxrpcEnumType;
import com.bea.staxb.buildtime.internal.bts.ListArrayType;
import com.bea.staxb.buildtime.internal.bts.MethodName;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.bts.SimpleBindingType;
import com.bea.staxb.buildtime.internal.bts.SimpleContentBean;
import com.bea.staxb.buildtime.internal.bts.SimpleDocumentBinding;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.WrappedArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.namespace.QName;
import weblogic.j2ee.descriptor.JavaWsdlMappingBean;
import weblogic.j2ee.descriptor.JavaXmlTypeMappingBean;
import weblogic.j2ee.descriptor.PackageMappingBean;
import weblogic.j2ee.descriptor.VariableMappingBean;
import weblogic.wsee.util.ExceptionUtil;

class Buildtime109MappingHelper {
   private Tylar mTylar;
   private List mErrors = null;
   private Map mPackage2Namespace = new HashMap();

   public Buildtime109MappingHelper(Tylar var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null tylars");
      } else {
         this.mTylar = var1;
      }
   }

   public void buildMappings(JavaWsdlMappingBean var1) {
      Iterator var2 = this.getBindingTypes().iterator();

      while(true) {
         while(true) {
            while(var2.hasNext()) {
               BindingType var3 = (BindingType)var2.next();
               if (!(var3 instanceof ByNameBean) && !(var3 instanceof SimpleContentBean) && !(var3 instanceof WrappedArrayType) && !(var3 instanceof SoapArrayType)) {
                  if (var3 instanceof SimpleDocumentBinding) {
                     this.buildTypeMappingForElement(var1, var3.getName().getJavaName(), var3.getName().getXmlName(), ((SimpleDocumentBinding)var3).getTypeOfElement(), "element");
                  } else if (!(var3 instanceof JaxrpcEnumType) && !(var3 instanceof ListArrayType) && !(var3 instanceof SimpleBindingType)) {
                     this.addError("BindingType is ignored " + var3.getName());
                  } else {
                     this.buildSimpleType(var1, var3);
                  }
               } else {
                  this.buildTypeMapping(var1, var3.getName().getJavaName(), var3.getName().getXmlName(), "complexType");
               }
            }

            this.buildPackageMappings(var1);
            return;
         }
      }
   }

   public Exception[] getErrors() {
      if (this.mErrors == null) {
         return new Exception[0];
      } else {
         Exception[] var1 = new Exception[this.mErrors.size()];
         this.mErrors.toArray(var1);
         return var1;
      }
   }

   private void addError(Throwable var1) {
      if (this.mErrors == null) {
         this.mErrors = new ArrayList();
      }

      this.mErrors.add(var1);
   }

   private void addError(String var1) {
      this.addError((Throwable)(new Exception(var1)));
   }

   private void addPackageMapping(String var1, XmlTypeName var2) {
      if (var2.getQName() == null) {
         this.addPackageMapping(var1, var2.getOuterComponent());
      } else {
         this.addPackageMapping(var1, var2.getQName());
      }

   }

   private void addPackageMapping(String var1, QName var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Buildtime109MappingHelper.addPackageMapping:  null javaclass corresponding to QName:" + var2);
      } else if (var2 == null) {
         throw new IllegalArgumentException("Buildtime109MappingHelper.addPackageMapping:  null QName corresponding to JavaClass " + var1);
      } else {
         String var3 = var2.getNamespaceURI();
         boolean var4 = ExceptionUtil.classNameIsSchemaBuiltin(var1);
         if (!var3.equals("http://www.w3.org/2001/XMLSchema") || !var4) {
            int var5 = var1.lastIndexOf(".");
            boolean var6 = var5 != -1;
            int var7 = var1.indexOf("[]");
            boolean var8 = var7 != -1;
            String var9 = null;
            String var10;
            if (var6) {
               if (var8) {
                  var10 = var1.substring(0, var7);
                  if (ExceptionUtil.classNameIsSchemaBuiltin(var10)) {
                     return;
                  }
               }

               var9 = var1.substring(0, var5);
            } else {
               if (var8) {
                  return;
               }

               if (var4) {
                  return;
               }

               var9 = var1;
            }

            var10 = (String)this.mPackage2Namespace.get(var9);
            if (var10 == null) {
               this.mPackage2Namespace.put(var9, var3);
            } else if (!var10.equals(var9)) {
               this.addError("Namespace for " + var1 + " already mapped to " + var10);
            }

         }
      }
   }

   private void buildPackageMappings(JavaWsdlMappingBean var1) {
      Iterator var2 = this.mPackage2Namespace.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         PackageMappingBean var4 = var1.createPackageMapping();
         var4.setNamespaceURI((String)var3.getValue());
         var4.setPackageType((String)var3.getKey());
      }

   }

   private void buildSimpleType(JavaWsdlMappingBean var1, BindingType var2) {
      JavaTypeName var3 = var2.getName().getJavaName();
      XmlTypeName var4 = var2.getName().getXmlName();
      this.addPackageMapping(var3.toString(), var4);
      JavaXmlTypeMappingBean var5 = var1.createJavaXmlTypeMapping();
      var5.setJavaType(var3.toString());
      var5.setQnameScope("simpleType");
      if (var4.isAnonymousType()) {
         var5.setAnonymousTypeQname(getAnonymousString(var4));
      } else {
         var5.setRootTypeQname(var4.getQName());
      }

   }

   private void buildTypeMapping(JavaWsdlMappingBean var1, JavaTypeName var2, XmlTypeName var3, String var4) {
      BindingLoader var5 = this.mTylar.getBindingLoader();
      String var6 = var2.toString();
      BindingTypeName var7 = var5.lookupTypeFor(var2);
      BindingType var8 = var5.getBindingType(var7);
      JavaXmlTypeMappingBean var9 = var1.createJavaXmlTypeMapping();
      this.addPackageMapping(var6, var3);
      var9.setJavaType(var6);
      if (var3.isAnonymousType()) {
         var9.setAnonymousTypeQname(getAnonymousString(var3));
      } else {
         var9.setRootTypeQname(var3.getQName());
      }

      var9.setQnameScope(var4);
      if (var8 instanceof ByNameBean) {
         ByNameBean var10 = (ByNameBean)var8;
         this.addVariableMappings(var9, var10.getProperties());
         if (var10.getAnyElementProperty() != null) {
            VariableMappingBean var11 = var9.createVariableMapping();
            var11.createXmlWildcard();
            this.fillVariableMappingBean(var10.getAnyElementProperty(), var11);
         }
      } else if (var8 instanceof SimpleContentBean) {
         this.addVariableMappings(var9, ((SimpleContentBean)var8).getAttributeProperties());
      }

   }

   private void buildTypeMappingForElement(JavaWsdlMappingBean var1, JavaTypeName var2, XmlTypeName var3, XmlTypeName var4, String var5) {
      if (var2 == null) {
         throw new IllegalArgumentException("null jtn");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null xmlType");
      } else if (!var2.toString().equals("javax.xml.soap.SOAPElement")) {
         String var6 = var2.toString();
         JavaXmlTypeMappingBean var7 = var1.createJavaXmlTypeMapping();
         this.addPackageMapping(var6, var4);
         var7.setJavaType(var6);
         if (var3.isAnonymousType()) {
            var7.setAnonymousTypeQname(getAnonymousString(var3));
         } else {
            var7.setRootTypeQname(var3.getQName());
         }

         var7.setQnameScope(var5);
      }
   }

   private void addVariableMappings(JavaXmlTypeMappingBean var1, Collection var2) {
      QNameProperty var4;
      VariableMappingBean var5;
      for(Iterator var3 = var2.iterator(); var3.hasNext(); this.fillVariableMappingBean(var4, var5)) {
         var4 = (QNameProperty)var3.next();
         var5 = var1.createVariableMapping();
         if (var4.isAttribute()) {
            var5.setXmlAttributeName(var4.getQName().getLocalPart());
         } else {
            var5.setXmlElementName(var4.getQName().getLocalPart());
         }
      }

   }

   private void fillVariableMappingBean(QNameProperty var1, VariableMappingBean var2) {
      String var3 = var1.getFieldName();
      if (var3 != null) {
         var2.setJavaVariableName(var3);
         var2.createDataMember();
      } else {
         MethodName var4 = var1.getGetterName();
         if (var4 == null) {
            this.addError((Throwable)(new Exception("no getter for " + var1.getQName().getLocalPart())));
         }

         String var5 = var4.getSimpleName();
         String var6;
         if (var5.startsWith("is")) {
            var6 = var5.substring(2);
         } else {
            var6 = var5.substring(3);
         }

         var2.setJavaVariableName(var6);
      }

   }

   private List getBindingTypes() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getBindingFiles().iterator();

      while(var2.hasNext()) {
         Iterator var3 = ((BindingFile)var2.next()).bindingTypes().iterator();

         while(var3.hasNext()) {
            var1.add(var3.next());
         }
      }

      return var1;
   }

   private List getBindingFiles() {
      return Arrays.asList((Object[])this.mTylar.getBindingFiles());
   }

   private static String getAnonymousString(XmlTypeName var0) {
      try {
         return to109AnonymousTypeName(var0);
      } catch (IllegalArgumentException var2) {
         return var0.toString().trim();
      }
   }

   private static String to109AnonymousTypeName(XmlTypeName var0) {
      String var1 = var0.toString().trim();
      StringBuffer var2 = new StringBuffer();
      StringTokenizer var3 = new StringTokenizer(var1, "|");
      if (var1.length() < 1) {
         throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
      } else {
         boolean var4 = var1.charAt(0) == 'e';

         while(var3.hasMoreTokens()) {
            String var5 = var3.nextToken();
            char var6 = var5.charAt(0);
            int var7;
            switch (var6) {
               case 'd':
                  var7 = var5.indexOf(64);
                  if (var7 != -1 && var7 != var5.length() - 1) {
                     var2.insert(0, var5.substring(var7 + 1) + ":");
                     break;
                  }

                  throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
               case 'e':
                  if (var4 && var2.length() == 0) {
                     var2.append(var5);
                     break;
                  }

                  throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
               case 't':
                  int var8;
                  if (var5.length() == 1) {
                     if (!var3.hasMoreTokens()) {
                        throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
                     }

                     String var9 = var3.nextToken();
                     if ('e' != var9.charAt(0)) {
                        throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
                     }

                     if (var9.charAt(1) == '=' && var9.length() >= 3) {
                        var9 = var9.substring(2);
                        if (var2.length() == 0) {
                           var2.append(">" + var9);
                        } else {
                           var8 = findInsertPoint(var2);
                           var2.insert(var8, ">" + var9 + ">");
                        }
                        break;
                     }

                     throw new IllegalArgumentException("malformed XmlTypeName element decl: " + var1);
                  } else {
                     var7 = var5.indexOf(64);
                     if (var7 != -1 && var7 != var5.length() - 1) {
                        var8 = findInsertPoint(var2);
                        var2.insert(var8, var5.substring(2, var7) + ">");
                        var2.insert(0, var5.substring(var7 + 1) + ":");
                        break;
                     }

                     throw new IllegalArgumentException("malformed XmlTypeName document_type decl: " + var1);
                  }
               default:
                  throw new IllegalArgumentException("unupported XmlTypeName component '" + var6 + "' in: " + var1);
            }
         }

         return var2.toString();
      }
   }

   private static int findInsertPoint(StringBuffer var0) {
      if (var0.charAt(0) == '>') {
         int var1 = var0.length();

         for(int var2 = 0; var2 < var1; ++var2) {
            if (var0.charAt(var2) != '>') {
               return var2;
            }
         }

         throw new IllegalArgumentException(var0.toString() + " not a valid path.");
      } else {
         return 0;
      }
   }
}
