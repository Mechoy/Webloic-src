package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.Schema2Java;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xbean.schema.BuiltinSchemaTypeSystem;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaParticle;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.util.ExceptionUtil;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class XmlBeansBuildtimeBindings extends XmlBeansBaseBuildtimeBindings implements BuildtimeBindings {
   private static final boolean VERBOSE = Verbose.isVerbose(XmlBeansBuildtimeBindings.class);
   private static SchemaTypeLoader _baseLoader = BuiltinSchemaTypeSystem.get();

   public XmlBeansBuildtimeBindings(SchemaTypeLoader var1, SchemaDocument[] var2) throws XmlException {
      assert var1 != null;

      this._typeLoader = var1;
      this._documents = var2;

      try {
         this._emptyBindings = EmptyBuildtimeBindings.Factory.getInstance().loadFromURI((URI)null);
      } catch (Exception var4) {
         throw new XmlException(" ERROR getting EmptyBuildtimeBindings !  " + var4.getMessage());
      }
   }

   public String getClassFromSchemaType(QName var1) {
      SchemaType var2 = _baseLoader.findType(var1);
      if (var2 != null) {
         if (VERBOSE) {
            Verbose.log((Object)("  $$$$$$  for xmlType '" + var1 + "'  SchemaType is '" + var2 + "', fullJavaName from BEA baseLoader is '" + var2.getFullJavaName() + "'\n"));
         }

         return var2.getFullJavaName();
      } else {
         var2 = this._typeLoader.findType(var1);
         if (var2 == null) {
            return null;
         } else {
            if (VERBOSE) {
               Verbose.log((Object)("  $$$$$$  for xmlType '" + var1 + "'  SchemaType is '" + var2 + "', fullJavaName from BEA typeLoader is '" + var2.getFullJavaName() + "'\n"));
            }

            return var2.getFullJavaName();
         }
      }
   }

   public String getClassFromSchemaElement(QName var1) {
      SchemaType var2 = this._typeLoader.findDocumentType(var1);
      if (var2 != null) {
         return var2.getFullJavaName();
      } else {
         var2 = _baseLoader.findDocumentType(var1);
         return var2 == null ? null : var2.getFullJavaName();
      }
   }

   public QName getSchemaType(String var1) {
      SchemaType var2 = this._typeLoader.typeForClassname(var1);
      if (var2 != null) {
         return var2.getName();
      } else {
         var2 = _baseLoader.typeForClassname(var1);
         return var2 == null ? null : var2.getName();
      }
   }

   public Class getBuilderClass() {
      return XmlBeansBindingsBuilderImpl.class;
   }

   String getWildcardClassName() {
      return XmlObject.class.getName();
   }

   public LinkedHashMap getJavaTypesForWrapperElement(QName var1) {
      SchemaGlobalElement var2 = this._typeLoader.findElement(var1);
      if (var2 == null) {
         return null;
      } else {
         SchemaType var3 = var2.getType();
         if (!Schema2Java.hasWrapperFormat(var3, false)) {
            return null;
         } else {
            LinkedHashMap var4 = new LinkedHashMap();
            SchemaParticle var5 = var3.getContentModel();
            if (var5 != null) {
               if (var5.getParticleType() == 5) {
                  this.addAnyWildcard(var5, var4);
               } else if (var5.getParticleType() == 4) {
                  this.addElement(var5, var4, var1);
               } else {
                  SchemaParticle[] var6 = var5.getParticleChildren();
                  SchemaParticle[] var7 = var6;
                  int var8 = var6.length;

                  for(int var9 = 0; var9 < var8; ++var9) {
                     SchemaParticle var10 = var7[var9];
                     if (var10.getParticleType() == 5) {
                        this.addAnyWildcard(var10, var4);
                     } else if (var10.getParticleType() == 4) {
                        this.addElement(var10, var4, var1);
                     }
                  }
               }
            }

            return var4;
         }
      }
   }

   private void addElement(SchemaParticle var1, LinkedHashMap var2, QName var3) {
      SchemaType var4 = var1.getType();
      QName var5 = var4.getName();
      String var6 = this.getClassName(var5);
      if (!StringUtil.isEmpty(var6)) {
         var2.put(var1.getName().getLocalPart(), var6);
      } else {
         boolean var7 = false;
         LinkedHashMap var8 = (LinkedHashMap)this._S2JWrapperElements.get(var3);
         if (var8 != null) {
            Map.Entry var9 = this.getCurrentParameter(var8, var2.size());

            assert var9 != null;

            String var10 = (String)var9.getValue();
            int var11 = var10.indexOf("[]");
            boolean var12 = var11 != -1;
            if (var12) {
               String var13 = var10.substring(0, var11);
               if (ExceptionUtil.classNameIsSchemaBuiltin(var13)) {
                  var2.put(var9.getKey(), var9.getValue());
                  var7 = true;
               } else if (this.requiresWildcardClass(var13)) {
                  var2.put(var9.getKey(), this.getWildcardClassName() + var10.substring(var11));
                  var7 = true;
               } else {
                  SchemaType var14 = this._typeLoader.findType(var5);
                  if (var14 != null) {
                     SchemaParticle var15 = var14.getContentModel();
                     if (var15 != null && var15.getParticleType() == 4) {
                        SchemaType var16 = this._typeLoader.findDocumentType(var15.getName());
                        if (var16 != null) {
                           var13 = var16.getFullJavaName();
                        }

                        var2.put(var9.getKey(), var13 + "[]");
                        var7 = true;
                     }
                  }
               }
            }
         }

         if (!var7) {
            SchemaType var17 = this._typeLoader.findDocumentType(var1.getName());
            if (var17 != null) {
               var4 = var17;
            }

            var2.put(var1.getName().getLocalPart(), var4.getFullJavaName());
         }
      }

   }

   private void addAnyWildcard(SchemaParticle var1, LinkedHashMap var2) {
      String var3 = this.getWildcardClassName();
      if (var1.getMaxOccurs() == null || var1.getMaxOccurs().compareTo(BigInteger.ONE) > 0) {
         var3 = var3 + "[]";
      }

      var2.put(XmlTypeName.ANY_ELEMENT_WILDCARD_ELEMENT_NAME.getLocalPart(), var3);
   }

   public static class Factory implements BuildtimeBindings.Factory {
      private static Factory instance;

      private Factory() {
      }

      public BuildtimeBindings loadFromURI(URI var1) throws IOException, XmlException {
         URLClassLoader var2 = new URLClassLoader(new URL[]{var1.toURL()});
         SchemaTypeLoader var3 = XmlBeans.typeLoaderForClassLoader(var2);
         return new XmlBeansBuildtimeBindings(var3, (SchemaDocument[])null);
      }

      public static Factory getInstance() {
         if (instance == null) {
            instance = new Factory();
         }

         return instance;
      }
   }
}
