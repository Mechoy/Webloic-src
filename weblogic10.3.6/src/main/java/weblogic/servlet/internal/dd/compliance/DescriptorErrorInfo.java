package weblogic.servlet.internal.dd.compliance;

import weblogic.j2ee.validation.IDescriptorErrorInfo;

public class DescriptorErrorInfo implements IDescriptorErrorInfo {
   private Object key;
   private Object[] elementErrorKeys = new Object[1];
   private String[] elements = new String[1];

   public DescriptorErrorInfo(String var1, Object var2, Object var3) {
      this.elements[0] = var1;
      this.key = var2;
      this.elementErrorKeys[0] = var3;
   }

   public DescriptorErrorInfo(String[] var1, Object var2, Object[] var3) {
      this.elements = var1;
      this.key = var2;
      this.elementErrorKeys = var3;
   }

   public Object getTopLevelSearchKey() {
      return this.key;
   }

   public Object[] getElementErrorKeys() {
      return this.elementErrorKeys;
   }

   public String[] getElementTypes() {
      return this.elements;
   }
}
