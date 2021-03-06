package javaforce.webui;

/** Label.java
 *
 * Generic Label.
 *
 * @author pquiring
 */

public class Label extends Component {
  public String text;
  public Label(String text) {
    this.text = text;
    addEvent("onclick", "onClick(event, this);");
    setClass("noselect");
  }
  public String html() {
    return "<label" + getAttrs() + ">" + text + "</label>";
  }
  public void setText(String txt) {
    text = txt;
    client.sendEvent(id, "settext", new String[] {"text=" + text});
  }
}
