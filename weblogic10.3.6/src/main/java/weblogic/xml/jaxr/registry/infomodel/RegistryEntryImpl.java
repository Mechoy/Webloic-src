package weblogic.xml.jaxr.registry.infomodel;

import java.util.Date;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.RegistryEntry;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Versionable;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class RegistryEntryImpl extends RegistryObjectImpl implements RegistryEntry {
   private static final long serialVersionUID = -1L;
   private Versionable m_versionable;

   public RegistryEntryImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
      this.m_versionable = new VersionableImpl(var1);
   }

   public RegistryEntryImpl(RegistryEntry var1, RegistryServiceImpl var2) throws JAXRException {
      super((RegistryObject)var1, var2);
      if (var1 != null) {
         this.m_versionable = new VersionableImpl(var2);
         if (var2.getCapabilityProfile().getCapabilityLevel() >= 1) {
            this.setMajorVersion(var1.getMajorVersion());
            this.setMinorVersion(var1.getMinorVersion());
            this.setUserVersion(var1.getUserVersion());
         }
      }

   }

   public int getMajorVersion() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public void setMajorVersion(int var1) throws JAXRException {
      this.checkCapability(1);
   }

   public int getMinorVersion() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public void setMinorVersion(int var1) throws JAXRException {
      this.checkCapability(1);
   }

   public String getUserVersion() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setUserVersion(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public int getStatus() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public int getStability() throws JAXRException {
      this.checkCapability(1);
      return 0;
   }

   public void setStability(int var1) throws JAXRException {
      this.checkCapability(1);
   }

   public Date getExpiration() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setExpiration(Date var1) throws JAXRException {
      this.checkCapability(1);
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_versionable};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_versionable"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
