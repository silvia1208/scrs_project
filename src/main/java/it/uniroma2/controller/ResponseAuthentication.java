package it.uniroma2.controller;

public class ResponseAuthentication {

    String domain;
    String password;
    String status;

    public ResponseAuthentication(String domain, String password,String status){
        this.domain=domain;
        this.password= password;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public String getDomain(){
        return domain;
    }

    public String getPassword(){ return password;}
}
