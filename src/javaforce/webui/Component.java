package javaforce.webui;

/** Base class for all components.
 *
 * @author pquiring
 */

import java.util.*;

import javaforce.webui.event.*;

public abstract class Component {
  public String id;
  public Container parent;
  public Client client;
  public ArrayList<String> classes = new ArrayList<String>();
  public HashMap<String, String> attrs = new HashMap<String, String>();
  public HashMap<String, String> styles = new HashMap<String, String>();
  public int x,y,width,height;  //position and size (updated with mouseenter event)

  private static class OnEvent {
    public String event;
    public String js;
  }
  public ArrayList<OnEvent> events = new ArrayList<OnEvent>();

  public Component() {
    client = Client.NULL;
  }

  /** Provides the client (connection to web browser side) and init other variables. */
  public void setClient(Client client) {
    if (id != null) return;
    this.client = client;
    id = "c" + client.getNextID();
  }
  /** Perform any initialization with the client.
   * Containers should call init() on all sub-components.
   */
  public void init() {}
  /** Returns HTML to render component. */
  public abstract String html();
  /** Component constructor.
   * @param parent = Panel
   * @param name = name of component
   */
  private HashMap<String,Object> map = new HashMap<>();
  /** Set user define property. */
  public void setProperty(String key, Object value) {
    map.put(key, value);
  }
  /** Get user define property. */
  public Object getProperty(String key) {
    return map.get(key);
  }
  public void setClass(String cls) {
    classes.clear();
    classes.add(cls);
    client.sendEvent(id, "setclass", new String [] {"cls=" + cls});
  }
  public boolean hasClass(String cls) {
    return classes.contains(cls);
  }
  public void addClass(String cls) {
    if (hasClass(cls)) return;
    classes.add(cls);
  }
  public void removeClass(String cls) {
    classes.remove(cls);
  }

  public boolean hasAttr(String attr) {
    return attrs.containsKey(attr);
  }
  public void addAttr(String attr, String value) {
    attrs.put(attr, value);
  }
  public void removeAttr(String attr) {
    attrs.remove(attr);
  }

  public boolean hasStyle(String style) {
    return styles.containsKey(style);
  }
  public void setStyle(String style, String value) {
    styles.put(style, value);
  }
  public void removeStyle(String style) {
    styles.remove(style);
  }

  private OnEvent getEvent(String onX) {
    int cnt = events.size();
    for(int a=0;a<cnt;a++) {
      OnEvent event = events.get(a);
      if (event.event.equals(onX)) return event;
    }
    return null;
  }
  public void addEvent(String onX, String js) {
    OnEvent event;
    event = getEvent(onX);
    if (event != null) {
      event.js = js;
    } else {
      event = new OnEvent();
      event.event = onX;
      event.js = js;
      events.add(event);
    }
  }
  public String getEvents() {
    StringBuffer sb = new StringBuffer();
    int cnt = events.size();
    for(int a=0;a<cnt;a++) {
      sb.append(' ');
      OnEvent event = events.get(a);
      sb.append(event.event);
      sb.append("='");
      sb.append(event.js);
      sb.append("'");
    }
    return sb.toString();
  }
  public void setWidth(String width) {
    setStyle("width", width);
  }
  public void setHeight(String height) {
    setStyle("height", height);
  }
  public void setBackColor(String clr) {
    setStyle("background-color", clr);
  }
  /** Returns all attributes defined for a component (id, attrs, class, styles) */
  public String getAttrs() {
    StringBuffer sb = new StringBuffer();
    sb.append(" id='" + id + "'");
    if (attrs.size() > 0) {
      int size = attrs.size();
      String keys[] = attrs.keySet().toArray(new String[size]);
      String vals[] = attrs.values().toArray(new String[size]);
      for(int a=0;a<size;a++) {
        sb.append(" " + keys[a] + "='" + vals[a] + "'");
      }
    }
    if (classes.size() > 0) {
      sb.append(" class='");
      for(int a=0;a<classes.size();a++) {
        if (a > 0) sb.append(' ');
        sb.append(classes.get(a));
      }
      sb.append("'");
    }
    sb.append(getEvents());
    if (styles.size() > 0) {
      sb.append(" style='");
      int size = styles.size();
      String keys[] = styles.keySet().toArray(new String[size]);
      String vals[] = styles.values().toArray(new String[size]);
      for(int a=0;a<size;a++) {
        sb.append(keys[a] + ":" + vals[a] + ";");
      }
      sb.append("'");
    }
    return sb.toString();
  }

