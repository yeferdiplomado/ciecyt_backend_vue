package co.edu.itp.ciecyt.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.itp.ciecyt.domain.Elemento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ElementoDTO implements Serializable {

    private Long id;

    @NotNull
    private String elemento;

    private String descripcion;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ProyectoDTO getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoDTO proyecto) {
        this.proyecto = proyecto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElementoDTO)) {
            return false;
        }

        ElementoDTO elementoDTO = (ElementoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, elementoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ElementoDTO{" +
            "id=" + getId() +
            ", elemento='" + getElemento() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
