package etu2079.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModelView {
    String view;
    HashMap<String, Object> data = new HashMap<>();
    HashMap<String, Object> session = new HashMap<>();
    boolean isJson = false;
    boolean invalidateSession = false;
    List<String> removeSession = new ArrayList<>();

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

    public boolean isJson() {
        return isJson;
    }
    public void setJson(boolean json) {
        isJson = json;
    }

    public boolean isInvalidateSession() {
        return invalidateSession;
    }
    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public List<String> getRemoveSession() {
        return removeSession;
    }
    public void setRemoveSession(List<String> removeSession) {
        this.removeSession = removeSession;
    }

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
