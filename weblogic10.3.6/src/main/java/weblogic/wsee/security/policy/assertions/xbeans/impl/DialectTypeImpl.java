package weblogic.wsee.security.policy.assertions.xbeans.impl;

import com.bea.xbean.values.JavaUriHolderEx;
import com.bea.xml.SchemaType;
import weblogic.wsee.security.policy.assertions.xbeans.DialectType;

public class DialectTypeImpl extends JavaUriHolderEx implements DialectType {
   private static final long serialVersionUID = 1L;

   public DialectTypeImpl(SchemaType var1) {
      super(var1, false);
   }

   protected DialectTypeImpl(SchemaType var1, boolean var2) {
      super(var1, var2);
   }
}