  public String display = "inline-block";

  public void setVisible(boolean state) {
    if (state)
      client.sendEvent(id, "display", new String[] {"val=" + display});
    else
      client.sendEvent(id, "display", new String[] {"val=none"});
  }

  public void setPosition(int x, int y) {
    client.sendEvent(id, "setpos", new String[] {"x=" + x, "y=" + y});
    this.x = x;
    this.y = y;
  }

  public String toString() {
    return getClass().getName() + ":" + id;
  }

  //event handlers

  /** Dispatches event. */
  public void dispatchEvent(String event, String args[]) {
    MouseEvent e = new MouseEvent();
    for(int a=0;a<args.length;a++) {
      if (args[a].equals("ck=true")) {
        e.ctrlKey = true;
      }
      if (args[a].equals("ak=true")) {
        e.altKey = true;
      }
      if (args[a].equals("sk=true")) {
        e.shiftKey = true;
      }
    }
    switch (event) {
      case "click":
        onClick(args);
        if (click != null) click.onClick(e, this);
        break;
      case "changed":
        onChanged(args);
        if (changed != null) changed.onChanged(this);
        break;
      case "mousedown":
        onMouseDown(args);
        if (mouseDown != null) mouseDown.onMouseDown(this);
        break;
      case "mouseup":
        onMouseUp(args);
        if (mouseUp != null) mouseUp.onMouseUp(this);
        break;
      case "mousemove":
        onMouseMove(args);
        if (mouseMove != null) mouseMove.onMouseMove(this);
        break;
      case "mouseenter":
        onMouseEnter(args);
        if (mouseEnter != null) mouseEnter.onMouseEnter(this);
        break;
      case "possize":
        onPosSize(args);
        break;
      case "pos":
        onPos(args);
        break;
      case "size":
        onSize(args);
        break;
    }
  }

  public void onPosSize(String args[]) {
    for(int c=0;c<args.length;c++) {
      String a = args[c];
      if (a.startsWith("x=")) {
        x = Integer.valueOf(a.substring(2));
      }
      if (a.startsWith("y=")) {
        y = Integer.valueOf(a.substring(2));
      }
      if (a.startsWith("w=")) {
        width = Integer.valueOf(a.substring(2));
      }
      if (a.startsWith("h=")) {
        height = Integer.valueOf(a.substring(2));
      }
    }
  }

  public void onSize(String args[]) {
    for(int c=0;c<args.length;c++) {
      String a = args[c];
      if (a.startsWith("w=")) {
        width = Integer.valueOf(a.substring(2));
      }
      if (a.startsWith("h=")) {
        height = Integer.valueOf(a.substring(2));
      }
    }
  }

  public void onPos(String args[]) {
    for(int c=0;c<args.length;c++) {
      String a = args[c];
      if (a.startsWith("x=")) {
        x = Integer.valueOf(a.substring(2));
      }
      if (a.startsWith("y=")) {
        y = Integer.valueOf(a.substring(2));
      }
    }
  }

  protected void onClick(String args[]) {}
  public Click click;
  public void addClickListener(Click handler) {
    click = handler;
  }

  protected void onMouseUp(String args[]) {}
  private MouseUp mouseUp;
  public void addMouseUpListener(MouseUp handler) {
    mouseUp = handler;
  }

  protected void onMouseDown(String args[]) {}
  private MouseDown mouseDown;
  public void addMouseDownListener(MouseDown handler) {
    mouseDown = handler;
  }

  protected void onMouseMove(String args[]) {}
  private MouseMove mouseMove;
  public void addMouseMoveListener(MouseMove handler) {
    mouseMove = handler;
  }

  protected void onMouseEnter(String args[]) {}
  private MouseEnter mouseEnter;
  public void addMouseEnterListener(MouseEnter handler) {
    mouseEnter = handler;
  }

  protected void onChanged(String args[]) {}
  private Changed changed;
  public void addChangedListener(Changed handler) {
    changed = handler;
  }
}
