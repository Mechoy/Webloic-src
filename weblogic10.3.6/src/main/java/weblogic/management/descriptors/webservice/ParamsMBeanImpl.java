package weblogic.management.descriptors.webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ParamsMBeanImpl extends XMLElementMBeanDelegate implements ParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_returnParam = false;
   private ReturnParamMBean returnParam;
   private boolean isSet_params = false;
   private List params;
   private boolean isSet_faults = false;
   private List faults;

   public ReturnParamMBean getReturnParam() {
      return this.returnParam;
   }

   public void setReturnParam(ReturnParamMBean var1) {
      ReturnParamMBean var2 = this.returnParam;
      this.returnParam = var1;
      this.isSet_returnParam = var1 != null;
      this.checkChange("returnParam", var2, this.returnParam);
   }

   public ParamMBean[] getParams() {
      if (this.params == null) {
         return new ParamMBean[0];
      } else {
         ParamMBean[] var1 = new ParamMBean[this.params.size()];
         var1 = (ParamMBean[])((ParamMBean[])this.params.toArray(var1));
         return var1;
      }
   }

   public void setParams(ParamMBean[] var1) {
      ParamMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getParams();
      }

      this.isSet_params = true;
      if (this.params == null) {
         this.params = Collections.synchronizedList(new ArrayList());
      } else {
         this.params.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.params.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Params", var2, this.getParams());
      }

   }

   public void addParam(ParamMBean var1) {
      this.isSet_params = true;
      if (this.params == null) {
         this.params = Collections.synchronizedList(new ArrayList());
      }

      this.params.add(var1);
   }

   public void removeParam(ParamMBean var1) {
      if (this.params != null) {
         this.params.remove(var1);
      }
   }

   public FaultMBean[] getFaults() {
      if (this.faults == null) {
         return new FaultMBean[0];
      } else {
         FaultMBean[] var1 = new FaultMBean[this.faults.size()];
         var1 = (FaultMBean[])((FaultMBean[])this.faults.toArray(var1));
         return var1;
      }
   }

   public void setFaults(FaultMBean[] var1) {
      FaultMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getFaults();
      }

      this.isSet_faults = true;
      if (this.faults == null) {
         this.faults = Collections.synchronizedList(new ArrayList());
      } else {
         this.faults.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.faults.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Faults", var2, this.getFaults());
      }

   }

   public void addFault(FaultMBean var1) {
      this.isSet_faults = true;
      if (this.faults == null) {
         this.faults = Collections.synchronizedList(new ArrayList());
      }

      this.faults.add(var1);
   }

   public void removeFault(FaultMBean var1) {
      if (this.faults != null) {
         this.faults.remove(var1);
      }
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<params");
      var2.append(">\n");
      int var3;
      if (null != this.getParams()) {
         for(var3 = 0; var3 < this.getParams().length; ++var3) {
            var2.append(this.getParams()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getReturnParam()) {
         var2.append(this.getReturnParam().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getFaults()) {
         for(var3 = 0; var3 < this.getFaults().length; ++var3) {
            var2.append(this.getFaults()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</params>\n");
      return var2.toString();
   }
}
