package weblogic.ejb.container.persistence;

import java.sql.ResultSet;
import weblogic.ejb.container.persistence.spi.CMPBean;
import weblogic.ejb.container.persistence.spi.RSInfo;
import weblogic.utils.Debug;

public final class RSInfoImpl implements RSInfo {
   private static final boolean debug = System.getProperty("weblogic.ejb.container.persistence.debug") != null;
   private static final boolean verbose = System.getProperty("weblogic.ejb.container.persistence.verbose") != null;
   private final ResultSet rs;
   private final int groupIndex;
   private final int offset;
   private final String cmrField;
   private final int cmrFieldOffset;
   private final Object pk;
   private final CMPBean cmpBean;

   public RSInfoImpl(ResultSet var1, int var2, int var3, Object var4) {
      this(var1, var2, var3, (String)null, 0, var4);
   }

   public RSInfoImpl(ResultSet var1, int var2, int var3, String var4, int var5, Object var6) {
      if (debug) {
         Debug.assertion(var1 != null);
      }

      this.rs = var1;
      this.offset = var3;
      this.groupIndex = var2;
      this.cmrField = var4;
      this.cmrFieldOffset = var5;
      this.pk = var6;
      this.cmpBean = null;
   }

   public RSInfoImpl(CMPBean var1, Object var2) {
      this.cmpBean = var1;
      this.pk = var2;
      this.rs = null;
      this.offset = 0;
      this.groupIndex = 0;
      this.cmrField = null;
      this.cmrFieldOffset = 0;
   }

   public ResultSet getRS() {
      return this.rs;
   }

   public int getGroupIndex() {
      return this.groupIndex;
   }

   public Integer getOffset() {
      return new Integer(this.offset);
   }

   public String getCmrField() {
      return this.cmrField;
   }

   public Integer getCmrFieldOffset() {
      return new Integer(this.cmrFieldOffset);
   }

   public Object getPK() {
      return this.pk;
   }

   public CMPBean getCmpBean() {
      return this.cmpBean;
   }

   public boolean usesCmpBean() {
      return this.cmpBean != null;
   }

   public String toString() {
      return "(rs=" + this.rs + ", groupIndex=" + this.groupIndex + ", offset=" + this.offset + ", cmrField=" + this.cmrField + ", cmrFieldOffset=" + this.cmrFieldOffset + ", pk=" + this.pk + ", cmpBean=" + this.cmpBean + ")";
   }
}
