package weblogic.wsee.bind.buildtime;

import java.io.File;
import weblogic.jws.WildcardParticle;

public interface TylarJ2SBindingsBuilder extends J2SBindingsBuilder {
   void setXsdConfig(File[] var1);

   void addWildcardBinding(String var1, WildcardParticle var2);

   void setJaxRpcByteArrayStyle(boolean var1);

   void setUpperCasePropName(boolean var1);

   void setLocalElementDefaultRequired(boolean var1);

   void setLocalElementDefaultNillable(boolean var1);
}
