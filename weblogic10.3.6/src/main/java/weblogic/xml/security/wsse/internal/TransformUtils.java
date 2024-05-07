package weblogic.xml.security.wsse.internal;

import weblogic.xml.security.transforms.Transform;

public class TransformUtils {
   private static final String[] neutralTransforms = new String[]{"http://www.w3.org/2001/10/xml-exc-c14n#", "http://www.w3.org/TR/2001/REC-xml-c14n-20010315"};

   public static final boolean preservesSemantics(Transform var0) {
      String var1 = var0.getURI();
      return "http://www.w3.org/2001/10/xml-exc-c14n#".equals(var1) || "http://www.w3.org/TR/2001/REC-xml-c14n-20010315".equals(var1);
   }
}
