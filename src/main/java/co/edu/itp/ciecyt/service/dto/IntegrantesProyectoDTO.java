package co.edu.itp.ciecyt.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link co.edu.itp.ciecyt.domain.IntegrantesProyecto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IntegrantesProyectoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nombre;

    private String descripcion;

    private ProyectoDTO proyecto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(o instanceof IntegrantesProyectoDTO)) {
            return false;
        }

        IntegrantesProyectoDTO integrantesProyectoDTO = (IntegrantesProyectoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, integrantesProyectoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IntegrantesProyectoDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", proyecto=" + getProyecto() +
            "}";
    }
}
