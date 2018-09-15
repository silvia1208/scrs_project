package it.uniroma2.controller;

public class ResponseAuthentication {

    String domain;
    String status;

    public ResponseAuthentication(String domain,String status){
        this.domain=domain;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public String getDomain(){
        return domain;
    }
}
