/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tintt.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author TiTi
 */
@Entity
@Table(name = "Model", catalog = "PaperModel", schema = "dbo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Model.findAll", query = "SELECT m FROM Model m")
    , @NamedQuery(name = "Model.findById", query = "SELECT m FROM Model m WHERE m.id = :id")
    , @NamedQuery(name = "Model.findByName", query = "SELECT m FROM Model m WHERE m.name = :name")
    , @NamedQuery(name = "Model.findByNumOfSheets", query = "SELECT m FROM Model m WHERE m.numOfSheets = :numOfSheets")
    , @NamedQuery(name = "Model.findByNumOfParts", query = "SELECT m FROM Model m WHERE m.numOfParts = :numOfParts")
    , @NamedQuery(name = "Model.findByDifficulty", query = "SELECT m FROM Model m WHERE m.difficulty = :difficulty")
    , @NamedQuery(name = "Model.findByImageSrc", query = "SELECT m FROM Model m WHERE m.imageSrc = :imageSrc")
    , @NamedQuery(name = "Model.findByLink", query = "SELECT m FROM Model m WHERE m.link = :link")
         , @NamedQuery(name = "Model.findByTagAndCategory", query = "SELECT m FROM Model m INNER JOIN m.categoryId AS c WHERE c.id = :categoryId"
                 + "")
    , @NamedQuery(name = "Model.findByHasInstruction", query = "SELECT m FROM Model m WHERE m.hasInstruction = :hasInstruction")})
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;
    @Column(name = "Name", length = 100)
    private String name;
    @Column(name = "NumOfSheets")
    private Integer numOfSheets;
    @Column(name = "NumOfParts")
    private Integer numOfParts;
    @Column(name = "Difficulty")
    private Integer difficulty;
    @Column(name = "ImageSrc", length = 500)
    private String imageSrc;
    @Column(name = "Link", length = 500)
    private String link;
    @Column(name = "HasInstruction")
    private Boolean hasInstruction;
    @JoinTable(name = "Tag_Mapping", joinColumns = {
        @JoinColumn(name = "ModelId", referencedColumnName = "Id", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "TagId", referencedColumnName = "Id", nullable = false)})
    @ManyToMany
    private Collection<Tag> tagCollection;
    @JoinColumn(name = "CategoryId", referencedColumnName = "Id")
    @ManyToOne
    private Category categoryId;

    public Model() {
    }

    public Model(Integer id) {
        this.id = id;
    }

    public Model(Integer id, String name, Integer numOfSheets,
            Integer numOfParts, Integer difficulty, String imageSrc,
            String link, Category categoryId, Collection<Tag> tagCollection, Boolean hasInstruction) {
        this.id = id;
        this.name = name;
        this.numOfSheets = numOfSheets;
        this.numOfParts = numOfParts;
        this.difficulty = difficulty;
        this.imageSrc = imageSrc;
        this.link = link;
        this.hasInstruction = hasInstruction;
        this.tagCollection = tagCollection;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumOfSheets() {
        return numOfSheets;
    }

    public void setNumOfSheets(Integer numOfSheets) {
        this.numOfSheets = numOfSheets;
    }

    public Integer getNumOfParts() {
        return numOfParts;
    }

    public void setNumOfParts(Integer numOfParts) {
        this.numOfParts = numOfParts;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getHasInstruction() {
        return hasInstruction;
    }

    public void setHasInstruction(Boolean hasInstruction) {
        this.hasInstruction = hasInstruction;
    }

    @XmlTransient
    public Collection<Tag> getTagCollection() {
        return tagCollection;
    }

    public void setTagCollection(Collection<Tag> tagCollection) {
        this.tagCollection = tagCollection;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tintt.entities.Model[ id=" + id + " ]";
    }

}
