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
 * A Elemento.
 */
@Entity
@Table(name = "elemento")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Elemento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "elemento", nullable = false)
    private String elemento;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "elemento")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "elemento" }, allowSetters = true)
    private Set<ElementoProyecto> elementosProyectos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "elementos", "integrantes" }, allowSetters = true)
    private Proyecto proyecto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Elemento id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getElemento() {
        return this.elemento;
    }

    public Elemento elemento(String elemento) {
        this.setElemento(elemento);
        return this;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Elemento descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<ElementoProyecto> getElementosProyectos() {
        return this.elementosProyectos;
    }

    public void setElementosProyectos(Set<ElementoProyecto> elementoProyectos) {
        if (this.elementosProyectos != null) {
            this.elementosProyectos.forEach(i -> i.setElemento(null));
        }
        if (elementoProyectos != null) {
            elementoProyectos.forEach(i -> i.setElemento(this));
        }
        this.elementosProyectos = elementoProyectos;
    }

    public Elemento elementosProyectos(Set<ElementoProyecto> elementoProyectos) {
        this.setElementosProyectos(elementoProyectos);
        return this;
    }

    public Elemento addElementosProyectos(ElementoProyecto elementoProyecto) {
        this.elementosProyectos.add(elementoProyecto);
        elementoProyecto.setElemento(this);
        return this;
    }

    public Elemento removeElementosProyectos(ElementoProyecto elementoProyecto) {
        this.elementosProyectos.remove(elementoProyecto);
        elementoProyecto.setElemento(null);
        return this;
    }

    public Proyecto getProyecto() {
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Elemento proyecto(Proyecto proyecto) {
        this.setProyecto(proyecto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Elemento)) {
            return false;
        }
        return getId() != null && getId().equals(((Elemento) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Elemento{" +
            "id=" + getId() +
            ", elemento='" + getElemento() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
