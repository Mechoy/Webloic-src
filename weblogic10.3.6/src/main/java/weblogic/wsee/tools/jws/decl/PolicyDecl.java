package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import weblogic.jws.Policy;
import weblogic.jws.Policy.Direction;
import weblogic.wsee.util.JamUtil;
import weblogic.wsee.util.StringUtil;

public class PolicyDecl {
   private boolean attachToWsdl = false;
   private String uri = null;
   private URI policyURI = null;
   private Policy.Direction direction;
   private boolean builtInPolicy;
   private String uriError;

   PolicyDecl(File var1, JAnnotation var2) {
      this.direction = Direction.both;
      this.builtInPolicy = false;
      this.uriError = null;
      this.attachToWsdl = JamUtil.getAnnotationBooleanValue(var2, "attachToWsdl", false);
      this.uri = JamUtil.getAnnotationStringValue(var2, "uri");
      this.direction = (Policy.Direction)JamUtil.getAnnotationEnumValue(var2, "direction", Policy.Direction.class, Direction.both);
      if (StringUtil.isEmpty(this.uri)) {
         this.uriError = "uri must not be empty.";
      } else {
         if (this.uri.startsWith("policy:")) {
            this.uri = baseName(this.uri);
            this.builtInPolicy = true;
         }

         if (this.isRelativeUri()) {
            if (var1 != null) {
               File var3 = new File(var1, this.uri);
               this.uri = var3.getName();
               this.policyURI = var3.toURI();
            } else {
               this.uriError = "Unable to use relative path with class file.";
            }
         } else {
            try {
               this.policyURI = new URI(this.uri);
            } catch (URISyntaxException var4) {
               this.uriError = var4.getMessage();
            }
         }
      }

   }

   public boolean isRelativeUri() {
      if (this.uri.indexOf(58) > 0) {
         return false;
      } else {
         return !this.uri.startsWith("/");
      }
   }

   public boolean isAttachToWsdl() {
      return this.attachToWsdl;
   }

   public String getUri() {
      return this.uri;
   }

   public String getUriWithoutExt() {
      return baseName(this.uri);
   }

   public String getBuiltInUriWithoutPrefix() {
      if (this.builtInPolicy) {
         return this.uri.substring("policy:".length());
      } else {
         throw new RuntimeException("The current PolicyDecl is not a built-in typed!");
      }
   }

   public Policy.Direction getDirection() {
      return this.direction;
   }

   public boolean isBuiltInPolicy() {
      return this.builtInPolicy;
   }

   public URI getPolicyURI() {
      return this.policyURI;
   }

   public String getUriError() {
      return this.uriError;
   }

   private static String baseName(String var0) {
      if (var0.endsWith(".xml")) {
         var0 = var0.substring(0, var0.lastIndexOf(".xml"));
      }

      return var0;
   }
}
