package com.company.luongchung.model;

/**
 * Created by luongchung on 9/22/17.
 */

public class O_file_choose {
    private  int id;
    private boolean choose;
    private String urlFile;
    private String nameFile;

    public O_file_choose() {
    }

    public O_file_choose(int id, boolean choose, String urlFile, String nameFile) {
        this.id = id;
        this.choose = choose;
        this.urlFile = urlFile;
        this.nameFile = nameFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
}
