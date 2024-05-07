package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WLDFServerDiagnosticMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WLDFServerDiagnosticMBeanImpl bean;

   protected WLDFServerDiagnosticMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WLDFServerDiagnosticMBeanImpl)var1;
   }

   public WLDFServerDiagnosticMBeanBinder() {
      super(new WLDFServerDiagnosticMBeanImpl());
      this.bean = (WLDFServerDiagnosticMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DiagnosticDataArchiveType")) {
                  try {
                     this.bean.setDiagnosticDataArchiveType((String)var2);
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("DiagnosticJDBCResource")) {
                  this.bean.setDiagnosticJDBCResourceAsString((String)var2);
               } else if (var1.equals("DiagnosticStoreBlockSize")) {
                  try {
                     this.bean.setDiagnosticStoreBlockSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreDir")) {
                  try {
                     this.bean.setDiagnosticStoreDir((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreIoBufferSize")) {
                  try {
                     this.bean.setDiagnosticStoreIoBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreMaxFileSize")) {
                  try {
                     this.bean.setDiagnosticStoreMaxFileSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreMaxWindowBufferSize")) {
                  try {
                     this.bean.setDiagnosticStoreMaxWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreMinWindowBufferSize")) {
                  try {
                     this.bean.setDiagnosticStoreMinWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("EventPersistenceInterval")) {
                  try {
                     this.bean.setEventPersistenceInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("EventsImageCaptureInterval")) {
                  try {
                     this.bean.setEventsImageCaptureInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("ImageDir")) {
                  try {
                     this.bean.setImageDir((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("ImageTimeout")) {
                  try {
                     this.bean.setImageTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("PreferredStoreSizeLimit")) {
                  try {
                     this.bean.setPreferredStoreSizeLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("StoreSizeCheckPeriod")) {
                  try {
                     this.bean.setStoreSizeCheckPeriod(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("WLDFDataRetirementByAge")) {
                  try {
                     this.bean.addWLDFDataRetirementByAge((WLDFDataRetirementByAgeMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                     this.bean.removeWLDFDataRetirementByAge((WLDFDataRetirementByAgeMBean)var11.getExistingBean());
                     this.bean.addWLDFDataRetirementByAge((WLDFDataRetirementByAgeMBean)((AbstractDescriptorBean)((WLDFDataRetirementByAgeMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WLDFDiagnosticVolume")) {
                  try {
                     this.bean.setWLDFDiagnosticVolume((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("DataRetirementEnabled")) {
                  try {
                     this.bean.setDataRetirementEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("DataRetirementTestModeEnabled")) {
                  try {
                     this.bean.setDataRetirementTestModeEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("DiagnosticContextEnabled")) {
                  try {
                     this.bean.setDiagnosticContextEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("DiagnosticStoreFileLockingEnabled")) {
                  try {
                     this.bean.setDiagnosticStoreFileLockingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SynchronousEventPersistenceEnabled")) {
                  try {
                     this.bean.setSynchronousEventPersistenceEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var26) {
         System.out.println(var26 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var26;
      } catch (RuntimeException var27) {
         throw var27;
      } catch (Exception var28) {
         if (var28 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var28);
         } else if (var28 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var28.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var28);
         }
      }
   }
}
