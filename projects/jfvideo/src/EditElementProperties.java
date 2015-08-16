/**
 * @author pquiring
 */

import java.awt.*;

import javaforce.*;

public class EditElementProperties extends javax.swing.JDialog {

  /**
   * Creates new form EditElementProperties
   */
  public EditElementProperties(java.awt.Frame parent, boolean modal, Element element, float fov) {
    super(parent, modal);
    initComponents();
    this.element = element;
    switch (element.type) {
      case Element.TYPE_IMAGE:
        addPos();
        addImage();
        if (element.path.length == 1) {
          addDuration();
        }
        addGL(fov);
        break;
      case Element.TYPE_AUDIO:
        addSound();
        break;
      case Element.TYPE_VIDEO:
        addPos();
        addSound();
        addImage();
        addGL(fov);
        break;
      case Element.TYPE_SPECIAL_CUT:
        addDuration();
        break;
      case Element.TYPE_SPECIAL_BLUR:
        addBlur();  //includes duration
        break;
      case Element.TYPE_CAMERA:
        addGL(fov);
        break;
      case Element.TYPE_SPECIAL_TEXT:
        addDuration();
        addText();
        addColor();
        break;
    }
    pack();  //calls setSize()
    JF.sleep(100);  //JVM Bug - setSize() and setLocation() need a small delay inbetween
    JF.centerWindow(this);
//    if (gl != null) gl.init();  //crashes
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    tabs = new javax.swing.JTabbedPane();
    save = new javax.swing.JButton();
    cancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Edit Properties");
    setResizable(false);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    tabs.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        tabsStateChanged(evt);
      }
    });

    save.setText("Save");
    save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveActionPerformed(evt);
      }
    });

    cancel.setText("Cancel");
    cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(tabs)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(350, Short.MAX_VALUE)
        .addComponent(cancel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(save)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(save)
          .addComponent(cancel))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
    close();
  }//GEN-LAST:event_cancelActionPerformed

  private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
    save();
    close();
  }//GEN-LAST:event_saveActionPerformed

  private void tabsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabsStateChanged
  }//GEN-LAST:event_tabsStateChanged

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    if (gl != null) {
      gl.uninit();
      gl = null;
    }
  }//GEN-LAST:event_formWindowClosing

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancel;
  private javax.swing.JButton save;
  private javax.swing.JTabbedPane tabs;
  // End of variables declaration//GEN-END:variables

  private Element element;
  private PositionProps pos;
  private SoundProps sound;
  private ImageProps image;
  private DurationProps cut;
  private GLProps gl;
  private BlurProps blur;
  private TextProps text;
  private ColorProps clr;

  public float fov;

  public boolean saved;

  private void addPos() {
    pos = new PositionProps(element);
    tabs.add("Position", pos);
  }

  private void addSound() {
    sound = new SoundProps(element);
    tabs.add("Audio", sound);
  }

  private void addImage() {
    image = new ImageProps(element);
    tabs.add("Image", image);
  }

  private void addDuration() {
    cut = new DurationProps(element);
    tabs.add("Duration", cut);
  }

  private void addBlur() {
    blur = new BlurProps(element);
    tabs.add("Blur", blur);
  }

  private void addGL(float fov) {
    gl = new GLProps(element, fov);
    tabs.add("3D", gl);
  }

  private void addText() {
    text = new TextProps(element);
    tabs.add("Text", text);
  }

  private void addColor() {
    clr = new ColorProps(element);
    tabs.add("Color", clr);
  }

  private void save() {
    if (pos != null) pos.save(element);
    if (sound != null) sound.save(element);
    if (image != null) image.save(element);
    if (cut != null) cut.save(element);
    if (gl != null) {
      fov = gl.getFOV();
      gl.save(element);
    }
    if (blur != null) blur.save(element);
    if (text != null) text.save(element);
    if (clr != null) clr.save(element);
    saved = true;
  }

  private void close() {
    if (gl != null) {
      gl.uninit();
      gl = null;
    }
    dispose();
  }
}
