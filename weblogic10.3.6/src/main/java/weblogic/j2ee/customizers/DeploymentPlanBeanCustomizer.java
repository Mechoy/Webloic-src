package weblogic.j2ee.customizers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.DescriptorHelper;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.VariableDefinitionBean;

public class DeploymentPlanBeanCustomizer implements weblogic.j2ee.descriptor.wl.customizers.DeploymentPlanBeanCustomizer {
   private static final boolean debug = false;
   private DescriptorHelper ddhelper = null;
   private DeploymentPlanBean plan;
   private List newBeans = new ArrayList();
   private int varCtr = 0;

   public DeploymentPlanBeanCustomizer(DeploymentPlanBean var1) {
      this.plan = var1;
   }

   private DescriptorHelper getDescriptorHelper() {
      if (this.ddhelper == null) {
         this.ddhelper = DescriptorHelper.getInstance();
      }

      return this.ddhelper;
   }

   public ModuleOverrideBean findModuleOverride(String var1) {
      if (var1 == null) {
         return null;
      } else {
         ModuleOverrideBean[] var2 = this.plan.getModuleOverrides();
         if (var2 != null) {
            for(int var4 = 0; var4 < var2.length; ++var4) {
               ModuleOverrideBean var3 = var2[var4];
               if (var1.equals(var3.getModuleName())) {
                  return var3;
               }
            }
         }

         return null;
      }
   }

   public ModuleDescriptorBean findModuleDescriptor(String var1, String var2) {
      ModuleOverrideBean var3 = this.findModuleOverride(var1);
      if (var3 != null) {
         ModuleDescriptorBean[] var4 = var3.getModuleDescriptors();

         for(int var6 = 0; var6 < var4.length; ++var6) {
            ModuleDescriptorBean var5 = var4[var6];
            if (var5.getUri().endsWith(var2)) {
               return var5;
            }
         }
      }

      return null;
   }

   public boolean rootModule(String var1) {
      return this.rootModule(this.plan.findModuleOverride(var1));
   }

