package co.edu.itp.ciecyt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proyecto.
 */
@Entity
@Table(name = "proyecto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "elementosProyectos", "proyecto" }, allowSetters = true)
    private Set<Elemento> elementos = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "proyecto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "proyecto" }, allowSetters = true)
    private Set<IntegrantesProyecto> integrantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proyecto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Proyecto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Proyecto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Elemento> getElementos() {
        return this.elementos;
    }

    public void setElementos(Set<Elemento> elementos) {
        if (this.elementos != null) {
            this.elementos.forEach(i -> i.setProyecto(null));
        }
        if (elementos != null) {
            elementos.forEach(i -> i.setProyecto(this));
        }
        this.elementos = elementos;
    }

    public Proyecto elementos(Set<Elemento> elementos) {
        this.setElementos(elementos);
        return this;
    }

    public Proyecto addElementos(Elemento elemento) {
        this.elementos.add(elemento);
        elemento.setProyecto(this);
        return this;
    }

    public Proyecto removeElementos(Elemento elemento) {
        this.elementos.remove(elemento);
        elemento.setProyecto(null);
        return this;
    }

    public Set<IntegrantesProyecto> getIntegrantes() {
        return this.integrantes;
    }

    public void setIntegrantes(Set<IntegrantesProyecto> integrantesProyectos) {
        if (this.integrantes != null) {
            this.integrantes.forEach(i -> i.setProyecto(null));
        }
        if (integrantesProyectos != null) {
            integrantesProyectos.forEach(i -> i.setProyecto(this));
        }
        this.integrantes = integrantesProyectos;
    }

    public Proyecto integrantes(Set<IntegrantesProyecto> integrantesProyectos) {
        this.setIntegrantes(integrantesProyectos);
        return this;
    }

    public Proyecto addIntegrantes(IntegrantesProyecto integrantesProyecto) {
        this.integrantes.add(integrantesProyecto);
        integrantesProyecto.setProyecto(this);
        return this;
    }

    public Proyecto removeIntegrantes(IntegrantesProyecto integrantesProyecto) {
        this.integrantes.remove(integrantesProyecto);
        integrantesProyecto.setProyecto(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proyecto)) {
            return false;
        }
        return getId() != null && getId().equals(((Proyecto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proyecto{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
