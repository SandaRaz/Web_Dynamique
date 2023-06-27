package etu2079.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data = new HashMap<>();
    HashMap<String, Object> session = new HashMap<>();

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData(){
        return this.data;
    }
    public void setData(HashMap<String, Object> data){
        this.data = data;
    }
    public void addItem(String key, Object value){
        this.getData().put(key, value);
    }

    public HashMap<String, Object> getSession() {
        return session;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    public void addSession(String nomSession, Object valeurSession){ this.getSession().put(nomSession, valeurSession); }

    public ModelView() {}

    public ModelView(String view) {
        this.view = view;
    }

    public ModelView(String view, HashMap<String,Object> data) {
        this.setView(view);
        this.setData(data);
    }

    public ModelView(String view, HashMap<String, Object> data, HashMap<String, Object> session) {
        this.setView(view);
        this.setData(data);
        this.setSession(session);
    }
}
