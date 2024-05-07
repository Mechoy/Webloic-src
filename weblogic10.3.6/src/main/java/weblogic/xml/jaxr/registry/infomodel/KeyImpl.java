package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Key;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class KeyImpl extends BaseInfoModelObject implements Key {
   private static final long serialVersionUID = -1L;
   private String m_id;

   public KeyImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public KeyImpl(String var1, RegistryServiceImpl var2) {
      this(var2);
      if (var1 == null) {
         this.m_id = null;
      } else {
         this.m_id = var1;
      }

   }

   public KeyImpl(Key var1, RegistryServiceImpl var2) throws JAXRException {
      this(var1.getId(), var2);
   }

   public String getId() throws JAXRException {
      return this.m_id;
   }

   public void setId(String var1) throws JAXRException {
      if (var1 == null) {
         this.m_id = null;
      } else {
         this.m_id = var1;
      }

   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_id};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_id"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
