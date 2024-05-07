package weblogic.tools.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

public class UIFactory {
   public static int STD_DIALOG_WIDTH = 300;
   public static int STD_DIALOG_HEIGHT = 125;
   private static MarathonTextFormatter m_fmt = new MarathonTextFormatter();
   private static Action copyAction;
   private static Action pasteAction;
   private static Action cutAction;
   private static Class clazz = (new UIFactory()).getClass();
   private static ImageIcon emptyIcon = getIcon("/weblogic/tools/ui/images/emptyIcon.gif");
   private static Action clearAction = new AbstractAction("Clear") {
      public void actionPerformed(ActionEvent var1) {
         Object var2 = var1.getSource();
         Object var3 = null;
         if (var2 instanceof Component) {
            var3 = (Component)var2;

            while(var3 != null) {
               if (var3 instanceof JTextComponent) {
                  ((JTextComponent)var3).setText("");
                  break;
               }

               if (var3 instanceof JPopupMenu) {
                  var3 = ((JPopupMenu)var3).getInvoker();
               } else {
                  var3 = ((Component)var3).getParent();
               }
            }
         }

      }
   };
   private static Action[] editableComponentActions;
   private static Action[] nonEditableComponentActions;
   private static int comboBoxMinWidth;
   private static Image m_busyImage;
   private static ImageIcon m_busyIcon;
   String[] m_funBusyImages = new String[]{"/weblogic/marathon/resources/images/animations/itchy.gif", "/weblogic/marathon/resources/images/animations/hellokty.gif", "/weblogic/marathon/resources/images/animations/womanwrench.gif", "/weblogic/marathon/resources/images/animations/stimpy.gif"};
   private boolean m_funImages = true;
   public static final int STD_COMP_BUFFER_VALUE = 11;

   public static ImageIcon getIcon(String var0) {
      ImageIcon var1 = null;
      if (var0 != null) {
         URL var2 = clazz.getResource(var0);
         if (var2 != null) {
            var1 = new ImageIcon(var2);
         }
      }

      if (var1 == null) {
         var1 = emptyIcon;
      }

      return var1;
   }

   public static JFrame getFrame(String var0) {
      JFrame var1 = new JFrame(var0);
      var1.setSize(500, 300);
      return var1;
   }

   public static Action getCutAction() {
      return cutAction;
   }

   public static Action getCopyAction() {
      return copyAction;
   }

   public static Action getPasteAction() {
      return pasteAction;
   }

   public static JTextArea getTextArea(boolean var0) {
      JTextArea var1 = new JTextArea();
      var1.setEditable(var0);
      return (JTextArea)setupListeners(var1, var0);
   }

   private static JTextComponent setupListeners(JTextComponent var0, boolean var1) {
      return var0;
   }

   public static JTextArea getTextArea() {
      return getTextArea(true);
   }

   public static JTextField getTextField() {
      return getTextField(true);
   }

   public static JTextField getTextField(int var0) {
      return getTextField(true, var0);
   }

   public static JTextField getTextField(boolean var0, int var1) {
      return (JTextField)setupListeners(new JTextField(var1), var0);
   }

   public static JTextField getTextField(boolean var0) {
      return (JTextField)setupListeners(new JTextField(), var0);
   }

   public static JTextField getPasswordField() {
      return new JPasswordField();
   }

   public static NumberBox getIntegerField() {
      return new NumberBox();
   }

   public static JRadioButton getRadioButton() {
      return new JRadioButton();
   }

   public static JRadioButton getRadioButton(String var0) {
      return new JRadioButton(var0);
   }

   public static JCheckBox getCheckBox() {
      return new JCheckBox();
   }

   public static JToggleButton getToggleButton(String var0) {
      return new JToggleButton(var0);
   }

   public static JButton getButton(String var0) {
      return new JButton(var0);
   }

   public static JButton getChooser() {
      return new JButton(m_fmt.getBrowseEllipsis());
   }

   public static JTextField getTextField(String var0) {
      JTextField var1 = getTextField(true);
      var1.setText(var0);
      var1 = (JTextField)setupListeners(var1, true);
      return var1;
   }

   public static JLabel getLabel(String var0) {
      JLabel var1 = new JLabel(var0);
      return var1;
   }

   public static JLabel getMandatoryLabel(String var0) {
      JLabel var1 = new JLabel(formatMandatory(var0));
      return var1;
   }

   public static JLabel getMandatoryLabel(String var0, int var1) {
      JLabel var2 = new JLabel(formatMandatory(var0), var1);
      return var2;
   }

   public static JLabel getLabel(String var0, int var1) {
      JLabel var2 = new JLabel(var0, var1);
      return var2;
   }

