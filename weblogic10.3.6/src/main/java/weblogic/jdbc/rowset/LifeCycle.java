package weblogic.jdbc.rowset;

import java.io.Serializable;
import java.lang.reflect.Field;
import weblogic.jdbc.JDBCLogger;

class LifeCycle {
   private static final int DESIGNING_CODE = 0;
   private static final int POPULATING_CODE = 1;
   private static final int CONFIGURING_METADATA_CODE = 2;
   private static final int MANIPULATING_CODE = 3;
   private static final int INSERTING_CODE = 4;
   private static final int UPDATING_CODE = 5;
   public static final int DESIGN = 0;
   public static final int CONFIGURE_QUERY = 1;
   public static final int POPULATE = 2;
   public static final int CONFIGURE_METADATA = 3;
   public static final int NAVIGATE = 4;
   public static final int MOVE_TO_INSERT = 5;
   public static final int MOVE_TO_CURRENT = 6;
   public static final int UPDATE = 7;
   public static final int FINISH_UPDATE = 8;
   public static final int ACCEPT_CHANGES = 9;
   public static final int ACCEPT_CHANGES_REUSE = 10;
   private static final int OP_COUNT = 11;
   public static final State DESIGNING = new State(0, new int[]{4, 0, 1, 5, 2});
   public static final State POPULATING = new State(1, new int[]{1, 2, 3, 0, 4, 5, 7, 9, 10});
   public static final State CONFIGURING_METADATA = new State(2, new int[]{3, 4, 5, 7});
   public static final State MANIPULATING = new State(3, new int[]{0, 3, 4, 5, 7, 9, 10});
   public static final State INSERTING = new State(4, new int[]{5, 7, 8, 6, 9, 10}) {
      private static final long serialVersionUID = 7393324511593576778L;

      State next(int var1) {
         switch (var1) {
            case 7:
            case 8:
               return LifeCycle.INSERTING;
            default:
               return LifeCycle.NEXT[var1];
         }
      }
   };
   public static final State UPDATING = new State(5, new int[]{7, 8});
   private static final State[] NEXT = new State[11];
   private static final int PFS = 25;

   private static String getOpName(int var0) {
      Field[] var1 = LifeCycle.class.getFields();

      try {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if ((var1[var2].getModifiers() & 25) == 25 && var1[var2].getType() == Integer.TYPE && var1[var2].getInt((Object)null) == var0) {
               return var1[var2].getName();
            }
         }
      } catch (IllegalAccessException var3) {
         JDBCLogger.logStackTrace(var3);
      }

      return "unknown op " + var0;
   }

   private static String getStateName(State var0) {
      Field[] var1 = LifeCycle.class.getFields();

      try {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if ((var1[var2].getModifiers() & 25) == 25 && var1[var2].getType() == State.class && var1[var2].get((Object)null) == var0) {
               return var1[var2].getName();
            }
         }
      } catch (IllegalAccessException var3) {
         JDBCLogger.logStackTrace(var3);
      }

      return "unknown state " + var0;
   }

   private static State lookupState(int var0) {
      Field[] var1 = LifeCycle.class.getFields();

      try {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if ((var1[var2].getModifiers() & 25) == 25 && var1[var2].getType() == State.class) {
               State var3 = (State)var1[var2].get((Object)null);
               if (var3.code == var0) {
                  return var3;
               }
            }
         }
      } catch (IllegalAccessException var4) {
         JDBCLogger.logStackTrace(var4);
      }

      throw new AssertionError(var0);
   }

   static {
      NEXT[0] = DESIGNING;
      NEXT[1] = POPULATING;
      NEXT[2] = POPULATING;
      NEXT[3] = CONFIGURING_METADATA;
      NEXT[4] = MANIPULATING;
      NEXT[5] = INSERTING;
      NEXT[6] = MANIPULATING;
      NEXT[7] = UPDATING;
      NEXT[8] = MANIPULATING;
      NEXT[9] = DESIGNING;
      NEXT[10] = POPULATING;
   }

   static class State implements Serializable {
      private static final long serialVersionUID = 7867643086257295621L;
      private final int code;
      private final int validOps;
      private String name;

      State(int var1, int[] var2) {
         this.code = var1;
         int var3 = 0;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3 |= 1 << var2[var4];
         }

         this.validOps = var3;
      }

      State next(int var1) {
         return LifeCycle.NEXT[var1];
      }

      String getName() {
         if (this.name == null) {
            this.name = LifeCycle.getStateName(this);
         }

         return this.name;
      }

      State checkOp(int var1) {
         if ((this.validOps & 1 << var1) == 0) {
            throw new UnsupportedOperationException("Operation class " + LifeCycle.getOpName(var1) + " not supported in state " + this.getName());
         } else {
            return this.next(var1);
         }
      }

      private Object readResolve() {
         return LifeCycle.lookupState(this.code);
      }
   }
}
