package com.example.proyectoec03.Model;

import java.util.List;

public class ResponseList<t> {

    public Info info;
    public List<t> results;

    public ResponseList(){

    }

    public ResponseList(Info info, List<t> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<t> getResults() {
        return results;
    }

    public void setResults(List<t> results) {
        this.results = results;
    }
}

