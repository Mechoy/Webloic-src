package weblogic.xml.registry;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

public abstract class XMLAbstractRegistryEntry implements Serializable {
   private String publicId = null;
   private String systemId = null;
   private PropertyChangeListener listener;
   private ConfigAbstraction.EntryConfig mbean = null;
   private boolean isPrivate = false;

   public XMLAbstractRegistryEntry(String var1, String var2, ConfigAbstraction.EntryConfig var3) {
      this.publicId = var1;
      this.systemId = var2;
      this.mbean = var3;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public String getRootElementTag() {
      return null;
   }

   public PropertyChangeListener getListener() {
      return this.listener;
   }

   public ConfigAbstraction.EntryConfig getMBean() {
      return this.mbean;
   }

   public void setPrivate(boolean var1) {
      this.isPrivate = var1;
   }

   public void setListener(PropertyChangeListener var1) {
      this.listener = var1;
   }

   public boolean isPrivate() {
      return this.isPrivate;
   }
}
