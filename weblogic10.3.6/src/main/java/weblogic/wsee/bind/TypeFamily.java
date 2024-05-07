package weblogic.wsee.bind;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.JParameter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import weblogic.wsee.bind.buildtime.BuildtimeBindings;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.bind.buildtime.internal.EmptyBuildtimeBindings;
import weblogic.wsee.bind.buildtime.internal.TylarBuildtimeBindings;
import weblogic.wsee.bind.buildtime.internal.TylarS2JBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.XmlBeansApacheBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.XmlBeansApacheBuildtimeBindings;
import weblogic.wsee.bind.buildtime.internal.XmlBeansBindingsBuilderImpl;
import weblogic.wsee.bind.buildtime.internal.XmlBeansBuildtimeBindings;

public final class TypeFamily {
   private static HashMap<String, TypeFamily> keyMap = new HashMap();
   private String id;
   private String jarName;
   private BuildtimeBindings.Factory buildtimeBindingsFactory;
   private S2JBindingsBuilder s2jBindingsBuilder;
   public static final TypeFamily TYLAR = new TypeFamily("TYLAR", "tylar", TylarBuildtimeBindings.Factory.getInstance(), new TylarS2JBindingsBuilderImpl());
   public static final TypeFamily XMLBEANS = new TypeFamily("XMLBEANS", "xmlbeans", XmlBeansBuildtimeBindings.Factory.getInstance(), new XmlBeansBindingsBuilderImpl());
   public static final TypeFamily XMLBEANS_APACHE = new TypeFamily("XMLBEANS_APACHE", "xmlbeans_apache", XmlBeansApacheBuildtimeBindings.Factory.getInstance(), new XmlBeansApacheBindingsBuilderImpl());
   public static final TypeFamily NO_COMPLEX_TYPES = new TypeFamily("NO_COMPLEX_TYPES", "noCmplxTypes", EmptyBuildtimeBindings.Factory.getInstance(), (S2JBindingsBuilder)null);

   private TypeFamily(String var1, String var2, BuildtimeBindings.Factory var3, S2JBindingsBuilder var4) {
      this.id = var1;
      this.jarName = var2;
      this.buildtimeBindingsFactory = var3;
      this.s2jBindingsBuilder = var4;
   }

   public String getId() {
      return this.id;
   }

   public String getJarName() {
      return this.jarName;
   }

   public BuildtimeBindings.Factory getBuildtimeBindingsFactory() {
      return this.buildtimeBindingsFactory;
   }

   public S2JBindingsBuilder getS2JBindingsBuilder() {
      return this.s2jBindingsBuilder;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof TypeFamily)) {
         return false;
      } else {
         TypeFamily var2 = (TypeFamily)var1;
         return var2.getId().equals(this.id);
      }
   }

   public String toString() {
      return "id='" + this.id + "',  builderClass='" + this.getS2JBindingsBuilder().getClass().getName() + "'";
   }

   public static TypeFamily getTypeFamilyForKey(String var0) {
      return (TypeFamily)keyMap.get(var0.toUpperCase(Locale.ENGLISH));
   }

   public static Set<String> getTypeFamilyKeys() {
      return keyMap.keySet();
   }

   public static TypeFamily getTypeFamilyForClass(JClass var0) {
      JMethod[] var1 = var0.getMethods();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         JMethod var4 = var1[var3];
         if (isMatch(var4.getReturnType(), XmlObject.class)) {
            return XMLBEANS_APACHE;
         }

         if (isMatch(var4.getReturnType(), com.bea.xml.XmlObject.class)) {
            return XMLBEANS;
         }

         JParameter[] var5 = var4.getParameters();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            JParameter var8 = var5[var7];
            if (isMatch(var8.getType(), XmlObject.class)) {
               return XMLBEANS_APACHE;
            }

            if (isMatch(var8.getType(), com.bea.xml.XmlObject.class)) {
               return XMLBEANS;
            }
         }
      }

      return TYLAR;
   }

   private static boolean isMatch(JClass var0, Class var1) {
      JClass var2 = var0.forName(var1.getName());
      return var2.isUnresolvedType() ? false : var2.isAssignableFrom(var0);
   }

   static {
      keyMap.put(TYLAR.getId().toUpperCase(Locale.ENGLISH), TYLAR);
      keyMap.put(XMLBEANS.getId().toUpperCase(Locale.ENGLISH), XMLBEANS);
      keyMap.put(XMLBEANS_APACHE.getId().toUpperCase(Locale.ENGLISH), XMLBEANS_APACHE);
      keyMap.put(NO_COMPLEX_TYPES.getId().toUpperCase(Locale.ENGLISH), NO_COMPLEX_TYPES);
   }
}
