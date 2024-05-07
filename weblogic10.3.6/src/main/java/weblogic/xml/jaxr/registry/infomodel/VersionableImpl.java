package weblogic.xml.jaxr.registry.infomodel;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Versionable;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class VersionableImpl extends BaseInfoModelObject implements Versionable {
   private static final long serialVersionUID = -1L;
   private int m_majorVersion;
   private int m_minorVersion;
   private String m_userVersion;

   public VersionableImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public VersionableImpl(Versionable var1, RegistryServiceImpl var2) throws JAXRException {
      super(var2);
      if (var1 != null) {
         this.m_majorVersion = var1.getMajorVersion();
         this.m_minorVersion = var1.getMinorVersion();
         this.m_userVersion = var1.getUserVersion();
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

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[0];
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[0];
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
