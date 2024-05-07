package weblogic.xml.jaxr.registry.infomodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ExtensibleObject;
import javax.xml.registry.infomodel.Slot;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.JAXRUtil;

public abstract class ExtensibleObjectImpl extends BaseInfoModelObject implements ExtensibleObject {
   private Map m_slots;
   private static final long serialVersionUID = -1L;

   protected ExtensibleObjectImpl(RegistryServiceImpl var1) {
      super(var1);
      this.m_slots = new HashMap();
   }

   protected ExtensibleObjectImpl(ExtensibleObject var1, RegistryServiceImpl var2) throws JAXRException {
      this(var2);
      if (var1 != null) {
         Iterator var3 = var1.getSlots().iterator();
         this.m_slots = new HashMap();

         while(var3.hasNext()) {
            Slot var4 = (Slot)var3.next();
            SlotImpl var5 = new SlotImpl(var4, var2);
            this.m_slots.put(var5.getName(), var5);
         }
      }

   }

   public void addSlot(Slot var1) throws JAXRException {
      if (this.isInvalidSlot(var1)) {
         String var2 = JAXRMessages.getMessage("jaxr.extensibleObject.invalidSlot", new Object[]{var1});
         throw new InvalidRequestException(var2);
      } else {
         this.m_slots.put(var1.getName(), var1);
      }
   }

   public void addSlots(Collection var1) throws JAXRException {
      if (var1 == null) {
         String var4 = JAXRMessages.getMessage("jaxr.extensibleObject.invalidSlot", new Object[]{var1});
         throw new InvalidRequestException(var4);
      } else {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Slot var3 = (Slot)var2.next();
            this.addSlot(var3);
         }

      }
   }

   public void removeSlot(String var1) throws JAXRException {
      if (var1 == null) {
         String var2 = JAXRMessages.getMessage("jaxr.extensibleObject.invalidSlot", new Object[]{var1});
         throw new InvalidRequestException(var2);
      } else {
         this.m_slots.remove(var1);
      }
   }

   public void removeSlots(Collection var1) throws JAXRException {
      JAXRUtil.verifyCollectionContent(var1, String.class);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.removeSlot(var3);
      }

   }

   public Slot getSlot(String var1) throws JAXRException {
      if (var1 != null && var1.length() != 0) {
         return (Slot)this.m_slots.get(var1);
      } else {
         String var2 = JAXRMessages.getMessage("jaxr.extensibleObject.invalidSlot", new Object[]{var1});
         throw new InvalidRequestException(var2);
      }
   }

   public Collection getSlots() throws JAXRException {
      ArrayList var1 = new ArrayList(this.m_slots.values());
      return var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_slots};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_slots"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }

   private boolean isInvalidSlot(Slot var1) throws JAXRException {
      return var1 == null || var1.getName() == null || var1.getName().length() == 0;
   }
}
