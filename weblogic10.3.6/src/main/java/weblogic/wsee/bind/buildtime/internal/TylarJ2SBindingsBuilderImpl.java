package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.Java2SchemaWrapperElement;
import com.bea.staxb.buildtime.internal.annobeans.ClassBindingInfoBean;
import com.bea.staxb.buildtime.internal.annobeans.TargetSchemaTypeBean;
import com.bea.staxb.buildtime.internal.annobeans.TargetTopLevelElementBean;
import com.bea.staxb.buildtime.internal.tylar.CompositeTylar;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.util.annogen.override.AnnoBeanSet;
import com.bea.util.annogen.override.JamElementIdPool;
import com.bea.util.annogen.override.StoredAnnoOverrider;
import com.bea.util.annogen.view.AnnoViewerParams;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JamClassLoader;
import com.bea.util.jam.JamServiceFactory;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import weblogic.jws.WildcardParticle;
import weblogic.wsee.bind.EncodingStyles;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.J2SBindingsBuilder;
import weblogic.wsee.bind.buildtime.TylarJ2SBindingsBuilder;
import weblogic.wsee.util.ClassUtil;
import weblogic.wsee.util.Verbose;

public class TylarJ2SBindingsBuilderImpl extends TylarBindingsBuilderBase implements TylarJ2SBindingsBuilder {
   static final String SOAPELEMENT_CLASSNAME = SOAPElement.class.getName();
   private static final String JAVA_UTIL_NAMESPACE = "language-builtins.util";
   public static final QName JAVA_LIST_QNAME = new QName("language-builtins.util", "List");
   private static final JClass SOAPELEMENT_JCLASS;
   private File[] xsdConfigFiles = null;
   private static final boolean VERBOSE = Verbose.isVerbose(TylarJ2SBindingsBuilderImpl.class);
   protected SoapAwareJava2Schema mBinder;
   private StoredAnnoOverrider mOverrides = null;
   private JamElementIdPool mIdPool = null;

   public TylarJ2SBindingsBuilderImpl() {
      if (VERBOSE) {
         Verbose.log((Object)"Constructed a TylarJ2SBindingsBuilderImpl");
      }

      this.mBinder = new SoapAwareJava2Schema(this);
      this.mBinder.setJaxRpcRules(true);
   }

   private StoredAnnoOverrider getOverrides() {
      if (this.mOverrides == null) {
         this.mOverrides = com.bea.util.annogen.override.StoredAnnoOverrider.Factory.create();
      }

      return this.mOverrides;
   }

   private JamElementIdPool getIdPool() {
      if (this.mIdPool == null) {
         this.mIdPool = com.bea.util.annogen.override.JamElementIdPool.Factory.create();
      }

      return this.mIdPool;
   }

   public void addWildcardBinding(String var1, WildcardParticle var2) {
      this.mBinder.addWildcardBinding(var1, com.bea.staxb.buildtime.WildcardParticle.valueOf(var2.toString()));
   }

   public void javaTypeToSchemaType(JClass var1, JClass var2, int var3) {
      if (var2 == null) {
         throw new IllegalArgumentException("null clazz");
      } else if (TylarBuildtimeBindings.isSpecialJavaType(var2.getQualifiedName())) {
         this.mBinder.addSpecialTypeQNames(TylarBuildtimeBindings.getSpecialJavaType(var2.getQualifiedName()));
      } else if (!var2.getQualifiedName().equals(SOAPELEMENT_CLASSNAME)) {
         if (VERBOSE) {
            Verbose.log((Object)("bindJavaType " + var2.getQualifiedName()));
         }

         boolean var4 = EncodingStyles.isEncoded(var3);
         if (var2.isArrayType()) {
            if (var4) {
               this.mBinder.bindJavaArrayToSoapArray(ClassUtil.getTargetNamespace(var1), var1, var2);
            } else {
               this.mBinder.bindJavaArrayToLiteralArray(ClassUtil.getTargetNamespace(var1), var1, var2);
            }
         } else if (var3 == 6) {
            this.mBinder.addEncodedClassToBind(var2);
         } else {
            this.mBinder.addClassToBind(var2);
         }

      }
   }

   public void javaTypeToSchemaElement(JClass var1, JClass var2, QName var3, int var4) {
      if (var2 == null) {
         throw new IllegalArgumentException("null clazz");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null elemName");
      } else {
         if (VERBOSE) {
            Verbose.log((Object)("bindJavaTypeToSchemaElement " + var2.getQualifiedName() + "  " + var3));
            Verbose.log((Object)("\n\n" + SOAPELEMENT_JCLASS.getQualifiedName() + "  " + var2.getQualifiedName() + "  " + SOAPELEMENT_JCLASS.isAssignableFrom(var2)));
         }

         if (isSoapElement(var2)) {
            this.mBinder.addSoapElement(var3);
         } else {
            if (TylarBuildtimeBindings.isSpecialJavaType(var2.getQualifiedName())) {
               this.mBinder.addSpecialJavaElement(var3, var2);
               return;
            }

            if (var2.isArrayType()) {
               if (EncodingStyles.isEncoded(var4)) {
                  this.mBinder.bindJavaArrayToSoapArray(ClassUtil.getTargetNamespace(var1), var1, var2, var3);
               } else {
                  if (!EncodingStyles.isLiteral(var4)) {
                     throw new IllegalArgumentException("unknown encodingStyle, neither literal nor encoded: " + var4);
                  }

                  this.mBinder.bindJavaArrayToLiteralArray(ClassUtil.getTargetNamespace(var1), var1, var2, var3);
               }
            } else {
               this.mBinder.addClassToBind(var2);
               this.addTargetElement(var2, var3);
               Java2SchemaTypeWrapperElement var5 = new Java2SchemaTypeWrapperElement(var1, var3, var2);
               this.mBinder.addTypeWrapperElement(var5);
            }
         }

      }
   }

