package weblogic.wsee.policy.runtime.schema.impl;

import com.bea.xbean.values.JavaStringEnumerationHolderEx;
import com.bea.xml.SchemaType;
import weblogic.wsee.policy.runtime.schema.CategoryEnum;

public class CategoryEnumImpl extends JavaStringEnumerationHolderEx implements CategoryEnum {
   private static final long serialVersionUID = 1L;

   public CategoryEnumImpl(SchemaType var1) {
      super(var1, false);
   }

   protected CategoryEnumImpl(SchemaType var1, boolean var2) {
      super(var1, var2);
   }
}