   public static JCheckBox getCheckBox(String var0) {
      JCheckBox var1 = new JCheckBox(var0);
      return var1;
   }

   public static JComboBox getSortedComboBox() {
      SortedComboBox var0 = new SortedComboBox();
      Component var1 = var0.getEditor().getEditorComponent();
      if (var1 instanceof JTextField) {
         ((JTextField)var1).setColumns(40);
      }

      return var0;
   }

   public static JComboBox getComboBox() {
      return getComboBox(new String[0]);
   }

   public static JComboBox getComboBox(Object[] var0) {
      JComboBox var1 = new JComboBox(var0) {
         public Dimension getPreferredSize() {
            Dimension var1 = super.getPreferredSize();
            Dimension var2 = super.getMinimumSize();
            var1.width = Math.max(var1.width, var2.width);
            var1.height = Math.max(var1.height, var2.height);
            return var1;
         }
      };
      Component var2 = var1.getEditor().getEditorComponent();
      if (var2 instanceof JTextField) {
         ((JTextField)var2).setColumns(40);
      }

      Dimension var3 = var1.getMinimumSize();
      var3.width = Math.max(var3.width, getComboBoxMinWidth(var1));
      var1.setMinimumSize(var3);
      var1.setEditable(true);
      return var1;
   }

   private static synchronized int getComboBoxMinWidth(JComboBox var0) {
      if (comboBoxMinWidth != -1) {
         return comboBoxMinWidth;
      } else {
         Font var1 = var0.getFont();
         FontMetrics var2 = Toolkit.getDefaultToolkit().getFontMetrics(var1);
         return comboBoxMinWidth = var2.stringWidth("abcdefghij");
      }
   }

   public static JList getList() {
      return new JList(new DefaultListModel());
   }

   public static JTable getTable() {
      return new JTable();
   }

   public static Image getBusyImage() {
      if (null == m_busyImage) {
         m_busyImage = Util.loadImage("/weblogic/marathon/resources/images/animations/validate_animation.gif");
      }

      return m_busyImage;
   }

   public static ImageIcon getBusyIcon() {
      if (null == m_busyIcon) {
         m_busyIcon = new ImageIcon(getBusyImage());
      }

      return m_busyIcon;
   }

   public static JDialog getBusyDialog(Component var0, String var1, String var2) {
      JDialog var3 = null;
      JOptionPane var4 = new JOptionPane(var1, 1, -1, getBusyIcon());
      var3 = var4.createDialog(var0, var2);
      var3.setModal(false);
      return var3;
   }

   public static GridBagConstraints getBasicGridBagConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      var0.gridx = var0.gridy = 0;
      var0.fill = 1;
      var0.weightx = 1.0;
      return var0;
   }

   public static GridBagConstraints getSimpleGridBagConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      byte var1 = 5;
      var0.insets = new Insets(var1, var1, var1, var1);
      var0.gridx = 0;
      var0.gridy = 0;
      return var0;
   }

   public static GridBagConstraints getGridBagConstraints() {
      GridBagConstraints var0 = new GridBagConstraints();
      byte var1 = 0;
      var0.insets = new Insets(var1, var1, var1, var1);
      var0.anchor = 18;
      var0.gridx = 0;
      var0.gridy = 0;
      var0.fill = 1;
      var0.weightx = 1.0;
      var0.gridwidth = 0;
      return var0;
   }

   public static String formatMandatory(String var0) {
      return "* " + var0;
   }

   private static String formatNormal(String var0) {
      return var0;
   }

   private static void ppp(String var0) {
      System.out.println("[UIFactory] " + var0);
   }

   static {
      ImageIcon var0 = getIcon("/weblogic/marathon/resources/images/toolbar/copy.gif");
      copyAction = new DefaultEditorKit.CopyAction();
      copyAction.putValue("Name", m_fmt.getCopy());
      copyAction.putValue("SmallIcon", var0);
      var0 = getIcon("/weblogic/marathon/resources/images/toolbar/cut.gif");
      cutAction = new DefaultEditorKit.CutAction();
      cutAction.putValue("Name", m_fmt.getCut());
      cutAction.putValue("SmallIcon", var0);
      var0 = getIcon("/weblogic/marathon/resources/images/toolbar/paste.gif");
      pasteAction = new DefaultEditorKit.PasteAction();
      pasteAction.putValue("Name", m_fmt.getPaste());
      pasteAction.putValue("SmallIcon", var0);
      Action[] var1 = new Action[]{cutAction, copyAction, pasteAction};
      editableComponentActions = var1;
      Action[] var2 = new Action[]{copyAction, clearAction};
      nonEditableComponentActions = var2;
      comboBoxMinWidth = -1;
      m_busyImage = null;
      m_busyIcon = null;
   }
}
