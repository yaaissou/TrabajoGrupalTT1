package com.trabajo.servidor.model;

public class BattleshipResponse {

    private boolean done;
    private int tokenSolicitud;
    private String errorMessage;
    private String data;

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public int getTokenSolicitud() { return tokenSolicitud; }
    public void setTokenSolicitud(int tokenSolicitud) { this.tokenSolicitud = tokenSolicitud; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
}
