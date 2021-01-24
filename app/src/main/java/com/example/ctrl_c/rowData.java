package com.example.ctrl_c;

//클라스 변수 선언
public class rowData {
    private int row;
    private int startH;
    private int startM;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getStartH() {
        return startH;
    }

    public int getStartM() {
        return startM;
    }

    public void setStartTime(int startH, int startM) {
        this.startH = startH;
        this.startM = startM;
    }
}
