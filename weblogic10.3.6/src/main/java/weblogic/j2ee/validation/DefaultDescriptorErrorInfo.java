package weblogic.j2ee.validation;

public abstract class DefaultDescriptorErrorInfo implements IDescriptorErrorInfo {
   private Object m_key;
   private Object[] m_elementErrorKeys = new Object[1];
   private String[] m_elements = new String[1];

   public DefaultDescriptorErrorInfo(String var1, Object var2, Object var3) {
      this.m_elements[0] = var1;
      this.m_key = var2;
      this.m_elementErrorKeys[0] = var3;
   }

   public DefaultDescriptorErrorInfo(String[] var1, Object var2, Object[] var3) {
      this.m_elements = var1;
      this.m_key = var2;
      this.m_elementErrorKeys = var3;
   }

   public DefaultDescriptorErrorInfo(String[] var1, Object var2, Object var3) {
      this.m_elements = var1;
      this.m_key = var2;
      this.m_elementErrorKeys[0] = var3;
   }

   public Object getTopLevelSearchKey() {
      return this.m_key;
   }

   public Object[] getElementErrorKeys() {
      return this.m_elementErrorKeys;
   }

   public String[] getElementTypes() {
      return this.m_elements;
   }
}