   public boolean hasVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3) throws IllegalArgumentException {
      return this.findVariable(var1, var2, var3, (Object)null, false) != null;
   }

   public void findAndRemoveAllBeanVariables(ModuleDescriptorBean var1, DescriptorBean var2) throws IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException("No module descriptor provided");
      } else {
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         String var5 = this.getDescriptorHelper().buildKeyXpath(var2);
         VariableDefinitionBean var6 = this.plan.getVariableDefinition();
         VariableAssignmentBean[] var7 = var1.getVariableAssignments();
         VariableBean[] var8 = var6.getVariables();
         if (var7 == null) {
            throw new IllegalArgumentException("No VariableAssignemnts to remove for bean");
         } else {
            for(int var9 = 0; var9 < var7.length; ++var9) {
               if (var7[var9].getXpath().startsWith(var5)) {
                  var3.add(var7[var9]);

                  for(int var10 = 0; var10 < var8.length; ++var10) {
                     if (var8[var10].getName().equals(var7[var9].getName())) {
                        var4.add(var8[var10]);
                     }
                  }
               }
            }

            if (var3.isEmpty()) {
               throw new IllegalArgumentException("Unable to remove bean since not defined in plan");
            } else {
               Iterator var12 = var3.iterator();

               while(var12.hasNext()) {
                  VariableAssignmentBean var13 = (VariableAssignmentBean)var12.next();
                  var1.destroyVariableAssignment(var13);
               }

               Iterator var14 = var4.iterator();

               while(var14.hasNext()) {
                  VariableBean var11 = (VariableBean)var14.next();
                  var6.destroyVariable(var11);
               }

               var1.setChanged(true);
            }
         }
      }
   }

   public VariableBean findVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3) throws IllegalArgumentException {
      return this.findVariable(var1, var2, var3, (Object)null, false);
   }

   private VariableBean findVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3, Object var4, boolean var5) throws IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException("No module descriptor provided");
      } else {
         VariableBean var6 = null;
         String var7 = null;
         String var8 = this.getDescriptorHelper().buildXpath(var2, var3, var4, var5);
         VariableAssignmentBean[] var9 = var1.getVariableAssignments();
         if (var9 != null) {
            for(int var10 = 0; var10 < var9.length; ++var10) {
               VariableAssignmentBean var11 = var9[var10];
               if (var8.equals(var11.getXpath())) {
                  var7 = var11.getName();
                  break;
               }
            }

            if (var7 != null) {
               VariableDefinitionBean var14 = this.plan.getVariableDefinition();
               VariableBean[] var15 = var14.getVariables();
               if (var15 != null) {
                  for(int var12 = 0; var12 < var15.length; ++var12) {
                     VariableBean var13 = var15[var12];
                     if (var13.getName().equals(var7)) {
                        var6 = var13;
                        break;
                     }
                  }
               }
            }
         }

         return var6;
      }
   }

   public VariableBean findOrCreateVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3) throws IllegalArgumentException {
      return this.findOrCreateVariable(var1, var2, var3, false, (Object)null, false);
   }

   public VariableBean findOrCreateVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3, boolean var4) throws IllegalArgumentException {
      return this.findOrCreateVariable(var1, var2, var3, var4, (Object)null, false);
   }

   public VariableBean findOrCreateVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3, boolean var4, Object var5) throws IllegalArgumentException {
      return this.findOrCreateVariable(var1, var2, var3, var4, var5, true);
   }

   private VariableBean findOrCreateVariable(ModuleDescriptorBean var1, DescriptorBean var2, String var3, boolean var4, Object var5, boolean var6) throws IllegalArgumentException {
      if (this.getDescriptorHelper().isTransient(var2, var3)) {
         throw new IllegalArgumentException("Variables can't be created for transient properties");
      } else {
         VariableBean var7 = this.findVariable(var1, var2, var3, var5, var6);
         if (var7 != null) {
            return var7;
         } else {
            VariableAssignmentBean var8 = var1.createVariableAssignment();
            var8.setXpath(this.getDescriptorHelper().buildXpath(var2, var3, var5, var6));
            if (var6) {
               var8.setOperation("replace");
            }

            String var9 = this._createVarName(var1, var2, var3);
            var8.setName(var9);
            if (var4) {
               String var10 = this.getDescriptorHelper().buildKeyXpath(var2);
               if (!this.newBeans.contains(var10)) {
                  this.newBeans.add(var10);
               }

               var8.setOrigin("planbased");
            }

            try {
               var7 = this.findVariable(var1, var2, var3, var5, var6);
            } catch (Throwable var11) {
               var11.printStackTrace();
            }

            if (var7 != null) {
               return var7;
            } else {
               var7 = this.plan.getVariableDefinition().createVariable();
               var7.setName(var9);
               return var7;
            }
         }
      }
   }

   private String _createVarName(ModuleDescriptorBean var1, DescriptorBean var2, String var3) {
      String var4 = new String();
      String var5 = var2.getClass().getName();
      var5 = var5.substring(var5.lastIndexOf(".") + 1, var5.length() - 8);
      var4 = var4 + var5 + "_";
      String var6 = this.getDescriptorHelper().findKey(var2);
      if (var6 != null) {
         var4 = var4 + this.getDescriptorHelper().getKeyValue(var2, var6) + "_";
      } else {
         String[] var7 = this.getDescriptorHelper().findKeyComponents(var2);
         if (var7 != null) {
            for(int var8 = 0; var8 < var7.length; ++var8) {
               String var9 = var7[var8];
               var4 = var4 + this.getDescriptorHelper().getKeyValue(var2, var9) + "_";
            }
         }
      }

      var4 = var4 + var3;
      if (!this.plan.isGlobalVariables()) {
         var4 = var4 + "_" + Long.toString(System.currentTimeMillis()) + Integer.toString(this.varCtr++);
      }

      return var4;
   }

   public VariableAssignmentBean[] findVariableAssignments(VariableBean var1) {
      ArrayList var2 = new ArrayList();
      ModuleOverrideBean[] var3 = this.plan.getModuleOverrides();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            ModuleOverrideBean var5 = var3[var4];
            ModuleDescriptorBean[] var6 = var5.getModuleDescriptors();
            if (var6 != null) {
               for(int var7 = 0; var7 < var6.length; ++var7) {
                  ModuleDescriptorBean var8 = var6[var7];
                  VariableAssignmentBean[] var9 = var8.getVariableAssignments();
                  if (var9 != null) {
                     for(int var10 = 0; var10 < var9.length; ++var10) {
                        VariableAssignmentBean var11 = var9[var10];
                        if (var1 == this._findVar(var11)) {
                           var2.add(var11);
                        }
                     }
                  }
               }
            }
         }
      }

      return (VariableAssignmentBean[])((VariableAssignmentBean[])var2.toArray(new VariableAssignmentBean[0]));
   }

   public Object valueOf(VariableBean var1) {
      return var1.getValue();
   }

   public VariableAssignmentBean assignVariable(VariableBean var1, ModuleDescriptorBean var2, DescriptorBean var3, String var4) {
      VariableAssignmentBean var5 = this._createAssignment(var2, var3, var4);
      var5.setName(var1.getName());
      return var5;
   }

   private VariableBean _findVar(VariableAssignmentBean var1) {
      VariableBean[] var2 = this.plan.getVariableDefinition().getVariables();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            VariableBean var4 = var2[var3];
            if (var4.getName().equals(var1.getName())) {
               return var4;
            }
         }
      }

      return null;
   }

   private VariableAssignmentBean _createAssignment(ModuleDescriptorBean var1, DescriptorBean var2, String var3) {
      String var4 = this.getDescriptorHelper().buildXpath(var2, var3);
      VariableAssignmentBean[] var5 = var1.getVariableAssignments();
      VariableAssignmentBean var6 = null;
      if (var5 != null) {
         for(int var7 = 0; var7 < var5.length; ++var7) {
            if (var4.equals(var5[var7].getXpath())) {
               var6 = var5[var7];
               break;
            }
         }
      }

      if (var6 == null) {
         var6 = var1.createVariableAssignment();
         var6.setXpath(var4);
      }

      return var6;
   }

   public ModuleOverrideBean findRootModule() {
      ModuleOverrideBean[] var1 = this.plan.getModuleOverrides();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (this.rootModule(var1[var2])) {
               return var1[var2];
            }
         }
      }

      return null;
   }

   private boolean rootModule(ModuleOverrideBean var1) {
      if (var1 == null) {
         return false;
      } else if (var1.getModuleType().equals(ModuleType.EAR.toString())) {
         return true;
      } else {
         return this.plan.getModuleOverrides().length == 1;
      }
   }

   public boolean isRemovable(DescriptorBean var1) throws IllegalArgumentException {
      if (var1 == null) {
         throw new IllegalArgumentException("No descriptor bean provided");
      } else {
         String var2 = this.getDescriptorHelper().buildKeyXpath(var1);
         if (this.newBeans.contains(var2)) {
            return true;
         } else {
            ModuleOverrideBean[] var3 = this.plan.getModuleOverrides();
            if (var3 != null) {
               for(int var4 = 0; var4 < var3.length; ++var4) {
                  ModuleOverrideBean var5 = var3[var4];
                  ModuleDescriptorBean[] var6 = var5.getModuleDescriptors();
                  if (var6 != null) {
                     for(int var7 = 0; var7 < var6.length; ++var7) {
                        ModuleDescriptorBean var8 = var6[var7];
                        VariableAssignmentBean[] var9 = var8.getVariableAssignments();
                        if (var9 != null) {
                           for(int var10 = 0; var10 < var9.length; ++var10) {
                              String var11 = var9[var10].getXpath();
                              if (var11.startsWith(var2) && var9[var10].getOrigin().equals("planbased") && var11.lastIndexOf(47) <= var2.length()) {
                                 return true;
                              }
                           }
                        }
                     }
                  }
               }
            }

            return false;
         }
      }
   }
}
