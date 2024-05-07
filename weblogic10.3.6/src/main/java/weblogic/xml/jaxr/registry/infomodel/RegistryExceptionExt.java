package weblogic.xml.jaxr.registry.infomodel;

import java.io.Serializable;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.BaseJAXRObject;

public class RegistryExceptionExt extends BaseJAXRObject implements Serializable {
   private Class m_type;
   private String m_message;
   private static final long serialVersionUID = -1L;

   public RegistryExceptionExt(JAXRException var1) {
      this.m_type = var1.getClass();
      this.m_message = var1.getMessage();
   }

   public Class getType() {
      return this.m_type;
   }

   public String getMessage() {
      return this.m_message;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_type, this.m_message};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_type", "m_message"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