   public void wrapJavaTypeToSchemaElement(JClass var1, JClass[] var2, String[] var3, QName var4) {
      Java2SchemaWrapperElement var5 = new Java2SchemaWrapperElement(var1, var4, var2, var3);
      this.mBinder.addWrapperElement(var5);
   }

   public BuildtimeBindings createBuildtimeBindings(File var1) throws IOException, XmlException {
      if (var1 == null) {
         throw new IllegalArgumentException("null outputDir");
      } else {
         if (VERBOSE) {
            Verbose.banner("TylarJ2SBindingsBuilderImpl.createBuildtimeBindings");
            Verbose.log((Object)("generating BuildtimeBindings in " + var1));
         }

         if (this.xsdConfigFiles != null) {
            this.mBinder.setBindingConfig(this.getBindingConfig(this.xsdConfigFiles));
         }

         Tylar var2 = this.getBaseTypeLibraries();
         if (var2 != null) {
            this.mBinder.setBaseLibrary(var2);
         }

         this.mBinder.setVerbose(VERBOSE);
         if (this.mOverrides != null) {
            AnnoViewerParams var3 = com.bea.util.annogen.view.AnnoViewerParams.Factory.create();
            var3.addOverrider(this.mOverrides);
            this.mBinder.setAnnoViewer(com.bea.util.annogen.view.JamAnnoViewer.Factory.create(var3));
         }

         Object var4 = this.mBinder.bindAsExplodedTylar(var1);
         if (var4 == null) {
            throw new IOException("binding failed, check log for details");
         } else {
            if (var2 != null) {
               var4 = new CompositeTylar(new Tylar[]{(Tylar)var4, var2});
            }

            return new TylarBuildtimeBindings((Tylar)var4, var1, J2SBindingsBuilder.class, this.getXmlObjectClassLoader());
         }
      }
   }

   public void setXsdConfig(File[] var1) {
      this.xsdConfigFiles = var1;
   }

   public static boolean isSoapElement(JClass var0) {
      return SOAPELEMENT_JCLASS.isAssignableFrom(var0);
   }

   private void addTargetElement(JClass var1, QName var2) {
      StoredAnnoOverrider var3 = this.getOverrides();
      AnnoBeanSet var4 = var3.findOrCreateStoredAnnoSetFor(this.getIdPool().getIdFor(var1));
      ClassBindingInfoBean var5 = (ClassBindingInfoBean)var4.findOrCreateBeanFor(ClassBindingInfoBean.class);
      TargetSchemaTypeBean var6 = var5.schemaType();
      if (var6 == null) {
         var6 = new TargetSchemaTypeBean();
         var5.set_schemaType(var6);
      }

      TargetTopLevelElementBean var7 = new TargetTopLevelElementBean();
      var7.set_localName(var2.getLocalPart());
      var7.set_namespaceUri(var2.getNamespaceURI());
      TargetTopLevelElementBean[] var8 = var6.topLevelElements();
      if (var8 != null && var8.length != 0) {
         TargetTopLevelElementBean[] var9 = new TargetTopLevelElementBean[var8.length + 1];
         System.arraycopy(var8, 0, var9, 0, var8.length);
         var9[var9.length - 1] = var7;
         var6.set_topLevelElements(var9);
      } else {
         var6.set_topLevelElements(new TargetTopLevelElementBean[]{var7});
      }

   }

   public void setJaxRpcByteArrayStyle(boolean var1) {
      this.mBinder.setJaxRpcByteArrayStyle(var1);
   }

   public void setUpperCasePropName(boolean var1) {
      this.mBinder.setUpperCasePropName(var1);
   }

   public void setLocalElementDefaultRequired(boolean var1) {
      this.mBinder.setLocalElementDefaultRequired(var1);
   }

   public void setLocalElementDefaultNillable(boolean var1) {
      this.mBinder.setLocalElementDefaultNillable(var1);
   }

   static {
      ClassLoader var0 = TylarJ2SBindingsBuilderImpl.class.getClassLoader();
      JamClassLoader var1 = JamServiceFactory.getInstance().createJamClassLoader(var0);
      if (var1 == null) {
         throw new IllegalStateException("null sysCl");
      } else {
         JClass var2 = var1.loadClass(SOAPELEMENT_CLASSNAME);
         if (var2 == null) {
            throw new IllegalStateException("null jclass");
         } else if (var2.isUnresolvedType()) {
            throw new IllegalStateException(SOAPELEMENT_CLASSNAME + " is not in the classpath");
         } else {
            SOAPELEMENT_JCLASS = var2;
         }
      }
   }
}
