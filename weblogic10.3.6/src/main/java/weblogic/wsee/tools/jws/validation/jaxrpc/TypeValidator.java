package weblogic.wsee.tools.jws.validation.jaxrpc;

import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BuiltinBindingLoader;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JConstructor;
import com.bea.util.jam.JField;
import com.bea.util.jam.JProperty;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.holders.Holder;
import weblogic.wsee.tools.jws.JwsLogEvent;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebResultDecl;
import weblogic.wsee.tools.jws.decl.WebTypeDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.LogEvent;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.util.XBeanUtil;

class TypeValidator {
   private BindingLoader bindingLoader = BuiltinBindingLoader.getBuiltinBindingLoader(true);
   private final Logger logger;
   private final WebMethodDecl method;

   TypeValidator(Logger var1, WebMethodDecl var2) {
      this.logger = var1;
      this.method = var2;
   }

   void validate(WebTypeDecl var1) {
      this.validate(var1, (JClass)null, var1.getJClass(), new HashSet());
   }

   private void validate(WebTypeDecl var1, JClass var2, JClass var3, Set<JClass> var4) {
      JClass var5 = getRealType(var3);
      if (!var4.contains(var5)) {
         var4.add(var5);
         if (!this.isBuiltinType(var5)) {
            this.checkUnsupportedTypes(var5, var1);
            if (this.checkXmlBean(var5, var2)) {
               this.checkJavaBean(var5, var1);
               this.checkChildren(var5, var1, var4);
            }

         }
      }
   }

   private void checkChildren(JClass var1, WebTypeDecl var2, Set<JClass> var3) {
      JProperty[] var4 = var1.getProperties();
      int var5 = var4.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         JProperty var7 = var4[var6];
         if (var7.getSetter() != null && var7.getGetter() != null) {
            this.validate(var2, var1, var7.getType(), var3);
         }
      }

      JField[] var8 = var1.getFields();
      var5 = var8.length;

      for(var6 = 0; var6 < var5; ++var6) {
         JField var9 = var8[var6];
         if (var9.isPublic() && !var9.isStatic() && !var9.isFinal()) {
            this.validate(var2, var1, var9.getType(), var3);
         }
      }

   }

   private boolean checkXmlBean(JClass var1, JClass var2) {
      if (XBeanUtil.isXmlBean(var1)) {
         if (var2 == null) {
            return false;
         }

         if (XBeanUtil.isXmlBean(var2)) {
            return false;
         }

         JClass var3 = var2.getClassLoader().loadClass(Holder.class.getName());
         if (var3.isAssignableFrom(var2)) {
            return false;
         }

         this.logError("binding.javaBeanWithXmlBeanPropertyOrField", var2.getQualifiedName(), var1.getQualifiedName());
      }

      return true;
   }

   private void checkJavaBean(JClass var1, WebTypeDecl var2) {
      if (!var1.isInterface() && !hasDefaultConstructor(var1)) {
         if (var2 instanceof WebResultDecl) {
            this.logWarning("parameter.binding.invalidReturn", var1.getQualifiedName(), this.method.getJMethod().getSimpleName());
         } else {
            this.logWarning("parameter.binding.invalidParameter", var1.getQualifiedName(), var2.getName(), this.method.getJMethod().getSimpleName());
         }
      }

   }

   private void checkUnsupportedTypes(JClass var1, WebTypeDecl var2) {
      JClass var3 = var1.getClassLoader().loadClass(Map.class.getName());
      if (var1.isAssignableFrom(var3)) {
         this.logError("binding.invalidType", var1.getQualifiedName());
      }

      if (var1.isEnumType()) {
         this.logError("unsupported.enumTypes", var1.getQualifiedName());
      }

   }

   private static JClass getRealType(JClass var0) {
      JClass var1 = var0;
      if (var0.isArrayType()) {
         var1 = var0.getArrayComponentType();
      }

      return var1;
   }

   private boolean isBuiltinType(JClass var1) {
      return this.bindingLoader.lookupTypeFor(JavaTypeName.forJClass(var1)) != null;
   }

   private static boolean hasDefaultConstructor(JClass var0) {
      JConstructor[] var1 = var0.getConstructors();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         JConstructor var4 = var1[var3];
         if (var4.isPublic() && var4.getParameters().length == 0) {
            return true;
         }
      }

      return false;
   }

   private void logError(String var1, Object... var2) {
      this.logger.log(EventLevel.ERROR, (LogEvent)(new JwsLogEvent(this.method.getJMethod(), var1, var2)));
   }

   private void logWarning(String var1, Object... var2) {
      this.logger.log(EventLevel.WARNING, (LogEvent)(new JwsLogEvent(this.method.getJMethod(), var1, var2)));
   }
}
