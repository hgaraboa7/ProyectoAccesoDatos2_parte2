/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author hecto
 */
public class Cliente {

    private String idcliente;
    private String nombrecli;
    private String apellidocli;
    private String dircli;

    public Cliente(String idcliente, String nombrecli, String apellidocli, String dircli) {
        this.idcliente = idcliente;
        this.nombrecli = nombrecli;
        this.apellidocli = apellidocli;
        this.dircli = dircli;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(String idcliente) {
        this.idcliente = idcliente;
    }

    public String getNombrecli() {
        return nombrecli;
    }

    public void setNombrecli(String nombrecli) {
        this.nombrecli = nombrecli;
    }

    public String getApellidocli() {
        return apellidocli;
    }

    public void setApellidocli(String apellidocli) {
        this.apellidocli = apellidocli;
    }

    public String getDircli() {
        return dircli;
    }

    public void setDircli(String dircli) {
        this.dircli = dircli;
    }

    @Override
    public String toString() {
        return idcliente  ;
    }

    
    
    
}
