/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.vo;

/**
 *
 * @author hector.garaboacasas
 */
public class Empleado {
    
        private String idempleado;

    /**
     * Get the value of idempleado
     *
     * @return the value of idempleado
     */
    public String getIdempleado() {
        return idempleado;
    }

    /**
     * Set the value of idempleado
     *
     * @param idempleado new value of idempleado
     */
    public void setIdempleado(String idempleado) {
        this.idempleado = idempleado;
    }

    
        private String nombreemp;

    /**
     * Get the value of nombreemp
     *
     * @return the value of nombreemp
     */
    public String getNombreemp() {
        return nombreemp;
    }

    /**
     * Set the value of nombreemp
     *
     * @param nombreemp new value of nombreemp
     */
    public void setNombreemp(String nombreemp) {
        this.nombreemp = nombreemp;
    }

    private double salario;

    /**
     * Get the value of salario
     *
     * @return the value of salario
     */
    public double getSalario() {
        return salario;
    }

    /**
     * Set the value of salario
     *
     * @param salario new value of salario
     */
    public void setSalario(double salario) {
        this.salario = salario;
    }

        private double incentivo;

    /**
     * Get the value of incentivo
     *
     * @return the value of incentivo
     */
    public double getIncentivo() {
        return incentivo;
    }

    /**
     * Set the value of incentivo
     *
     * @param incentivo new value of incentivo
     */
    public void setIncentivo(double incentivo) {
        this.incentivo = incentivo;
    }

        private int operativas;

    /**
     * Get the value of operativas
     *
     * @return the value of operativas
     */
    public int getOperativas() {
        return operativas;
    }

    /**
     * Set the value of operativas
     *
     * @param operativas new value of operativas
     */
    public void setOperativas(int operativas) {
        this.operativas = operativas;
    }

    public Empleado(String idempleado, String nombreemp, double salario, double incentivo, int operativas) {
        this.idempleado = idempleado;
        this.nombreemp = nombreemp;
        this.salario = salario;
        this.incentivo = incentivo;
        this.operativas = operativas;
    }

    @Override
    public String toString() {
        return nombreemp ;
    }

    
    
    
    
}
