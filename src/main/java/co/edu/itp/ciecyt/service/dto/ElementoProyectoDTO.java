package co.edu.itp.ciecyt.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.itp.ciecyt.domain.ElementoProyecto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ElementoProyectoDTO implements Serializable {

    private Long id;

    @Lob
    private String dato;

    private String descripcion;

    private ElementoDTO elemento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ElementoDTO getElemento() {
        return elemento;
    }

    public void setElemento(ElementoDTO elemento) {
        this.elemento = elemento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementoProyectoDTO)) {
            return false;
        }

        ElementoProyectoDTO elementoProyectoDTO = (ElementoProyectoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, elementoProyectoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ElementoProyectoDTO{" +
            "id=" + getId() +
            ", dato='" + getDato() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", elemento=" + getElemento() +
            "}";
    }
}
