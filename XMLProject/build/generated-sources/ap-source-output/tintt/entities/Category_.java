package tintt.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import tintt.entities.Model;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-07-16T22:36:24")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile SingularAttribute<Category, String> name;
    public static volatile CollectionAttribute<Category, Model> modelCollection;
    public static volatile SingularAttribute<Category, String> id;

}