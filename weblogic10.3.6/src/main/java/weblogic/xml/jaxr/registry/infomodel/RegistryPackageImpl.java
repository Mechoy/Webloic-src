package weblogic.xml.jaxr.registry.infomodel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.RegistryPackage;
import weblogic.xml.jaxr.registry.RegistryServiceImpl;

public class RegistryPackageImpl extends RegistryEntryImpl implements RegistryPackage {
   private static final long serialVersionUID = -1L;
   private Set m_registryObjects = new HashSet();

   public RegistryPackageImpl(RegistryServiceImpl var1) throws JAXRException {
      super(var1);
   }

   public RegistryPackageImpl(RegistryPackage var1, RegistryServiceImpl var2) throws JAXRException {
      super(var1, var2);
      if (var1 != null) {
         Set var3 = var1.getRegistryObjects();
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               RegistryObject var5 = (RegistryObject)var4.next();
               RegistryObjectImpl var6 = new RegistryObjectImpl(var5, var2);
               this.m_registryObjects.add(var6);
            }
         }
      }

   }

   public void addRegistryObject(RegistryObject var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void addRegistryObjects(Collection var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void removeRegistryObject(RegistryObject var1) throws JAXRException {
      this.checkCapability(1);
   }

   public void removeRegistryObjects(Collection var1) throws JAXRException {
      this.checkCapability(1);
   }

   public Set getRegistryObjects() throws JAXRException {
      this.checkCapability(1);
      return null;
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
