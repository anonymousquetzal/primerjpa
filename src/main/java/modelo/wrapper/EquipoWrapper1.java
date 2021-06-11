/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.wrapper;
import java.time.LocalDateTime;


/**
 *
 * @author Leticia Boch
 */
public class EquipoWrapper1 {
    
    private String nombre;

    private String direccion;

    private LocalDateTime creadoEl;

    public EquipoWrapper1() {
    }

    public EquipoWrapper1(String nombre, String direccion, LocalDateTime creadoEl) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.creadoEl = creadoEl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getCreadoEl() {
        return creadoEl;
    }

    public void setCreadoEl(LocalDateTime creadoEl) {
        this.creadoEl = creadoEl;
    }

    @Override
    public String toString() {
        return "EquipoWrapper1{" + "nombre=" + nombre + ", direccion=" + direccion + ", creadoEl=" + creadoEl + '}';
    }
    
}
