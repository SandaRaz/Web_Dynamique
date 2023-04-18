package etu2079.framework;

import java.util.HashMap;

public class ViewModel {
    HashMap<String, Object> data = new HashMap<>();

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String key, Object value){
        this.getData().put(key, value);
    }

    public ViewModel() {
    }

    public ViewModel(HashMap<String, Object> data) {
        this.data = data;
    }
}
