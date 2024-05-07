package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.xml.XmlObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.soap.SOAPElement;
import weblogic.jws.WildcardBindings;
import weblogic.jws.WildcardParticle;
import weblogic.wsee.util.JamUtil;

public class WildcardBindingsDecl {
   private static final Map<String, WildcardParticle> DEFAULT_BINDINGS = new HashMap();
   private Map<String, WildcardParticle> bindings = new HashMap();

   public WildcardBindingsDecl(JClass var1) {
      this.bindings.putAll(DEFAULT_BINDINGS);
      JAnnotation var2 = var1.getAnnotation(WildcardBindings.class);
      if (var2 != null) {
         JAnnotationValue var3 = var2.getValue("value");
         if (var3 != null) {
            this.processBindingAnnotations(var3.asAnnotationArray());
         }
      }

   }

   private void processBindingAnnotations(JAnnotation[] var1) {
      if (var1 != null) {
         JAnnotation[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            JAnnotation var5 = var2[var4];
            String var6 = JamUtil.getAnnotationStringValue(var5, "className");
            WildcardParticle var7 = (WildcardParticle)JamUtil.getAnnotationEnumValue(var5, "binding", WildcardParticle.class, WildcardParticle.ANYTYPE);
            this.bindings.put(var6, var7);
         }
      }

   }

   public Map<String, WildcardParticle> getBindings() {
      return Collections.unmodifiableMap(this.bindings);
   }

   static {
      DEFAULT_BINDINGS.put(SOAPElement.class.getName(), WildcardParticle.ANY);
      DEFAULT_BINDINGS.put(SOAPElement.class.getName() + "[]", WildcardParticle.ANY);
      DEFAULT_BINDINGS.put(XmlObject.class.getName(), WildcardParticle.ANY);
      DEFAULT_BINDINGS.put(XmlObject.class.getName() + "[]", WildcardParticle.ANYTYPE);
      DEFAULT_BINDINGS.put(org.apache.xmlbeans.XmlObject.class.getName(), WildcardParticle.ANY);
      DEFAULT_BINDINGS.put(org.apache.xmlbeans.XmlObject.class.getName() + "[]", WildcardParticle.ANYTYPE);
   }
}
