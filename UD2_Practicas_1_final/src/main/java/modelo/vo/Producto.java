/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author hector.garaboacasas
 */
public class Producto {

    public String idproducto;

    public String nomproducto;

    public int stock;

    public double precio;

    public Producto(String idproducto, String nomproducto, int stock, double precio) {
        this.idproducto = idproducto;
        this.nomproducto = nomproducto;
        this.stock = stock;
        this.precio = precio;
    }

    public String getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(String idproducto) {
        this.idproducto = idproducto;
    }

    public String getNomproducto() {
        return nomproducto;
    }

    public void setNomproducto(String nomproducto) {
        this.nomproducto = nomproducto;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return  nomproducto ;
    }

    
    
    
    
}
