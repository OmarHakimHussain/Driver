package com.example.mybus_driver;

public class item {
    private String text , subText , Line,Line1,Line2;
    private boolean expandable;

    public String getLine() {
        return Line;
    }

    public void setLine(String line) {
        Line = line;
    }

    public item() {
    }

    public item(String text, String subText, boolean expandable , String Line, String Line1, String Line2) {
        this.text = text;
        this.subText = subText;
        this.expandable = expandable;
        this.Line = Line;
        this.Line1 = Line1;
        this.Line2 = Line2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getLine1() {
        return Line1;
    }

    public void setLine1(String line1) {
        Line1 = line1;
    }

    public String getLine2() {
        return Line2;
    }

    public void setLine2(String line2) {
        Line2 = line2;
    }
}
