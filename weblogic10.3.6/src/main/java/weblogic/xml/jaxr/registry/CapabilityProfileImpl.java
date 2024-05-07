package weblogic.xml.jaxr.registry;

import javax.xml.registry.CapabilityProfile;
import javax.xml.registry.JAXRException;

public class CapabilityProfileImpl extends BaseJAXRObject implements CapabilityProfile {
   private String m_version;
   private int m_capability;

   public CapabilityProfileImpl(String var1, int var2) {
      this.m_version = var1;
      this.m_capability = var2;
   }

   public String getVersion() throws JAXRException {
      return this.m_version;
   }

   public int getCapabilityLevel() throws JAXRException {
      return this.m_capability;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{new Integer(this.m_capability), this.m_version};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_capability", "m_version"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
