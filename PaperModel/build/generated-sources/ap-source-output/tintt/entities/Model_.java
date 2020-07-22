package tintt.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import tintt.entities.Category;
import tintt.entities.Tag;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-07-22T13:20:19")
@StaticMetamodel(Model.class)
public class Model_ { 

    public static volatile SingularAttribute<Model, Integer> difficulty;
    public static volatile CollectionAttribute<Model, Tag> tagCollection;
    public static volatile SingularAttribute<Model, Integer> numOfParts;
    public static volatile SingularAttribute<Model, String> name;
    public static volatile SingularAttribute<Model, String> link;
    public static volatile SingularAttribute<Model, String> imageSrc;
    public static volatile SingularAttribute<Model, Integer> id;
    public static volatile SingularAttribute<Model, Boolean> hasInstruction;
    public static volatile SingularAttribute<Model, Integer> numOfSheets;
    public static volatile SingularAttribute<Model, Category> categoryId;

}