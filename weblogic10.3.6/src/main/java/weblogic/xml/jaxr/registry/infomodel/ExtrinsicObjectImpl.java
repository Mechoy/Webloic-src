package weblogic.xml.jaxr.registry.infomodel;

import javax.activation.DataHandler;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.ExtrinsicObject;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class ExtrinsicObjectImpl extends RegistryEntryImpl implements ExtrinsicObject {
   private static final long serialVersionUID = -1L;
   private String m_mimeType;
   private DataHandler m_repositoryItem;
   private boolean m_opaque;

   public ExtrinsicObjectImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public ExtrinsicObjectImpl(ExtrinsicObject var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      if (var1 != null) {
         this.m_mimeType = var1.getMimeType();
         this.m_repositoryItem = var1.getRepositoryItem();
         this.m_opaque = var1.isOpaque();
      }

   }

   public String getMimeType() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setMimeType(String var1) throws JAXRException {
      this.checkCapability(1);
   }

   public boolean isOpaque() throws JAXRException {
      this.checkCapability(1);
      return false;
   }

   public void setOpaque(boolean var1) throws JAXRException {
      this.checkCapability(1);
   }

   public DataHandler getRepositoryItem() throws JAXRException {
      this.checkCapability(1);
      return null;
   }

   public void setRepositoryItem(DataHandler var1) throws JAXRException {
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
