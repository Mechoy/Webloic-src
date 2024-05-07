package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Slot;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class SlotImpl extends BaseInfoModelObject implements Slot {
   private static final long serialVersionUID = -1L;
   private String m_name;
   private String m_slotType;
   private ArrayList m_values = new ArrayList();

   public SlotImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public SlotImpl(Slot var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_name = var1.getName();
         this.m_slotType = var1.getSlotType();
         this.m_values = new ArrayList(var1.getValues());
      }

   }

   public String getName() throws JAXRException {
      return this.m_name;
   }

   public void setName(String var1) throws JAXRException {
      this.m_name = var1;
   }

   public String getSlotType() throws JAXRException {
      return this.m_slotType;
   }

   public void setSlotType(String var1) throws JAXRException {
      this.m_slotType = var1;
   }

   public Collection getValues() throws JAXRException {
      return this.m_values;
   }

   public void setValues(Collection var1) throws JAXRException {
      this.m_values = new ArrayList();
      if (var1 != null) {
         this.m_values.addAll(var1);
      }

   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_name, this.m_slotType, this.m_values};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_name", "m_slotType", "m_values"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
