package com.udea.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Materia.
 */
@Entity
@Table(name = "materia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "creditos")
    private Long creditos;

    @ManyToMany(mappedBy = "materias")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materias" }, allowSetters = true)
    private Set<Pensum> pensums = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Materia nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getCreditos() {
        return this.creditos;
    }

    public Materia creditos(Long creditos) {
        this.setCreditos(creditos);
        return this;
    }

    public void setCreditos(Long creditos) {
        this.creditos = creditos;
    }

    public Set<Pensum> getPensums() {
        return this.pensums;
    }

    public void setPensums(Set<Pensum> pensums) {
        if (this.pensums != null) {
            this.pensums.forEach(i -> i.removeMaterias(this));
        }
        if (pensums != null) {
            pensums.forEach(i -> i.addMaterias(this));
        }
        this.pensums = pensums;
    }

    public Materia pensums(Set<Pensum> pensums) {
        this.setPensums(pensums);
        return this;
    }

    public Materia addPensums(Pensum pensum) {
        this.pensums.add(pensum);
        pensum.getMaterias().add(this);
        return this;
    }

    public Materia removePensums(Pensum pensum) {
        this.pensums.remove(pensum);
        pensum.getMaterias().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materia)) {
            return false;
        }
        return id != null && id.equals(((Materia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", creditos=" + getCreditos() +
            "}";
    }
}
